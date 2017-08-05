
package cn.qgg.redsun.bos.ws.impl;

import cn.qgg.redsun.bos.ws.ObjectFactory;
import cn.qgg.redsun.bos.ws.Waybilldetail;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import java.util.List;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "IWaybillWs", targetNamespace = "http://ws.bos.redsun.xtjun.cn/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface IWaybillWs {


    /**
     * 
     * @param arg3
     * @param arg2
     * @param arg4
     * @param arg1
     * @param arg0
     * @return
     *     returns java.lang.Long
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "addWwaybill", targetNamespace = "http://ws.bos.redsun.xtjun.cn/", className = "cn.qgg.redsun.bos.ws.AddWwaybill")
    @ResponseWrapper(localName = "addWwaybillResponse", targetNamespace = "http://ws.bos.redsun.xtjun.cn/", className = "cn.qgg.redsun.bos.ws.AddWwaybillResponse")
    public Long addWwaybill(
        @WebParam(name = "arg0", targetNamespace = "")
        Long arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2,
        @WebParam(name = "arg3", targetNamespace = "")
        String arg3,
        @WebParam(name = "arg4", targetNamespace = "")
        String arg4);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<cn.qgg.redsun.bos.ws.Waybilldetail>
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "waybillDetailList", targetNamespace = "http://ws.bos.redsun.xtjun.cn/", className = "cn.qgg.redsun.bos.ws.WaybillDetailList")
    @ResponseWrapper(localName = "waybillDetailListResponse", targetNamespace = "http://ws.bos.redsun.xtjun.cn/", className = "cn.qgg.redsun.bos.ws.WaybillDetailListResponse")
    public List<Waybilldetail> waybillDetailList(
        @WebParam(name = "arg0", targetNamespace = "")
        Long arg0);

}