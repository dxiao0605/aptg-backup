package aptg.cathaybkeco.bean;

import java.util.List;

public class BaseChartBean {
	private String id;
	private String desc;
	private List<BaseChartBean> children; 
	
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
	public List<BaseChartBean> getChildren() {
		return children;
	}
	public void setChildren(List<BaseChartBean> children) {
		this.children = children;
	}
}
