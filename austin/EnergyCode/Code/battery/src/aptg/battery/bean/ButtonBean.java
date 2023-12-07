package aptg.battery.bean;

public class ButtonBean {
	private int AuthorityId;
	private int ProgramId;
	private String ButtonId;
	private String ButtonDesc;
	private int Enabled = 1;
	
	public int getAuthorityId() {
		return AuthorityId;
	}
	public void setAuthorityId(int authorityId) {
		AuthorityId = authorityId;
	}
	public int getProgramId() {
		return ProgramId;
	}
	public void setProgramId(int programId) {
		ProgramId = programId;
	}
	public String getButtonId() {
		return ButtonId;
	}
	public void setButtonId(String buttonId) {
		ButtonId = buttonId;
	}
	public String getButtonDesc() {
		return ButtonDesc;
	}
	public void setButtonDesc(String buttonDesc) {
		ButtonDesc = buttonDesc;
	}
	public int getEnabled() {
		return Enabled;
	}
	public void setEnabled(int enabled) {
		Enabled = enabled;
	}
	
}
