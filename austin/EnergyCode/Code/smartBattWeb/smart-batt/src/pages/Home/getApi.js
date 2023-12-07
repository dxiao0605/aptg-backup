import { ajax } from '../../utils/ajax';

// get api 地圖群組資訊(pie)
export const fetchMapInfo = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getMapInfo`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
}
// get api 電池狀態
export const fetchBattStateChartData = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getStatusNow`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
}
// get api 電池狀態變化(line)
export const fetchBattStateLineChartData = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getStatus`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
}
// type=check檢查有EXCEL無資料
export const fetchCheckStatusNowDataExcel = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getStatusNow`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
}
// type=check檢查有EXCEL無資料
export const fetchCheckStatusDataEXCEL = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getStatus`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
}
