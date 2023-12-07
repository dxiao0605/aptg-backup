export const initOpenData = { //彈跳視窗資料格式化
    BatteryTypeCode: '',//電池型號 
    BatteryTypeName: '',//電池型號中文 1506
    Company: '',//公司別 1064
}

export const FilterNames = {//篩選欄位命名
    isFilterCompanyData: "isFilterCompanyData",//公司別
    isFilterBatteryTypeNameData: "isFilterBatteryTypeNameData",//電池型號中文
}

export const CompareWindowns = {//彈跳視窗    
    Edit: { model: "1050", title: '1510' },//編輯
    Add: { model: "1051", title: '1509' },//新增
    Del: { model: "1052", title: '1511' },//刪除
    Export: { model: "1053", title: '' },//匯出
    Import: { model: "1054", title: '' },//匯入   
}
export const inputList = {//彈跳視窗-所有input的名稱與數據對應使用
    BatteryTypeName: 'BatteryTypeName',
};

export const initFilterSelectData = { //選單格式化
    isOpen: false,
    isChecked: true,
    isDataList: [],
    isButtonList: [],
}