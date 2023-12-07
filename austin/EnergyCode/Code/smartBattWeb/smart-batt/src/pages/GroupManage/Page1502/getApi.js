import { ajax } from '../../../utils/ajax';

//站台管理API(POST):(含EXCEL)
export const ajaxGetGroupManage = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getGroupManage`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    //站台管理API(POST):
    //https://www.gtething.tw/battery/getGroupManage
}

//新增站台(POST):
export const ajaxAddBatteryGroup = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `addBatteryGroup`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    //新增站台(POST):
    //https://www.gtething.tw/battery/addBatteryGroup
}

//修改站台(POST):
export const ajaxUpdBatteryGroup = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `updBatteryGroup`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    //修改站台(POST):
    //https://www.gtething.tw/battery/updBatteryGroup
}

//刪除站台(POST):
export const ajaxDelBatteryGroup = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `delBatteryGroup`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    //刪除站台(POST):
    //https://www.gtething.tw/battery/delBatteryGroup
}
//儲存篩選API(POST):
export const ajaxSaveFilter = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `saveFilter`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    //儲存篩選API(POST):
    //https://www.gtething.tw/battery/saveFilter
}