package com.lhh.vista.service.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.util.DateTool;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.s2v.AddConcessionWarp;
import com.lhh.vista.customer.s2v.AddTicketWarp;
import com.lhh.vista.customer.s2v.AddTicketWarp.SelectedSeat;
import com.lhh.vista.customer.s2v.AddTicketWarp.TicketType;
import com.lhh.vista.customer.s2v.CompleteOrderWarp;
import com.lhh.vista.customer.s2v.dto.AddOrRemoveConcession;
import com.lhh.vista.customer.v2s.CompleteOrderRes;
import com.lhh.vista.customer.v2s.GetConcessionListRes;
import com.lhh.vista.customer.v2s.MemberValidateRes;
import com.lhh.vista.customer.v2s.OrderRes;
import com.lhh.vista.customer.v2s.StateResult;
import com.lhh.vista.customer.v2s.value.GetTicketsForSessionRes;
import com.lhh.vista.customer.v2s.value.GetTicketsForSessionRes.Ticket;
import com.lhh.vista.customer.v2s.value.SeatLayoutData;
import com.lhh.vista.service.dao.AppUserDao;
import com.lhh.vista.service.dao.AppUserOrderDao;
import com.lhh.vista.service.dao.AppUserRewardDao;
import com.lhh.vista.service.dto.BaseOrderForPay;
import com.lhh.vista.service.dto.BaseUserOrder;
import com.lhh.vista.service.dto.Concession;
import com.lhh.vista.service.dto.SeatInfo;
import com.lhh.vista.service.dto.UserTicket;
import com.lhh.vista.service.model.AppUser;
import com.lhh.vista.service.model.AppUserOrder;
import com.lhh.vista.temp.dao.CinemaDao;
import com.lhh.vista.temp.dao.MovieDao;
import com.lhh.vista.temp.dao.SessionDao;
import com.lhh.vista.temp.dao.TicketInfoDao;
import com.lhh.vista.temp.model.Cinema;
import com.lhh.vista.temp.model.Movie;
import com.lhh.vista.temp.model.Session;
import com.lhh.vista.temp.model.TicketInfo;
import com.lhh.vista.util.Const;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by liu on 2016/12/23.
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private VistaApi vistaApi;

	@Autowired
	private SessionDao sessionDao;

	@Autowired
	private CinemaDao cinemaDao;
	@Autowired
	private MovieDao movieDao;
	@Autowired
	private AppUserOrderDao appUserOrderDao;

	@Autowired
	private AppUserRewardDao appUserRewardDao;

	@Autowired
	private AppUserDao appUserDao;

	@Autowired
	private TicketInfoDao ticketInfoDao;

	@Override
	public List<Concession> getConcession(String userId, Integer orderId) {
		AppUserOrder appUserOrder = appUserOrderDao.find(orderId);
		StateTool.checkState(appUserOrder != null && appUserOrder.getUserId().equals(userId), StateTool.State.FAIL);
		GetConcessionListRes res = vistaApi.getOrderApi().getConcessionList(appUserOrder.getCid());
		List<Concession> list = new ArrayList<>();
		if (res != null && res.getItems().size() > 0) {
			for (GetConcessionListRes.Concession con : res.getItems()) {
				if (con.getDescription().indexOf("储值") < 0 || con.getDescription().indexOf("充值") < 0) {
					list.add(new Concession(con.getId(), con.getDescription(), con.getPrice()));
				}
			}
		}
		return list;
	}

	@Override
	public void setConcession(List<Concession> usercons, String userId, Integer orderId, String mphone) {
		AppUserOrder appUserOrder = appUserOrderDao.find(orderId);
		StateTool.checkState(appUserOrder != null && appUserOrder.getUserId().equals(userId), StateTool.State.FAIL);
		GetConcessionListRes res = vistaApi.getOrderApi().getConcessionList(appUserOrder.getCid());
		// 这里检测一下卖品是不是正确的
		Map<String, GetConcessionListRes.Concession> scons = new HashMap<>();
		List<String> idList = new ArrayList<>();
		for (Concession concession : usercons) {
			idList.add(concession.getId());
		}
		for (GetConcessionListRes.Concession con : res.getItems()) {
			if (idList.contains(con.getId())) {
				scons.put(con.getId(), con);
			}
		}
		String conName = "";
		String cons = "";
		Integer conPrice = 0;
		List<AddOrRemoveConcession> aconList = new ArrayList<>();
		for (int i = 0; i < usercons.size(); i++) {
			Concession con = usercons.get(i);
			AddOrRemoveConcession aorConcession = new AddOrRemoveConcession();
			aorConcession.setItemId(con.getId());
			aorConcession.setQuantity(con.getCount());
			aconList.add(aorConcession);
			GetConcessionListRes.Concession scon = scons.get(con.getId());
			StateTool.checkState(scon != null, StateTool.State.FAIL);

			conName = conName + scon.getDescription() + con.getCount() + "份";
			cons = cons + con.getId() + "-" + con.getCount();
			conPrice = conPrice + con.getCount() * scon.getPrice();

			if (i != usercons.size() - 1) {
				conName = conName + ",";
				cons = cons + ",";
			}
		}

		System.out.println("conName:" + conName);
		System.out.println("cons:" + cons);
		System.out.println("conPrice:" + conPrice);
		AddConcessionWarp addConcessionWarp = new AddConcessionWarp();
		addConcessionWarp.setCinemaId(appUserOrder.getCid());
		addConcessionWarp.setOrderId(appUserOrder.getOrderId());
		addConcessionWarp.setConcessions(aconList);

		StateResult stateResult = vistaApi.getOrderApi().addConcessionList(addConcessionWarp);
		StateTool.checkState(stateResult != null && stateResult.getResult() == 0, StateTool.State.FAIL);

		AppUserOrder temp = new AppUserOrder();
		temp.setId(appUserOrder.getId());

		temp.setTotalPrice(conPrice + appUserOrder.getTicketPrice());
		temp.setConcessionPrice(conPrice);
		temp.setConcessionNames(conName);
		temp.setConcessions(cons);
		temp.setMphone(mphone);
		appUserOrderDao.update(temp);
	}

	@Override
	public Map<String, List<UserTicket>> checkTicket(List<String> areaList, String sid, String memberId) {
		// 此方法只是生成一个临时的订单号,并不是真正的订单,只是为了确认有几个票可用
		long t1 = System.currentTimeMillis();
		StateTool.checkState(!(areaList == null || areaList.size() == 0 || Strings.isNullOrEmpty(sid)
				|| Strings.isNullOrEmpty(memberId)), StateTool.State.FAIL);
		// 再检查sid(场次ID)和cid(电影院ID)
		Session session = sessionDao.find(sid);
		StateTool.checkState(session != null, StateTool.State.FAIL);
		// 这里首先生成订单号,然后直接插入订单即可,如果座位被占,则抛出异常就可以回滚
		String orderId = (System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "")).substring(0, 32);
		// 使用订单号进行会员认证,这里还不能确认怎么做
		StateResult stateResult = vistaApi.getOrderApi().checkUser(memberId, orderId);
		StateTool.checkState(stateResult != null && stateResult.getResult() == 0, StateTool.State.FAIL);
		// 根据订单号和认证会员,去获取当前用户的票务情况
		GetTicketsForSessionRes ticketsForSession = vistaApi.getUserApi().getTicketsForSessionWithUser(session.getCid(),
				sid, orderId);
		StateTool.checkState(ticketsForSession != null && ticketsForSession.getResponseCode() == 0
				&& ticketsForSession.getTickets().size() > 0, StateTool.State.FAIL);
		Map<String, List<UserTicket>> tickets = new HashMap<>();
		// 将电影票信息存储到map中,方便使用
		for (Ticket ticket : ticketsForSession.getTickets()) {
			// 这里看看包含不包含,包含的话,就使用这个票
			if (!areaList.contains(ticket.getAreaCategoryCode())) {
				continue;
			}
			List<UserTicket> ticketsTemp = tickets.get(ticket.getAreaCategoryCode());
			if (ticketsTemp == null) {
				ticketsTemp = new ArrayList<>();
				tickets.put(ticket.getAreaCategoryCode(), ticketsTemp);
			}
			UserTicket userTicket = new UserTicket();
			userTicket.setArea(ticket.getAreaCategoryCode());
			userTicket.setPrice(ticket.getPriceInCents());
			userTicket.setTicketCode(ticket.getTicketTypeCode());
			userTicket.setTicketName(ticket.getDescription());
			ticketsTemp.add(userTicket);
		}
		System.out.println("会员票务处理时间:" + (System.currentTimeMillis() - t1));
		return tickets;
	}

	@Override
	@Transactional
	public Integer createOrder(List<SeatInfo> seatInfoList, String sid, String payment, String memberId,
			String mphone) {

		String orderId = "";
		String hopk = "";
		int bookingFee = 0;
		boolean needCancel = false;
		List<TicketInfo> ticketInfoList = ticketInfoDao.selectAllTicket();
		// 获取用户所属俱乐部信息
		for (TicketInfo ticketInfo : ticketInfoList) {
			if (ticketInfo.getKey().equals(payment)) {
				hopk = ticketInfo.getHopk();
				bookingFee = (int) (ticketInfo.getBookingFee() * 100);
			}
		}

		try {
			long t1 = System.currentTimeMillis();
			StateTool.checkState(!(seatInfoList == null || seatInfoList.size() == 0 || Strings.isNullOrEmpty(sid)
					|| Strings.isNullOrEmpty(memberId)), StateTool.State.ORDER_ITEM_ERROR);
			// 再检查sid(场次ID)和cid(电影院ID)
			Session session = sessionDao.find(sid);
			StateTool.checkState(session != null, StateTool.State.ORDER_SESSION_ERROR);
			Cinema cinema = cinemaDao.find(session.getCid());
			Movie movie = movieDao.find4Session(session.getMid());
			// 这里首先生成订单号,然后直接插入订单即可,如果座位被占,则抛出异常就可以回滚
			orderId = (System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "")).substring(0, 32);
			// 使用订单号进行会员认证
			StateResult stateResult = vistaApi.getOrderApi().checkUser(memberId, orderId);
			StateTool.checkState(stateResult != null && stateResult.getResult() == 0,
					StateTool.State.ORDER_MEMMBER_ERROR);
			// 根据订单号和认证会员,去获取当前用户的票务情况
			GetTicketsForSessionRes ticketsForSession = vistaApi.getUserApi()
					.getTicketsForSessionWithUser(session.getCid(), sid, orderId);

			StateTool.checkState(ticketsForSession != null && ticketsForSession.getResponseCode() == 0
					&& ticketsForSession.getTickets().size() > 0, StateTool.State.ORDER_MEMMBER_TICKET_ERROR);
			Map<String, Ticket> tickets = new HashMap<>();
			// 价格进行匹配
			for (Ticket ticket : ticketsForSession.getTickets()) {
				if (!hopk.equals(ticket.getHopk())) {
					continue;
				}
				Ticket temp = tickets.get(ticket.getAreaCategoryCode());
				if (temp == null) {
					tickets.put(ticket.getAreaCategoryCode(), ticket);
				} else {
					if (temp.getPriceInCents() > ticket.getPriceInCents()) {
						tickets.put(ticket.getAreaCategoryCode(), ticket);
					}
				}
			}
			// 声明总价格
			int ticketPrice = 0;
			// 票的类型,在这里进行统计了
			Map<String, TicketType> ticketTypes = new HashMap<>();
			// 座位信息,也在这里统计好了
			Map<String, SelectedSeat> selectSeats = new HashMap<>();

			for (SeatInfo seatInfo : seatInfoList) {
				Ticket ticket = tickets.get(seatInfo.getAreaCode());
				StateTool.checkState(ticket != null, StateTool.State.ORDER_MEMMBER_TICKET_ERROR);
				ticketPrice = ticketPrice + ticket.getPriceInCents();
				// 根据区域码和位置信息拼装KEY
				StringBuffer sb = new StringBuffer(seatInfo.getAreaCode());
				sb.append("-").append(seatInfo.getRow()).append("-").append(seatInfo.getCol());
				selectSeats.put(sb.toString(), new SelectedSeat(seatInfo.getAreaCode(), seatInfo.getAreaNum(),
						seatInfo.getRow(), seatInfo.getCol()));

				TicketType ticketType = ticketTypes.get(ticket.getTicketTypeCode());
				if (ticketType == null) {
					// ticketType = new TicketType("0001", 1, ticket.getPriceInCents());
					ticketType = new TicketType(ticket.getTicketTypeCode(), 1, ticket.getPriceInCents(),
							bookingFee + "");
					ticketTypes.put(ticket.getTicketTypeCode(), ticketType);
				} else {
					ticketType.setQty(ticketType.getQty() + 1);
				}
			}

			System.out.println("总价是:" + ticketPrice);
			// 从vista方获取座位信息,验证座位是否是可用的
			int checkSeat = 0;
			SeatLayoutData seatLayoutData = vistaApi.getSessionApi().getSeatForSession(session.getCid(), sid);
			String pname = "";

			for (SeatLayoutData.Area area : seatLayoutData.getAreas()) {
				String areaCode = area.getAreaCode();
				for (SeatLayoutData.Row row : area.getRows()) {
					String rowName = row.getPhysicalName();
					for (SeatLayoutData.Seat seat : row.getSeats()) {
						String colName = seat.getId();

						SeatLayoutData.Position p = seat.getPosition();

						StringBuffer sb = new StringBuffer(areaCode);
						sb.append("-").append(p.getRow()).append("-").append(p.getCol());

						SelectedSeat selectedSeat = selectSeats.get(sb.toString());
						// 这里最后检测一下座位状态
						if (selectedSeat != null) {
							checkSeat++;
							StateTool.checkState(seat.getStatus() == 0, StateTool.State.ORDER_SEAT_CAN_NOT_USE_ERROR);

							if (seatLayoutData.getAreas().size() == 1) {
								pname = pname + rowName + "排" + colName + "座  ";
							} else {
								pname = pname + area.getAreaCode() + "区" + rowName + "排" + colName + "座  ";
							}
						}

						// 如果处理完毕,直接跳出
						if (checkSeat == selectSeats.size()) {
							break;
						}
					}
					// 如果处理完毕,直接跳出
					if (checkSeat == selectSeats.size()) {
						break;
					}
				}
				// 如果处理完毕,直接跳出
				if (checkSeat == selectSeats.size()) {
					break;
				}
			}

			StateTool.checkState(checkSeat == selectSeats.size(), StateTool.State.ORDER_SEAT_ERROR);
			// 向Vista系统提交锁座请求
			AddTicketWarp addTicketWarp = new AddTicketWarp();
			addTicketWarp.setOrderId(orderId);
			addTicketWarp.setCinemaId(session.getCid());
			addTicketWarp.setSessionId(sid);
			addTicketWarp.setTicketTypes(new ArrayList<>(ticketTypes.values()));
			addTicketWarp.setSelectedSeats(new ArrayList<>(selectSeats.values()));
			OrderRes addTicketRes = vistaApi.getOrderApi().addTicket(addTicketWarp);

			// 先检查接口返回情况
			StateTool.checkState(addTicketRes.getResult() == 0, StateTool.State.ORDER_ADD_TICKET_ERROR);

			needCancel = true;

			AppUserOrder appUserOrder = new AppUserOrder();
			appUserOrder.setOrderId(orderId);
			appUserOrder.setUserId(memberId);
			appUserOrder.setTotalPrice(addTicketRes.getOrder().getTotalPrice());
			appUserOrder.setConcessionPrice(0);
			appUserOrder.setTicketPrice(ticketPrice + (bookingFee * addTicketRes.getOrder().getTotalOrderCount()));
			appUserOrder.setCid(session.getCid());
			appUserOrder.setSid(sid);
			appUserOrder.setMid(session.getMid());
			appUserOrder.setState(AppUserOrder.STATE_WAIT_PAY);
			appUserOrder.setCreateTime(System.currentTimeMillis());
			appUserOrder.setCadd(cinema.getCadd());
			appUserOrder.setMphone(mphone);

			appUserOrder.setPlaceNames(pname);
			String place = "";
			for (String key : selectSeats.keySet()) {
				place = place + key + ",";
			}
			place.substring(0, place.length() - 1);
			appUserOrder.setPlaces(place);
			appUserOrder.setCname(cinema.getCname());
			appUserOrder.setMname(movie.getMname());
			appUserOrder.setSname(session.getScreenName());
			appUserOrder.setStime(session.getStime());

			appUserOrderDao.create(appUserOrder);

			System.out.println("订单处理时间:" + (System.currentTimeMillis() - t1));
			return appUserOrder.getId();
		} catch (Exception e) {
			e.printStackTrace();
			if (!Strings.isNullOrEmpty(orderId) && needCancel) {
				vistaApi.getOrderApi().cancelOrder(orderId);
			}
			if (e instanceof StateTool.StateException) {
				StateTool.checkState(false, ((StateTool.StateException) e).getState());
			} else {
				StateTool.checkState(false, StateTool.State.FAIL);
			}
		}
		return -1;
	}

	@Override
	public PagerResponse<BaseUserOrder> getUserOrder(PagerRequest pagerRequest, String userId, Integer otype) {
		StateTool.checkState(!Strings.isNullOrEmpty(userId), StateTool.State.FAIL);
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("otype", otype);
		PagerResponse<BaseUserOrder> pagerResponse = appUserOrderDao.getPager(pagerRequest, param);
		for (BaseUserOrder order : pagerResponse.getRows()) {

			// 返回买票数
			String palaces = order.getPlaces();
			int ticketCount = 0;
			Pattern p = Pattern.compile(",");
			Matcher m = p.matcher(palaces);
			while (m.find()) {
				ticketCount++;
			}
			order.setTicketCount(ticketCount);

			Movie movie = movieDao.find4Session(order.getMid());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date d = null;
			try {
				d = sdf.parse(order.getStime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - d.getTime() > movie.getRunTime() * 60 * 1000) {
				order.setType(2);
			} else {
				order.setType(1);
			}

		}
		return pagerResponse;
	}

	@Override
	public AppUserOrder getOrder(String userId, Integer orderId) {
		AppUserOrder appUserOrder = appUserOrderDao.find(orderId);
		String palaces = appUserOrder.getPlaces();
		int ticketCount = 0;
		Pattern p = Pattern.compile(",");
		Matcher m = p.matcher(palaces);
		while (m.find()) {
			ticketCount++;
		}
		// 设置影片结束时间
		Movie movie = movieDao.find4Session(appUserOrder.getMid());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(appUserOrder.getStime());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, movie.getRunTime());// 24小时制
		date = cal.getTime();
		appUserOrder.setEtime(format.format(date));
		appUserOrder.setTicketCount(ticketCount);
		StateTool.checkState(appUserOrder != null && appUserOrder.getUserId().equals(userId), StateTool.State.FAIL);
		return appUserOrder;
	}

	@Override
	public AppUserOrder getOrder(String orderId) {
		AppUserOrder appUserOrder = appUserOrderDao.find(orderId);
		return appUserOrder;
	}

	@Override
	public BaseOrderForPay getOrderForPay(String userId, Integer orderId) {
		AppUserOrder appUserOrder = appUserOrderDao.find(orderId);

		StateTool.checkState(appUserOrder != null && appUserOrder.getUserId().equals(userId), StateTool.State.FAIL);
		BaseOrderForPay r = new BaseOrderForPay();
		r.setMname(appUserOrder.getMname());
		r.setId(orderId);
		r.setCid(appUserOrder.getCid());
		r.setOrderId(appUserOrder.getOrderId());
		r.setCreateTime(appUserOrder.getCreateTime());
		r.setTotalPrice(appUserOrder.getTotalPrice());
		return r;
	}

	@Override
	@Transactional
	public AppUserOrder completeExchangeOrder(String userId, Integer orderId, List<String> codes, String payment) {
		AppUserOrder appUserOrder = appUserOrderDao.find(orderId);
		StateTool.checkState(appUserOrder != null && appUserOrder.getUserId().equals(userId), StateTool.State.FAIL);
		MemberValidateRes validateRes = vistaApi.getUserApi().checkUser(userId, true);
		AppUser user = appUserDao.getByMemberId(appUserOrder.getUserId());

		StateTool.checkState(!Strings.isNullOrEmpty(validateRes.getMember().getCardNumber())
				&& !Strings.isNullOrEmpty(validateRes.getMember().getExpiryDate()), StateTool.State.FAIL);

		// 取消锁座
		vistaApi.getOrderApi().cancelOrder(appUserOrder.getOrderId());

		// 重新锁座
		List<SeatInfo> seatInfoList = new ArrayList<>();
		String[] seatInfoArr = appUserOrder.getPlaces().split(",");
		for (int i = 0; i < seatInfoArr.length; i++) {
			if (seatInfoArr[i].length() != 0) {
				String[] seatTempArr = seatInfoArr[i].split("-");
				SeatInfo seatInfo = new SeatInfo();
				seatInfo.setAreaCode(seatTempArr[0]);
				seatInfo.setRow(Integer.parseInt(seatTempArr[1]));
				seatInfo.setCol(Integer.parseInt(seatTempArr[2]));
				seatInfoList.add(seatInfo);
			}
		}

		// 订单
		String voucherHopk = "";
		int voucherBookingFee = 0;
		String hopk = "";
		int bookingFee = 0;
		boolean needCancel = false;
		List<TicketInfo> ticketInfoList = ticketInfoDao.selectAllTicket();
		// 获取用户所属俱乐部信息
		for (TicketInfo ticketInfo : ticketInfoList) {
			if (ticketInfo.getKey().equals(payment)) {
				hopk = ticketInfo.getHopk();
				bookingFee = (int) (ticketInfo.getBookingFee() * 100);
			}
			if (ticketInfo.getKey().equals(Const.DHQ)) {
				voucherHopk = ticketInfo.getHopk();
				voucherBookingFee = (int) (ticketInfo.getBookingFee() * 100);
			}
		}

		long t1 = System.currentTimeMillis();
		Session session = sessionDao.find(appUserOrder.getSid());
		StateTool.checkState(session != null, StateTool.State.ORDER_SESSION_ERROR);
		Cinema cinema = cinemaDao.find(session.getCid());
		Movie movie = movieDao.find4Session(session.getMid());
		StateResult stateResult = vistaApi.getOrderApi().checkUser(user.getMemberId(), appUserOrder.getOrderId());
		StateTool.checkState(stateResult != null && stateResult.getResult() == 0, StateTool.State.ORDER_MEMMBER_ERROR);
		// 根据订单号和认证会员,去获取当前用户的票务情况
		GetTicketsForSessionRes ticketsForSession = vistaApi.getUserApi().getTicketsForSessionWithUser(session.getCid(),
				appUserOrder.getSid(), appUserOrder.getOrderId());

		StateTool.checkState(ticketsForSession != null && ticketsForSession.getResponseCode() == 0
				&& ticketsForSession.getTickets().size() > 0, StateTool.State.ORDER_MEMMBER_TICKET_ERROR);
		Map<String, Ticket> tickets = new HashMap<>();
		// 价格进行匹配 优先匹配券类
		int idx = 0;
		for (Ticket ticket : ticketsForSession.getTickets()) {
			// 验证是否符合所选两种票类
			if (!hopk.equals(ticket.getHopk()) && !voucherHopk.equals(ticket.getHopk())) {
				continue;
			}
			for (int j = 0; j < codes.size(); j++) {
				// 优先验证券码
				if (codes.size() != 0 && voucherHopk.equals(ticket.getHopk())) {
					Ticket tempTicket = new Ticket();
					tempTicket.setAreaCategoryCode(ticket.getAreaCategoryCode());
					tempTicket.setHopk(ticket.getHopk());
					tempTicket.setPriceInCents(ticket.getPriceInCents());
					tempTicket.setTicketTypeCode(ticket.getTicketTypeCode());
					tempTicket.setDescription(ticket.getDescription());
					tempTicket.setBarcode(codes.get(j));
					// ticket.setBarcode(codes.get(j));
					tickets.put(ticket.getAreaCategoryCode() + idx, tempTicket);
					// codes.remove(0);
					idx++;
					continue;
				} else if (hopk.equals(ticket.getHopk())) {
					tickets.put(ticket.getAreaCategoryCode(), ticket);
				}
			}

		}
		int ticketPrice = 0;
		Map<String, TicketType> ticketTypes = new HashMap<>();
		Map<String, SelectedSeat> selectSeats = new HashMap<>();

		int index = 0;
		for (SeatInfo seatInfo : seatInfoList) {
			Ticket ticket = null;
			if (index < idx) {
				ticket = tickets.get(seatInfo.getAreaCode() + index);
				ticketPrice = ticketPrice + ticket.getPriceInCents() + voucherBookingFee;
				++index;
			} else {
				ticket = tickets.get(seatInfo.getAreaCode());
				ticketPrice = ticketPrice + ticket.getPriceInCents() + bookingFee;
			}
			StateTool.checkState(ticket != null, StateTool.State.ORDER_MEMMBER_TICKET_ERROR);
			// 根据区域码和位置信息拼装KEY
			StringBuffer sb = new StringBuffer(seatInfo.getAreaCode());
			sb.append("-").append(seatInfo.getRow()).append("-").append(seatInfo.getCol());
			selectSeats.put(sb.toString(), new SelectedSeat(seatInfo.getAreaCode(), seatInfo.getAreaNum(),
					seatInfo.getRow(), seatInfo.getCol()));

			TicketType ticketType = ticketTypes.get(ticket.getTicketTypeCode());
			if (ticketType == null) {
				// ticketType = new TicketType("0001", 1, ticket.getPriceInCents());
				if (!Strings.isNullOrEmpty(ticket.getBarcode())) {
					ticketType = new TicketType(ticket.getTicketTypeCode(), 1, ticket.getPriceInCents(),
							voucherBookingFee + "", ticket.getBarcode());
					ticketTypes.put(ticket.getTicketTypeCode() + index, ticketType);
				} else {
					ticketType = new TicketType(ticket.getTicketTypeCode(), 1, ticket.getPriceInCents(),
							bookingFee + "");
					ticketTypes.put(ticket.getTicketTypeCode(), ticketType);
				}
			} else {
				ticketType.setQty(ticketType.getQty() + 1);
			}
		}

		System.out.println("总价是:" + ticketPrice);
		// 从vista方获取座位信息,验证座位是否是可用的
		int checkSeat = 0;
		SeatLayoutData seatLayoutData = vistaApi.getSessionApi().getSeatForSession(session.getCid(),
				appUserOrder.getSid());
		String pname = "";

		for (SeatLayoutData.Area area : seatLayoutData.getAreas()) {
			String areaCode = area.getAreaCode();
			for (SeatLayoutData.Row row : area.getRows()) {
				String rowName = row.getPhysicalName();
				for (SeatLayoutData.Seat seat : row.getSeats()) {
					String colName = seat.getId();

					SeatLayoutData.Position p = seat.getPosition();

					StringBuffer sb = new StringBuffer(areaCode);
					sb.append("-").append(p.getRow()).append("-").append(p.getCol());

					SelectedSeat selectedSeat = selectSeats.get(sb.toString());
					// 这里最后检测一下座位状态
					if (selectedSeat != null) {
						checkSeat++;

						StateTool.checkState(seat.getStatus() == 0, StateTool.State.ORDER_SEAT_CAN_NOT_USE_ERROR);
						selectedSeat.setAreaNum(Short.parseShort(p.getAreaNum() + ""));
						if (seatLayoutData.getAreas().size() == 1) {
							pname = pname + rowName + "排" + colName + "座  ";
						} else {
							pname = pname + area.getAreaCode() + "区" + rowName + "排" + colName + "座  ";
						}
					}
					if (checkSeat == selectSeats.size()) {
						break;
					}
				}
				if (checkSeat == selectSeats.size()) {
					break;
				}
			}
			if (checkSeat == selectSeats.size()) {
				break;
			}
		}

		StateTool.checkState(checkSeat == selectSeats.size(), StateTool.State.ORDER_SEAT_ERROR);
		// 向Vista系统提交锁座请求
		AddTicketWarp addTicketWarp = new AddTicketWarp();
		addTicketWarp.setOrderId(appUserOrder.getOrderId());
		addTicketWarp.setCinemaId(session.getCid());
		addTicketWarp.setSessionId(appUserOrder.getSid());
		addTicketWarp.setTicketTypes(new ArrayList<>(ticketTypes.values()));
		addTicketWarp.setSelectedSeats(new ArrayList<>(selectSeats.values()));
		OrderRes addTicketRes = vistaApi.getOrderApi().addTicket(addTicketWarp);

		// 先检查接口返回情况
		StateTool.checkState(addTicketRes.getResult() == 0, StateTool.State.ORDER_ADD_TICKET_ERROR);

		needCancel = true;

		AppUserOrder appUserOrderT = new AppUserOrder();
		appUserOrderT.setOrderId(appUserOrder.getOrderId());
		appUserOrderT.setUserId(user.getMemberId());
		// appUserOrderT.setTotalPrice(addTicketRes.getOrder().getTotalPrice());
		appUserOrderT.setTotalPrice(ticketPrice);
		appUserOrderT.setConcessionPrice(0);
		appUserOrderT.setTicketPrice(ticketPrice);
		// appUserOrderT.setTicketPrice(ticketPrice + (voucherBookingFee * idx)
		// +(bookingFee * (addTicketRes.getOrder().getTotalOrderCount() - idx)));
		appUserOrderT.setCid(session.getCid());
		appUserOrderT.setSid(appUserOrder.getSid());
		appUserOrderT.setMid(session.getMid());
		appUserOrderT.setState(AppUserOrder.STATE_WAIT_PAY);
		appUserOrderT.setCreateTime(System.currentTimeMillis());
		appUserOrderT.setCadd(cinema.getCadd());
		appUserOrderT.setConcessions(appUserOrder.getConcessions());
		appUserOrderT.setConcessionNames(appUserOrder.getConcessionNames());
		appUserOrderT.setConcessionPrice(appUserOrder.getConcessionPrice());
		appUserOrderT.setMphone(user.getMphone());

		appUserOrderT.setPlaceNames(pname);
		String place = "";
		for (String key : selectSeats.keySet()) {
			place = place + key + ",";
		}
		place.substring(0, place.length() - 1);
		appUserOrderT.setPlaces(place);
		appUserOrderT.setCname(cinema.getCname());
		appUserOrderT.setMname(movie.getMname());
		appUserOrderT.setSname(session.getScreenName());
		appUserOrderT.setStime(session.getStime());

		appUserOrderDao.create(appUserOrderT);

		System.out.println("订单处理时间:" + (System.currentTimeMillis() - t1));

		Integer orderIdYE = appUserOrderT.getId();

		AppUserOrder appUserOrderNew = appUserOrderDao.find(orderIdYE);

		// 记录一下上一个order的主键 以便更新 并且删除上一个订单
		int lastId = appUserOrder.getId();
		String lastOrderId = appUserOrder.getOrderId();
		appUserOrderDao.del(lastId);
		appUserOrderNew.setId(lastId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", lastOrderId);
		map.put("order", appUserOrderNew);
		appUserOrderDao.update(map);

		return appUserOrderNew;

	}

	// 含有券类的余额支付
	@Override
	@Transactional
	public AppUserOrder completeVoucherOrder(String userId, Integer orderId, List<String> codes) {

		AppUserOrder appUserOrder = appUserOrderDao.find(orderId);
		StateTool.checkState(appUserOrder != null && appUserOrder.getUserId().equals(userId), StateTool.State.FAIL);
		MemberValidateRes validateRes = vistaApi.getUserApi().checkUser(userId, true);
		AppUser user = appUserDao.getByMemberId(appUserOrder.getUserId());

		StateTool.checkState(!Strings.isNullOrEmpty(validateRes.getMember().getCardNumber())
				&& !Strings.isNullOrEmpty(validateRes.getMember().getExpiryDate()), StateTool.State.FAIL);

		// 取消锁座
		vistaApi.getOrderApi().cancelOrder(appUserOrder.getOrderId());

		// 重新锁座
		List<SeatInfo> seatInfoList = new ArrayList<>();
		String[] seatInfoArr = appUserOrder.getPlaces().split(",");
		for (int i = 0; i < seatInfoArr.length; i++) {
			if (seatInfoArr[i].length() != 0) {
				String[] seatTempArr = seatInfoArr[i].split("-");
				SeatInfo seatInfo = new SeatInfo();
				seatInfo.setAreaCode(seatTempArr[0]);
				seatInfo.setRow(Integer.parseInt(seatTempArr[1]));
				seatInfo.setCol(Integer.parseInt(seatTempArr[2]));
				seatInfoList.add(seatInfo);
			}
		}

		// 订单
		String voucherHopk = "";
		int voucherBookingFee = 0;
		String hopk = "";
		int bookingFee = 0;
		boolean needCancel = false;
		List<TicketInfo> ticketInfoList = ticketInfoDao.selectAllTicket();
		// 获取用户所属俱乐部信息
		for (TicketInfo ticketInfo : ticketInfoList) {
			if (ticketInfo.getKey().equals(Const.HYZKPRICE)) {
				hopk = ticketInfo.getHopk();
				bookingFee = (int) (ticketInfo.getBookingFee() * 100);
			}
			if (ticketInfo.getKey().equals(Const.DHQ)) {
				voucherHopk = ticketInfo.getHopk();
				voucherBookingFee = (int) (ticketInfo.getBookingFee() * 100);
			}
		}

		long t1 = System.currentTimeMillis();
		Session session = sessionDao.find(appUserOrder.getSid());
		StateTool.checkState(session != null, StateTool.State.ORDER_SESSION_ERROR);
		Cinema cinema = cinemaDao.find(session.getCid());
		Movie movie = movieDao.find4Session(session.getMid());
		StateResult stateResult = vistaApi.getOrderApi().checkUser(user.getMemberId(), appUserOrder.getOrderId());
		StateTool.checkState(stateResult != null && stateResult.getResult() == 0, StateTool.State.ORDER_MEMMBER_ERROR);
		// 根据订单号和认证会员,去获取当前用户的票务情况
		GetTicketsForSessionRes ticketsForSession = vistaApi.getUserApi().getTicketsForSessionWithUser(session.getCid(),
				appUserOrder.getSid(), appUserOrder.getOrderId());

		StateTool.checkState(ticketsForSession != null && ticketsForSession.getResponseCode() == 0
				&& ticketsForSession.getTickets().size() > 0, StateTool.State.ORDER_MEMMBER_TICKET_ERROR);
		Map<String, Ticket> tickets = new HashMap<>();
		// 价格进行匹配 优先匹配券类
		int idx = 0;
		for (Ticket ticket : ticketsForSession.getTickets()) {
			// 验证是否符合所两种票类
			if (!hopk.equals(ticket.getHopk()) && !voucherHopk.equals(ticket.getHopk())) {
				continue;
			}
			for (int j = 0; j < codes.size(); j++) {
				// 优先验证券码
				if (codes.size() != 0 && voucherHopk.equals(ticket.getHopk())) {
					Ticket tempTicket = new Ticket();
					tempTicket.setAreaCategoryCode(ticket.getAreaCategoryCode());
					tempTicket.setHopk(ticket.getHopk());
					tempTicket.setPriceInCents(ticket.getPriceInCents());
					tempTicket.setTicketTypeCode(ticket.getTicketTypeCode());
					tempTicket.setDescription(ticket.getDescription());
					tempTicket.setBarcode(codes.get(j));
					// ticket.setBarcode(codes.get(j));
					tickets.put(ticket.getAreaCategoryCode() + idx, tempTicket);
					// codes.remove(0);
					idx++;
					continue;
				} else if (hopk.equals(ticket.getHopk())) {
					tickets.put(ticket.getAreaCategoryCode(), ticket);
				}
			}

		}
		int ticketPrice = 0;
		Map<String, TicketType> ticketTypes = new HashMap<>();
		Map<String, SelectedSeat> selectSeats = new HashMap<>();

		int index = 0;
		for (SeatInfo seatInfo : seatInfoList) {
			Ticket ticket = null;
			if (index < idx) {
				ticket = tickets.get(seatInfo.getAreaCode() + index);
				ticketPrice = ticketPrice + ticket.getPriceInCents() + voucherBookingFee;
				++index;
			} else {
				ticket = tickets.get(seatInfo.getAreaCode());
				ticketPrice = ticketPrice + ticket.getPriceInCents() + bookingFee;
			}
			StateTool.checkState(ticket != null, StateTool.State.ORDER_MEMMBER_TICKET_ERROR);
			// 根据区域码和位置信息拼装KEY
			StringBuffer sb = new StringBuffer(seatInfo.getAreaCode());
			sb.append("-").append(seatInfo.getRow()).append("-").append(seatInfo.getCol());
			selectSeats.put(sb.toString(), new SelectedSeat(seatInfo.getAreaCode(), seatInfo.getAreaNum(),
					seatInfo.getRow(), seatInfo.getCol()));

			TicketType ticketType = ticketTypes.get(ticket.getTicketTypeCode());
			if (ticketType == null) {
				// ticketType = new TicketType("0001", 1, ticket.getPriceInCents());
				if (!Strings.isNullOrEmpty(ticket.getBarcode())) {
					ticketType = new TicketType(ticket.getTicketTypeCode(), 1, ticket.getPriceInCents(),
							voucherBookingFee + "", ticket.getBarcode());
					ticketTypes.put(ticket.getTicketTypeCode() + index, ticketType);
				} else {
					ticketType = new TicketType(ticket.getTicketTypeCode(), 1, ticket.getPriceInCents(),
							bookingFee + "");
					ticketTypes.put(ticket.getTicketTypeCode(), ticketType);
				}
			} else {
				ticketType.setQty(ticketType.getQty() + 1);
			}
		}

		System.out.println("总价是:" + ticketPrice);
		// 从vista方获取座位信息,验证座位是否是可用的
		int checkSeat = 0;
		SeatLayoutData seatLayoutData = vistaApi.getSessionApi().getSeatForSession(session.getCid(),
				appUserOrder.getSid());
		String pname = "";

		for (SeatLayoutData.Area area : seatLayoutData.getAreas()) {
			String areaCode = area.getAreaCode();
			for (SeatLayoutData.Row row : area.getRows()) {
				String rowName = row.getPhysicalName();
				for (SeatLayoutData.Seat seat : row.getSeats()) {
					String colName = seat.getId();

					SeatLayoutData.Position p = seat.getPosition();

					StringBuffer sb = new StringBuffer(areaCode);
					sb.append("-").append(p.getRow()).append("-").append(p.getCol());

					SelectedSeat selectedSeat = selectSeats.get(sb.toString());
					// 这里最后检测一下座位状态
					if (selectedSeat != null) {
						checkSeat++;

						StateTool.checkState(seat.getStatus() == 0, StateTool.State.ORDER_SEAT_CAN_NOT_USE_ERROR);
						selectedSeat.setAreaNum(Short.parseShort(p.getAreaNum() + ""));
						if (seatLayoutData.getAreas().size() == 1) {
							pname = pname + rowName + "排" + colName + "座  ";
						} else {
							pname = pname + area.getAreaCode() + "区" + rowName + "排" + colName + "座  ";
						}
					}
					if (checkSeat == selectSeats.size()) {
						break;
					}
				}
				if (checkSeat == selectSeats.size()) {
					break;
				}
			}
			if (checkSeat == selectSeats.size()) {
				break;
			}
		}

		StateTool.checkState(checkSeat == selectSeats.size(), StateTool.State.ORDER_SEAT_ERROR);
		// 向Vista系统提交锁座请求
		AddTicketWarp addTicketWarp = new AddTicketWarp();
		addTicketWarp.setOrderId(appUserOrder.getOrderId());
		addTicketWarp.setCinemaId(session.getCid());
		addTicketWarp.setSessionId(appUserOrder.getSid());
		addTicketWarp.setTicketTypes(new ArrayList<>(ticketTypes.values()));
		addTicketWarp.setSelectedSeats(new ArrayList<>(selectSeats.values()));
		OrderRes addTicketRes = vistaApi.getOrderApi().addTicket(addTicketWarp);

		// 先检查接口返回情况
		StateTool.checkState(addTicketRes.getResult() == 0, StateTool.State.ORDER_ADD_TICKET_ERROR);
		needCancel = true;
		// 重新添加卖品

		if (!Strings.isNullOrEmpty(appUserOrder.getConcessions())) {
			List<Concession> consList = new ArrayList<>();
			String[] concessionsArr = appUserOrder.getConcessions().split(",");
			for (int i = 0; i < concessionsArr.length; i++) {
				String[] tempArr = concessionsArr[i].split("-");
				Concession concession = new Concession();
				concession.setId(tempArr[0]);
				concession.setCount(Integer.parseInt(tempArr[1]));
				consList.add(concession);
			}
			setConcession(consList, userId, orderId, appUserOrder.getMphone());
		}

		AppUserOrder appUserOrderT = new AppUserOrder();
		appUserOrderT.setOrderId(appUserOrder.getOrderId());
		appUserOrderT.setUserId(user.getMemberId());
		// appUserOrderT.setTotalPrice(addTicketRes.getOrder().getTotalPrice());
		appUserOrderT.setTotalPrice(addTicketRes.getOrder().getTotalPrice() + appUserOrder.getConcessionPrice());
		appUserOrderT.setConcessionPrice(appUserOrder.getConcessionPrice());
		appUserOrderT.setTicketPrice(ticketPrice);
		// appUserOrderT.setTicketPrice(ticketPrice + (voucherBookingFee * idx)
		// +(bookingFee * (addTicketRes.getOrder().getTotalOrderCount() - idx)));
		appUserOrderT.setCid(session.getCid());
		appUserOrderT.setSid(appUserOrder.getSid());
		appUserOrderT.setMid(session.getMid());
		appUserOrderT.setState(AppUserOrder.STATE_WAIT_PAY);
		appUserOrderT.setCreateTime(System.currentTimeMillis());
		appUserOrderT.setCadd(cinema.getCadd());
		appUserOrderT.setConcessions(appUserOrder.getConcessions());
		appUserOrderT.setConcessionNames(appUserOrder.getConcessionNames());
		appUserOrderT.setConcessionPrice(appUserOrder.getConcessionPrice());
		appUserOrderT.setMphone(user.getMphone());

		appUserOrderT.setPlaceNames(pname);
		String place = "";
		for (String key : selectSeats.keySet()) {
			place = place + key + ",";
		}
		place.substring(0, place.length() - 1);
		appUserOrderT.setPlaces(place);
		appUserOrderT.setCname(cinema.getCname());
		appUserOrderT.setMname(movie.getMname());
		appUserOrderT.setSname(session.getScreenName());
		appUserOrderT.setStime(session.getStime());

		appUserOrderDao.create(appUserOrderT);

		System.out.println("订单处理时间:" + (System.currentTimeMillis() - t1));

		Integer orderIdYE = appUserOrderT.getId();

		AppUserOrder appUserOrderNew = appUserOrderDao.find(orderIdYE);

		// 记录一下上一个order的主键 以便更新 并且删除上一个订单
		int lastId = appUserOrder.getId();
		String lastOrderId = appUserOrder.getOrderId();
		appUserOrderDao.del(lastId);

		// 余额支付end
		String ed = validateRes.getMember().getExpiryDate();
		Date d = new Date(Long.parseLong(ed.substring(ed.indexOf("(") + 1, ed.indexOf(")"))));

		CompleteOrderWarp warp = new CompleteOrderWarp();
		warp.setOrderId(appUserOrderNew.getOrderId());
		warp.setPerformPayment(true);
		warp.setCustomerPhone(appUserOrder.getMphone());
		CompleteOrderWarp.PaymentInfo paymentInfo = new CompleteOrderWarp.PaymentInfo();
		paymentInfo.setCardNumber(validateRes.getMember().getCardNumber());
		paymentInfo.setCardType(CompleteOrderWarp.CARD_TYPE_POYAL);
		paymentInfo.setPaymentValueCents(appUserOrderNew.getTotalPrice());
		paymentInfo.setPaymentTenderCategory(CompleteOrderWarp.PT_CARD);
		paymentInfo.setCardExpiryYear(DateTool.dateToString(d, "yyyy"));
		paymentInfo.setCardExpiryMonth(DateTool.dateToString(d, "MM"));
		warp.setPaymentInfo(paymentInfo);
		CompleteOrderRes res = vistaApi.getOrderApi().completeOrder(warp);
		StateTool.checkState(res != null, StateTool.State.FAIL);

		appUserOrderNew.setId(lastId);
		appUserOrderNew.setState(AppUserOrder.STATE_ORDER_SUCCESS);
		appUserOrderNew.setVistaBookingId(res.getVistaBookingId());
		appUserOrderNew.setVistaBookingNumber(res.getVistaBookingNumber());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", lastOrderId);
		map.put("order", appUserOrderNew);
		appUserOrderDao.update(map);

		return appUserOrderNew;
	}

	@Override
	@Transactional
	public AppUserOrder completeOrder(String userId, Integer orderId, Integer payType, String pin) {
		StateTool.checkState(payType == 2 || payType == 1 || payType == 3 || payType == 4, StateTool.State.FAIL);
		AppUserOrder appUserOrder = appUserOrderDao.find(orderId);
		StateTool.checkState(appUserOrder != null && appUserOrder.getUserId().equals(userId), StateTool.State.FAIL);

		System.out.println("==============================payType : " + payType + "===============================");
		if (payType == 1) {
			StateTool.checkState(!Strings.isNullOrEmpty(pin), StateTool.State.ORDER_PIN_ERROR);
			System.out.println("使用余额支付");
			MemberValidateRes validateRes = vistaApi.getUserApi().checkUser(userId, true);
			// 進行PIN碼驗證 會員服務器+本地服務器兩次驗證
			AppUser user = appUserDao.getByMemberId(appUserOrder.getUserId());
			String server_pin = validateRes.getMember().getPin();
			if ("".equals(server_pin) || null == server_pin) {
				server_pin = user.getPin();
			}
			// 会员服务器与app服务器pin码同步
			if (!"".equals(server_pin) && null != server_pin) {
				if (user.getPin() != server_pin) {
					user.setPin(server_pin);
					appUserDao.update(user);
				}
			}

			StateTool.checkState(pin.equals(server_pin), StateTool.State.ORDER_PIN_ERROR);
			// 看看有没有会员卡
			StateTool.checkState(!Strings.isNullOrEmpty(validateRes.getMember().getCardNumber())
					&& !Strings.isNullOrEmpty(validateRes.getMember().getExpiryDate()), StateTool.State.FAIL);
			// 验证余额够不够
			StateTool.checkState(validateRes.obtainBalance() > (appUserOrder.getTotalPrice() / 100),
					StateTool.State.BLANCE_ERR);

			// 余额支付逻辑start 取消锁座
			vistaApi.getOrderApi().cancelOrder(appUserOrder.getOrderId());

			// 重新锁座
			List<SeatInfo> seatInfoList = new ArrayList<>();
			String[] seatInfoArr = appUserOrder.getPlaces().split(",");
			for (int i = 0; i < seatInfoArr.length; i++) {
				if (seatInfoArr[i].length() != 0) {
					String[] seatTempArr = seatInfoArr[i].split("-");
					SeatInfo seatInfo = new SeatInfo();
					seatInfo.setAreaCode(seatTempArr[0]);
					seatInfo.setRow(Integer.parseInt(seatTempArr[1]));
					seatInfo.setCol(Integer.parseInt(seatTempArr[2]));
					seatInfoList.add(seatInfo);
				}
			}

			// 订单
			String hopk = "";
			int bookingFee = 0;
			boolean needCancel = false;
			List<TicketInfo> ticketInfoList = ticketInfoDao.selectAllTicket();
			// 获取用户所属俱乐部信息
			for (TicketInfo ticketInfo : ticketInfoList) {
				if (ticketInfo.getKey().equals("1002")) {
					hopk = ticketInfo.getHopk();
					bookingFee = (int) (ticketInfo.getBookingFee() * 100);
				}
			}

			long t1 = System.currentTimeMillis();
			Session session = sessionDao.find(appUserOrder.getSid());
			StateTool.checkState(session != null, StateTool.State.ORDER_SESSION_ERROR);
			Cinema cinema = cinemaDao.find(session.getCid());
			Movie movie = movieDao.find4Session(session.getMid());
			StateResult stateResult = vistaApi.getOrderApi().checkUser(user.getMemberId(), appUserOrder.getOrderId());
			StateTool.checkState(stateResult != null && stateResult.getResult() == 0,
					StateTool.State.ORDER_MEMMBER_ERROR);
			// 根据订单号和认证会员,去获取当前用户的票务情况
			GetTicketsForSessionRes ticketsForSession = vistaApi.getUserApi()
					.getTicketsForSessionWithUser(session.getCid(), appUserOrder.getSid(), appUserOrder.getOrderId());

			StateTool.checkState(ticketsForSession != null && ticketsForSession.getResponseCode() == 0
					&& ticketsForSession.getTickets().size() > 0, StateTool.State.ORDER_MEMMBER_TICKET_ERROR);
			Map<String, Ticket> tickets = new HashMap<>();
			// 价格进行匹配
			for (Ticket ticket : ticketsForSession.getTickets()) {
				if (!hopk.equals(ticket.getHopk())) {
					continue;
				}
				Ticket temp = tickets.get(ticket.getAreaCategoryCode());
				if (temp == null) {
					tickets.put(ticket.getAreaCategoryCode(), ticket);
				} else {
					if (temp.getPriceInCents() > ticket.getPriceInCents()) {
						tickets.put(ticket.getAreaCategoryCode(), ticket);
					}
				}
			}
			// 声明总价格
			int ticketPrice = 0;
			// 票的类型,在这里进行统计了
			Map<String, TicketType> ticketTypes = new HashMap<>();
			// 座位信息,也在这里统计好了
			Map<String, SelectedSeat> selectSeats = new HashMap<>();

			for (SeatInfo seatInfo : seatInfoList) {
				Ticket ticket = tickets.get(seatInfo.getAreaCode());
				StateTool.checkState(ticket != null, StateTool.State.ORDER_MEMMBER_TICKET_ERROR);
				ticketPrice = ticketPrice + ticket.getPriceInCents();
				// 根据区域码和位置信息拼装KEY
				StringBuffer sb = new StringBuffer(seatInfo.getAreaCode());
				sb.append("-").append(seatInfo.getRow()).append("-").append(seatInfo.getCol());
				selectSeats.put(sb.toString(), new SelectedSeat(seatInfo.getAreaCode(), seatInfo.getAreaNum(),
						seatInfo.getRow(), seatInfo.getCol()));

				TicketType ticketType = ticketTypes.get(ticket.getTicketTypeCode());
				if (ticketType == null) {
					// ticketType = new TicketType("0001", 1, ticket.getPriceInCents());
					ticketType = new TicketType(ticket.getTicketTypeCode(), 1, ticket.getPriceInCents(),
							bookingFee + "");
					ticketTypes.put(ticket.getTicketTypeCode(), ticketType);
				} else {
					ticketType.setQty(ticketType.getQty() + 1);
				}
			}

			System.out.println("总价是:" + ticketPrice);
			// 从vista方获取座位信息,验证座位是否是可用的
			int checkSeat = 0;
			SeatLayoutData seatLayoutData = vistaApi.getSessionApi().getSeatForSession(session.getCid(),
					appUserOrder.getSid());
			String pname = "";

			for (SeatLayoutData.Area area : seatLayoutData.getAreas()) {
				String areaCode = area.getAreaCode();
				for (SeatLayoutData.Row row : area.getRows()) {
					String rowName = row.getPhysicalName();
					for (SeatLayoutData.Seat seat : row.getSeats()) {
						String colName = seat.getId();

						SeatLayoutData.Position p = seat.getPosition();

						StringBuffer sb = new StringBuffer(areaCode);
						sb.append("-").append(p.getRow()).append("-").append(p.getCol());

						SelectedSeat selectedSeat = selectSeats.get(sb.toString());
						// 这里最后检测一下座位状态
						if (selectedSeat != null) {
							checkSeat++;

							StateTool.checkState(seat.getStatus() == 0, StateTool.State.ORDER_SEAT_CAN_NOT_USE_ERROR);
							selectedSeat.setAreaNum(Short.parseShort(p.getAreaNum() + ""));
							if (seatLayoutData.getAreas().size() == 1) {
								pname = pname + rowName + "排" + colName + "座  ";
							} else {
								pname = pname + area.getAreaCode() + "区" + rowName + "排" + colName + "座  ";
							}
						}

						// 如果处理完毕,直接跳出
						if (checkSeat == selectSeats.size()) {
							break;
						}
					}
					// 如果处理完毕,直接跳出
					if (checkSeat == selectSeats.size()) {
						break;
					}
				}
				// 如果处理完毕,直接跳出
				if (checkSeat == selectSeats.size()) {
					break;
				}
			}

			StateTool.checkState(checkSeat == selectSeats.size(), StateTool.State.ORDER_SEAT_ERROR);
			// 向Vista系统提交锁座请求
			AddTicketWarp addTicketWarp = new AddTicketWarp();
			addTicketWarp.setOrderId(appUserOrder.getOrderId());
			addTicketWarp.setCinemaId(session.getCid());
			addTicketWarp.setSessionId(appUserOrder.getSid());
			addTicketWarp.setTicketTypes(new ArrayList<>(ticketTypes.values()));
			addTicketWarp.setSelectedSeats(new ArrayList<>(selectSeats.values()));
			OrderRes addTicketRes = vistaApi.getOrderApi().addTicket(addTicketWarp);

			// 先检查接口返回情况
			StateTool.checkState(addTicketRes.getResult() == 0, StateTool.State.ORDER_ADD_TICKET_ERROR);

			needCancel = true;

			if (!Strings.isNullOrEmpty(appUserOrder.getConcessions())) {
				List<Concession> consList = new ArrayList<>();
				String[] concessionsArr = appUserOrder.getConcessions().split(",");
				for (int i = 0; i < concessionsArr.length; i++) {
					String[] tempArr = concessionsArr[i].split("-");
					Concession concession = new Concession();
					concession.setId(tempArr[0]);
					concession.setCount(Integer.parseInt(tempArr[1]));
					consList.add(concession);
				}
				setConcession(consList, userId, orderId, appUserOrder.getMphone());
			}

			AppUserOrder appUserOrderT = new AppUserOrder();
			appUserOrderT.setOrderId(appUserOrder.getOrderId());
			appUserOrderT.setUserId(user.getMemberId());
			appUserOrderT.setTotalPrice(addTicketRes.getOrder().getTotalPrice() + appUserOrder.getConcessionPrice());
			// appUserOrderT.setTotalPrice(appUserOrder.getTotalPrice());
			appUserOrderT.setConcessionPrice(0);
			appUserOrderT.setTicketPrice(ticketPrice + (bookingFee * addTicketRes.getOrder().getTotalOrderCount()));
			appUserOrderT.setCid(session.getCid());
			appUserOrderT.setSid(appUserOrder.getSid());
			appUserOrderT.setMid(session.getMid());
			appUserOrderT.setState(AppUserOrder.STATE_WAIT_PAY);
			appUserOrderT.setCreateTime(System.currentTimeMillis());
			appUserOrderT.setCadd(cinema.getCadd());
			appUserOrderT.setConcessions(appUserOrder.getConcessions());
			appUserOrderT.setConcessionNames(appUserOrder.getConcessionNames());
			appUserOrderT.setConcessionPrice(appUserOrder.getConcessionPrice());
			appUserOrderT.setMphone(user.getMphone());

			appUserOrderT.setPlaceNames(pname);
			String place = "";
			for (String key : selectSeats.keySet()) {
				place = place + key + ",";
			}
			place.substring(0, place.length() - 1);
			appUserOrderT.setPlaces(place);
			appUserOrderT.setCname(cinema.getCname());
			appUserOrderT.setMname(movie.getMname());
			appUserOrderT.setSname(session.getScreenName());
			appUserOrderT.setStime(session.getStime());

			appUserOrderDao.create(appUserOrderT);

			System.out.println("订单处理时间:" + (System.currentTimeMillis() - t1));

			Integer orderIdYE = appUserOrderT.getId();

			AppUserOrder appUserOrderNew = appUserOrderDao.find(orderIdYE);

			// 记录一下上一个order的主键 以便更新 并且删除上一个订单
			int lastId = appUserOrder.getId();
			String lastOrderId = appUserOrder.getOrderId();
			appUserOrderDao.del(lastId);

			// 余额支付end
			String ed = validateRes.getMember().getExpiryDate();
			Date d = new Date(Long.parseLong(ed.substring(ed.indexOf("(") + 1, ed.indexOf(")"))));

			CompleteOrderWarp warp = new CompleteOrderWarp();
			warp.setOrderId(appUserOrderNew.getOrderId());
			warp.setPerformPayment(true);
			warp.setCustomerPhone(appUserOrder.getMphone());
			CompleteOrderWarp.PaymentInfo paymentInfo = new CompleteOrderWarp.PaymentInfo();
			paymentInfo.setCardNumber(validateRes.getMember().getCardNumber());
			paymentInfo.setCardType(CompleteOrderWarp.CARD_TYPE_POYAL);
			paymentInfo.setPaymentValueCents(appUserOrderNew.getTotalPrice());
			paymentInfo.setPaymentTenderCategory(CompleteOrderWarp.PT_CARD);
			paymentInfo.setCardExpiryYear(DateTool.dateToString(d, "yyyy"));
			paymentInfo.setCardExpiryMonth(DateTool.dateToString(d, "MM"));
			warp.setPaymentInfo(paymentInfo);
			CompleteOrderRes res = vistaApi.getOrderApi().completeOrder(warp);
			StateTool.checkState(res != null, StateTool.State.FAIL);

			appUserOrderNew.setId(lastId);
			appUserOrderNew.setState(AppUserOrder.STATE_ORDER_SUCCESS);
			appUserOrderNew.setVistaBookingId(res.getVistaBookingId());
			appUserOrderNew.setVistaBookingNumber(res.getVistaBookingNumber());

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("orderId", lastOrderId);
			map.put("order", appUserOrderNew);
			appUserOrderDao.update(map);

			appUserOrder.setState(AppUserOrder.STATE_ORDER_SUCCESS);
			appUserOrder.setVistaBookingId(res.getVistaBookingId());
			appUserOrder.setVistaBookingNumber(res.getVistaBookingNumber());

		} else if (payType == 2) {
			CompleteOrderWarp warp = new CompleteOrderWarp();
			warp.setOrderId(appUserOrder.getOrderId());
			warp.setPerformPayment(false);
			warp.setCustomerPhone(appUserOrder.getMphone());
			CompleteOrderWarp.PaymentInfo paymentInfo = new CompleteOrderWarp.PaymentInfo();
			paymentInfo.setCardNumber(CompleteOrderWarp.CARDNUMBER);
			paymentInfo.setCardType(CompleteOrderWarp.CARD_TYPE_ALIPAY);
			paymentInfo.setPaymentValueCents(appUserOrder.getTotalPrice());
			paymentInfo.setPaymentTenderCategory(CompleteOrderWarp.CARD_TYPE_ALIPAY);
			warp.setPaymentInfo(paymentInfo);
			CompleteOrderRes res = vistaApi.getOrderApi().completeOrder(warp);
			StateTool.checkState(res != null && res.getResult() == 0, StateTool.State.FAIL);

			AppUserOrder temp = new AppUserOrder();
			temp.setId(appUserOrder.getId());
			temp.setState(AppUserOrder.STATE_ORDER_SUCCESS);
			temp.setVistaBookingId(res.getVistaBookingId());
			temp.setVistaBookingNumber(res.getVistaBookingNumber());
			appUserOrderDao.update(temp);

			appUserOrder.setState(AppUserOrder.STATE_ORDER_SUCCESS);
			appUserOrder.setVistaBookingId(res.getVistaBookingId());
			appUserOrder.setVistaBookingNumber(res.getVistaBookingNumber());

		} else if (payType == 3) {
			// 支付宝
			CompleteOrderWarp warp = new CompleteOrderWarp();
			warp.setOrderId(appUserOrder.getOrderId());
			warp.setPerformPayment(false);
			warp.setCustomerPhone(appUserOrder.getMphone());
			CompleteOrderWarp.PaymentInfo paymentInfo = new CompleteOrderWarp.PaymentInfo();
			paymentInfo.setCardNumber(CompleteOrderWarp.CARDNUMBER);
			paymentInfo.setCardType(CompleteOrderWarp.CARD_TYPE_ALIPAY);
			paymentInfo.setPaymentValueCents(appUserOrder.getTotalPrice());
			paymentInfo.setPaymentTenderCategory(CompleteOrderWarp.CARD_TYPE_ALIPAY);

			warp.setPaymentInfo(paymentInfo);
			CompleteOrderRes res = vistaApi.getOrderApi().completeOrder(warp);
			StateTool.checkState(res != null && res.getResult() == 0, StateTool.State.FAIL);

			AppUserOrder temp = new AppUserOrder();
			temp.setId(appUserOrder.getId());
			temp.setState(AppUserOrder.STATE_ORDER_SUCCESS);
			temp.setVistaBookingId(res.getVistaBookingId());
			temp.setVistaBookingNumber(res.getVistaBookingNumber());
			appUserOrderDao.update(temp);

			appUserOrder.setState(AppUserOrder.STATE_ORDER_SUCCESS);
			appUserOrder.setVistaBookingId(temp.getVistaBookingId());
			appUserOrder.setVistaBookingNumber(temp.getVistaBookingNumber());

		} else {
			StateTool.checkState(false, StateTool.State.FAIL);
		}

		return appUserOrder;

		// 增加一个大转盘的抽奖机会
		// AppUserReward reward = new AppUserReward();
		// reward.setUserId(userId);
		// reward.setBuyOrderId(appUserOrder.getOrderId());
		// reward.setBuyTime(System.currentTimeMillis());
		// reward.setRtype(AppUserReward.TYPE_DZP);
		// reward.setUsed(AppUserReward.NOUSE);
		// appUserRewardDao.create(reward);
	}

	@Override
	@Transactional
	public void cancelOrder(String userId, Integer orderId) {
		AppUserOrder appUserOrder = appUserOrderDao.find(orderId);
		StateTool.checkState(appUserOrder != null && appUserOrder.getUserId().equals(userId), StateTool.State.FAIL);
		vistaApi.getOrderApi().cancelOrder(appUserOrder.getOrderId());
		AppUserOrder temp = new AppUserOrder();
		temp.setId(appUserOrder.getId());
		temp.setState(AppUserOrder.STATE_CANCEL);
		appUserOrderDao.update(temp);
	}

	@Override
	public AppUserOrder getOrderById(Integer orderId) {
		AppUserOrder appUserOrder = appUserOrderDao.find(orderId);
		return appUserOrder;
	}
}

@Setter
@Getter
@ToString
class CreateOrderWarp {
	private List<SeatInfo> seatInfoList;
	private String sid;
	private String payment;
}
