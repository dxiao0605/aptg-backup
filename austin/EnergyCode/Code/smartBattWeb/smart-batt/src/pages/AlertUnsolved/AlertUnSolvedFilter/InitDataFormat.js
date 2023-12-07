import moment from "moment";
export const format = "YYYY-MM-DD";
export const FilterNames = {//篩選欄位命名
    isFilterEventTypeCodeDataUnSolved: "isFilterEventTypeCodeDataUnSolved",//告警類型 * 1315
    isFilterCompanyDataUnSolved: "isFilterCompanyDataUnSolved",//公司 * 1064
    isFilterCountryDataUnSolved: "isFilterCountryDataUnSolved",//國家 * 1028
    isFilterAreaDataUnSolved: "isFilterAreaDataUnSolved",//地域 * 1029
    isFilterGroupIdDataUnSolved: "isFilterGroupIdDataUnSolved",//站台號碼 * 1012
    isFilterRecordTimeUnSolved: "isFilterRecordTimeUnSolved",//數據時間
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
    Radio: "4",
    Start: moment(new Date().setMonth(new Date().getMonth() - 1)).format(format),
    StartHH: '00',
    StartMM: '00',
    End: moment(new Date()).format(format),
    EndHH: '00',
    EndMM: '00',
    isButtonList: [{ Value: "4", Label: `1110`, LabelShow: true, ButtonShow: false }]
}
