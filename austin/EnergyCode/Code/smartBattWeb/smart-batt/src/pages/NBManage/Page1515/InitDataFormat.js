export const NBSelectNames = {//Filter
    AllocateI18nKey: "Allocate",//分配/未分配 * 1519
    CompanyCodeI18nKey: "CompanyCode",//已分配-選擇公司 * 1074
    ActiveI18nKey: "Active",//啟用/停用 *1529
    StartI18nKey: "Start",//起
    EndI18nKey: "End",//迄
    Company: "Company",//分配用的公司別
    Remark: "Remark",//異動說明
}

export const initOpenData = { //彈跳視窗資料
    Company: '',
    NBList: [],
    Remark: ''
}

export const GetModel = {
    SentEnabled: "SentEnabled",
    SentDisable: "SentDisable",

}

export const ModelTable = {
    i18n13: {
        SentEnabled: { Model: GetModel.SentEnabled, title: '1530', },
    },
    i18n14: {
        SentDisable: { Model: GetModel.Sent, title: '1532', },
    },
}

export const i18n1518SelectList = {//分配/未分配
    i18n13: { Value: "13", Label: "13", SubmitVal: "10", showDelBtn: true, SentModel: "14" },//啟用
    i18n14: { Value: "14", Label: "14", SubmitVal: "9", showDelBtn: false, SentModel: "13" },//停用
    // i18n13: { Value: "13", Label: "13", SubmitVal: "14", showDelBtn: true, SentModel: "14" },//啟用
    // i18n14: { Value: "14", Label: "14", SubmitVal: "13", showDelBtn: false, SentModel: "13" },//停用    
}

