
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
 *         &lt;element name="executecmdResult" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="cmddata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="returndata" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "executecmdResult",
    "cmddata",
    "returndata"
})
@XmlRootElement(name = "executecmdResponse")
public class ExecutecmdResponse {

    protected int executecmdResult;
    protected String cmddata;
    protected String returndata;

    /**
     * 获取executecmdResult属性的值。
     * 
     */
    public int getExecutecmdResult() {
        return executecmdResult;
    }

    /**
     * 设置executecmdResult属性的值。
     * 
     */
    public void setExecutecmdResult(int value) {
        this.executecmdResult = value;
    }

    /**
     * 获取cmddata属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCmddata() {
        return cmddata;
    }

    /**
     * 设置cmddata属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmddata(String value) {
        this.cmddata = value;
    }

    /**
     * 获取returndata属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReturndata() {
        return returndata;
    }

    /**
     * 设置returndata属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReturndata(String value) {
        this.returndata = value;
    }

}
