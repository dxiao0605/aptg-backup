
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
 * <p>anonymous complex type �� Java ���O.
 * 
 * <p>�U�C���n���q�|���w�����O���]�t���w�����e.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contactAndGroup" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}contactGroupType" maxOccurs="unbounded"/>
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
    "contactAndGroup"
})
@XmlRootElement(name = "addContactToGroupReq")
public class AddContactToGroupReq {

    @XmlElement(required = true)
    protected List<ContactGroupType> contactAndGroup;
    @XmlAttribute(name = "subId", namespace = "http://truetel.com/war/aptpns/iweb/ws/pbk", required = true)
    protected String subId;

    /**
     * Gets the value of the contactAndGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contactAndGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContactAndGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContactGroupType }
     * 
     * 
     */
    public List<ContactGroupType> getContactAndGroup() {
        if (contactAndGroup == null) {
            contactAndGroup = new ArrayList<ContactGroupType>();
        }
        return this.contactAndGroup;
    }

    /**
     * ���o subId �S�ʪ���.
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
     * �]�w subId �S�ʪ���.
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
