
package tw.com.aptg.ws.api.core.profileservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>getTerminateProfileResponse complex type 的 Java 類別.
 * 
 * <p>下列綱要片段會指定此類別中包含的預期內容.
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
     * 取得 terminateResponse 特性的值.
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
     * 設定 terminateResponse 特性的值.
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
