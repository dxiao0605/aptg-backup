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
 * 01        20180323    Gary Chang          新增Serializable
 */

package tw.com.aptg.ws.api.core.profileservice;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>profile complex type 的 Java 類別.
 * 
 * <p>下列綱要片段會指定此類別中包含的預期內容.
 * 
 * <pre>
 * &lt;complexType name="profile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="acceptEpaper" type="{http://www.aptg.com.tw/ws/api/core/ProfileService}acceptance" minOccurs="0"/>
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="birthday" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bscsCOID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bscsCSCODE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bscsCSID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="companyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contactPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contractID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contractStatus" type="{http://www.aptg.com.tw/ws/api/core/ProfileService}yesNoOption" minOccurs="0"/>
 *         &lt;element name="contractStatusCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="disableWeb" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="education" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ename" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gender" type="{http://www.aptg.com.tw/ws/api/core/ProfileService}gender" minOccurs="0"/>
 *         &lt;element name="isAdult" type="{http://www.aptg.com.tw/ws/api/core/ProfileService}yesNoOption" minOccurs="0"/>
 *         &lt;element name="jobTitle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mdn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="min" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mobile1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mobile2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mobilePhoneType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nickname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="occupation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="otpStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="personalID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="promotionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pushMail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="smscMail" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telphone1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telphone2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="terminateType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="userType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "profile", propOrder = {
    "acceptEpaper",
    "address",
    "bc",
    "birthday",
    "bscsCOID",
    "bscsCSCODE",
    "bscsCSID",
    "companyName",
    "contactPhone",
    "contractID",
    "contractStatus",
    "contractStatusCode",
    "customerID",
    "disableWeb",
    "education",
    "email",
    "ename",
    "gender",
    "isAdult",
    "jobTitle",
    "mdn",
    "min",
    "mobile1",
    "mobile2",
    "mobilePhoneType",
    "name",
    "nickname",
    "occupation",
    "otpStatus",
    "personalID",
    "productType",
    "promotionCode",
    "pushMail",
    "smscMail",
    "telphone1",
    "telphone2",
    "terminateType",
    "uid",
    "userType"
})

//#01
public class Profile implements Serializable {
	private static final long serialVersionUID = 1L;
    protected Acceptance acceptEpaper;
    protected String address;
    protected String bc;
    protected String birthday;
    protected String bscsCOID;
    protected String bscsCSCODE;
    protected String bscsCSID;
    protected String companyName;
    protected String contactPhone;
    protected String contractID;
    protected YesNoOption contractStatus;
    protected String contractStatusCode;
    protected String customerID;
    protected String disableWeb;
    protected String education;
    protected String email;
    protected String ename;
    protected Gender gender;
    protected YesNoOption isAdult;
    protected String jobTitle;
    protected String mdn;
    protected String min;
    protected String mobile1;
    protected String mobile2;
    protected String mobilePhoneType;
    protected String name;
    protected String nickname;
    protected String occupation;
    protected String otpStatus;
    protected String personalID;
    protected String productType;
    protected String promotionCode;
    protected String pushMail;
    protected String smscMail;
    protected String telphone1;
    protected String telphone2;
    protected String terminateType;
    protected String uid;
    protected String userType;

    /**
     * 取得 acceptEpaper 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link Acceptance }
     *     
     */
    public Acceptance getAcceptEpaper() {
        return acceptEpaper;
    }

