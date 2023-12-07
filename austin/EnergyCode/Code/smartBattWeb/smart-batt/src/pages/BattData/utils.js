// postData
export const getPostData = ({props,type}) =>{
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
        Status: {
            All: isFilterBatteryStatusData.isChecked ? '1' : '0',
            List: isFilterBatteryStatusData.isDataList,
        },
        Type: type
    }
    return postData
}