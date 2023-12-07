
package com.truetel.war.aptpns.iweb.ws.pbk;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>contactGroupType complex type 的 Java 類別.
 * 
 * <p>下列綱要片段會指定此類別中包含的預期內容.
 * 
 * <pre>
 * &lt;complexType name="contactGroupType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="groupName" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64"/>
 *         &lt;element name="contactId" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}contactIdType"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contactGroupType", propOrder = {

})
public class ContactGroupType {

    @XmlElement(required = true)
    protected String groupName;
    protected int contactId;

    /**
     * 取得 groupName 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 設定 groupName 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupName(String value) {
        this.groupName = value;
    }

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

}
