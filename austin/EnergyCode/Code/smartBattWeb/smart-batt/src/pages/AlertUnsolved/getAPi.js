import { ajax } from '../../utils/ajax';


// get api 取得未解決告警
export const getUnsolvedData = ({query,postData}) => {
    const {token,company,curLanguage,timeZone} = query;
    const url = `getAlert?eventStatus=5`;   //未解決
    return ajax(url,'POST',token,curLanguage,timeZone,company,postData)
}
// 關閉多個告警API(POST) 暫無
export const fetchCloseAlert = ({query,list,changeBtnStatus}) => {
    const {token,company,curLanguage,timeZone} = query;
    const url = `closeAlert`;
    changeBtnStatus();
    return ajax(url,'POST',token,curLanguage,timeZone,company,list)
    // https://www.gtething.tw/battery/battery/closeAlert
}
 // type=check檢查有EXCEL無資料
export const fetchCheckunsolvedDataEXCEL = ({query,postData}) => {
    const { token,curLanguage,timeZone,company} =query;
    const url = `getAlert?eventStatus=5`;
    return ajax(url,'POST',token,curLanguage,timeZone,company,postData)
}