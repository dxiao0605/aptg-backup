package aptg.battery.vo;

import java.util.List;

import aptg.battery.bean.AuthorityBean;
import aptg.battery.bean.ButtonBean;

public class RoleVO {
	private String systemId;
	private String companyCode;
	private String roleRank;
	private String roleId;
	private String account;
	private String roleName;
	private String roleDesc;
	private String roleDescE;
	private String roleDescJ;
	private boolean error;
	private String code;
	private String description;
	private String authorityId;
	private String copyAuthorityId;
	private String roleRankLT;//小於
	private String roleRankLE;//小於等於

	private List<AuthorityBean> authorityList;
	private List<ButtonBean> buttonList;
	private String userName;

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getRoleRank() {
		return roleRank;
	}

	public void setRoleRank(String roleRank) {
		this.roleRank = roleRank;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getRoleDescE() {
		return roleDescE;
	}

	public void setRoleDescE(String roleDescE) {
		this.roleDescE = roleDescE;
	}

	public String getRoleDescJ() {
		return roleDescJ;
	}

	public void setRoleDescJ(String roleDescJ) {
		this.roleDescJ = roleDescJ;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(String authorityId) {
		this.authorityId = authorityId;
	}

	public String getCopyAuthorityId() {
		return copyAuthorityId;
	}

	public void setCopyAuthorityId(String copyAuthorityId) {
		this.copyAuthorityId = copyAuthorityId;
	}
	
	public List<AuthorityBean> getAuthorityList() {
		return authorityList;
	}

	public void setAuthorityList(List<AuthorityBean> authorityList) {
		this.authorityList = authorityList;
	}

	public List<ButtonBean> getButtonList() {
		return buttonList;
	}

	public void setButtonList(List<ButtonBean> buttonList) {
		this.buttonList = buttonList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleRankLT() {
		return roleRankLT;
	}

	public void setRoleRankLT(String roleRankLT) {
		this.roleRankLT = roleRankLT;
	}

	public String getRoleRankLE() {
		return roleRankLE;
	}

	public void setRoleRankLE(String roleRankLE) {
		this.roleRankLE = roleRankLE;
	}
	
}
