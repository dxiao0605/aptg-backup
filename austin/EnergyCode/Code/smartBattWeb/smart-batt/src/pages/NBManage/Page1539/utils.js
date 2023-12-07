// 回傳篩選條件
export const returnPostData = (state, type) => {
    const {isFilterCompanyData,isFilterNBIDData, isFilterModifyItemData, isFilterModifyTimeData,} = state;
    return {
        "Company": {
            "All": isFilterCompanyData.isChecked ? "1" : "0",
            "List": [...isFilterCompanyData.isDataList]
        },
        "NBID": {
            "All": isFilterNBIDData.isChecked ? "1" : "0",
            "List": [...isFilterNBIDData.isDataList]
        },
        "ModifyItem": {
            "All": isFilterModifyItemData.isChecked ? "1" : "0",
            "List": [...isFilterModifyItemData.isDataList]
        },
        "ModifyTime": { ...isFilterModifyTimeData },
        "Type": type

    }
}
