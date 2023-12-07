import { ajax } from '../../utils/ajax';

// get api 取得電池歷史
export const fetchBattHistory = ({query,postData}) => {
    const {token,curLanguage,timeZone,company} = query;
    const url = `getBatteryHistory`;
    return ajax(url,'POST',token,curLanguage,timeZone,company,postData)
}
// get api 取得電池歷史圖表資料
export const fetchBattHistoryChart = ({query,postData}) => {
    const {token,curLanguage,timeZone,company } = query;
    // const {isFilterRecordTimeData} = this.props;
    // const postData = {
    //     Type:"",
    //     BattInternalId: battInternalId,
    //     RecTime: isFilterRecordTimeData
    // };
    const url = `getBatteryHistoryChart`;
    return ajax(url,'POST',token,curLanguage,timeZone,company,postData)
}
// type=excelcheck檢查有EXCEL無資料,取得EXCEL檔名;type=csvcheck檢查有CSV無資料,取得CSV檔名
export const fetchCheckBattHistoryEXCEL = ({query,postData}) => {
    const {token,curLanguage,timeZone,company} = query;
    const url = `getBatteryHistory`;
    return ajax(url,'POST',token,curLanguage,timeZone,company,postData)
}
