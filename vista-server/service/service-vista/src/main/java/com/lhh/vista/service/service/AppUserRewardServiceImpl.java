package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.util.DateTool;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.s2v.AddConcessionWarp;
import com.lhh.vista.customer.s2v.CompleteOrderWarp;
import com.lhh.vista.customer.s2v.dto.AddOrRemoveConcession;
import com.lhh.vista.customer.v2s.*;
import com.lhh.vista.service.dao.*;
import com.lhh.vista.service.model.*;
import com.lhh.vista.util.PrizeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by liu on 2017/1/7.
 */
@Service
public class AppUserRewardServiceImpl implements AppUserRewardService {
    @Autowired
    private SystemValueDao systemValueDao;
    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private VistaApi vistaApi;

    @Autowired
    private AppUserRewardDao appUserRewardDao;

    @Autowired
    private RewardControlDao rewardControlDao;


    @Override
    public PagerResponse<AppUserReward> getPager(PagerRequest pagerRequest, String memberId) {
        return appUserRewardDao.getPager(pagerRequest, memberId);
    }

    @Override
    public Integer getAppUserRewardCount(Integer type, String userId) {
        return appUserRewardDao.getAppUserRewardCount(type, userId);
    }

    @Override
    @Transactional
    public Integer addRewardCount(Integer type, String userId) {
        AppUser appUser = appUserDao.getByMemberId(userId);
        StateTool.checkState(appUser != null, StateTool.State.FAIL);
        //首先：验证userid的正确性
        MemberValidateRes validateRes = vistaApi.getUserApi().checkUser(userId, true);
        StateTool.checkState(validateRes != null && validateRes.getResult() == 0, StateTool.State.FAIL);
        //首先：验证卖品id的正确性
        GetConcessionListRes concessionListRes = vistaApi.getOrderApi().getConcessionList(appUser.getDcinema());//根据影城id获取卖品的id
        StateTool.checkState(concessionListRes != null, StateTool.State.FAIL);
        List<GetConcessionListRes.Concession> concessionList = concessionListRes.getItems();//获取卖品的list集合
        List<AddOrRemoveConcession> ucons = new ArrayList<>();

        GetMemberItemRes getMemberItemRes = vistaApi.getUserApi().memberItem(userId, false);
        StateTool.checkState(getMemberItemRes != null, StateTool.State.FAIL);
        List<GetMemberItemRes.Item> items = getMemberItemRes.toList(new ArrayList<String>());
        Map<String, GetMemberItemRes.Item> itemMap = new HashMap<>();
        for (GetMemberItemRes.Item item : items) {
            itemMap.put(item.getVistaID(), item);
        }

        for (GetConcessionListRes.Concession con : concessionList) {
            if (con.getDescription().indexOf("转盘游戏") > -1 && type == AppUserReward.TYPE_DZP) {
                GetMemberItemRes.Item item = itemMap.get(con.getHeadOfficeItemCode());
                if (item != null) {
                    AddOrRemoveConcession con1 = new AddOrRemoveConcession();
                    con1.setItemId(con.getId());//这里先测试试试
                    con1.setQuantity(1);
                    con1.setRecognitionId(item.getRecognitionID());
                    con1.setRecognitionSequenceNumber(item.getSequenceNumber());
                    ucons.add(con1);
                    break;
                }
            }
        }

        StateTool.checkState(ucons.size() == 1, StateTool.State.FAIL);
        //用户id和卖品id均验证成功，则进行订单和充值操作
        //首先生成订单号
        String orderId = (System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "")).substring(0, 32);
        StateResult checkUserRes = vistaApi.getOrderApi().checkUser(userId, orderId);//生成订单,传当前登录用户的ID和生成的订单号
        StateTool.checkState(checkUserRes != null && checkUserRes.getResult() == 0, StateTool.State.FAIL);
        //将兑换奖励的信息保存到数据库
        AppUserReward reward = new AppUserReward();
        reward.setUserId(userId);
        reward.setBuyOrderId(orderId);
        reward.setBuyTime(System.currentTimeMillis());
        reward.setRtype(type);
        reward.setUsed(AppUserReward.NOUSE);
        Integer aLong = appUserRewardDao.create(reward);
        StateTool.checkState(aLong > 0, StateTool.State.FAIL);
        //

