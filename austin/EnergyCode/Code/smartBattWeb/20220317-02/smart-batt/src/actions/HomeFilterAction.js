import {
    UPDATE_COMPANY_HOME,
    UPDATE_COUNTRY_HOME,
    UPDATE_AREA_HOME,
    UPDATE_GROUPID_HOME,
    // UPDATE_GROUPNAME_HOME,
    UPDATE_GROUPIDLIST_STATUS,
    RESET_COMPANY_HOME,
    RESET_COUNTRY_HOME,
    RESET_AREA_HOME,
    RESET_GROUPID_HOME,
    // RESET_GROUPNAME_HOME,
    RESET_ALL_HOME,
} from '../constants/HomeFilter-action-type';

export const updateHomeCompany = (object) => ({
    type: UPDATE_COMPANY_HOME,
    payload: { ...object }
})
export const updateHomeCountry = (object) => ({
    type: UPDATE_COUNTRY_HOME,
    payload: { ...object }
})
export const updateHomeArea = (object) => ({
    type: UPDATE_AREA_HOME,
    payload: { ...object }
})
export const updateHomeGroupId = (object) => ({
    type: UPDATE_GROUPID_HOME,
    payload: { ...object }
})
// export const updateGroupName = (object) => ({
//     type: UPDATE_GROUPNAME_HOME,
//     payload: { ...object }
// })
export const updateGroupIdListStatus = (status) => ({
    type: UPDATE_GROUPIDLIST_STATUS,
    payload: {
        isGroupIdListStatus: status
    }
})


export const resetHomeCompany = () => ({
    type: RESET_COMPANY_HOME,
})
export const resetHomeCountry = () => ({
    type: RESET_COUNTRY_HOME,
})
export const resetHomeArea = () => ({
    type: RESET_AREA_HOME,
})
export const resetHomeGroupId = () => ({
    type: RESET_GROUPID_HOME,
})
// export const resetGroupName = () => ({
//     type: RESET_GROUPNAME_HOME,
// })


export const resetAllHomeData = () => ({
    type: RESET_ALL_HOME,
})