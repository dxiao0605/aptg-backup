
package com.truetel.war.aptpns.iweb.ws.pbk;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>queryContactType complex type 的 Java 類別.
 * 
 * <p>下列綱要片段會指定此類別中包含的預期內容.
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
     * 取得 exactGroupName 特性的值.
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
     * 設定 exactGroupName 特性的值.
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
     * 取得 contactId 特性的值.
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
     * 設定 contactId 特性的值.
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
     * 取得 familyName 特性的值.
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
     * 設定 familyName 特性的值.
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
     * 取得 givenName 特性的值.
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
     * 設定 givenName 特性的值.
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
     * 取得 cell 特性的值.
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
     * 設定 cell 特性的值.
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
     * 取得 nameOrCell 特性的值.
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
     * 設定 nameOrCell 特性的值.
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
