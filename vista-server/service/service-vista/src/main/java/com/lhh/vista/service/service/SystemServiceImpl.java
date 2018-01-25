package com.lhh.vista.service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.v2s.value.CinemasRes;
import com.lhh.vista.customer.v2s.value.GetTicketsForSessionRes;
import com.lhh.vista.customer.v2s.value.MovieRes;
import com.lhh.vista.customer.v2s.value.ShowingMovieRes;
import com.lhh.vista.customer.v2s.value.SoonComeMovieRes;
import com.lhh.vista.service.dao.SystemDao;
import com.lhh.vista.service.dao.SystemValueDao;
import com.lhh.vista.temp.dao.CinemaDao;
import com.lhh.vista.temp.dao.MovieDao;
import com.lhh.vista.temp.dao.MtypeDao;
import com.lhh.vista.temp.dao.SequenceDao;
import com.lhh.vista.temp.dao.SessionDao;
import com.lhh.vista.temp.dao.TicketDao;
import com.lhh.vista.temp.dao.TicketInfoDao;
import com.lhh.vista.temp.model.Cinema;
import com.lhh.vista.temp.model.Movie;
import com.lhh.vista.temp.model.Mtype;
import com.lhh.vista.temp.model.Sequence;
import com.lhh.vista.temp.model.Session;
import com.lhh.vista.temp.model.Ticket;
import com.lhh.vista.temp.model.TicketInfo;

/**
 * Created by soap on 2016/12/10.
 */
