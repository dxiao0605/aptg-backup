import {
	UPDATE_COMPANY_P1572,
	UPDATE_COUNTRY_P1572,
	UPDATE_AREA_P1572,
	UPDATE_GROUPID_P1572,
	UPDATE_NBID_P1572,
	UPDATE_PREVNBID_P1572,
	UPDATE_STARTTIME_P1572,
	RESET_COMPANY_P1572,
	RESET_COUNTRY_P1572,
	RESET_AREA_P1572,
	RESET_GROUPID_P1572,
	RESET_NBID_P1572,
	RESET_PREVNBID_P1572,
	RESET_STARTTIME_P1572,
	RESET_ALL_P1572,
} from "../constants/NBManage-action-type";


export const updateCompany = (object) => ({
    type: UPDATE_COMPANY_P1572,
    payload: { ...object }
})
export const updateCountry = (object) => ({
    type: UPDATE_COUNTRY_P1572,
    payload: { ...object }
})
export const updateArea = (object) => ({
    type: UPDATE_AREA_P1572,
    payload: { ...object }
})
export const updateGroupId = (object) => ({
    type: UPDATE_GROUPID_P1572,
    payload: { ...object }
})
export const updateNBID = (object) => ({
    type: UPDATE_NBID_P1572,
    payload: { ...object }
})
export const updatePreviousNBID = (object) => ({
    type: UPDATE_PREVNBID_P1572,
    payload: { ...object }
})
export const updateStartTime = (object) => ({
    type: UPDATE_STARTTIME_P1572,
    payload: { ...object }
})
export const resetCompany = () => ({
    type: RESET_COMPANY_P1572,
})
export const resetCountry = (object) => ({
    type: RESET_COUNTRY_P1572,
    payload: { ...object }
})
export const resetArea = (object) => ({
    type: RESET_AREA_P1572,
    payload: { ...object }
})
export const resetGroupId = () => ({
    type: RESET_GROUPID_P1572,
})
export const resetNBID = () => ({
    type: RESET_NBID_P1572,
})
export const resetPreviousNBID = () => ({
    type: RESET_PREVNBID_P1572,
})
export const resetStartTime = () => ({
    type: RESET_STARTTIME_P1572,
})
export const resetAll = () => ({
    type: RESET_ALL_P1572,
})

export const resetAll_P1572_Action = () => ({
    type: RESET_ALL_P1572,
})