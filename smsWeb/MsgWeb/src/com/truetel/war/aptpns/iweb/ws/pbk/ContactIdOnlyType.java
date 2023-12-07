
package com.truetel.war.aptpns.iweb.ws.pbk;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>contactIdOnlyType complex type 的 Java 類別.
 * 
 * <p>下列綱要片段會指定此類別中包含的預期內容.
 * 
 * <pre>
 * &lt;complexType name="contactIdOnlyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="contactId" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}contactIdType"/>
 *       &lt;/all>
 *       &lt;attribute name="index" type="{http://www.w3.org/2001/XMLSchema}int" default="0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contactIdOnlyType", propOrder = {

})
public class ContactIdOnlyType {

    protected int contactId;
    @XmlAttribute(name = "index", namespace = "http://truetel.com/war/aptpns/iweb/ws/pbk")
    protected Integer index;

    /**
     * 取得 contactId 特性的值.
     * 
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * 設定 contactId 特性的值.
     * 
     */
    public void setContactId(int value) {
        this.contactId = value;
    }

    /**
     * 取得 index 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getIndex() {
        if (index == null) {
            return  0;
        } else {
            return index;
        }
    }

    /**
     * 設定 index 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIndex(Integer value) {
        this.index = value;
    }

}
