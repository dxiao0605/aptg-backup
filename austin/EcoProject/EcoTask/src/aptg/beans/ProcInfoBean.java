package aptg.beans;

import java.io.Serializable;

public class ProcInfoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String pid;
	private String cmd;

	public ProcInfoBean(String pid, String cmd) {
		this.pid = pid;
		this.cmd = cmd;
	}
	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
}
