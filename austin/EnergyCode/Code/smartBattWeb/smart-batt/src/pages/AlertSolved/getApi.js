import { ajax } from '../../utils/ajax';


// get api 取得已解決告警(POST)
export const getSolvedData = ({query,postData}) => {
    const {token,company,curLanguage,timeZone} = query;
    const url = `getAlert?eventStatus=6`;   //已解決
    return ajax(url,'POST',token,curLanguage,timeZone,company,postData)
}
// type=check檢查有EXCEL無資料
export const fetchCheckSolvedDataEXCEL = ({query,postData}) => {
    const {token,curLanguage,timeZone,company} = query;
    const url = `getAlert?eventStatus=6`;
    return ajax(url,'POST',token,curLanguage,timeZone,company,postData)
}