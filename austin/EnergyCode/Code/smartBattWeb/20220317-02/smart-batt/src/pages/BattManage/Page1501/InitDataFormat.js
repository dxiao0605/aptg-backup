import moment from "moment";
export const format = "YYYY-MM-DD";
export const initOpenData = { //彈跳視窗資料格式化
    InstallDate: moment(new Date()).format(format),
    BatteryGroupID: '',
    BatteryTypeName: '',
    BatteryTypeCode: '',
    Company: ''
}


export const FilterNames = {//篩選欄位命名
    isFilterCompanyData: "isFilterCompanyData",
    isFilterBatteryGroupIdData: "isFilterBatteryGroupIdData",
    isFilterInstallDateData: "isFilterInstallDateData",
}

export const CompareWindowns = {//彈跳視窗    
    Edit: { model: "1050", title: '1551' },//編輯
    Add: { model: "1051", title: '' },//新增
    Del: { model: "1052", title: '' },//刪除
    Export: { model: "1053", title: '' },//匯出
    Import: { model: "1054", title: '' },//匯入   
}

export const inputList = {
    BatteryGroupID: 'BatteryGroupID',
    BatteryType: 'BatteryType',
};


export const initFilter = {//選單格式化
    isOpen: false,
    isChecked: true,
    isDataList: [],
    isButtonList: [],
}

export const initFilterDate = {//選單格式化
    Radio: "0",
    Start: moment(new Date(new Date().getTime() - (7 * 24 * 60 * 60 * 1000))).format(format),
    End: moment(new Date()).format(format),
    isButtonList: [{ Value: "0", Label: `1073`, LabelShow: false, ButtonShow: false }]
}