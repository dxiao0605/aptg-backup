export const returnPostData = (state, type) => {
    const {
        isFilterEventTypeCodeData, isFilterCompanyData, isFilterCountryData, isFilterAreaData, 
        isFilterGroupIdData, isFilterRecordTimeAlert,
    } = state;
    return {
        "Alert": { //EventTypeCode
            "All": isFilterEventTypeCodeData.isChecked ? "1" : "0",
            "List": [...isFilterEventTypeCodeData.isDataList]
        },
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
        "RecTime": { ...isFilterRecordTimeAlert } ,
        "Type": type

    }
}