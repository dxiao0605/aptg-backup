export const getFilterCondition = (filterList) => { //載入篩選清單
    const country = [];
    const area = [];
    const groupId = [];
    const groupName = [];
    // 建立清單
    filterList.filterCountry.map( (item,idx) => {
        return country.push({Seq:idx,Label:item.Label,Value:item.Value,Checked:false})
    })
    filterList.filterArea.map( (item,idx) => {
        return area.push({Seq:idx,Label:item.Label,Value:item.Value,Checked:false})
    })
    filterList.filterGroupId.map( (item,idx) => {
        return groupId.push({Seq:idx,Label:item.Label,Value:item.Value,Checked:false})
    })
    filterList.filterGroupName.map( (item,idx) => {
        return groupName.push({Seq:idx,Label:item.Label,Value:item.Value,Checked:false})
    })
    
    return {
        country,
        area,
        groupId,
        groupName,
    }
}

// filter用postData
export const returnPostData = (state, type) => {
    const {
        isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
        isFilterBatteryGroupIdData, isFilterBatteryStatusData,
    } = state;
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
        "BatteryGroupId": {
            "All": isFilterBatteryGroupIdData.isChecked ? "1" : "0",
            "List": [...isFilterBatteryGroupIdData.isDataList]
        },
        "Status": {
            "All": isFilterBatteryStatusData.isChecked ? "1" : "0",
            "List": [...isFilterBatteryStatusData.isDataList]
        },
        "Type": type

    }
}


//excel用postData
export const getExcelPostData = ({props,type}) =>{
    const {
        isFilterCompanyData,isFilterCountryData,isFilterAreaData,isFilterGroupIdData,isFilterBatteryStatusData,
    } = props;
    const postData = {
        Company: {
            All: isFilterCompanyData.isChecked ? '1' :'0',
            List: isFilterCompanyData.isDataList
        },
        Country: {
            All: isFilterCountryData.isChecked ? '1' :'0',
            List: isFilterCountryData.isDataList
        },
        Area: {
            All: isFilterAreaData.isChecked ? '1' :'0',
            List: isFilterAreaData.isDataList,
        },
        GroupID: {
            All: isFilterGroupIdData.isChecked ? '1' :'0',
            List: isFilterGroupIdData.isDataList,
        },
        BatteryStatus: {
            All: isFilterBatteryStatusData.isChecked ? '1' : '0',
            List: isFilterBatteryStatusData.isDataList,
        },
        Type:type
    }
    return postData
}

