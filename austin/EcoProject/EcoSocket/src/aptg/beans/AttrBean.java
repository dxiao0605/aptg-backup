package aptg.beans;

import java.io.Serializable;

public class AttrBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String attrID;
	private String attrValue;
	
	public String getAttrID() {
		return attrID;
	}
	public void setAttrID(String attrID) {
		this.attrID = attrID;
	}
	public String getAttrValue() {
		return attrValue;
	}
	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}
}
