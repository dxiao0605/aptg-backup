import { ajax } from '../../../utils/ajax';

//取選擇公司清單(GET):
export const ajaxGetCompanyList = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getCompanyList?admin=`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //取選擇公司清單(GET):
    //https://www.gtething.tw/battery/getCompanyList?admin=
}
//通訊序號列表(GET):
export const ajaxGetNBList = ({query,data}) => {
    const { token, curLanguage, timeZone, company } = query;
    const { companyCode, allocate, active, start, end } = data;
    const url = `getNBList?companyCode=${companyCode}&allocate=${allocate}&active=${active}&start=${start}&end=${end}`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //通訊序號列表(GET):
    //https://www.gtething.tw/battery/getNBList?companyCode=&allocate=&active=&start=&end=
}
//修改通訊序號狀態/刪除通訊序號(POST):
export const ajaxUpdNBListActive = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `updNBListActive`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    // 4. 修改通訊序號狀態/刪除通訊序號(POST):
    // https://www.gtething.tw/battery/updNBListActive
}