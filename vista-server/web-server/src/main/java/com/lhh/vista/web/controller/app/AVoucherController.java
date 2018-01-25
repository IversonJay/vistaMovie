package com.lhh.vista.web.controller.app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.customer.v2s.value.VoucherRes;
import com.lhh.vista.service.model.AppUser;
import com.lhh.vista.temp.service.VoucherService;
import com.lhh.vista.web.common.CommonData;


@Controller
@RequestMapping("a_voucher")
public class AVoucherController  extends BaseController {
	
	@Autowired
	private VoucherService voucherService;
	
	@RequestMapping("list")
	@ResponseBody
	public Object list() {
		AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
		List<VoucherRes> list = (List<VoucherRes>) voucherService.list(user.getMphone());
		return list;
	}
}
