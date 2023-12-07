import { ajax } from '../../utils/ajax';

// get api 取得多個電池群組(POST)
export const fetchBattHistoryData = ({query,postData}) => {
    const {token, company, curLanguage, timeZone} = query;
    const url = `getBatteryHistoryCheck`;
    return ajax(url, "POST", token, curLanguage, timeZone, company, postData)
}