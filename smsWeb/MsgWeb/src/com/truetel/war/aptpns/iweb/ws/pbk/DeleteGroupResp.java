
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
 *         &lt;element ref="{http://truetel.com/war/aptpns/iweb/ws/pbk}SuccExecute"/>
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
    "succExecute"
})
@XmlRootElement(name = "deleteGroupResp")
public class DeleteGroupResp {

    @XmlElement(name = "SuccExecute", defaultValue = "true")
    protected boolean succExecute;

    /**
     * ���o succExecute �S�ʪ���.
     * 
     */
    public boolean isSuccExecute() {
        return succExecute;
    }

    /**
     * �]�w succExecute �S�ʪ���.
     * 
     */
    public void setSuccExecute(boolean value) {
        this.succExecute = value;
    }

}
