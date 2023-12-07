import { ajax } from '../../utils/ajax';

// 告警條件API(GET):
export const fetchAlertSetup = ({query}) => {
    const {token,language,timeZone,company} = query;
    const url = `getAlertSetup`;
    return ajax(url,'GET',token,language,timeZone,company)
    // https://www.gtething.tw/battery/getAlertSetup?companyCode=aptg
    /**
     * CompanyCode	公司代碼
     * IMPType	0: 內阻值 1:電導值 2:毫內阻
     * Alert1	警告值1
     * Alert2	警告值2
     * Disconnect	判斷斷線時間(小時)
     */
}

// 修改告警條件API(POST):
export const fetchUpdAlertSetup = ({query,postData}) => {
    const {token,language,timeZone,company} = query;
    const url = `updAlertSetup`;
    return ajax(url,'POST',token,language,timeZone,company,postData)
    // https://www.gtething.tw/battery/getAlertSetup?companyCode=aptg
    /**
     * CompanyCode	公司代碼
     * IMPType	0: 內阻值 1:電導值 2:毫內阻
     * Alert1	警告值1
     * Alert2	警告值2
     * Temperature1 溫度告警值
     * Disconnect	判斷斷線時間(小時)
     */
}