package aptg.bean;

import java.io.Serializable;

public class BABean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int UploadCycle;
	private int IRCycle;
	private int CommunicationCycle;
	
	public int getUploadCycle() {
		return UploadCycle;
	}
	public void setUploadCycle(int uploadCycle) {
		UploadCycle = uploadCycle;
	}
	public int getIRCycle() {
		return IRCycle;
	}
	public void setIRCycle(int iRCycle) {
		IRCycle = iRCycle;
	}
	public int getCommunicationCycle() {
		return CommunicationCycle;
	}
	public void setCommunicationCycle(int communicationCycle) {
		CommunicationCycle = communicationCycle;
	}
}
