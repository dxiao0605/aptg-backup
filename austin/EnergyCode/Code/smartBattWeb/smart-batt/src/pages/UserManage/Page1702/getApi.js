import { ajax } from "../../../utils/ajax";

//角色資訊API(GET):
export const ajaxGetRoleInfo = ({query}) => {
	const { token, curLanguage, timeZone, company, account } = query;
	const url = `getRoleInfo?account=${account}&type=${""}`;
	return ajax(url, "GET", token, curLanguage, timeZone, company);
	//角色資訊API(GET):
	//https://www.gtething.tw/battery/getRoleInfo?account=&type=
};
//角色資訊:取EXCEL API(Get):
export const ajaxGetRoleInfoCheckExcel = ({query}) => {
	const { token, curLanguage, timeZone, company, account } = query;
	const url = `getRoleInfo?account=${account}&type=${"check"}`;
	return ajax(url, "GET", token, curLanguage, timeZone, company);
	//角色資訊:取EXCEL API(Get):
	//https://www.gtething.tw/battery/getRoleInfo?account=&type=
};
//新增角色API(POST):
export const ajaxAddRole = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `addRole`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//新增角色API(POST):
	//https://www.gtething.tw/battery/addRole
};
//刪除角色API(POST):
export const ajaxDelRole = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `delRole`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//刪除角色API(POST):
	//https://www.gtething.tw/battery/delRole
};
//角色權限API(GET):
export const ajaxGetRoleAuthority = ({query,roleId}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `getRoleAuthority?roleId=${roleId}`;
	return ajax(url, "GET", token, curLanguage, timeZone, company);
	//角色權限API(GET):
	//https://www.gtething.tw/battery/getRoleAuthority?roleId=
};
//修改角色權限設定API(POST):
export const ajaxUpdRoleAuthority = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `updRoleAuthority`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//修改角色權限設定API(POST):
	//https://www.gtething.tw/battery/updRoleAuthority
};
