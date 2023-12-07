package aptg.cathaybkeco.bean;

import java.util.List;

public class BaseBean {
	private String id;
	private String desc;
	private String sortName;
	private boolean isChecked;
	private List<BaseBean> children; 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getSortName() {
		return sortName;
	}
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public List<BaseBean> getChildren() {
		return children;
	}
	public void setChildren(List<BaseBean> children) {
		this.children = children;
	}
}
