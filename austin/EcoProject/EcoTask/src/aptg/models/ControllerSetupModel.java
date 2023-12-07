package aptg.models;

import java.io.Serializable;

public class ControllerSetupModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ECO5Account;

	public String getECO5Account() {
		return ECO5Account;
	}
	public void setECO5Account(String eCO5Account) {
		ECO5Account = eCO5Account;
	}
}
