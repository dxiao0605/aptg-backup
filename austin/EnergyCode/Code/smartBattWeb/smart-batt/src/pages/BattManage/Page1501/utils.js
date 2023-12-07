// 篩選回傳值
export const returnPostData = (state, type) => {
    const { isFilterCompanyData, isFilterBatteryGroupIdData,isFilterInstallDateData } = state;
    return {
        "Company": {
            "All": isFilterCompanyData.isChecked ? "1" : "0",
            "List": [...isFilterCompanyData.isDataList]
        },
        "BatteryGroupId": {
            "All": isFilterBatteryGroupIdData.isChecked ? "1" : "0",
            "List": [...isFilterBatteryGroupIdData.isDataList]
        },
        "InstallDate": { ...isFilterInstallDateData },
        "Type": type
    }
}