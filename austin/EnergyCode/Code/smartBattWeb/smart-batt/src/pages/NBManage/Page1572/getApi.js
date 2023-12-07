import { ajax } from '../../../utils/ajax';

//接續序號歷史API(POST):(含EXCEL)
export const ajaxGetNBGroupHis = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getNBGroupHis`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    //接續序號歷史API(POST):
    //https://www.gtething.tw/battery/getNBGroupHis
}