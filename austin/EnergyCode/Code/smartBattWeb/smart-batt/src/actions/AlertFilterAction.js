import {
    UPDATE_EVENTTYPECODE_ALERT,
    UPDATE_COMPANY_ALERT,    //
    UPDATE_COUNTRY_ALERT,
    UPDATE_AREA_ALERT,
    UPDATE_GROUPID_ALERT,
    UPDATE_GROUPNAME_ALERT,
    UPDATE_BATTERYGROUPID_ALERT,
    UPDATE_BATTERYTYPE_ALERT,
    UPDATE_RECORDTIME_ALERT,
    RESET_EVENTTYPECODE_ALERT,
    RESET_COMPANY_ALERT,
    RESET_COUNTRY_ALERT,
    RESET_AREA_ALERT,
    RESET_GROUPID_ALERT,
    RESET_GROUPNAME_ALERT,
    RESET_BATTERYGROUPID_ALERT,
    RESET_BATTERYTYPE_ALERT,
    RESET_RECORDTIME_ALERT,
    RESET_ALL_ALERT,
} from '../constants/AlertFilter-action-type';


export const updateEventTypeCode = (object) => ({
    type: UPDATE_EVENTTYPECODE_ALERT,
    payload: { ...object }
})
export const updateCompany = (object) => ({
    type: UPDATE_COMPANY_ALERT,
    payload: { ...object }
})
export const updateCountry = (object) => ({
    type: UPDATE_COUNTRY_ALERT,
    payload: { ...object }
})
export const updateArea = (object) => ({
    type: UPDATE_AREA_ALERT,
    payload: { ...object }
})
export const updateGroupId = (object) => ({
    type: UPDATE_GROUPID_ALERT,
    payload: { ...object }
})
export const updateGroupName = (object) => ({
    type: UPDATE_GROUPNAME_ALERT,
    payload: { ...object }
})

export const updateBatteryGroupId = (object) => ({
    type: UPDATE_BATTERYGROUPID_ALERT,
    payload: { ...object }
})
export const updateBatteryType = (object) => ({
    type: UPDATE_BATTERYTYPE_ALERT,
    payload: { ...object }
})
export const updateRecordTimeAlert = (object) => ({
    type: UPDATE_RECORDTIME_ALERT,
    payload: { ...object }
})


export const resetEventTypeCode = () => ({
    type: RESET_EVENTTYPECODE_ALERT,
})
export const resetCompany = () => ({
    type: RESET_COMPANY_ALERT,
})
export const resetCountry = () => ({
    type: RESET_COUNTRY_ALERT,
})
export const resetArea = () => ({
    type: RESET_AREA_ALERT,
})
export const resetGroupId = () => ({
    type: RESET_GROUPID_ALERT,
})
export const resetGroupName = () => ({
    type: RESET_GROUPNAME_ALERT,
})


export const resetBatteryGroupId = () => ({
    type: RESET_BATTERYGROUPID_ALERT,
})
export const resetBatteryType = () => ({
    type: RESET_BATTERYTYPE_ALERT,
})
export const resetRecordTimeAlert = () => ({
    type: RESET_RECORDTIME_ALERT,
})

export const resetAllAlert = () => ({
    type: RESET_ALL_ALERT,
})