    /**
     * 設定 acceptEpaper 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link Acceptance }
     *     
     */
    public void setAcceptEpaper(Acceptance value) {
        this.acceptEpaper = value;
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
     * 取得 bc 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBc() {
        return bc;
    }

    /**
     * 設定 bc 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBc(String value) {
        this.bc = value;
    }

    /**
     * 取得 birthday 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * 設定 birthday 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthday(String value) {
        this.birthday = value;
    }

    /**
     * 取得 bscsCOID 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBscsCOID() {
        return bscsCOID;
    }

    /**
     * 設定 bscsCOID 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBscsCOID(String value) {
        this.bscsCOID = value;
    }

    /**
     * 取得 bscsCSCODE 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBscsCSCODE() {
        return bscsCSCODE;
    }

    /**
     * 設定 bscsCSCODE 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBscsCSCODE(String value) {
        this.bscsCSCODE = value;
    }

    /**
     * 取得 bscsCSID 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBscsCSID() {
        return bscsCSID;
    }

    /**
     * 設定 bscsCSID 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBscsCSID(String value) {
        this.bscsCSID = value;
    }

    /**
     * 取得 companyName 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 設定 companyName 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyName(String value) {
        this.companyName = value;
    }

    /**
     * 取得 contactPhone 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * 設定 contactPhone 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactPhone(String value) {
        this.contactPhone = value;
    }

    /**
     * 取得 contractID 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContractID() {
        return contractID;
    }

    /**
     * 設定 contractID 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContractID(String value) {
        this.contractID = value;
    }

    /**
     * 取得 contractStatus 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link YesNoOption }
     *     
     */
    public YesNoOption getContractStatus() {
        return contractStatus;
    }

    /**
     * 設定 contractStatus 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link YesNoOption }
     *     
     */
    public void setContractStatus(YesNoOption value) {
        this.contractStatus = value;
    }

    /**
     * 取得 contractStatusCode 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContractStatusCode() {
        return contractStatusCode;
    }

    /**
     * 設定 contractStatusCode 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContractStatusCode(String value) {
        this.contractStatusCode = value;
    }

    /**
     * 取得 customerID 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerID() {
        return customerID;
    }

    /**
     * 設定 customerID 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerID(String value) {
        this.customerID = value;
    }

    /**
     * 取得 disableWeb 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisableWeb() {
        return disableWeb;
    }

    /**
     * 設定 disableWeb 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisableWeb(String value) {
        this.disableWeb = value;
    }

    /**
     * 取得 education 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEducation() {
        return education;
    }

    /**
     * 設定 education 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEducation(String value) {
        this.education = value;
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
     * 取得 ename 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEname() {
        return ename;
    }

    /**
     * 設定 ename 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEname(String value) {
        this.ename = value;
    }

    /**
     * 取得 gender 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link Gender }
     *     
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * 設定 gender 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link Gender }
     *     
     */
    public void setGender(Gender value) {
        this.gender = value;
    }

    /**
     * 取得 isAdult 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link YesNoOption }
     *     
     */
    public YesNoOption getIsAdult() {
        return isAdult;
    }

    /**
     * 設定 isAdult 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link YesNoOption }
     *     
     */
    public void setIsAdult(YesNoOption value) {
        this.isAdult = value;
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
     * 取得 mdn 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMdn() {
        return mdn;
    }

    /**
     * 設定 mdn 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMdn(String value) {
        this.mdn = value;
    }

    /**
     * 取得 min 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMin() {
        return min;
    }

    /**
     * 設定 min 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMin(String value) {
        this.min = value;
    }

    /**
     * 取得 mobile1 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobile1() {
        return mobile1;
    }

    /**
     * 設定 mobile1 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobile1(String value) {
        this.mobile1 = value;
    }

    /**
     * 取得 mobile2 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobile2() {
        return mobile2;
    }

    /**
     * 設定 mobile2 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobile2(String value) {
        this.mobile2 = value;
    }

    /**
     * 取得 mobilePhoneType 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobilePhoneType() {
        return mobilePhoneType;
    }

    /**
     * 設定 mobilePhoneType 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobilePhoneType(String value) {
        this.mobilePhoneType = value;
    }

    /**
     * 取得 name 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * 設定 name 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * 取得 nickname 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 設定 nickname 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNickname(String value) {
        this.nickname = value;
    }

    /**
     * 取得 occupation 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * 設定 occupation 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOccupation(String value) {
        this.occupation = value;
    }

    /**
     * 取得 otpStatus 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtpStatus() {
        return otpStatus;
    }

    /**
     * 設定 otpStatus 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtpStatus(String value) {
        this.otpStatus = value;
    }

    /**
     * 取得 personalID 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPersonalID() {
        return personalID;
    }

    /**
     * 設定 personalID 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPersonalID(String value) {
        this.personalID = value;
    }

    /**
     * 取得 productType 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductType() {
        return productType;
    }

    /**
     * 設定 productType 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductType(String value) {
        this.productType = value;
    }

    /**
     * 取得 promotionCode 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPromotionCode() {
        return promotionCode;
    }

    /**
     * 設定 promotionCode 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPromotionCode(String value) {
        this.promotionCode = value;
    }

    /**
     * 取得 pushMail 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPushMail() {
        return pushMail;
    }

    /**
     * 設定 pushMail 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPushMail(String value) {
        this.pushMail = value;
    }

    /**
     * 取得 smscMail 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmscMail() {
        return smscMail;
    }

    /**
     * 設定 smscMail 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmscMail(String value) {
        this.smscMail = value;
    }

    /**
     * 取得 telphone1 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelphone1() {
        return telphone1;
    }

    /**
     * 設定 telphone1 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelphone1(String value) {
        this.telphone1 = value;
    }

    /**
     * 取得 telphone2 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelphone2() {
        return telphone2;
    }

    /**
     * 設定 telphone2 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelphone2(String value) {
        this.telphone2 = value;
    }

    /**
     * 取得 terminateType 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminateType() {
        return terminateType;
    }

    /**
     * 設定 terminateType 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminateType(String value) {
        this.terminateType = value;
    }

    /**
     * 取得 uid 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUid() {
        return uid;
    }

    /**
     * 設定 uid 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUid(String value) {
        this.uid = value;
    }

    /**
     * 取得 userType 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserType() {
        return userType;
    }

    /**
     * 設定 userType 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserType(String value) {
        this.userType = value;
    }

}
