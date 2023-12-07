
package com.truetel.war.aptpns.iweb.ws.pbk;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>contactBaseType complex type �� Java ���O.
 * 
 * <p>�U�C���n���q�|���w�����O���]�t���w�����e.
 * 
 * <pre>
 * &lt;complexType name="contactBaseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
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
@XmlType(name = "contactBaseType", propOrder = {

})
public class ContactBaseType {

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
     * ���o cellHome �S�ʪ���.
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
     * �]�w cellHome �S�ʪ���.
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
     * ���o cellWork �S�ʪ���.
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
     * �]�w cellWork �S�ʪ���.
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
     * ���o phone �S�ʪ���.
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
     * �]�w phone �S�ʪ���.
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
     * ���o phoneHome �S�ʪ���.
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
     * �]�w phoneHome �S�ʪ���.
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
     * ���o phoneWork �S�ʪ���.
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
     * �]�w phoneWork �S�ʪ���.
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
     * ���o fax �S�ʪ���.
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
     * �]�w fax �S�ʪ���.
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
     * ���o email �S�ʪ���.
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
     * �]�w email �S�ʪ���.
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
     * ���o emailHome �S�ʪ���.
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
     * �]�w emailHome �S�ʪ���.
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
     * ���o emailWork �S�ʪ���.
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
     * �]�w emailWork �S�ʪ���.
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
     * ���o webUrlHome �S�ʪ���.
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
     * �]�w webUrlHome �S�ʪ���.
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
     * ���o webUrlWork �S�ʪ���.
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
     * �]�w webUrlWork �S�ʪ���.
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
     * ���o company �S�ʪ���.
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
     * �]�w company �S�ʪ���.
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
     * ���o jobTitle �S�ʪ���.
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
     * �]�w jobTitle �S�ʪ���.
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
     * ���o nickName �S�ʪ���.
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
     * �]�w nickName �S�ʪ���.
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
     * ���o country �S�ʪ���.
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
     * �]�w country �S�ʪ���.
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
     * ���o postcode �S�ʪ���.
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
     * �]�w postcode �S�ʪ���.
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
     * ���o city �S�ʪ���.
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
     * �]�w city �S�ʪ���.
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
     * ���o address �S�ʪ���.
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
     * �]�w address �S�ʪ���.
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
     * ���o birthday �S�ʪ���.
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
     * �]�w birthday �S�ʪ���.
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
     * ���o yahooId �S�ʪ���.
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
     * �]�w yahooId �S�ʪ���.
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
     * ���o msLiveId �S�ʪ���.
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
     * �]�w msLiveId �S�ʪ���.
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
     * ���o googleId �S�ʪ���.
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
     * �]�w googleId �S�ʪ���.
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
     * ���o skypeId �S�ʪ���.
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
     * �]�w skypeId �S�ʪ���.
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
     * ���o note �S�ʪ���.
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
     * �]�w note �S�ʪ���.
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
     * ���o index �S�ʪ���.
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
     * �]�w index �S�ʪ���.
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
