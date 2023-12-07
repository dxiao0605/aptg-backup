import { ajax } from '../../utils/ajax';

// get api 取得電池組ID(POST)
export const getBattData = ({query, postData}) => {
    const {token, company, language, timeZone} = query;
    const url = `getBattery`;
    // const postData = { Type: "" };
    return ajax(url, "POST", token, language, timeZone, company, postData)
    /**
     * Seq	序
     * Company	公司
     * Country	國家
     * Area	地域
     * GroupID	站台編號
     * GroupName	站台名稱
     * Address	地址
     * IMPType	0: 內阻值 1:電導值 2:毫內阻
     * BatteryGroupID	電池組ID
     * InstallDate	安裝日期
     * BatteryType	型號
     * RecTime	數據更新時間
     * IR	內阻
     * Vol	電壓
     * Temperature	溫度
     * StatusCode	狀態代碼
     * StatusDesc	狀態說明
     */
}

// api 變更BA設定清單(POST)
export const fetchBASetting = ({query,postData}) => {
    const { token, language, timeZone, company } = query;
    const url = `addPeriodSettingBatch`;
    return ajax(url, "POST", token, language, timeZone, company,postData);
};
// api 變更BB設定清單(POST)
export const fetchBBSetting = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `addIRSettingBatch`;
    return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
};
// api 變更B3設定清單(POST)
export const fetchB3Setting = ({query,postData}) => {
    const { token, language, timeZone, company } = query;
    const url = `addCorrectionIRTask`;
    return ajax(url, "POST", token, language, timeZone, company, postData);
};
// api 變更B5設定清單(POST)
export const fetchB5Setting = ({query,postData}) => {
    const { token, language, timeZone, company } = query;
    const url = `addCorrectionVolTask`;
    return ajax(url, "POST", token, language, timeZone, company, postData);
};
// api 變更站台編輯(POST)
export const fetchGroupSetting = ({query,postData}) => {
    const { token, language, timeZone, company } = query;
    const url = `updGroupSetup`;
    return ajax(url, 'POST', token, language, timeZone, company, postData)
};
// api 變更電池組編輯(POST)
export const fetchBatterySetting = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `updBattery`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
};

 // type=check檢查有EXCEL無資料
export const fetchCheckBattDataEXCEL = ({query}) => {
    const {token,language,timeZone,company } = query;
    const url = `getBattery`;
    const postData = { Type: "check" };
    return ajax(url, "POST", token, language, timeZone, company, postData);
};