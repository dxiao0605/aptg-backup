import { combineReducers } from 'redux';
import LoginReducer from './LoginReducer';                                  //登入、登出
import LanguageListReducer from './LanguageListReducer';                    //語系清單
import TimeZoneListReducer from './TimeZoneListReducer';                    //時區清單
import MainNavReducer from './MainNavReducer';                              //左邊目錄紀錄
import HomeFilterReducer from './HomeFilterReducer';                        //總覽篩選
import AlertReducer from './AlertReducer';                                  //告警(未解決,己解決)
import AlertCountReducer from './AlertCountReducer';                        //告警數
import AlertFilterReducer from './AlertFilterReducer';                      //告警篩選(已解決)
import AlertUnsolvedFilterReducer from './AlertUnsolvedFilterReducer';      //告警篩選(未解決)
import BattDataReducer from './BattDataReducer';                            //電池數據(第一層battGroup,第二層battData)
import BattFilterReducer from './BattFilterReducer';                        //電池數據(第一層,第二層),電池歷史(第一層,第二層)篩選
import LastBAListReducer from './LastBAListReducer';                        //前次BA設定清單
import LastBBListReducer from './LastBBListReducer';                        //前次BB設定清單
import LastB3ListReducer from './LastB3ListReducer';                        //前次B3設定清單
import LastB5ListReducer from './LastB5ListReducer';                        //前次B5設定清單
import BattTypeListReducer from './BattTypeListReducer';                    //電池類型清單
import BattManageReducer from './BattManageReducer';                        //電池組管理
import BattManageP1501Reducer from './BattManageP1501Reducer';              //電池組管理-電池組管理
import BattManageP1505Reducer from './BattManageP1505Reducer';              //電池組管理-電池型號管理
import GroupManageReducer from './GroupManageReducer';                      //站台管理
import GroupManageP1502Reducer from './GroupManageP1502Reducer';            //站台管理-站台管理
import FilterCountryReducer from './FilterCountryReducer';                  //篩選國家
import FilterAreaReducer from './FilterAreaReducer';                        //篩選地域
import FilterGroupIdReducer from './FilterGroupIdReducer';                  //篩選站台編號
import FilterGroupNameReducer from './FilterGroupNameReducer';              //篩選站台名稱
import FilterBatteryGroupIdReducer from './FilterBatteryGroupIdReducer';    //篩選電池組ID
import FilterCompanyReducer from './FilterCompanyReducer';                  //篩選公司
import NBManageReducer from './NBManageReducer';                            //通訊序號管理
import NBManageP1539Reducer from './NBManageP1539Reducer';                  //通訊序號管理-異動紀錄
import NBManageP1572Reducer from './NBManageP1572Reducer';                  //通訊序號管理-接續歷史
import CommandReducer from './CommandReducer';                              //電池參數設定
import CommandP1504Reducer from './CommandP1504Reducer';                    //電池參數設定-電池參數設定
import CommandP1559Reducer from './CommandP1559Reducer';                    //電池參數設定-參數設定歷史
import UserManageReducer from './UserManageReducer';                        //使用者管理
import UserManageP1701Reducer from './UserManageP1701Reducer';              //使用者管理-使用者設定
import SystemSettingsReducer from './SystemSettingsReducer';                //系統設定


const rootReducer = combineReducers({
    LoginReducer: LoginReducer,
    LanguageListReducer: LanguageListReducer,
    TimeZoneListReducer: TimeZoneListReducer,
    MainNavReducer: MainNavReducer,
    HomeFilterReducer: HomeFilterReducer,
    AlertReducer: AlertReducer,
    AlertCountReducer: AlertCountReducer,
    AlertFilterReducer: AlertFilterReducer,
    AlertUnsolvedFilterReducer: AlertUnsolvedFilterReducer,
    BattDataReducer: BattDataReducer,
    BattFilterReducer: BattFilterReducer,
    LastBAListReducer: LastBAListReducer,
    LastBBListReducer: LastBBListReducer,
    LastB3ListReducer: LastB3ListReducer,
    LastB5ListReducer: LastB5ListReducer,
    BattTypeListReducer: BattTypeListReducer,
    BattManageReducer: BattManageReducer,
    BattManageP1501Reducer: BattManageP1501Reducer,
    BattManageP1505Reducer: BattManageP1505Reducer,
    GroupManageReducer: GroupManageReducer,
    GroupManageP1502Reducer: GroupManageP1502Reducer,
    FilterCountryReducer: FilterCountryReducer,
    FilterAreaReducer: FilterAreaReducer,
    FilterGroupIdReducer: FilterGroupIdReducer,
    FilterGroupNameReducer: FilterGroupNameReducer,
    FilterBatteryGroupIdReducer: FilterBatteryGroupIdReducer,
    FilterCompanyReducer: FilterCompanyReducer,
    NBManageReducer: NBManageReducer,
    NBManageP1539Reducer: NBManageP1539Reducer,
    NBManageP1572Reducer: NBManageP1572Reducer,
    CommandReducer: CommandReducer,
    CommandP1504Reducer: CommandP1504Reducer,
    CommandP1559Reducer: CommandP1559Reducer,
    UserManageReducer: UserManageReducer,
    UserManageP1701Reducer:UserManageP1701Reducer,
    SystemSettingsReducer: SystemSettingsReducer,
});

export default rootReducer;
