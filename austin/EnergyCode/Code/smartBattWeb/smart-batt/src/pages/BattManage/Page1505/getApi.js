import { ajax } from "../../../utils/ajax";

//電池類型API(POST):(含EXCEL)
export const ajaxGetBatteryTypeList = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `getBatteryTypeList`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//電池類型API(POST):
	//https://www.gtething.tw/battery/getBatteryTypeList
};

//新增電池型號API:
export const ajaxAddBatteryType = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `addBatteryType`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//新增電池型號API:
	//https://www.gtething.tw/battery/addBatteryType
};

//修改電池型號API:
export const ajaxUpdBatteryType = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `updBatteryType`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//修改電池型號API:
	//https://www.gtething.tw/battery/updBatteryType
};

//刪除電池型號API:
export const ajaxDelBatteryType = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `delBatteryType`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//刪除電池型號API:
	//https://www.gtething.tw/battery/delBatteryType
};
