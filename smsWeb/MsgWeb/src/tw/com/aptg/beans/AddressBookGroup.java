package tw.com.aptg.beans;

import java.io.Serializable;

public class AddressBookGroup implements Serializable {
	private static final long serialVersionUID = 1L;
	private String groupName = ""; 
	private String groupDesc = ""; 
	private int count = 0;
	
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
