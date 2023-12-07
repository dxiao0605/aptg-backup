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
    NotSent: "NotSent",
    Sent: "Sent",
    Del: "Del",
}

export const ModelTable = {
    i18n17: {
        NotSent: { Model: GetModel.NotSent, title: '1516', },
        Del: { Model: GetModel.Del, title: '1526', },
    },
    i18n16: {
        Sent: { Model: GetModel.Sent, title: '1524', },
    },
}

export const i18n1519SelectList = {//分配/未分配
    // i18n17: { Value: "17", Label: "17", SubmitVal: "1520", showDelBtn: true, SentModel: "16" },//未分配
    // i18n16: { Value: "16", Label: "16", SubmitVal: "17", showDelBtn: false, SentModel: "17" },//分配
    i18n17: { Value: "17", Label: "17", SubmitVal: "1520", showDelBtn: true, SentModel: "16" },//未分配 SubmitVal:藍色箭頭文字
    i18n16: { Value: "16", Label: "16", SubmitVal: "11", showDelBtn: false, SentModel: "17" },//分配 SubmitVal:藍色箭頭文字
}


// const dT = new ClipboardEvent('').clipboardData || // Firefox < 62 workaround exploiting https://bugzilla.mozilla.org/show_bug.cgi?id=1422655
//     new DataTransfer(); // specs compliant (as of March 2018 only Chrome)
// dT.items.add(new File([''], ''));
export const initImportData = {
    // File: dT?.files === undefined ? '' : dT.files,
    File: null,
    FileValue: '',
    FileName: '',
}

