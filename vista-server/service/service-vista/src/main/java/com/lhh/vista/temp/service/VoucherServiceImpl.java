package com.lhh.vista.temp.service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.namespace.QName;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.v2s.value.VoucherRes;
import com.lhh.vista.wbservice.WSVistaVoucher;
import com.lhh.vista.wbservice.WSVistaVoucherSoap;

@Service
public class VoucherServiceImpl implements VoucherService {

	private static final QName SERVICE_NAME = new QName("http://vista.co.nz/webservices/WSVistaVoucher", "WSVistaVoucher");
	@Autowired
	private VistaApi vistaApi;

	@Override
	public Object list(String mphone) {
		List<VoucherRes> list =  vistaApi.getVoucher(mphone);
		for (VoucherRes voucherRes : list) {
			String postValue = "<proplist><prop name=\"ID\" value=\"VOUCHER@_@ID\"/><prop name=\"REQUESTACTION\" value=\"16\"/><prop name=\"DUPLICATENO\" value=\"0\"/><prop name=\"VOUCHERCODE\" value=\"VOUCHER@_@VOUCHERCODE\"/><prop name=\"SERIALNO\" value=\"VOUCHER@_@SERIALNO\"/><prop name=\"ISCONNECTED\" value=\"Y\"/><prop name=\"LOCALDATETIME\" value=\"VOUCHER@_@TIMESTAMP\"/></proplist>";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			String time_stamp = sdf.format(new Date());
			postValue = postValue.replace("VOUCHER@_@ID", voucherRes.getVoucherBarCode()).replace("VOUCHER@_@TIMESTAMP", time_stamp).replace("VOUCHER@_@SERIALNO", voucherRes.getVoucherBarCode().substring(voucherRes.getVoucherBarCode().length() - 2, voucherRes.getVoucherBarCode().length())).replace("VOUCHER@_@VOUCHERCODE", voucherRes.getVoucherBarCode().substring(0, 4));
			String returndata = validateVoucher(postValue);
			Document doc;
			try {
				doc = DocumentHelper.parseText(returndata);
				List<org.dom4j.Element> nameList = doc.selectNodes("//prop[@name='VOUCHERSTATUS']");
				if(nameList.size() != 0) {
					String name = nameList.get(0).attributeValue("value");
					voucherRes.setStatus(name);
				} else {
					voucherRes.setStatus("ERROR");
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	
	private String validateVoucher(String postValue) {
		URL wsdlURL = WSVistaVoucher.WSDL_LOCATION;
        WSVistaVoucher ss = new WSVistaVoucher(wsdlURL, SERVICE_NAME);
        WSVistaVoucherSoap port = ss.getWSVistaVoucherSoap();  
        
        {
        System.out.println("Invoking executecmd...");
        java.lang.String _executecmd_syssettings = "<proplist><prop name=\"ID\" value=\"\"/><prop name=\"SERVICEURL\" value=\"http://123.207.174.230/WSVistaVoucher/WSVistaVoucher.asmx\"/><prop name=\"SERVICETIMEOUTSECS\" value=\"30\"/><prop name=\"CHECKSUMCODE\" value=\"SKIP\"/><prop name=\"TAXMODE\" value=\"I\"/><prop name=\"CASEINSENSITIVEVOUCHERCODES\" value=\"Y\"/><prop name=\"VOIDSALEONREFUND\" value=\"N\"/><prop name=\"LOGGINGLEVEL\" value=\"3\"/><prop name=\"HOSERVER\" value=\"\"/><prop name=\"MODE\" value=\"ONLINE\"/></proplist>";
//        java.lang.String _executecmd_cmddataVal = "<proplist><prop name=\"ID\" value=\"122100000002\"/><prop name=\"REQUESTACTION\" value=\"16\"/><prop name=\"DUPLICATENO\" value=\"0\"/><prop name=\"VOUCHERCODE\" value=\"1221\"/><prop name=\"SERIALNO\" value=\"02\"/><prop name=\"ISCONNECTED\" value=\"Y\"/><prop name=\"LOCALDATETIME\" value=\"20140831123227\"/></proplist>";
        java.lang.String _executecmd_cmddataVal = postValue;
        javax.xml.ws.Holder<java.lang.String> _executecmd_cmddata = new javax.xml.ws.Holder<java.lang.String>(_executecmd_cmddataVal);
        java.lang.String _executecmd_returndataVal = "<proplist></proplist>";
        javax.xml.ws.Holder<java.lang.String> _executecmd_returndata = new javax.xml.ws.Holder<java.lang.String>(_executecmd_returndataVal);
        javax.xml.ws.Holder<java.lang.Integer> _executecmd_executecmdResult = new javax.xml.ws.Holder<java.lang.Integer>();
        port.executecmd(_executecmd_syssettings, _executecmd_cmddata, _executecmd_returndata, _executecmd_executecmdResult);
        
        
        return _executecmd_returndata.value;
//        System.out.println("executecmd._executecmd_cmddata=" + _executecmd_cmddata.value);
//        System.out.println("executecmd._executecmd_returndata=" + _executecmd_returndata.value);
//        System.out.println("executecmd._executecmd_executecmdResult=" + _executecmd_executecmdResult.value);

        }
	}
	
	
	
	
	public static void main(String[] args) {
		String s = "V88880000073";
		System.out.println(s.substring(0, 4));
	}

}
