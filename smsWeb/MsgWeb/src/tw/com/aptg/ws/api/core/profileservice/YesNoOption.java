
package tw.com.aptg.ws.api.core.profileservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>yesNoOption �� Java ���O.
 * 
 * <p>�U�C���n���q�|���w�����O���]�t���w�����e.
 * <p>
 * <pre>
 * &lt;simpleType name="yesNoOption">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Y"/>
 *     &lt;enumeration value="N"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "yesNoOption")
@XmlEnum
public enum YesNoOption {

    Y,
    N;

    public String value() {
        return name();
    }

    public static YesNoOption fromValue(String v) {
        return valueOf(v);
    }

}
