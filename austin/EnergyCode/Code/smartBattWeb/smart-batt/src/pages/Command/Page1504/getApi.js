import { ajax } from "../../../utils/ajax";

//公司下拉選單:
export const ajaxGetCompanyList = ({query}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `getCompanyList`;
	return ajax(url, "GET", token, curLanguage, timeZone, company);
	//公司下拉選單:
	//https://www.gtething.tw/battery/getCompanyList
};
//國家/地域/站台下拉選單:
export const ajaxGetCommandList = ({ query, companyCode, radioVal }) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `getCommandList?companyCode=${companyCode}&commandId=${radioVal}`;
	return ajax(url, "GET", token, curLanguage, timeZone, company);
	//國家/地域/站台下拉選單:
	//https://www.gtething.tw/battery/getCommandList?companyCode=1
};
//電池組ID 下拉選單(GET):
export const ajaxGetCommandBattIdList = ({ query,groupInternalId, commandId }) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `getCommandBattIdList?groupInternalId=${groupInternalId}&commandId=${commandId}`;
	return ajax(url, "GET", token, curLanguage, timeZone, company);
	//電池組ID 下拉選單(GET):
	//https://www.gtething.tw/battery/getCommandBattIdList?groupInternalId=5,6&commandId=BB
};
//參數設定
//參數設定 查詢內阻設定BB:
export const ajaxGetIRSetting = ({ query,batteryGroupId }) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `getIRSetting?batteryGroupId=${batteryGroupId}`;
	return ajax(url, "GET", token, curLanguage, timeZone, company);
	//參數設定 查詢內阻設定BB:
	//https://www.gtething.tw/battery/getIRSetting?batteryGroupId=ZZ20B00039_0
};
//參數設定 查詢時間週期設定BA:
export const ajaxGetPeriodSetting = ({ query,batteryGroupId }) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `getPeriodSetting?batteryGroupId=${batteryGroupId}`;
	return ajax(url, "GET", token, curLanguage, timeZone, company);
	//參數設定 查詢時間週期設定BA:
	//https://www.gtething.tw/battery/getPeriodSetting?batteryGroupId=ZZ20B00039_0
};

//參數設定 查詢校正電壓B5:
export const ajaxGetCorrectionVol = ({ query,batteryGroupId }) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `getCorrectionVol?batteryGroupId=${batteryGroupId}`;
	return ajax(url, "GET", token, curLanguage, timeZone, company);
	//參數設定 查詢校正電壓B5:
	//https://www.gtething.tw/battery/getCorrectionVol?batteryGroupId=ZZ20B00040_0
};
//參數設定 查詢校正內阻B3:
export const ajaxGetCorrectionIR = ({ query,batteryGroupId }) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `getCorrectionIR?batteryGroupId=${batteryGroupId}`;
	return ajax(url, "GET", token, curLanguage, timeZone, company);
	//參數設定 查詢校正內阻B3:
	//https://www.gtething.tw/battery/getCorrectionIR?batteryGroupId=ZZ20B00040_0
};

//新增內阻設定BB(POST):
export const ajaxAddIRSettingBatch = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `addIRSettingBatch`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//新增內阻設定BB(POST):
	//https://www.gtething.tw/battery/addIRSettingBatch
};
//新增時間週期設定BA(POST):
export const ajaxAddPeriodSettingBatch = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `addPeriodSettingBatch`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//新增時間週期設定BA(POST):
	//https://www.gtething.tw/battery/addPeriodSettingBatch
};
//新增校正電壓B5(POST):
export const ajaxAddCorrectionVolTask = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `addCorrectionVolTask`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//新增校正電壓B5(POST):
	//https://www.gtething.tw/battery/addCorrectionVolTask
};
//新增校正內阻B3(POST):
export const ajaxAddCorrectionIRTask = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `addCorrectionIRTask`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//新增校正內阻B3(POST):
	//https://www.gtething.tw/battery/addCorrectionIRTask
};
