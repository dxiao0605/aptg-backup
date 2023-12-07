import { ajax } from '../../../utils/ajax';

//公司清單及內阻呈現下拉選單API(GET):
export const ajaxGetIMPTypeCompany = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getIMPTypeCompany`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //內阻呈現下拉選單API(GET):
    //https://www.gtething.tw/battery/getIMPTypeCompany
}

//查詢公司Logo API(GET):
export const ajaxGetCompanyLogo = ({query,companyCode}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getCompanyLogo?companyCode=${companyCode}`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //查詢公司Logo API(GET):
    //https://www.gtething.tw/battery/getCompanyLogo?companyCode=
}
//公司Logo API(GET):
export const ajaxGetLogoImages = ({query,companyCode}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getLogoImages?companyCode=${companyCode}`;
    return fetch(url, {
        method: 'GET',
        headers: new Headers({
            'Accept': '*/*',
            'Content-Type': 'application/json',
            'token': token,
            'language': curLanguage,
            'timezone': timeZone,
            'company': company,
        }),
    })
    //公司Logo API(GET):
    //https://www.gtething.tw/battery/getLogoImages?companyCode=
}
//取消Logo設定API(POST):
export const ajaxDelLogo = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `delLogo`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    //取消Logo設定API(POST):
    //https://www.gtething.tw/battery/delLogo
}