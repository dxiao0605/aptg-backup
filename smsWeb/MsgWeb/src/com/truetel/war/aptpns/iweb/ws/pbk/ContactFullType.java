/**
 * ================== Copyright Notice ===================
 * This file contains proprietary information of APTG.
 * Copying or reproduction without prior written
 * approval is prohibited.
 * Copyright (c) 2018
 *
 *
 * ------------------  History ---------------------------
 * Version   Date        Developer           Description
 * 01        20180329    Gary Chang          新增Serializable
 */

package com.truetel.war.aptpns.iweb.ws.pbk;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>contactFullType complex type 的 Java 類別.
 * 
 * <p>下列綱要片段會指定此類別中包含的預期內容.
 * 
 * <pre>
 * &lt;complexType name="contactFullType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="contactId" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}contactIdType" minOccurs="0"/>
 *         &lt;element name="familyName" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="givenName" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="cell" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}CellType" minOccurs="0"/>
 *         &lt;element name="cellHome" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}CellType" minOccurs="0"/>
 *         &lt;element name="cellWork" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}CellType" minOccurs="0"/>
 *         &lt;element name="phone" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}PhoneType" minOccurs="0"/>
 *         &lt;element name="phoneHome" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}PhoneType" minOccurs="0"/>
 *         &lt;element name="phoneWork" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}PhoneType" minOccurs="0"/>
 *         &lt;element name="fax" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}PhoneType" minOccurs="0"/>
 *         &lt;element name="email" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}EmailType" minOccurs="0"/>
 *         &lt;element name="emailHome" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}EmailType" minOccurs="0"/>
 *         &lt;element name="emailWork" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}EmailType" minOccurs="0"/>
 *         &lt;element name="webUrlHome" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="webUrlWork" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="company" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="jobTitle" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="nickName" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="country" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="postcode" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="city" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="address" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="birthday" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="yahooId" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="msLiveId" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="googleId" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="skypeId" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string64" minOccurs="0"/>
 *         &lt;element name="note" type="{http://truetel.com/war/aptpns/iweb/ws/pbk}string1000" minOccurs="0"/>
 *       &lt;/all>
 *       &lt;attribute name="index" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contactFullType", propOrder = {

})
//#01
public class ContactFullType implements Serializable {
	private static final long serialVersionUID = 1L;
	
    protected Integer contactId;
    protected String familyName;
    protected String givenName;
    protected String cell;
    protected String cellHome;
    protected String cellWork;
    protected String phone;
    protected String phoneHome;
    protected String phoneWork;
    protected String fax;
    protected String email;
    protected String emailHome;
    protected String emailWork;
    protected String webUrlHome;
    protected String webUrlWork;
    protected String company;
    protected String jobTitle;
    protected String nickName;
    protected String country;
    protected String postcode;
    protected String city;
    protected String address;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar birthday;
    protected String yahooId;
    protected String msLiveId;
    protected String googleId;
    protected String skypeId;
    protected String note;
    @XmlAttribute(name = "index", namespace = "http://truetel.com/war/aptpns/iweb/ws/pbk")
    protected Integer index;

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
     * 取得 cellHome 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCellHome() {
        return cellHome;
    }

    /**
     * 設定 cellHome 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCellHome(String value) {
        this.cellHome = value;
    }

    /**
     * 取得 cellWork 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCellWork() {
        return cellWork;
    }

    /**
     * 設定 cellWork 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCellWork(String value) {
        this.cellWork = value;
    }

    /**
     * 取得 phone 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 設定 phone 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhone(String value) {
        this.phone = value;
    }

    /**
     * 取得 phoneHome 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhoneHome() {
        return phoneHome;
    }

    /**
     * 設定 phoneHome 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhoneHome(String value) {
        this.phoneHome = value;
    }

    /**
     * 取得 phoneWork 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhoneWork() {
        return phoneWork;
    }

    /**
     * 設定 phoneWork 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhoneWork(String value) {
        this.phoneWork = value;
    }

    /**
     * 取得 fax 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFax() {
        return fax;
    }

    /**
     * 設定 fax 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFax(String value) {
        this.fax = value;
    }

    /**
     * 取得 email 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * 設定 email 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * 取得 emailHome 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailHome() {
        return emailHome;
    }

    /**
     * 設定 emailHome 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailHome(String value) {
        this.emailHome = value;
    }

    /**
     * 取得 emailWork 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailWork() {
        return emailWork;
    }

    /**
     * 設定 emailWork 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailWork(String value) {
        this.emailWork = value;
    }

    /**
     * 取得 webUrlHome 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebUrlHome() {
        return webUrlHome;
    }

    /**
     * 設定 webUrlHome 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebUrlHome(String value) {
        this.webUrlHome = value;
    }

    /**
     * 取得 webUrlWork 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebUrlWork() {
        return webUrlWork;
    }

    /**
     * 設定 webUrlWork 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebUrlWork(String value) {
        this.webUrlWork = value;
    }

    /**
     * 取得 company 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompany() {
        return company;
    }

    /**
     * 設定 company 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompany(String value) {
        this.company = value;
    }

    /**
     * 取得 jobTitle 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * 設定 jobTitle 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJobTitle(String value) {
        this.jobTitle = value;
    }

    /**
     * 取得 nickName 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 設定 nickName 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNickName(String value) {
        this.nickName = value;
    }

    /**
     * 取得 country 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * 設定 country 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * 取得 postcode 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * 設定 postcode 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostcode(String value) {
        this.postcode = value;
    }

    /**
     * 取得 city 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * 設定 city 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * 取得 address 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * 設定 address 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * 取得 birthday 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBirthday() {
        return birthday;
    }

    /**
     * 設定 birthday 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBirthday(XMLGregorianCalendar value) {
        this.birthday = value;
    }

    /**
     * 取得 yahooId 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYahooId() {
        return yahooId;
    }

    /**
     * 設定 yahooId 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYahooId(String value) {
        this.yahooId = value;
    }

    /**
     * 取得 msLiveId 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMsLiveId() {
        return msLiveId;
    }

    /**
     * 設定 msLiveId 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMsLiveId(String value) {
        this.msLiveId = value;
    }

    /**
     * 取得 googleId 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGoogleId() {
        return googleId;
    }

    /**
     * 設定 googleId 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGoogleId(String value) {
        this.googleId = value;
    }

    /**
     * 取得 skypeId 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSkypeId() {
        return skypeId;
    }

    /**
     * 設定 skypeId 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSkypeId(String value) {
        this.skypeId = value;
    }

    /**
     * 取得 note 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNote() {
        return note;
    }

    /**
     * 設定 note 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(String value) {
        this.note = value;
    }

    /**
     * 取得 index 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIndex() {
        return index;
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
