import { ajax } from "../../utils/ajax";

//參數設定歷史(POST):(含EXCEL)
export const ajaxGetCommandHistory = ({ query, postData }) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `getCommandHistory`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//參數設定歷史(POST):
	//https://www.gtething.tw/battery/getCommandHistory
};
