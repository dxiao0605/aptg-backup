// PostData
export const returnPostData = (state, type) => {
    const {isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,} = state;
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
        "Type": type
    }
}