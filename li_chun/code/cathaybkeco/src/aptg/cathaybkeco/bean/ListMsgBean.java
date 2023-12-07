package aptg.cathaybkeco.bean;

import java.util.List;

public class ListMsgBean {
	private List<ListBean> msg;
	private String code;
	
	public List<ListBean> getMsg() {
		return msg;
	}
	public void setMsg(List<ListBean> msg) {
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
