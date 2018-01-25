package com.lhh.vista.service.service;

import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.s2v.AddConcessionWarp;
import com.lhh.vista.customer.s2v.CompleteOrderWarp;
import com.lhh.vista.customer.s2v.dto.AddOrRemoveConcession;
import com.lhh.vista.customer.v2s.CompleteOrderRes;
import com.lhh.vista.customer.v2s.GetConcessionListRes;
import com.lhh.vista.customer.v2s.MemberValidateRes;
import com.lhh.vista.customer.v2s.StateResult;
import com.lhh.vista.service.dao.AppUserDao;
import com.lhh.vista.service.dao.AppUserRechargeDao;
import com.lhh.vista.service.model.AppUser;
import com.lhh.vista.service.model.AppUserRecharge;
import com.lhh.vista.service.model.SystemValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by chen on 2017/01/04.
 */
@Service
public class AppUserRechargeServiceImpl implements AppUserRechargeService {
    @Autowired
    private VistaApi vistaApi;
    @Autowired
    private SystemValueService systemValueService;

    @Autowired
    private AppUserRechargeDao appUserRechargeDao;

    @Autowired
    private AppUserDao appUserDao;

    /**
     * 充值流程：创建订单
     *
     * @param itemId
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public AppUserRecharge appUserRecharge(String itemId, String userId) {
        //首先：验证userid的正确性
        AppUser appUser = appUserDao.getByMemberId(userId);
        StateTool.checkState(appUser != null, StateTool.State.FAIL);
        MemberValidateRes memberValidateRes = vistaApi.getUserApi().checkUser(userId, false);
        StateTool.checkState(memberValidateRes != null && memberValidateRes.getResult() == 0, StateTool.State.FAIL);
        //首先：验证卖品id的正确性
        GetConcessionListRes concessionListRes = vistaApi.getOrderApi().getConcessionList(appUser.getDcinema());//根据影城id获取卖品的id
        StateTool.checkState(concessionListRes != null, StateTool.State.FAIL);
        List<GetConcessionListRes.Concession> concessionList = concessionListRes.getItems();//获取卖品的list集合
        List<AddOrRemoveConcession> ucons = new ArrayList<>();

        GetConcessionListRes.Concession scon = null;
        for (GetConcessionListRes.Concession con : concessionList) {
            if (itemId.equals(con.getId())) {
                AddOrRemoveConcession con1 = new AddOrRemoveConcession();
                con1.setItemId(itemId);//这里先测试试试
                con1.setQuantity(1);
                ucons.add(con1);
                scon = con;
                break;
            }
        }
        StateTool.checkState(ucons.size() > 0 && scon != null, StateTool.State.FAIL);
        //用户id和卖品id均验证成功，则进行订单和充值操作
        //首先生成订单号
        String orderId = (System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "")).substring(0, 32);
        StateResult checkUserRes = vistaApi.getOrderApi().checkUser(userId, orderId);//生成订单,传当前登录用户的ID和生成的订单号
        StateTool.checkState(checkUserRes != null && checkUserRes.getResult() == 0, StateTool.State.FAIL);
        //将充值信息保存到数据库
        AppUserRecharge appUserRecharge = new AppUserRecharge();
        appUserRecharge.setUserId(userId);
        appUserRecharge.setOrderId(orderId);
        appUserRecharge.setRechargeAmount(scon.getPrice());
        appUserRecharge.setOrderStatus(AppUserRecharge.STATE_WAIT_PAY);
        appUserRecharge.setCreateTime(System.currentTimeMillis());
        Integer aLong = appUserRechargeDao.create(appUserRecharge);
        StateTool.checkState(aLong > 0, StateTool.State.FAIL);

        AddConcessionWarp addConcessionWarp = new AddConcessionWarp();
        addConcessionWarp.setCinemaId(appUser.getDcinema());
        addConcessionWarp.setOrderId(orderId);
        addConcessionWarp.setConcessions(ucons);

        StateResult stateResult = vistaApi.getOrderApi().addConcessionList(addConcessionWarp);
        StateTool.checkState(stateResult != null && stateResult.getResult() == 0, StateTool.State.FAIL);

        return appUserRecharge;
    }

    public AppUserRecharge get(String orderId) {
        return appUserRechargeDao.find(orderId);
    }

    /**
     * 完成订单操作
     *
     * @param orderId 订单编号
     */
    @Override
    public void finishUserRecharge(String userId, Integer orderId, Integer payType) {
        StateTool.checkState(payType == 2 || payType == 3 || payType == 4, StateTool.State.FAIL);
        AppUserRecharge appUserRechargeQ = appUserRechargeDao.find(orderId);//这里的orderId 即为 appUserRecharge表里的ID
        StateTool.checkState(appUserRechargeQ != null && appUserRechargeQ.getUserId().equals(userId), StateTool.State.FAIL);

        CompleteOrderWarp completeOrderWarp = new CompleteOrderWarp();
        completeOrderWarp.setOrderId(appUserRechargeQ.getOrderId());
        completeOrderWarp.setPerformPayment(false);

        if (payType == 3) {
            MemberValidateRes res = vistaApi.getUserApi().checkUser(userId, true);
            //以下只是模拟流程
            CompleteOrderWarp.PaymentInfo paymentInfo = new CompleteOrderWarp.PaymentInfo();
            paymentInfo.setCardNumber(CompleteOrderWarp.CARDNUMBER);
            paymentInfo.setCardType(CompleteOrderWarp.CARD_TYPE_ALIPAY);
            //paymentInfo.setBankTransactionNumber(res.getMember().getCardNumber());
            paymentInfo.setPaymentValueCents(appUserRechargeQ.getRechargeAmount());
            paymentInfo.setPaymentTenderCategory(CompleteOrderWarp.CARD_TYPE_ALIPAY);
            completeOrderWarp.setPaymentInfo(paymentInfo);
        }

        CompleteOrderRes completeOrderRes = vistaApi.getOrderApi().completeOrder(completeOrderWarp);//完成订单
        StateTool.checkState(completeOrderRes != null && completeOrderRes.getResult() == 0, StateTool.State.FAIL);
        AppUserRecharge appUserRecharge = new AppUserRecharge();
        appUserRecharge.setOrderStatus(AppUserRecharge.STATE_ORDER_SUCCESS);//设置订单状态为1，即为完成
        appUserRecharge.setId(orderId);//根据Id进行更新
        appUserRechargeDao.update(appUserRecharge);
    }


    @Override
    @Transactional
    public void cancelOrder(String userId, Integer orderId) {
        AppUserRecharge appUserRechargeQ = appUserRechargeDao.find(orderId);//这里的orderId 即为 appUserRecharge表里的ID
        StateTool.checkState(appUserRechargeQ != null && appUserRechargeQ.getUserId().equals(userId), StateTool.State.FAIL);
        vistaApi.getOrderApi().cancelOrder(appUserRechargeQ.getOrderId());
        AppUserRecharge temp = new AppUserRecharge();
        temp.setId(appUserRechargeQ.getId());
        temp.setOrderStatus(AppUserRecharge.STATE_CANCEL);
        appUserRechargeDao.update(temp);
    }
}
