import {
    UPDATE_COMPANY_P1505,
    UPDATE_BATTERYTYPENAME_P1505,
    RESET_COMPANY_P1505,
    RESET_BATTERYTYPENAME_P1505,
    RESET_ALL_P1505,
} from '../constants/BattManage-action-type';


export const updateCompany = (object) => ({
    type: UPDATE_COMPANY_P1505,
    payload: { ...object }
})
export const updateBatteryTypeName = (object) => ({
    type: UPDATE_BATTERYTYPENAME_P1505,
    payload: { ...object }
})
export const resetCompany = () => ({
    type: RESET_COMPANY_P1505,
})
export const resetBatteryTypeName = () => ({
    type: RESET_BATTERYTYPENAME_P1505,
})
export const resetAll = () => ({
    type: RESET_ALL_P1505,
})

export const resetAll_P1505_Action = () => ({
    type: RESET_ALL_P1505,
})