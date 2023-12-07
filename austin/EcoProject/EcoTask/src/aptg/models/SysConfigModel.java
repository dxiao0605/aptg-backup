package aptg.models;

import java.io.Serializable;

public class SysConfigModel implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String paramname;
	private String paramvalue;
	
	public String getParamname() {
		return paramname;
	}
	public void setParamname(String paramname) {
		this.paramname = paramname;
	}
	public String getParamvalue() {
		return paramvalue;
	}
	public void setParamvalue(String paramvalue) {
		this.paramvalue = paramvalue;
	}
}
