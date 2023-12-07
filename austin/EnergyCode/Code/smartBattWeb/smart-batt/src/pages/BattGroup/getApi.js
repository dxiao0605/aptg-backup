import { ajax } from '../../utils/ajax';

// get api 取得多個電池群組(POST)
export const fetchBattGroupData = ({query,postData}) => {
    const {token, company, curLanguage, timeZone} = query;
    const url = `getBatteryGroup`;
    return ajax(url, "POST", token, curLanguage, timeZone, company, postData)
}
// api 變更BA設定清單(POST)
export const fetchBASetting = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `addPeriodSettingBatch`;
    return ajax(url, "POST", token, curLanguage, timeZone, company,postData);
};
// api 變更BB設定清單(POST)
export const fetchBBSetting = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `addIRSettingBatch`;
    return ajax(url, "POST", token, curLanguage, timeZone, company, postData);      
};
// type=check檢查有EXCEL無資料
export const fetchCheckBattGroupDataEXCEL = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getBatteryGroup`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company ,postData)
};