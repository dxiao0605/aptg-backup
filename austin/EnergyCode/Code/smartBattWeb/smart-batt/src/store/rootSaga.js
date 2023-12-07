import { all } from 'redux-saga/effects';
import { LoginSaga } from '../actions/LoginAction';                                 //登入
import { AlertCountSaga } from '../actions/AlertCountAction';                       //告警數
import { LanguageListSaga } from '../actions/LanguageListAction';                   //語系清單
import { TimeZoneListSaga } from '../actions/TimeZoneListAction';                   //時區清單
import { BattTypeListSaga } from '../actions/BattTypeListAction';                   //電池類型清單
import { LastBAListSaga } from '../actions/LastBAListAction';                       //最後BA設定清單
import { LastBBListSaga } from '../actions/LastBBListAction';                       //最後BB設定清單
import { LastB3ListSaga } from '../actions/LastB3ListAction';                       //最後B3設定清單
import { LastB5ListSaga } from '../actions/LastB5ListAction';                       //最後B5設定清單
// import { BattDataSaga} from '../actions/BattDataAction';                         //電池數據
// Filter list
import { FilterCountrySaga } from '../actions/FilterCountryAction';                 //篩選國家 
import { FilterAreaSaga } from '../actions/FilterAreaAction';                       //篩選地域
import { FilterGroupIdSaga } from '../actions/FilterGroupIdAction';                 //篩選站台編號
import { FilterGroupNameSaga } from '../actions/FilterGroupNameAction';             //篩選站台名稱
import { FilterBatteryGroupIdSaga } from '../actions/FilterBatteryGroupIdAction';   //篩選電池組ID
import { FilterCompanySaga } from '../actions/FilterCompanyAction';                 //篩選公司



function* rootSaga() {
    yield all([
        // Alert Count
        AlertCountSaga(),
        // List
        LanguageListSaga(),
        TimeZoneListSaga(),
        BattTypeListSaga(),
        LastBAListSaga(),
        LastBBListSaga(),
        LastB3ListSaga(),
        LastB5ListSaga(),
        // Filter
        FilterCountrySaga(),
        FilterAreaSaga(),
        FilterGroupIdSaga(),
        FilterGroupNameSaga(),
        FilterBatteryGroupIdSaga(),
        FilterCompanySaga(),
        // Page
        LoginSaga(),    //登入
        // BattDataSaga(),
    ]);
}
export default rootSaga;