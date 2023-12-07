
package com.truetel.war.aptpns.iweb.ws.pbk;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type 的 Java 類別.
 * 
 * <p>下列綱要片段會指定此類別中包含的預期內容.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="queryContact" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}queryContactType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="subId" use="required" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}subIdType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "queryContact"
})
@XmlRootElement(name = "listContactsReq")
public class ListContactsReq {

    protected QueryContactType queryContact;
    @XmlAttribute(name = "subId", namespace = "http://truetel.com/war/aptpns/iweb/ws/pbk", required = true)
    protected String subId;

    /**
     * 取得 queryContact 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link QueryContactType }
     *     
     */
    public QueryContactType getQueryContact() {
        return queryContact;
    }

    /**
     * 設定 queryContact 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryContactType }
     *     
     */
    public void setQueryContact(QueryContactType value) {
        this.queryContact = value;
    }

    /**
     * 取得 subId 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubId() {
        return subId;
    }

    /**
     * 設定 subId 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubId(String value) {
        this.subId = value;
    }

}
