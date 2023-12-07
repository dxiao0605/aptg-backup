
package com.truetel.war.aptpns.iweb.ws.pbk;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="contact" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}contactBaseType" maxOccurs="200"/>
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
    "contact"
})
@XmlRootElement(name = "createContactsReq")
public class CreateContactsReq {

    @XmlElement(required = true)
    protected List<ContactBaseType> contact;
    @XmlAttribute(name = "subId", namespace = "http://truetel.com/war/aptpns/iweb/ws/pbk", required = true)
    protected String subId;

    /**
     * Gets the value of the contact property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contact property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContact().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContactBaseType }
     * 
     * 
     */
    public List<ContactBaseType> getContact() {
        if (contact == null) {
            contact = new ArrayList<ContactBaseType>();
        }
        return this.contact;
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
