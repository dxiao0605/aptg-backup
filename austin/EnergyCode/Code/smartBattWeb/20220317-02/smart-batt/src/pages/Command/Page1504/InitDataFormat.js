export const FilterNames = {//Filter
    CommandI18nKey: "1566",//指令 * 1566
    CompanyI18nKey: "1064",//公司 * 1064
    AreaI18nKey: "1011",//國家/地域 * 1011
    GroupI18nKey: "1125",//站台名稱/號碼 * 1125
    BatteryGroupIdI18nKey: "1026",//電池組ID * 1026
}

export const initAreaList = [];
export const initGroupList = [];
export const initSubmitKeyList = [];


export const initLastBB = {
    IRTestTime: 20,//內組測試時間
    BatteryCapacity: 100,//電池容量
    CorrectionValue: 55,//補正值
    Resistance: 1.5,//放電電阻值
    LastSettingTime: "",//最後設定時間
}
export const initLastBA = {
    UploadCycle: 300,//通訊上傳週期時間(300~60000)
    IRCycle: 60,//內阻測試週期
    CommunicationCycle: 15,//子板通信週期
    LastSettingTime: "",//最後設定時間
}
export const initLastB5 = {
    Vol: [],//CHx電壓
    LastSettingTime: "",//最後設定時間
}
export const initLastB3 = {
    IR: [],//CHx內組
    LastSettingTime: "",//最後設定時間
}