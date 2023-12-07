
package com.truetel.war.aptpns.iweb.ws.pbk;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>contactGroupType complex type �� Java ���O.
 * 
 * <p>�U�C���n���q�|���w�����O���]�t���w�����e.
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
     * ���o groupName �S�ʪ���.
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
     * �]�w groupName �S�ʪ���.
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
     * ���o contactId �S�ʪ���.
     * 
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * �]�w contactId �S�ʪ���.
     * 
     */
    public void setContactId(int value) {
        this.contactId = value;
    }

}
