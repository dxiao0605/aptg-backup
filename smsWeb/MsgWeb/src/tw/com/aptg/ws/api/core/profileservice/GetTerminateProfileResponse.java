
package tw.com.aptg.ws.api.core.profileservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>getTerminateProfileResponse complex type �� Java ���O.
 * 
 * <p>�U�C���n���q�|���w�����O���]�t���w�����e.
 * 
 * <pre>
 * &lt;complexType name="getTerminateProfileResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="terminateResponse" type="{http://www.aptg.com.tw/ws/api/core/ProfileService}terminateResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTerminateProfileResponse", propOrder = {
    "terminateResponse"
})
public class GetTerminateProfileResponse {

    protected TerminateResponse terminateResponse;

    /**
     * ���o terminateResponse �S�ʪ���.
     * 
     * @return
     *     possible object is
     *     {@link TerminateResponse }
     *     
     */
    public TerminateResponse getTerminateResponse() {
        return terminateResponse;
    }

    /**
     * �]�w terminateResponse �S�ʪ���.
     * 
     * @param value
     *     allowed object is
     *     {@link TerminateResponse }
     *     
     */
    public void setTerminateResponse(TerminateResponse value) {
        this.terminateResponse = value;
    }

}
