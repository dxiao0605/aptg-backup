import {
    UPDATE_EVENTTYPECODE_ALERTUNSOLVED,
    UPDATE_COMPANY_ALERTUNSOLVED,
    UPDATE_COUNTRY_ALERTUNSOLVED,
    UPDATE_AREA_ALERTUNSOLVED,
    UPDATE_GROUPID_ALERTUNSOLVED,
    UPDATE_GROUPNAME_ALERTUNSOLVED,
    UPDATE_BATTERYGROUPID_ALERTUNSOLVED,
    UPDATE_BATTERYTYPE_ALERTUNSOLVED,
    UPDATE_RECORDTIME_ALERTUNSOLVED,
    RESET_EVENTTYPECODE_ALERTUNSOLVED,
    RESET_COMPANY_ALERTUNSOLVED,
    RESET_COUNTRY_ALERTUNSOLVED,
    RESET_AREA_ALERTUNSOLVED,
    RESET_GROUPID_ALERTUNSOLVED,
    RESET_GROUPNAME_ALERTUNSOLVED,
    RESET_BATTERYGROUPID_ALERTUNSOLVED,
    RESET_BATTERYTYPE_ALERTUNSOLVED,
    RESET_RECORDTIME_ALERTUNSOLVED,
    RESET_ALL_ALERTUNSOLVED,
} from '../constants/AlertUnsolvedFilter-action-type';


export const updateEventTypeCodeUnSolved = (object) => ({
    type: UPDATE_EVENTTYPECODE_ALERTUNSOLVED,
    payload: { ...object }
})
export const updateCompanyUnSolved  = (object) => ({
    type: UPDATE_COMPANY_ALERTUNSOLVED,
    payload: { ...object }
})
export const updateCountryUnSolved  = (object) => ({
    type: UPDATE_COUNTRY_ALERTUNSOLVED,
    payload: { ...object }
})
export const updateAreaUnSolved  = (object) => ({
    type: UPDATE_AREA_ALERTUNSOLVED,
    payload: { ...object }
})
export const updateGroupIdUnSolved  = (object) => ({
    type: UPDATE_GROUPID_ALERTUNSOLVED,
    payload: { ...object }
})
export const updateGroupNameUnSolved  = (object) => ({
    type: UPDATE_GROUPNAME_ALERTUNSOLVED,
    payload: { ...object }
})

export const updateBatteryGroupIdUnSolved  = (object) => ({
    type: UPDATE_BATTERYGROUPID_ALERTUNSOLVED,
    payload: { ...object }
})
export const updateBatteryTypeUnSolved  = (object) => ({
    type: UPDATE_BATTERYTYPE_ALERTUNSOLVED,
    payload: { ...object }
})
export const updateRecordTimeUnSolved = (object) => ({
    type: UPDATE_RECORDTIME_ALERTUNSOLVED,
    payload: { ...object }
})



export const resetEventTypeCodeUnSolved  = () => ({
    type: RESET_EVENTTYPECODE_ALERTUNSOLVED,
})
export const resetCompanyUnSolved  = () => ({
    type: RESET_COMPANY_ALERTUNSOLVED,
})
export const resetCountryUnSolved  = () => ({
    type: RESET_COUNTRY_ALERTUNSOLVED,
})
export const resetAreaUnSolved  = () => ({
    type: RESET_AREA_ALERTUNSOLVED,
})
export const resetGroupIdUnSolved  = () => ({
    type: RESET_GROUPID_ALERTUNSOLVED,
})
export const resetGroupNameUnSolved  = () => ({
    type: RESET_GROUPNAME_ALERTUNSOLVED,
})



export const resetBatteryGroupIdUnSolved  = () => ({
    type: RESET_BATTERYGROUPID_ALERTUNSOLVED,
})
export const resetBatteryTypeUnSolved  = () => ({
    type: RESET_BATTERYTYPE_ALERTUNSOLVED,
})
export const resetRecordTimeUnSolved = () => ({
    type: RESET_RECORDTIME_ALERTUNSOLVED,
})

export const resetAllAlertUnsolved = () => ({
    type: RESET_ALL_ALERTUNSOLVED,
})