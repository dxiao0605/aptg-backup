
package com.truetel.war.aptpns.iweb.ws.pbk;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type �� Java ���O.
 * 
 * <p>�U�C���n���q�|���w�����O���]�t���w�����e.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="numContacts" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "numContacts"
})
@XmlRootElement(name = "countContactsResp")
public class CountContactsResp {

    @XmlElement(defaultValue = "0")
    protected int numContacts;

    /**
     * ���o numContacts �S�ʪ���.
     * 
     */
    public int getNumContacts() {
        return numContacts;
    }

    /**
     * �]�w numContacts �S�ʪ���.
     * 
     */
    public void setNumContacts(int value) {
        this.numContacts = value;
    }

}
