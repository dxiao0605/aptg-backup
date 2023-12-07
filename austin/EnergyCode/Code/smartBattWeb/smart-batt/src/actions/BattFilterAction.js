import {
    UPDATE_COMPANY_BATT,    //
    UPDATE_COUNTRY_BATT,
    UPDATE_AREA_BATT,
    UPDATE_GROUPID_BATT,
    UPDATE_GROUPNAME_BATT,
    UPDATE_BATTERYGROUPID_BATT,
    UPDATE_BATTERYTYPE_BATT,
    UPDATE_BATTERYSTATUS_BATT,
    UPDATE_RECORDTIME_BATT,
    RESET_COMPANY_BATT,
    RESET_COUNTRY_BATT,
    RESET_AREA_BATT,
    RESET_GROUPID_BATT,
    RESET_GROUPNAME_BATT,
    RESET_BATTERYGROUPID_BATT,
    RESET_BATTERYTYPE_BATT,
    RESET_BATTERYSTATUS_BATT,
    RESET_RECORDTIME_BATT,
    RESET_ALL_BATT,
} from '../constants/BattFilter-action-type';
// GroupInternalId

export const updateCompany = (object) => ({
    type: UPDATE_COMPANY_BATT,
    payload: { ...object }
})
export const updateCountry = (object) => ({
    type: UPDATE_COUNTRY_BATT,
    payload: { ...object }
})
export const updateArea = (object) => ({
    type: UPDATE_AREA_BATT,
    payload: { ...object }
})
export const updateGroupId = (object) => ({
    type: UPDATE_GROUPID_BATT,
    payload: { ...object }
})
export const updateGroupName = (object) => ({
    type: UPDATE_GROUPNAME_BATT,
    payload: { ...object }
})

export const updateBatteryGroupIdBatt = (object) => ({
    type: UPDATE_BATTERYGROUPID_BATT,
    payload: { ...object }
})
export const updateBatteryType = (object) => ({
    type: UPDATE_BATTERYTYPE_BATT,
    payload: { ...object }
})
export const updateBatteryStatus = (object) => ({
    type: UPDATE_BATTERYSTATUS_BATT,
    payload: { ...object }
})

export const updateRecordTime = (object) => ({
    type: UPDATE_RECORDTIME_BATT,
    payload: { ...object }
})



export const resetCompany = () => ({
    type: RESET_COMPANY_BATT,
})
export const resetCountry = () => ({
    type: RESET_COUNTRY_BATT,
})
export const resetArea = () => ({
    type: RESET_AREA_BATT,
})
export const resetGroupId = () => ({
    type: RESET_GROUPID_BATT,
})
export const resetGroupName = () => ({
    type: RESET_GROUPNAME_BATT,
})


export const resetBatteryGroupId = () => ({
    type: RESET_BATTERYGROUPID_BATT,
})
export const resetBatteryType = () => ({
    type: RESET_BATTERYTYPE_BATT,
})
export const resetBatteryStatus = () => ({
    type: RESET_BATTERYSTATUS_BATT,
})
export const resetRecordTime = () => ({
    type: RESET_RECORDTIME_BATT,
})
export const resetAllBattData = () => ({
    type: RESET_ALL_BATT,
})

//P1502 Link 到電池數據第二層用
export const updateCompanyToBattFilter = (object) => ({
    type: UPDATE_COMPANY_BATT,
    payload: { ...object }
})
//P1502 Link 到電池數據第二層用
export const updateGroupIdToBattFilter = (object) => ({
    type: UPDATE_GROUPID_BATT,
    payload: { ...object }
})