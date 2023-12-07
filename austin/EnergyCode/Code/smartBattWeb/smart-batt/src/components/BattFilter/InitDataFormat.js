import moment from "moment";
const yesterday = new Date(new Date().getTime() - (24 * 60 * 60 * 1000));  // yesterday
const yesterdayH = yesterday.getHours().toString();
export const format = "YYYY-MM-DD";
export const FilterNames = {//篩選欄位命名
    isFilterCompanyData: "isFilterCompanyData",//公司 * 1064
    isFilterCountryData: "isFilterCountryData",//國家 * 1028
    isFilterAreaData: "isFilterAreaData",//地域 * 1029
    isFilterGroupIdData: "isFilterGroupIdData",//站台號碼 * 1012
    isFilterBatteryGroupIdData:'isFilterBatteryGroupIdData',//電池組ID * 1026
    isFilterBatteryStatusData: 'isFilterBatteryStatusData',//電池狀態 * 1021
    isFilterRecordTimeData:'isFilterRecordTimeData',//數據時間 * 1036
}

export const FilterNames_API = {//篩選欄位命名(api)
    isFilterCompanyVirtue: "CompanyValue",//公司
    isFilterCountryVirtue: "CountryValue",//國家
    isFilterAreaVirtue: "AreaValue",//地域
    isFilterGroupIdVirtue: "GroupValue",//站台編號
    isFilterBatteryIdVirtue: 'BatteryValue',//電池組ID
}

export const initFilterSelectData = { //選單格式化
    isOpen: false,
    isChecked: true,
    isDataList: [],
    isButtonList: [],
}

export const initFilterDate = {//選單格式化
    Radio: "0",
    Start: moment(yesterday).format(format),
    StartHH: yesterdayH,
    StartMM: '00',
    End: moment(new Date()).format(format),
    EndHH: yesterdayH,
    EndMM: '00',
    isButtonList: [{ Value: "0", Label: `1098`, LabelShow: true, ButtonShow: false }]
}