@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    private SystemValueDao systemValueDao;

    @Autowired
    private SystemDao systemDao;

    @Autowired
    private CinemaDao cinemaDao;

    @Autowired
    private MovieDao moviceDao;

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private TicketDao ticketDao;
    
    @Autowired
    private SequenceDao sequenceDao;
    
    @Autowired
    private TicketInfoDao ticketInfoDao;
    
    @Autowired
    private MtypeDao mtypeDao;

    @Autowired
    private VistaApi vistaApi;

    public void initDb() {
        systemDao.createTable();
        sysnMovieTypeData();
        synVistaData();
        synTicketsData();
    }
    
    
    @Transactional
    public void sysnMovieTypeData() {
    	System.out.println("同步电影类型信息至本地数据库");
    	String typeStr = vistaApi.getMovieType();
        try {
			Document doc = DocumentHelper.parseText(typeStr);
			List<org.dom4j.Element> nameList = doc.selectNodes("//d:Name");
			List<org.dom4j.Element> idList = doc.selectNodes("//d:ID");
			for (int i = 0; i < nameList.size(); i++) {
				String name = nameList.get(i).getTextTrim();
				String id = idList.get(i).getTextTrim();
				Mtype mtype = new Mtype();
				int updateCount = mtypeDao.update(mtype);
				if (updateCount == 0) {
					mtype.setId(id);
					mtype.setName(name);
					mtypeDao.insert(mtype);
				}
				
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
    }

    @Transactional
    public void synVistaData() {
        System.out.println("这里开始同步信息到本地数据库");
        long t1 = System.currentTimeMillis();
        
        //获取影片类型封装成map
        List<Mtype> typeList = mtypeDao.getAll();
        Map<String, String> map = new HashMap<>();
        for (Mtype mtype : typeList) {
        	map.put(mtype.getId(), mtype.getName());
        }

        System.out.println("正在获取电影信息");
        MovieRes moviceRes = vistaApi.getMovie();
        if (moviceRes != null && moviceRes.getValue() != null) {
            System.out.println("获取电影信息成功");
            List<Movie> insertMovies = new ArrayList<>();
            List<Movie> updateMovies = new ArrayList<>();
            for (MovieRes.Value val : moviceRes.getValue()) {
                if (Strings.isNullOrEmpty(val.getId())) {
                    continue;
                }

                Movie movice = moviceDao.find4Session(val.getId());
                try {
                    boolean isadd = (movice == null);
                    if (movice == null) {
                        movice = new Movie();
                    }
                    movice.setMid(val.getId());
                    if (val.getTitle().indexOf("（") != -1) {
                    	movice.setMname(val.getTitle().substring(0, val.getTitle().indexOf("（")));
                    } else {
                    	movice.setMname(val.getTitle());
                    }
                    if (!Strings.isNullOrEmpty(val.getOpeningDate()) && val.getOpeningDate().length() > 10) {
                        movice.setOpeningDate(val.getOpeningDate().substring(0, 10));
                    }
                    movice.setRunTime(val.getRunTime());
                    if (!Strings.isNullOrEmpty(val.getSynopsis())) {
                        movice.setSynopsis(val.getSynopsis().trim());
                    }
                    if (!Strings.isNullOrEmpty(val.getShortSynopsis())) {
                        String[] infos = val.getShortSynopsis().split("\n");
                        for (String info : infos) {
                            if (info.startsWith("导演：")) {
                                movice.setDirector(info.replace("导演：", "").trim());
                            }
                            if (info.startsWith("主演：")) {
                                movice.setPerformer(info.replace("主演：", "").trim());
                            }
//                            if (info.startsWith("类型：")) {
//                                movice.setMtype(info.replace("类型：", "").trim());
//                            }
                        }
                    }
                    
                    if (!Strings.isNullOrEmpty(val.getGenreId())) {
                        movice.setMtype(map.get(val.getGenreId()));
                    }

                    if (!Strings.isNullOrEmpty(val.getRatingDescription())) {
                        movice.setDetails(val.getRatingDescription().trim());
                    }
                    //movice.setCinemaCount(sessionDao.getCinemaCountByMovie(val.getId()));
                    movice.setHOFilmCode(val.getHOFilmCode());
                    if (isadd) {
                        insertMovies.add(movice);
                    } else {
                        updateMovies.add(movice);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (insertMovies.size() > 0) {
                moviceDao.createByList(insertMovies);
            }

            if (updateMovies.size() > 0) {
                moviceDao.updateByList(updateMovies);
            }
        }
        System.out.println("同步电影信息结束......花费时间:" + (System.currentTimeMillis() - t1));

        moviceDao.chongzhiState();
        sessionDao.delAll();

        System.out.println("正在获取正在热映的场次信息");
        t1 = System.currentTimeMillis();
        //首先获取排序表里的影片存放在list里 。。。从数据库取热映影片  进行从list里面过滤已经非热映影片  TODO  下方为临时测试数据 等待从数据库中筛选
        List<String> showingListInDb = sequenceDao.getAll(Sequence.NOWSHING);      
        
        ShowingMovieRes showingMovieRes = vistaApi.getShowingMovie();
        if (showingMovieRes != null && showingMovieRes.getValue() != null) {
            List<Session> insertSessions = new ArrayList<>();
            List<Session> updateSessions = new ArrayList<>();
            List<Movie> updateMovie = new ArrayList<>();
            for (ShowingMovieRes.Value moviceSession : showingMovieRes.getValue()) {
            	//热映影片存库排序序列 start By LiuHJ
        		if (!showingListInDb.contains(moviceSession.getScheduledFilmId())) {
        			//添加至数据库 TODO
        			Sequence sequence = new Sequence();
        			sequence.setMid(moviceSession.getScheduledFilmId());
        			sequence.setType(Sequence.NOWSHING);
        			sequence.setMerge(moviceSession.getScheduledFilmId());
        			sequenceDao.insert(sequence);
        		} else { 
        			showingListInDb.remove(moviceSession.getScheduledFilmId());
        		}
            	
            	
            	//end
                List<String> cinemas = new ArrayList<>();
                for (ShowingMovieRes.Session msession : moviceSession.getSessions()) {
                    if (Strings.isNullOrEmpty(msession.getSessionId())) {
                        continue;
                    }
                    Session session = sessionDao.find(msession.getSessionId());

                    try {
                        if (!cinemas.contains(msession.getCinemaId())) {
                            cinemas.add(msession.getCinemaId());
                        }

                        boolean isadd = (session == null);
                        if (session == null) {
                            session = new Session();
                        }
                        session.setCid(msession.getCinemaId());
                        session.setSid(msession.getSessionId());
                        session.setMid(moviceSession.getScheduledFilmId());
                        session.setScreenName(msession.getScreenName());
                        if (!Strings.isNullOrEmpty(msession.getShowtime())) {
                            session.setStime(msession.getShowtime().replace("T", " "));
                        }

                        String mtype = "";
                        if (msession.getAttributes() != null && msession.getAttributes().size() > 0) {
                            for (ShowingMovieRes.Attributes attributes : msession.getAttributes()) {
                                mtype = mtype + attributes.getDescription() + ",";
                            }
                            mtype = mtype.substring(0, mtype.length() - 1);
                        }
                        session.setStype(mtype);
                        if (isadd) {
                            insertSessions.add(session);
                        } else {
                            updateSessions.add(session);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Movie movie = new Movie();
                movie.setMid(moviceSession.getScheduledFilmId());
                movie.setMstate(Movie.STATE_ZZRY);
                movie.setCinemaCount(cinemas.size());
                updateMovie.add(movie);
            }
            
            //TODO 清除list剩下的元素
            if (showingListInDb != null && showingListInDb.size() > 0) {
            	Map<String, Object> unuseMap = new HashMap<>();
            	unuseMap.put("type", Sequence.NOWSHING);
            	unuseMap.put("list", showingListInDb);
            	sequenceDao.del(unuseMap);
            }

            if (insertSessions.size() > 0) {
                sessionDao.createByList(insertSessions);
            }

            if (updateSessions.size() > 0) {
                sessionDao.updateByList(updateSessions);
            }

            if (updateMovie.size() > 0) {
                moviceDao.updateByList(updateMovie);
            }
        }
        System.out.println("同步正在热映的场次信息.....花费时间:" + (System.currentTimeMillis() - t1));

        System.out.println("正在获取即将上映电影信息");
        t1 = System.currentTimeMillis();
        SoonComeMovieRes soonComeMovieRes = vistaApi.getSoonComeMovie();
        //逻辑同上
        List<String> comeSoonListInDb = sequenceDao.getAll(Sequence.COMESOON); 
        
        if (soonComeMovieRes != null && soonComeMovieRes.getValue() != null) {
            //moviceDao.
            List<Movie> insertMovies = new ArrayList<>();
            for (SoonComeMovieRes.Value val : soonComeMovieRes.getValue()) {
            	
            	//即将上线影片存库排序序列 start By LiuHJ
        		if (!comeSoonListInDb.contains(val.getScheduledFilmId())) {
        			//添加至数据库 TODO
        			Sequence sequence = new Sequence();
        			sequence.setMid(val.getScheduledFilmId());
        			sequence.setType(Sequence.COMESOON);
        			sequence.setMerge(val.getScheduledFilmId());
        			sequenceDao.insert(sequence);
        		} else { 
        			comeSoonListInDb.remove(val.getScheduledFilmId());
        		}
            	//end
            	
            	
                Movie movie = new Movie();
                movie.setMid(val.getScheduledFilmId());
                movie.setMstate(Movie.STATE_JJSY);
                insertMovies.add(movie);
            }
            
          //TODO 清除list剩下的元素
            if (comeSoonListInDb != null && comeSoonListInDb.size() > 0) {
            	Map<String, Object> unuseMap = new HashMap<>();
            	unuseMap.put("type", Sequence.COMESOON);
            	unuseMap.put("list", comeSoonListInDb);
            	sequenceDao.del(unuseMap);
            }
            
            if (insertMovies.size() > 0) {
                moviceDao.updateByList(insertMovies);
            }
        }
        System.out.println("同步即将上映电影信息结束......花费时间:" + (System.currentTimeMillis() - t1));

        System.out.println("正在获取影院信息");
        t1 = System.currentTimeMillis();
        CinemasRes cinemasRes = vistaApi.getCinemas();
        if (cinemasRes != null && cinemasRes.getValue() != null) {
            System.out.println("获取影院信息成功");

            List<Cinema> insertCinemas = new ArrayList<>();
            List<Cinema> updateCinemas = new ArrayList<>();
            for (CinemasRes.Value val : cinemasRes.getValue()) {
                if (Strings.isNullOrEmpty(val.getId())) {
                    continue;
                }
                System.out.println("处理影院:" + val.getName() + "(ID:" + val.getId() + ")的信息");
                Cinema cinema = cinemaDao.find(val.getId());
                try {
                    boolean isadd = (cinema == null);
                    if (cinema == null) {
                        cinema = new Cinema();
                    }
                    cinema.setCid(val.getId());
                    cinema.setCadd(val.getAddress2());
                    cinema.setCname(val.getName());
                    cinema.setLat(val.getLatitude());
                    cinema.setLon(val.getLongitude());
                    cinema.setParkingInfo(val.getParkingInfo());
                    cinema.setCity(val.getAddress1());
                    cinema.setLoyaltyCode(val.getLoyaltyCode());
//                    if (!Strings.isNullOrEmpty(val.getCity())) {
//                        cinema.setCity(val.getCity().split(",")[0]);
//                    }

                    if (isadd) {
                        insertCinemas.add(cinema);
                    } else {
                        updateCinemas.add(cinema);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (insertCinemas.size() > 0) {
                cinemaDao.createByList(insertCinemas);
            }

            if (updateCinemas.size() > 0) {
                cinemaDao.updateByList(updateCinemas);
            }
        }
        System.out.println("同步影院信息结束......花费时间:" + (System.currentTimeMillis() - t1));

        System.out.println("同步信息结束.....");
    }

    @Transactional
    public void synTicketsData() {
        System.out.println("这里开始票类信息到本地数据库");
        List<Session> sessions = sessionDao.getAll();
        List<Session> updateSession = new ArrayList<>();
        long t1 = System.currentTimeMillis();
        Map<String, Integer> cminPrice = new HashMap<>();
        
        //Start By LiuHJ  获取票类分类信息
        List<TicketInfo> ticketInfoList = ticketInfoDao.selectAll();
        Map<String, String> ticketInfoMap = new HashMap<>();
        for (TicketInfo info : ticketInfoList) {
        	ticketInfoMap.put(info.getKey(), info.getHopk());
        }
        
        for (Session session : sessions) {
            //调用接口返回电影票的信息
            GetTicketsForSessionRes res = vistaApi.getTicketsForSession(session.getCid(), session.getSid());
//           System.out.print("movie message=============="+res.toString());
            if (res != null && res.getResponseCode() == 0) {
                Integer minPrice = -1;
                Integer maxPrice = -1;
                ticketDao.delAll(session.getCid(), session.getSid());
                //临时用来做价格的参数,可能后期就删掉了
                Integer mustP = 0;
                
                for (GetTicketsForSessionRes.Ticket t : res.getTickets()) {
                    //临时代码（chenxu 2017-03-31修改）
//                    if (t.getTicketTypeCode().equals("0115")) {
//                        mustP = t.getPriceInCents();
//                    }

                    if (t.getAreaCategoryCode() != null && t.getPriceInCents() > 0 && t.getDescription() != null /*&& "0115".equals(t.getHopk())*/) {
                        //如果当前的比最小的还小,或者最小的还没有值,则赋值
                        if (minPrice == -1 || t.getPriceInCents() < minPrice ) {
                            minPrice = t.getPriceInCents();
                        }

                        if (maxPrice == -1 || t.getPriceInCents() > maxPrice) {
                            maxPrice = t.getPriceInCents();
                        }
                    	
                        Ticket ticket = new Ticket();
//                    	//设置不同票类的价格
                        for (Map.Entry<String, String> entry : ticketInfoMap.entrySet()) {  
                        	if ("1000".equals(entry.getKey()) && entry.getValue().equals(t.getHopk()))  { //标准票
                        		 ticket.setPrice(t.getPriceInCents());
                        	} else if ("1001".equals(entry.getKey()) && entry.getValue().equals(t.getHopk())) {//会员票
                        		ticket.setMemberPrice(t.getPriceInCents());
                        	} else if ("1002".equals(entry.getKey()) && entry.getValue().equals(t.getHopk())) {//会员折扣
                        		ticket.setSalePrice(t.getPriceInCents());
                        	} else if ("1003".equals(entry.getKey()) && entry.getValue().equals(t.getHopk())) { //尊享价
                        		ticket.setZxPrice(t.getPriceInCents());
                        	} else if ("1004".equals(entry.getKey()) && entry.getValue().equals(t.getHopk())) { //兑换券
                        		ticket.setDhqPrice(t.getPriceInCents());
                        	}
                        }  
                    	
                        ticket.setArea(t.getAreaCategoryCode());
                        ticket.setCid(session.getCid());
                        ticket.setSid(session.getSid());
                        ticket.setDesc(t.getDescription());
                        int updateNum = ticketDao.updateTicket(ticket);
                        if (updateNum == 0) {
                        	ticketDao.insert(ticket);                        	
                        }
                    }
                }
                if (minPrice > -1 || maxPrice > -1) {
                    Session temp = new Session();
                    temp.setSid(session.getSid());
                    if (minPrice > -1) {
                        temp.setSprice(minPrice);
                    }
                    if (maxPrice > -1) {
                        temp.setOriginalPrice(maxPrice);
                    }

                    //临时代码
//                    if (mustP > 0) {
//                        temp.setSprice(mustP);
//                        if (mustP < maxPrice) {
//                            temp.setOriginalPrice(maxPrice);
//                        } else {
//                            temp.setOriginalPrice(mustP);
//                        }
//                    }

                    updateSession.add(temp);

                    Integer cp = cminPrice.get(session.getCid());
                    if (cp == null || cp > minPrice) {
                        cminPrice.put(session.getCid(), minPrice);
                    }
                }
            }
        }
        if (updateSession.size() > 0) {
            sessionDao.updateByList(updateSession);
        }
        //同步最小价格信息
        List<Cinema> cinemas = cinemaDao.getAll();
        List<Cinema> updateCinema = new ArrayList<>();
        for (Cinema c : cinemas) {
            Integer cp = cminPrice.get(c.getCid());
            if (cp != null) {
                Cinema temp = new Cinema();
                temp.setCid(c.getCid());
                temp.setMinprice(cp);
                updateCinema.add(temp);
            }
        }
        if (updateCinema.size() > 0) {
            cinemaDao.updateByList(updateCinema);
        }
        System.out.println("同步票类信息结束.....花费时间:" + (System.currentTimeMillis() - t1));
    }
    

}
