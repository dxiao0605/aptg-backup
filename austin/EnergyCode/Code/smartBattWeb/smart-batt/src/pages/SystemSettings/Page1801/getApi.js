import { ajax } from '../../../utils/ajax';

//公司清單及內阻呈現下拉選單API(GET):
export const ajaxGetIMPTypeCompany = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getIMPTypeCompany`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //內阻呈現下拉選單API(GET):
    //https://www.gtething.tw/battery/getIMPTypeCompany
}
//修改內阻呈現方式API(POST):
export const ajaxUpdIMPType = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `updIMPType`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    //修改內阻呈現方式API(POST):
    //https://www.gtething.tw/battery/updIMPType
}