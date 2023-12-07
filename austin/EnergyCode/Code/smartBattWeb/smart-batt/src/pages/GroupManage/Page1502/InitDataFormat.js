export const initOpenData = { //彈跳視窗資料格式化
    Company: '',//公司別 * 1064
    Country: '',//國家 * 1028
    Area: '',//地域 * 1029
    GroupName: '',//站台名稱 * 1013
    GroupID: '',//站台編號 * 1012 
    Address: '',//地址
    File: {},//匯入
    FileName:''//匯入檔案路徑
}

export const FilterNames = {//篩選欄位命名(ui)
    isFilterCompanyData: "isFilterCompanyData",//公司
    isFilterCountryData: "isFilterCountryData",//國家
    isFilterAreaData: "isFilterAreaData",//地域
    isFilterGroupIdData: "isFilterGroupIdData",//站台編號
}

export const FilterNames_API = {//篩選欄位命名(api)
    isFilterCompanyVirtue: "CompanyValue",//公司
    isFilterCountryVirtue: "CountryValue",//國家
    isFilterAreaVirtue: "AreaValue",//地域
    isFilterGroupIdVirtue: "GroupValue",//站台編號    
}

export const CompareWindowns = {//彈跳視窗    
    Edit: { model: "1050", title: '1535' },//編輯
    Add: { model: "1051", title: '1541' },//新增
    Del: { model: "1052", title: '1542' },//刪除
    Export: { model: "1053", title: '' },//匯出
    Import: { model: "1054", title: '1545' },//匯入   
}
export const inputList = {//彈跳視窗-所有input的名稱與數據對應使用    
    GroupName: 'GroupName',//站台名稱
    GroupID: 'GroupID',//站台編號
    Country: 'Country',//國家
    Area: 'Area',//地域
    Address: 'Address',//地址
};

export const initFilterSelectData = { //選單格式化
    isOpen: false,
    isChecked: true,
    isDataList: [],
    isButtonList: [],
}