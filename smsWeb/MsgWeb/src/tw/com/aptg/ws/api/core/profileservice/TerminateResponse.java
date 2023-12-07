
package tw.com.aptg.ws.api.core.profileservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>terminateResponse complex type �� Java ���O.
 * 
 * <p>�U�C���n���q�|���w�����O���]�t���w�����e.
 * 
 * <pre>
 * &lt;complexType name="terminateResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resultCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="terminateprofile" type="{http://www.aptg.com.tw/ws/api/core/ProfileService}terminateProfile" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "terminateResponse", propOrder = {
    "description",
    "resultCode",
    "terminateprofile"
})
public class TerminateResponse {

    protected String description;
    protected String resultCode;
    protected TerminateProfile terminateprofile;

    /**
     * ���o description �S�ʪ���.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * �]�w description �S�ʪ���.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * ���o resultCode �S�ʪ���.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultCode() {
        return resultCode;
    }

    /**
     * �]�w resultCode �S�ʪ���.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultCode(String value) {
        this.resultCode = value;
    }

    /**
     * ���o terminateprofile �S�ʪ���.
     * 
     * @return
     *     possible object is
     *     {@link TerminateProfile }
     *     
     */
    public TerminateProfile getTerminateprofile() {
        return terminateprofile;
    }

    /**
     * �]�w terminateprofile �S�ʪ���.
     * 
     * @param value
     *     allowed object is
     *     {@link TerminateProfile }
     *     
     */
    public void setTerminateprofile(TerminateProfile value) {
        this.terminateprofile = value;
    }

}
