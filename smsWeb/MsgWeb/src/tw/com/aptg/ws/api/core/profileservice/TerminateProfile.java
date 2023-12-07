
package tw.com.aptg.ws.api.core.profileservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>terminateProfile complex type 的 Java 類別.
 * 
 * <p>下列綱要片段會指定此類別中包含的預期內容.
 * 
 * <pre>
 * &lt;complexType name="terminateProfile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="duplicate" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="profile" type="{http://www.aptg.com.tw/ws/api/core/ProfileService}profile" minOccurs="0"/>
 *         &lt;element name="recordDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="terminateType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "terminateProfile", propOrder = {
    "duplicate",
    "profile",
    "recordDate",
    "terminateType"
})
public class TerminateProfile {

    protected boolean duplicate;
    protected Profile profile;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar recordDate;
    protected String terminateType;

    /**
     * 取得 duplicate 特性的值.
     * 
     */
    public boolean isDuplicate() {
        return duplicate;
    }

    /**
     * 設定 duplicate 特性的值.
     * 
     */
    public void setDuplicate(boolean value) {
        this.duplicate = value;
    }

    /**
     * 取得 profile 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link Profile }
     *     
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * 設定 profile 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link Profile }
     *     
     */
    public void setProfile(Profile value) {
        this.profile = value;
    }

    /**
     * 取得 recordDate 特性的值.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRecordDate() {
        return recordDate;
    }

    /**
     * 設定 recordDate 特性的值.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRecordDate(XMLGregorianCalendar value) {
        this.recordDate = value;
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

}
