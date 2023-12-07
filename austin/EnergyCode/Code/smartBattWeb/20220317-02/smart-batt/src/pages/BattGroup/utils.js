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


