import {
    UPDATE_COMPANY_BATT,
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
import { initFilterSelectData, initFilterDate } from '../components/BattFilter/InitDataFormat';


// 初始化
const initialState = {
    isFilterCompanyData: {//預設公司別
        ...initFilterSelectData
    },
    isFilterCountryData: {//預設國家
        ...initFilterSelectData
    },
    isFilterAreaData: {//預設地域
        ...initFilterSelectData
    },
    isFilterGroupIdData: {//預設站台編號
        ...initFilterSelectData
    },
    isFilterGroupNameData: {//預設站台名稱
        ...initFilterSelectData
    },
    isFilterBatteryGroupIdData: {//預設電池組ID
        ...initFilterSelectData
    },
    isFilterBatteryTypeData: {//預設電池型號
        ...initFilterSelectData
    },
    isFilterBatteryStatusData: {//預設電池狀態
        ...initFilterSelectData
    },
    isFilterRecordTimeData: {//預設數據時間
        ...initFilterDate
    },
}

const BattFilterReducer = (state = initialState, action) => {
    switch (action.type) {
        case UPDATE_COMPANY_BATT://公司別
            return {
                ...state,
                isFilterCompanyData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_COUNTRY_BATT://國家
            return {
                ...state,
                isFilterCountryData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_AREA_BATT://地域
            return {
                ...state,
                isFilterAreaData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_GROUPID_BATT://站台編號
            return {
                ...state,
                isFilterGroupIdData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_GROUPNAME_BATT://站台名稱
            return {
                ...state,
                isFilterGroupNameData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_BATTERYGROUPID_BATT://電池組ID
            return {
                ...state,
                isFilterBatteryGroupIdData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_BATTERYTYPE_BATT://電池型號
            return {
                ...state,
                isFilterBatteryTypeData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_BATTERYSTATUS_BATT://電池狀態
            return {
                ...state,
                isFilterBatteryStatusData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_RECORDTIME_BATT://數據時間
            return {
                ...state,
                isFilterRecordTimeData: {
                    Radio: action.payload.Radio,
                    Start: action.payload.Start,
                    StartHH: action.payload.StartHH,
                    StartMM: action.payload.StartMM,
                    End: action.payload.End,
                    EndHH: action.payload.EndHH,
                    EndMM: action.payload.EndMM,
                    isButtonList: action.payload.isButtonList
                }
            }

        case RESET_COMPANY_BATT:
            return {
                ...state,
                isFilterCompanyData: {//初始值公司別
                    ...initFilterSelectData
                }
            }
        case RESET_COUNTRY_BATT:
            return {
                ...state,
                isFilterCountryData: {//初始值國家
                    ...initFilterSelectData
                }
            }
        case RESET_AREA_BATT:
            return {
                ...state,
                isFilterAreaData: {//初始值地域
                    ...initFilterSelectData
                },
            }
        case RESET_GROUPID_BATT:
            return {
                ...state,
                isFilterGroupIdData: {//初始值站台編號
                    ...initFilterSelectData
                }
            }
        case RESET_GROUPNAME_BATT:
            return {
                ...state,
                isFilterGroupNameData: {//初始值站台名稱
                    ...initFilterSelectData
                }
            }


        case RESET_BATTERYGROUPID_BATT://初始值電池組ID
            return {
                ...state,
                isFilterBatteryGroupIdData: {
                    ...initFilterSelectData
                }
            }
        case RESET_BATTERYTYPE_BATT://初始值電池型號
            return {
                ...state,
                isFilterBatteryTypeData: {
                    ...initFilterSelectData
                }
            }
        case RESET_BATTERYSTATUS_BATT://初始值電池狀態
            return {
                ...state,
                isFilterBatteryStatusData: {
                    ...initFilterSelectData
                }
            }
        case RESET_RECORDTIME_BATT://初始值數據時間
            return {
                ...state,
                isFilterRecordTimeData: {
                    ...initFilterDate
                }
            }

        case RESET_ALL_BATT://全部初始值
            return {
                ...initialState
            }
        default:
            return state
    }
}
export default BattFilterReducer;