        AddConcessionWarp addConcessionWarp = new AddConcessionWarp();
        addConcessionWarp.setCinemaId(appUser.getDcinema());
        addConcessionWarp.setOrderId(orderId);
        addConcessionWarp.setConcessions(ucons);

        StateResult stateResult = vistaApi.getOrderApi().addConcessionList(addConcessionWarp);
        StateTool.checkState(stateResult != null && stateResult.getResult() == 0, StateTool.State.FAIL);

        String ed = validateRes.getMember().getExpiryDate();
        Date d = new Date(Long.parseLong(ed.substring(ed.indexOf("(") + 1, ed.indexOf(")"))));

        CompleteOrderWarp warp = new CompleteOrderWarp();
        warp.setOrderId(orderId);
        warp.setPerformPayment(true);
        CompleteOrderWarp.PaymentInfo paymentInfo = new CompleteOrderWarp.PaymentInfo();
        paymentInfo.setCardNumber(validateRes.getMember().getCardNumber());
        paymentInfo.setCardType(CompleteOrderWarp.CARD_TYPE_POYAL);
        paymentInfo.setPaymentValueCents(0);
        paymentInfo.setPaymentTenderCategory(CompleteOrderWarp.PT_CARD);
        paymentInfo.setCardExpiryYear(DateTool.dateToString(d, "yyyy"));
        paymentInfo.setCardExpiryMonth(DateTool.dateToString(d, "MM"));
        warp.setPaymentInfo(paymentInfo);
        CompleteOrderRes res = vistaApi.getOrderApi().completeOrder(warp);
        StateTool.checkState(res != null && res.getResult() == 0, StateTool.State.FAIL);
        return 0;
    }

    @Override
    public String reward(Integer type, String userId, List<String> ids) {
        AppUserReward appUserReward = appUserRewardDao.getAppUserRewardToReward(type, userId);
        StateTool.checkState(appUserReward != null, StateTool.State.REWARD_CISHUBUZU);

        GetMemberItemRes getMemberItemRes = vistaApi.getUserApi().memberItem(userId, false);
        List<String> used = appUserRewardDao.getNotUse(userId);
        List<GetMemberItemRes.Item> memberItems = getMemberItemRes.toList(used);

        Map<String, GetMemberItemRes.Item> items = new HashMap<>();

        for (GetMemberItemRes.Item item : memberItems) {
            items.put(item.getRecognitionID(), item);
        }

        //拿到服务器里面的记录
        List<RewardControl> rcs = rewardControlDao.getRewardIdsByType(type);
        Map<String, RewardControl> xx = new HashMap<>();
        for (RewardControl rc : rcs) {
            xx.put(rc.getRid(), rc);
        }
        //对比本地记录,生成抽奖列表
        List<RewardControl> tempRcs = new ArrayList<>();
        if (ids != null && ids.size() > 0) {
            for (String id : ids) {
                StateTool.checkState(xx.containsKey(id) && items.containsKey(id), StateTool.State.FAIL);
                tempRcs.add(xx.get(id));
            }
        } else {
            for (RewardControl rc : rcs) {
                if (items.containsKey(rc.getRid())) {
                    tempRcs.add(rc);
                }
            }
        }

        //获取总数
        int zong = 1000000;
        try {
            if (type == AppUserReward.TYPE_DZP) {
                zong = Integer.parseInt(systemValueDao.getValue(SystemValue.VISTA_ACTIVITY_DAZHGUYANPAN));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<PrizeUtil.Prize> prizes = new ArrayList<>();
        for (RewardControl rc : tempRcs) {
            prizes.add(new PrizeUtil.Prize(rc.getRid(), rc.getRchance()));
            zong = zong - rc.getRchance();
        }

        prizes.add(new PrizeUtil.Prize("-9999", zong));
        System.out.println(items);
        PrizeUtil.Prize res = PrizeUtil.getPrizeIndex(prizes);
        System.out.println("res:" + res.getId());
        AppUserReward temp = new AppUserReward();
        temp.setId(appUserReward.getId());
        temp.setResId(res.getId());
        temp.setUsed(AppUserReward.USED);
        if (!res.getId().equals("-9999")) {
            GetMemberItemRes.Item item = items.get(res.getId());
            temp.setResName(item.getName());
            temp.setResVistaId(item.getVistaID());
            temp.setResSequence(item.getSequenceNumber());
        }
        appUserRewardDao.update(temp);

        return res.getId();
    }

    //调用获取商品信息RESTData.GetConcessionItemsList 这个接口 你可以看到这个影院所有的商品，在返回值中有1个HeadOfficeItemCode。
    // 调用Member/Item 接口 返回值中有1个VistaID，VistaID=HeadOfficeItemCode；你在调用order/concession 接口时
    // 根据关联把RESTData.GetConcessionItemsList中获得的ItemId 传过去
    @Override
    public String exchange(Integer oid, String userId) {
        AppUser appUser = appUserDao.getByMemberId(userId);
        StateTool.checkState(appUser != null, StateTool.State.FAIL);

        AppUserReward appUserReward = appUserRewardDao.find(oid);
        StateTool.checkState(appUserReward != null, StateTool.State.FAIL);

        MemberValidateRes validateRes = vistaApi.getUserApi().checkUser(userId, true);
        StateTool.checkState(validateRes != null && validateRes.getResult() == 0, StateTool.State.FAIL);

        GetConcessionListRes concessionListRes = vistaApi.getOrderApi().getConcessionList(appUser.getDcinema());//根据影城id获取卖品的id
        StateTool.checkState(concessionListRes != null, StateTool.State.FAIL);
        List<GetConcessionListRes.Concession> concessionList = concessionListRes.getItems();//获取卖品的list集合
        List<AddOrRemoveConcession> ucons = new ArrayList<>();

        for (GetConcessionListRes.Concession con : concessionList) {
            if (con.getHeadOfficeItemCode().equals(appUserReward.getResVistaId())) {
                ucons.add(new AddOrRemoveConcession(con.getId(), 1, appUserReward.getResId(), appUserReward.getResSequence()));
                break;
            }
        }
        StateTool.checkState(ucons.size() == 1, StateTool.State.FAIL);


        String orderId = (System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "")).substring(0, 32);
        StateResult checkUserRes = vistaApi.getOrderApi().checkUser(userId, orderId);//生成订单,传当前登录用户的ID和生成的订单号
        StateTool.checkState(checkUserRes != null && checkUserRes.getResult() == 0, StateTool.State.FAIL);

        AddConcessionWarp addConcessionWarp = new AddConcessionWarp();
        addConcessionWarp.setCinemaId(appUser.getDcinema());
        addConcessionWarp.setOrderId(orderId);
        addConcessionWarp.setConcessions(ucons);

        StateResult stateResult = vistaApi.getOrderApi().addConcessionList(addConcessionWarp);
        StateTool.checkState(stateResult != null && stateResult.getResult() == 0, StateTool.State.FAIL);

        String ed = validateRes.getMember().getExpiryDate();
        Date d = new Date(Long.parseLong(ed.substring(ed.indexOf("(") + 1, ed.indexOf(")"))));

        CompleteOrderWarp warp = new CompleteOrderWarp();
        warp.setOrderId(orderId);
        warp.setPerformPayment(true);
        CompleteOrderWarp.PaymentInfo paymentInfo = new CompleteOrderWarp.PaymentInfo();
        paymentInfo.setCardNumber(validateRes.getMember().getCardNumber());
        paymentInfo.setCardType(CompleteOrderWarp.CARD_TYPE_POYAL);
        paymentInfo.setPaymentValueCents(0);
        paymentInfo.setPaymentTenderCategory(CompleteOrderWarp.PT_CARD);
        paymentInfo.setCardExpiryYear(DateTool.dateToString(d, "yyyy"));
        paymentInfo.setCardExpiryMonth(DateTool.dateToString(d, "MM"));
        warp.setPaymentInfo(paymentInfo);
        CompleteOrderRes res = vistaApi.getOrderApi().completeOrder(warp);
        StateTool.checkState(res != null && res.getResult() == 0, StateTool.State.FAIL);

        AppUserReward temp = new AppUserReward();
        temp.setId(appUserReward.getId());
        temp.setUsed(AppUserReward.USED);
        temp.setVistaBookingId(res.getVistaBookingId());
        appUserRewardDao.update(temp);
        return res.getVistaBookingId();
    }

    /**
     * 获取用户尚未使用的奖励
     */
    @Override
    public List<String> getAppUserNotUseReward(String userId) {
        return appUserRewardDao.getNotUse(userId);
    }
}
