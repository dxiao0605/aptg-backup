export const initOpenData = { //彈跳視窗資料格式化
    Account: "",
    Company: { Label: "", Value: "" },
    Name: "",
    Password: "",
    PasswordTWO: "",
    RoleId: [""],
    Email: "",
    Language: [""],
    TimeZone: [""],
    Mobile: "",
    CreateTime: "",
    UserName: "",
    EditUser: true,
}

export const inputList = {
    Name: 'Name',//顯示名稱
    Company: 'Company',//公司
    Mobile: 'Mobile',//行動電話
    Email: 'Email',//電子郵件
    Account: 'Account',//帳號
    RoleId: 'RoleId',//角色
    Password: 'Password',//密碼
    PasswordTWO: 'PasswordTWO',//重新輸入密碼
    Language: 'Language',//語系
    TimeZone: 'TimeZone',//時區
    CreateTime: 'CreateTime',//建立日期
};

const Input_Edit = {
    Account: { i18nKey: '1727', disabled: true, isShow: true, maxLength: 10 },
    Company: { i18nKey: '1064', disabled: true, isShow: true, maxLength: 10 },
    Name: { i18nKey: '1724', disabled: false, isShow: true, maxLength: 10 },
    Password: { i18nKey: '1103', disabled: true, isShow: false, minLength: 8, maxLength: 20 },
    PasswordTWO: { i18nKey: '1733', disabled: true, isShow: false, minLength: 8, maxLength: 20 },
    RoleId: { i18nKey: '1702', disabled: false, isShow: true, },
    Email: { i18nKey: '1731', disabled: false, isShow: true, maxLength: 30 },
    Language: { i18nKey: '1118', disabled: false, isShow: true, },
    TimeZone: { i18nKey: '1119', disabled: false, isShow: true, },
    Mobile: { i18nKey: '1730', disabled: false, isShow: true, maxLength: 20 },
    CreateTime: { i18nKey: '1732', disabled: true, isShow: true, },
}

const Input_Add = {
    Account: { i18nKey: '1727', disabled: false, isShow: true, maxLength: 10 },
    Company: { i18nKey: '1064', disabled: false, isShow: true, maxLength: 10 },
    Name: { i18nKey: '1724', disabled: false, isShow: true, maxLength: 10 },
    Password: { i18nKey: '1103', disabled: false, isShow: true, minLength: 8, maxLength: 20 },
    PasswordTWO: { i18nKey: '1733', disabled: false, isShow: true, minLength: 8, maxLength: 20 },
    RoleId: { i18nKey: '1702', disabled: false, isShow: true, },
    Email: { i18nKey: '1731', disabled: false, isShow: true, maxLength: 30 },
    Language: { i18nKey: '1118', disabled: false, isShow: true, },
    TimeZone: { i18nKey: '1119', disabled: false, isShow: true, },
    Mobile: { i18nKey: '1730', disabled: false, isShow: true, maxLength: 20 },
    CreateTime: { i18nKey: '1732', disabled: true, isShow: false, },
}

export const CompareWindowns = {//彈跳視窗    
    Edit: { model: "1050", title: '1734', Inpt: { ...Input_Edit } },//編輯
    Add: { model: "1051", title: '1729', Inpt: { ...Input_Add } },//新增
    Del: { model: "1052", title: '1735' },//刪除
    Export: { model: "1053", title: '' },//匯出
    RestPwd: { model: "1106", title: '1106' },//重設密碼   
}





