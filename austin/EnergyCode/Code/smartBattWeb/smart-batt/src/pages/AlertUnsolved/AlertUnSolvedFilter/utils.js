export const returnPostData = (state, type,battInternalId) => {
    const {
        isFilterEventTypeCodeDataUnSolved, isFilterCompanyDataUnSolved, isFilterCountryDataUnSolved, isFilterAreaDataUnSolved, 
        isFilterGroupIdDataUnSolved, isFilterRecordTimeUnSolved,
    } = state;
    return {
        "Alert": {//EventTypeCode
            "All": isFilterEventTypeCodeDataUnSolved.isChecked ? "1" : "0",
            "List": [...isFilterEventTypeCodeDataUnSolved.isDataList]
        },
        "Company": {
            "All": isFilterCompanyDataUnSolved.isChecked ? "1" : "0",
            "List": [...isFilterCompanyDataUnSolved.isDataList]
        },
        "Country": {
            "All": isFilterCountryDataUnSolved.isChecked ? "1" : "0",
            "List": [...isFilterCountryDataUnSolved.isDataList]
        },
        "Area": {
            "All": isFilterAreaDataUnSolved.isChecked ? "1" : "0",
            "List": [...isFilterAreaDataUnSolved.isDataList]
        },
        "GroupID": {
            "All": isFilterGroupIdDataUnSolved.isChecked ? "1" : "0",
            "List": [...isFilterGroupIdDataUnSolved.isDataList]
        },
        "BattInternalId": battInternalId,
        "RecTime": { ...isFilterRecordTimeUnSolved },
        "Type": type

    }
}