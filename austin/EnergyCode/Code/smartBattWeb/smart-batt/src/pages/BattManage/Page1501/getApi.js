import { ajax } from '../../../utils/ajax';

//電池組管理API(Get):
export const ajaxGetBattManage = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `getBattManage`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//電池組管理API(Get):
	//https://www.gtething.tw/battery/getBattManage
};
//電池組編輯API(POST):
export const ajaxUpdBattery = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `updBattery`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//電池組編輯API(POST):
	//https://www.gtething.tw/battery/updBattery
};
//電池組管理取EXCEL NAME API(Get):
export const ajaxGetBattManageExcel = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `getBattManage`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//電池組管理取EXCEL NAME API(Get):
	//https://www.gtething.tw/battery/getBattManage
};
