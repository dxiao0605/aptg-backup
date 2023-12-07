package aptg.bean;

import java.io.Serializable;

public class DelNBIDBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String NBID;

	public String getNBID() {
		return NBID;
	}
	public void setNBID(String nBID) {
		NBID = nBID;
	}
}
