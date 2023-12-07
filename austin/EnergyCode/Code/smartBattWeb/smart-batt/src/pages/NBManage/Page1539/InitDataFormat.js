import moment from "moment";
export const format = "YYYY-MM-DD";
export const FilterNames = {//篩選欄位命名
    isFilterCompanyData: "isFilterCompanyData",//公司 * 1064
    isFilterNBIDData: "isFilterNBIDData",//通訊序號 * 1057
    isFilterModifyItemData: "isFilterModifyItemData",//異動項目 * 1059
    isFilterModifyTimeData: 'isFilterModifyTimeData',//異動時間 * 1060
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