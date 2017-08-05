
package cn.qgg.redsun.bos.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cn.qgg.redsun.bos.ws package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AddWwaybill_QNAME = new QName("http://ws.bos.redsun.xtjun.cn/", "addWwaybill");
    private final static QName _WaybillDetailListResponse_QNAME = new QName("http://ws.bos.redsun.xtjun.cn/", "waybillDetailListResponse");
    private final static QName _AddWwaybillResponse_QNAME = new QName("http://ws.bos.redsun.xtjun.cn/", "addWwaybillResponse");
    private final static QName _WaybillDetailList_QNAME = new QName("http://ws.bos.redsun.xtjun.cn/", "waybillDetailList");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cn.qgg.redsun.bos.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddWwaybillResponse }
     * 
     */
    public AddWwaybillResponse createAddWwaybillResponse() {
        return new AddWwaybillResponse();
    }

    /**
     * Create an instance of {@link WaybillDetailList }
     * 
     */
    public WaybillDetailList createWaybillDetailList() {
        return new WaybillDetailList();
    }

    /**
     * Create an instance of {@link WaybillDetailListResponse }
     * 
     */
    public WaybillDetailListResponse createWaybillDetailListResponse() {
        return new WaybillDetailListResponse();
    }

    /**
     * Create an instance of {@link AddWwaybill }
     * 
     */
    public AddWwaybill createAddWwaybill() {
        return new AddWwaybill();
    }

    /**
     * Create an instance of {@link Waybilldetail }
     * 
     */
    public Waybilldetail createWaybilldetail() {
        return new Waybilldetail();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddWwaybill }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.bos.redsun.xtjun.cn/", name = "addWwaybill")
    public JAXBElement<AddWwaybill> createAddWwaybill(AddWwaybill value) {
        return new JAXBElement<AddWwaybill>(_AddWwaybill_QNAME, AddWwaybill.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WaybillDetailListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.bos.redsun.xtjun.cn/", name = "waybillDetailListResponse")
    public JAXBElement<WaybillDetailListResponse> createWaybillDetailListResponse(WaybillDetailListResponse value) {
        return new JAXBElement<WaybillDetailListResponse>(_WaybillDetailListResponse_QNAME, WaybillDetailListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddWwaybillResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.bos.redsun.xtjun.cn/", name = "addWwaybillResponse")
    public JAXBElement<AddWwaybillResponse> createAddWwaybillResponse(AddWwaybillResponse value) {
        return new JAXBElement<AddWwaybillResponse>(_AddWwaybillResponse_QNAME, AddWwaybillResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WaybillDetailList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.bos.redsun.xtjun.cn/", name = "waybillDetailList")
    public JAXBElement<WaybillDetailList> createWaybillDetailList(WaybillDetailList value) {
        return new JAXBElement<WaybillDetailList>(_WaybillDetailList_QNAME, WaybillDetailList.class, null, value);
    }

}
