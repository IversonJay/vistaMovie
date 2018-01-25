
package com.lhh.vista.wbservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="batchimporttransactionsResult" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="responseMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "batchimporttransactionsResult",
    "responseMessage"
})
@XmlRootElement(name = "batchimporttransactionsResponse")
public class BatchimporttransactionsResponse {

    protected int batchimporttransactionsResult;
    protected String responseMessage;

    /**
     * 获取batchimporttransactionsResult属性的值。
     * 
     */
    public int getBatchimporttransactionsResult() {
        return batchimporttransactionsResult;
    }

    /**
     * 设置batchimporttransactionsResult属性的值。
     * 
     */
    public void setBatchimporttransactionsResult(int value) {
        this.batchimporttransactionsResult = value;
    }

    /**
     * 获取responseMessage属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * 设置responseMessage属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseMessage(String value) {
        this.responseMessage = value;
    }

}
