import moment from "moment";
export const format = "YYYY-MM-DD";
export const FilterNames = {//篩選欄位命名
    isFilterEventTypeCodeData: "isFilterEventTypeCodeData",//告警類型 * 1315
    isFilterCompanyData: "isFilterCompanyData",//公司 * 1064
    isFilterCountryData: "isFilterCountryData",//國家 * 1028
    isFilterAreaData: "isFilterAreaData",//地域 * 1029
    isFilterGroupIdData: "isFilterGroupIdData",//站台號碼 * 1012
    isFilterRecordTimeAlert: 'isFilterRecordTimeAlert',//數據時間 * 1098
}

export const FilterNames_API = {//篩選欄位命名(api)
    isFilterCompanyVirtue: "CompanyValue",//公司
    isFilterCountryVirtue: "CountryValue",//國家
    isFilterAreaVirtue: "AreaValue",//地域
    isFilterGroupIdVirtue: "GroupValue",//站台編號    
}

export const initFilterSelectData = { //選單格式化
    isOpen: false,
    isChecked: true,
    isDataList: [],
    isButtonList: [],
}

export const initFilterDate = {//選單格式化
    Radio: "3",
    Start: moment(new Date().setMonth(new Date().getMonth() - 1)).format(format),
    StartHH: '00',
    StartMM: '00',
    End: moment(new Date()).format(format),
    EndHH: '00',
    EndMM: '00',
    isButtonList: [{ Value: "3", Label: `1109`, LabelShow: true, ButtonShow: false }]
}