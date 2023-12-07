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
} from '../constants/NBManage-action-type';
import { initFilterSelectData, initFilterDate } from '../pages/NBManage/Page1572/InitDataFormat';


// 初始化
const initialState = {
    isFilterCompanyData: {//預設公司別
        ...initFilterSelectData
    },
    isFilterCountryData: {//預設國家別
        ...initFilterSelectData
    },
    isFilterAreaData: {//預設地域別
        ...initFilterSelectData
    },
    isFilterGroupIdData: {//預設站台別
        ...initFilterSelectData
    },
    isFilterNBIDData: {//預設通訊序號
        ...initFilterSelectData
    },
    isFilterPreviousNBIDData: {//預設(接續)通訊序號
        ...initFilterSelectData
    },
    isFilterStartTimeData: {//預設接續開始時間
        ...initFilterDate
    },
}

const NBManageP1572Reducer = (state = initialState, action) => {
    switch (action.type) {
        case UPDATE_COMPANY_P1572://公司別
            return {
                ...state,
                isFilterCompanyData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_COUNTRY_P1572://國家別
            return {
                ...state,
                isFilterCountryData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_AREA_P1572://地域
            return {
                ...state,
                isFilterAreaData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_GROUPID_P1572://站台編號/名稱
            return {
                ...state,
                isFilterGroupIdData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_NBID_P1572://通訊序號
            return {
                ...state,
                isFilterNBIDData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_PREVNBID_P1572://(接續)通訊序號
            return {
                ...state,
                isFilterPreviousNBIDData: {
                    isOpen: action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_STARTTIME_P1572://接續開始時間
            return {
                ...state,
                isFilterStartTimeData:  {
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
        case RESET_COMPANY_P1572:
            return {
                ...state,
                isFilterCompanyData: {//初始值公司別
                    ...initFilterSelectData
                }
            }
        case RESET_COUNTRY_P1572:
            return {
                ...state,
                isFilterCountryData: {//初始值國家別
                    ...initFilterSelectData
                }
            }
        case RESET_AREA_P1572:
            return {
                ...state,
                isFilterAreaData: {//初始值地域別
                    ...initFilterSelectData
                }
            }
        case RESET_GROUPID_P1572:
            return {
                ...state,
                isFilterGroupIdData: {//初始值站台別
                    ...initFilterSelectData
                }
            }
        case RESET_NBID_P1572:
            return {
                ...state,
                isFilterNBIDData: {//初始值通訊序號
                    ...initFilterSelectData
                },
            }
        case RESET_PREVNBID_P1572:
            return {
                ...state,
                isFilterPreviousNBIDData: {//初始值(接續)通訊序號
                    ...initFilterSelectData
                }
            }
        case RESET_STARTTIME_P1572://初始值接續開始時間
            return {
                ...state,
                isFilterStartTimeData: {
                    ...initFilterDate
                }
            }
        case RESET_ALL_P1572://全部初始值
            return {
                ...initialState
            }
        default:
            return state
    }
}
export default NBManageP1572Reducer;