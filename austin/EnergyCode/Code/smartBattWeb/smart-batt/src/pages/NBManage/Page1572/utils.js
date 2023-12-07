export const returnPostData = (state, type) => {
    const {
        isFilterCompanyData,isFilterCountryData,isFilterAreaData,isFilterGroupIdData,isFilterPreviousNBIDData, isFilterStartTimeData,} = state;
    return {
        "Company": {
            "All": isFilterCompanyData.isChecked ? "1" : "0",
            "List": [...isFilterCompanyData.isDataList]
        },
        "Country": {
            "All": isFilterCountryData.isChecked ? "1" : "0",
            "List": [...isFilterCountryData.isDataList]
        },
        "Area": {
            "All": isFilterAreaData.isChecked ? "1" : "0",
            "List": [...isFilterAreaData.isDataList]
        },
        "GroupID": {
            "All": isFilterGroupIdData.isChecked ? "1" : "0",
            "List": [...isFilterGroupIdData.isDataList]
        },
        "NBID": {
            "All": isFilterPreviousNBIDData.isChecked ? "1" : "0",
            "List": [...isFilterPreviousNBIDData.isDataList]
        },
        "PreviousTime": { ...isFilterStartTimeData },
        "Type": type

    }
}