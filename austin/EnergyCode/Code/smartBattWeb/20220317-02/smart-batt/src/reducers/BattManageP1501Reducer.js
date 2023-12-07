import {
    UPDATE_COMPANY_P1501,
    UPDATE_BATTERYGROUPID_P1501,
    UPDATE_BATTERYTYPE_P1501,
    UPDATE_INSTALLDATE_P1501,
    RESET_COMPANY_P1501,
    RESET_BATTERYGROUPID_P1501,
    RESET_BATTERYTYPE_P1501,
    RESET_INSTALLDATE_P1501,
    RESET_ALL_P1501,
} from '../constants/BattManage-action-type';
import { initFilter, initFilterDate } from '../pages/BattManage/Page1501/InitDataFormat';



// 初始化
const initialState = {
    isFilterCompanyData: {//預設公司別
        ...initFilter
    },
    isFilterBatteryGroupIdData: {//預設電池組ID
        ...initFilter
    },
    isFilterBatteryTypeData: {//預設電池型號
        ...initFilter
    },
    isFilterInstallDateData: {//安裝日期
        ...initFilterDate
    },
}

const BattManageP1501Reducer = (state = initialState, action) => {
    switch (action.type) {
        case UPDATE_COMPANY_P1501:
            return {
                ...state,
                isFilterCompanyData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_BATTERYGROUPID_P1501:
            return {
                ...state,
                isFilterBatteryGroupIdData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_BATTERYTYPE_P1501:
            return {
                ...state,
                isFilterBatteryTypeData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_INSTALLDATE_P1501:
            return {
                ...state,
                isFilterInstallDateData: {
                    Radio: action.payload.Radio,
                    Start: action.payload.Start,
                    End: action.payload.End,
                    isButtonList: action.payload.isButtonList
                }
            }
        case RESET_COMPANY_P1501:
            return {
                ...state,
                isFilterCompanyData: {//預設公司別
                    ...initFilter
                }
            }
        case RESET_BATTERYGROUPID_P1501:
            return {
                ...state,
                isFilterBatteryGroupIdData: {//預設電池組ID
                    ...initFilter
                }
            }
        case RESET_BATTERYTYPE_P1501:
            return {
                ...state,
                isFilterBatteryTypeData: {//預設電池型號
                    ...initFilter
                },
            }
        case RESET_INSTALLDATE_P1501:
            return {
                ...state,
                isFilterInstallDateData: {//安裝日期
                    ...initFilterDate
                }
            }
        case RESET_ALL_P1501:
            return {
                ...initialState
            }
        default:
            return state
    }
}
export default BattManageP1501Reducer;