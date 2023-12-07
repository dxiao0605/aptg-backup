import {
    UPDATE_COMPANY_P1505,
    UPDATE_BATTERYTYPENAME_P1505,
    RESET_COMPANY_P1505,
    RESET_BATTERYTYPENAME_P1505,
    RESET_ALL_P1505,
} from '../constants/BattManage-action-type';
import { initFilterSelectData } from '../pages/BattManage/Page1505/InitDataFormat';

// 初始化
const initialState = {
    isFilterCompanyData: {//預設公司別
        ...initFilterSelectData
    },
    isFilterBatteryTypeNameData: {//預設電池型號中文
        ...initFilterSelectData
    },
}

const BattManageP1505Reducer = (state = initialState, action) => {
    switch (action.type) {
        case UPDATE_COMPANY_P1505://公司別
            return {
                ...state,
                isFilterCompanyData: {
                    isOpen:action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case UPDATE_BATTERYTYPENAME_P1505://電池型號中文
            return {
                ...state,
                isFilterBatteryTypeNameData: {
                    isOpen:action.payload.isOpen,
                    isChecked: action.payload.isChecked,
                    isDataList: action.payload.isDataList,
                    isButtonList: action.payload.isButtonList,
                }
            }
        case RESET_COMPANY_P1505:
            return {
                ...state,
                isFilterCompanyData: {//初始值公司別
                    ...initFilterSelectData
                }
            }
        case RESET_BATTERYTYPENAME_P1505:
            return {
                ...state,
                isFilterBatteryTypeNameData: {//初始值電池型號中文
                    ...initFilterSelectData
                }
            }
        case RESET_ALL_P1505://全部初始值
            return {
                ...initialState
            }
        default:
            return state
    }
}
export default BattManageP1505Reducer;