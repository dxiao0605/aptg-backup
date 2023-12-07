package aptg.battery.bean;

public class AuthorityBean {
	private int SystemId;
	private int AuthorityId;
	private int FunctionId;
	private int Edit;
	private int Operate = 1;
	
	public int getSystemId() {
		return SystemId;
	}
	public void setSystemId(int systemId) {
		SystemId = systemId;
	}
	public int getAuthorityId() {
		return AuthorityId;
	}
	public void setAuthorityId(int authorityId) {
		AuthorityId = authorityId;
	}
	public int getFunctionId() {
		return FunctionId;
	}
	public void setFunctionId(int functionId) {
		FunctionId = functionId;
	}
	public int getEdit() {
		return Edit;
	}
	public void setEdit(int edit) {
		Edit = edit;
	}
	public int getOperate() {
		return Operate;
	}
	public void setOperate(int operate) {
		Operate = operate;
	}
}
