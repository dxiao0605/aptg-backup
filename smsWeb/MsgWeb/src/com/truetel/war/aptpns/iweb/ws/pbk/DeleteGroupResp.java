
package com.truetel.war.aptpns.iweb.ws.pbk;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type 的 Java 類別.
 * 
 * <p>下列綱要片段會指定此類別中包含的預期內容.
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
     * 取得 succExecute 特性的值.
     * 
     */
    public boolean isSuccExecute() {
        return succExecute;
    }

    /**
     * 設定 succExecute 特性的值.
     * 
     */
    public void setSuccExecute(boolean value) {
        this.succExecute = value;
    }

}
