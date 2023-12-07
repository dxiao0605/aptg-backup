import { ajax } from "../../../utils/ajax";

//公司清單及指令限制API(GET):
export const ajaxGetCommandSetup = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getCommandSetup`;
    return ajax(url, "GET", token, curLanguage, timeZone, company);
    //指令限制API(GET):
    //https://www.gtething.tw/battery/getCommandSetup
};
//修改指令限制API(POST):
export const ajaxUpdCommandSetup = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `updCommandSetup`;
    return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
    //修改指令限制API(POST):
    //https://www.gtething.tw/battery/updCommandSetup
};