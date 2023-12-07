import { ajax } from '../../../utils/ajax';

//使用者資訊API(GET):
export const ajaxGetUserInfo = ({query}) => {
    const { token, curLanguage, timeZone, company, account } = query;
    const url = `getUserInfo?account=${account}&type=${''}`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //使用者資訊API(GET):
    //https://www.gtething.tw/battery/getUserInfo?account=&type=
}
//使用者資訊:取EXCEL API(Get):
export const ajaxGetUserInfoCheckExcel = ({query}) => {
    const { token, curLanguage, timeZone, company, account } = query;
    const url = `getUserInfo?account=${account}&type=${"check"}`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //使用者資訊:取EXCEL API(Get):
    //https://www.gtething.tw/battery/getUserInfo?account=&type=
}
//新增使用者API(POST):
export const ajaxAddUser = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `addUser`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    //新增使用者API(POST):
    //https://www.gtething.tw/battery/addUser
}
//編輯使用者API(POST):
export const ajaxUpdUser = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `updUser`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    //編輯使用者API(POST):
    //https://www.gtething.tw/battery/updUser
}
//刪除使用者API(POST):
export const ajaxDelUser = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `delUser`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    //刪除使用者API(POST):
    //https://www.gtething.tw/battery/delUser
}
//重置密碼API(POST):
export const ajaxResetPassword = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `resetPassword`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    //重置密碼API(POST):
    //https://www.gtething.tw/battery/resetPassword
}