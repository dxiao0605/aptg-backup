package aptg.model;

import java.io.Serializable;

public class NBListModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nbID;
	private int groupInternalID;
	private int allocate;
	private int active;
	
	public String getNbID() {
		return nbID;
	}
	public void setNbID(String nbID) {
		this.nbID = nbID;
	}
	public int getGroupInternalID() {
		return groupInternalID;
	}
	public void setGroupInternalID(int groupInternalID) {
		this.groupInternalID = groupInternalID;
	}
	public int getAllocate() {
		return allocate;
	}
	public void setAllocate(int allocate) {
		this.allocate = allocate;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
}
