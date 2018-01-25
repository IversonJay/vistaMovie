package com.lhh.vista.wbservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.2.1
 * 2018-01-11T11:25:31.668+08:00
 * Generated source version: 3.2.1
 * 
 */
@WebService(targetNamespace = "http://vista.co.nz/webservices/WSVistaVoucher", name = "WSVistaVoucherSoap")
@XmlSeeAlso({ObjectFactory.class})
public interface WSVistaVoucherSoap {

    @WebMethod(action = "http://vista.co.nz/webservices/WSVistaVoucher/executecmd")
    @RequestWrapper(localName = "executecmd", targetNamespace = "http://vista.co.nz/webservices/WSVistaVoucher", className = "com.bimap.Executecmd")
    @ResponseWrapper(localName = "executecmdResponse", targetNamespace = "http://vista.co.nz/webservices/WSVistaVoucher", className = "com.bimap.ExecutecmdResponse")
    public void executecmd(
        @WebParam(name = "syssettings", targetNamespace = "http://vista.co.nz/webservices/WSVistaVoucher")
        java.lang.String syssettings,
        @WebParam(mode = WebParam.Mode.INOUT, name = "cmddata", targetNamespace = "http://vista.co.nz/webservices/WSVistaVoucher")
        javax.xml.ws.Holder<java.lang.String> cmddata,
        @WebParam(mode = WebParam.Mode.INOUT, name = "returndata", targetNamespace = "http://vista.co.nz/webservices/WSVistaVoucher")
        javax.xml.ws.Holder<java.lang.String> returndata,
        @WebParam(mode = WebParam.Mode.OUT, name = "executecmdResult", targetNamespace = "http://vista.co.nz/webservices/WSVistaVoucher")
        javax.xml.ws.Holder<java.lang.Integer> executecmdResult
    );

    @WebMethod(action = "http://vista.co.nz/webservices/WSVistaVoucher/batchimporttransactions")
    @RequestWrapper(localName = "batchimporttransactions", targetNamespace = "http://vista.co.nz/webservices/WSVistaVoucher", className = "com.bimap.Batchimporttransactions")
    @ResponseWrapper(localName = "batchimporttransactionsResponse", targetNamespace = "http://vista.co.nz/webservices/WSVistaVoucher", className = "com.bimap.BatchimporttransactionsResponse")
    public void batchimporttransactions(
        @WebParam(name = "xmlMsg", targetNamespace = "http://vista.co.nz/webservices/WSVistaVoucher")
        java.lang.String xmlMsg,
        @WebParam(mode = WebParam.Mode.INOUT, name = "responseMessage", targetNamespace = "http://vista.co.nz/webservices/WSVistaVoucher")
        javax.xml.ws.Holder<java.lang.String> responseMessage,
        @WebParam(mode = WebParam.Mode.OUT, name = "batchimporttransactionsResult", targetNamespace = "http://vista.co.nz/webservices/WSVistaVoucher")
        javax.xml.ws.Holder<java.lang.Integer> batchimporttransactionsResult
    );
}