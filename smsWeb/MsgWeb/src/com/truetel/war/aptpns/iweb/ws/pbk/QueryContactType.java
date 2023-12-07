
package com.truetel.war.aptpns.iweb.ws.pbk;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>queryContactType complex type �� Java ���O.
 * 
 * <p>�U�C���n���q�|���w�����O���]�t���w�����e.
 * 
 * <pre>
 * &lt;complexType name="queryContactType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="groupName" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="exactGroupName" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="contactId" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}contactIdType" minOccurs="0"/>
 *         &lt;element name="familyName" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="givenName" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="cell" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}CellType" minOccurs="0"/>
 *         &lt;element name="nameOrCell" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "queryContactType", propOrder = {

})
public class QueryContactType {

    protected String groupName;
    protected String exactGroupName;
    protected Integer contactId;
    protected String familyName;
    protected String givenName;
    protected String cell;
    protected String nameOrCell;

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
     * ���o exactGroupName �S�ʪ���.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExactGroupName() {
        return exactGroupName;
    }

    /**
     * �]�w exactGroupName �S�ʪ���.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExactGroupName(String value) {
        this.exactGroupName = value;
    }

    /**
     * ���o contactId �S�ʪ���.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getContactId() {
        return contactId;
    }

    /**
     * �]�w contactId �S�ʪ���.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setContactId(Integer value) {
        this.contactId = value;
    }

    /**
     * ���o familyName �S�ʪ���.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * �]�w familyName �S�ʪ���.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFamilyName(String value) {
        this.familyName = value;
    }

    /**
     * ���o givenName �S�ʪ���.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * �]�w givenName �S�ʪ���.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGivenName(String value) {
        this.givenName = value;
    }

    /**
     * ���o cell �S�ʪ���.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCell() {
        return cell;
    }

    /**
     * �]�w cell �S�ʪ���.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCell(String value) {
        this.cell = value;
    }

    /**
     * ���o nameOrCell �S�ʪ���.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameOrCell() {
        return nameOrCell;
    }

    /**
     * �]�w nameOrCell �S�ʪ���.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameOrCell(String value) {
        this.nameOrCell = value;
    }

}
