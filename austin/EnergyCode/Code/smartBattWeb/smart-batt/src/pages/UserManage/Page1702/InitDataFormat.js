//新增
export const initOpenData = { //彈跳視窗資料格式化
    RoleId: "",
    RoleName: "",
    CopyRoleId: [""],
    RoleDesc: "",
    RoleDescE: "",
    RoleDescJ: "",
}

export const inputList = {
    RoleId: "RoleId",
    RoleName: 'RoleName',//角色名稱
    CopyRoleId: 'CopyRoleId',//複製權限
    RoleDesc: 'RoleDesc',//說明(中文)
    RoleDescE: 'RoleDescE',//說明(英文)
    RoleDescJ: 'RoleDescJ',//說明(日文)
};

const Input_Add = {
    RoleId: { i18nKey: '', disabled: false, isShow: false, maxLength: 0 },
    RoleName: { i18nKey: '1709', disabled: false, isShow: true, maxLength: 20 },
    CopyRoleId: { i18nKey: '1708', disabled: false, isShow: true },
    RoleDesc: { i18nKey: '1704', disabled: false, isShow: true, maxLength: 30 },
    RoleDescE: { i18nKey: '1705', disabled: false, isShow: true, maxLength: 30 },
    RoleDescJ: { i18nKey: '1706', disabled: false, isShow: true, maxLength: 30 },
}

//------------------------------------------------------------------------------

//權限

export const inputAuthorityList = {
    P1200: "P1200",//總攬
    P1300: "P1300",//告警
    P1400: "P1400",//電池數據
    P1501: "P1501",//電池組管理
    P1502: "P1502",//站台管理
    P1503: "P1503",//通訊序號
    P1504: "P1504",//電池參數設定
    P1600: "P1600",//電池歷史
    P1700: "P1700",//使用者管理
    P1800: "P1800",//系統設定
};


export const initOpenAuthorityData = { //彈跳視窗資料格式化
    P1200: { View: 0 },
    P1300: { View: 0, Edit: 0 },
    P1400: { View: 0, Edit: 0, Settings: 0 },
    P1501: { View: 0, Edit: 0 },
    P1502: { View: 0, Edit: 0 },
    P1503: { View: 0, Edit: 0 },
    P1504: { View: 0, Edit: 0 },
    P1600: { View: 0 },
    P1700: { View: 0, Edit: 0 },
    P1800: { View: 0, IMPType: 0, Company: 0 , Command: 0},
    RoleDesc: "",
    RoleName: "",
}

const Input_Authority = {
    RoleId: '',
    P1200: { View: '1714' },
    P1300: { View: '1714', Edit: '1717' },
    P1400: { View: '1714', Edit: '1718', Settings: '1719' },
    P1501: { View: '1714', Edit: '1715' },
    P1502: { View: '1714', Edit: '1715' },
    P1503: { View: '1714', Edit: '1715' },
    P1504: { View: '1722', Edit: '1716' },
    P1600: { View: '1714' },
    P1700: { View: '1714', Edit: '1720' },
    P1800: { View: '1714', IMPType: '1721', Company: '1802' ,Command: '1815'},
}

export const CompareWindowns = {//彈跳視窗        
    Add: { model: "1051", title: '1707', Inpt: { ...Input_Add } },//新增    
    Export: { model: "1053", title: '' },//匯出
    Authority: { model: "1711", title: '1713', Inpt: { ...Input_Authority } },//權限
    ForwardUser: { model: "1712", title: '' },//查看使用者   
    Del: { model: "1052", title: '1735' },//刪除
}





