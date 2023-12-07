import { ajax } from "../../../utils/ajax";

//通訊模組異動紀錄(POST):(含EXCEL)
export const ajaxGetNBHistory = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `getNBHistory`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//通訊模組異動紀錄(POST):
	//https://www.gtething.tw/battery/getNBHistory
};
