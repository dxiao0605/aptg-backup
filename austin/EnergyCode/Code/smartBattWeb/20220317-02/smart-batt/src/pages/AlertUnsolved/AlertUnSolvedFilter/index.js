import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';
import { apipath, ajax } from '../../../utils/ajax';
import { setBattInternalIDAlert } from "../../../actions/BattDataAction"; //setBattInternalID
import { resetBatteryGroupIdUnSolved } from '../../../actions/AlertUnsolvedFilterAction';
import FilterSave from './FilterSave';//儲存標籤紀錄
import FilterItemHeader from '../../../components/FilterDrawer/FilterItemHeader';//每筆Item的header
import FilterItemSelect from '../../../components/FilterDrawer/FilterItemSelect';//每筆Item的Select
import FilterItemRadio from '../../../components/FilterDrawer/FilterItemRadio';//每筆Item的Radio
import { checkObject } from '../../../components/FilterDrawer/utils';//整理checkbox狀態
import { FilterNames, FilterNames_API, initFilterSelectData, initFilterDate } from './InitDataFormat';//初始化格式
import { CusMainBtnStyle } from '../../../components/CusMainBtnStyle';
import Divider from '@material-ui/core/Divider';


const apiUrl = apipath();
class AlertUnSolvedFilter extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {
            isFilterEventTypeCodeList: [],//預設告警類型清單
            isFilterCompanyList: [],//預設公司別清單
            isFilterCountryList: [],//預設國家清單
            isFilterAreaList: [],//預設地域清單
            isFilterGroupIdList: [],//預設站台編號清單
            isFilterBatteryGroupIdList: [],//預設電池組ID清單
            isFilterRecordTimeList: [//預設數據時間
                { Value: "0", Label: "1098", Type: "String", show: false },
                { Value: "1", Label: "", Type: "Date", show: true },
                { Value: "2", Label: "1083", Type: "String", show: false },
                { Value: "3", Label: "1109", Type: "String", show: false },
                { Value: "4", Label: "1110", Type: "String", show: true },
            ],
            isFilterEventTypeCodeDataUnSolved: {//預設告警類型
                ...initFilterSelectData
            },
            isFilterCompanyDataUnSolved: {//預設公司別
                ...initFilterSelectData
            },
            isFilterCountryDataUnSolved: {////預設國家
                ...initFilterSelectData
            },
            isFilterAreaDataUnSolved: {//預設地域
                ...initFilterSelectData
            },
            isFilterGroupIdDataUnSolved: {//預設站台編號
                ...this.props.isFilterGroupIdDataUnSolved,
            },
            isFilterBatteryGroupIdDataUnSolved: {//預設電池組ID
                ...initFilterSelectData
            },
            isFilterRecordTimeUnSolved: {//預設數據時間
                ...initFilterDate
            },
            isFilterTotal: 1,//總數
            defRadioRangeMaxDay: '90',//往未來多少天
            defRadioRangeMinDay: '90',//往從前多少天
        }
    }
    componentDidMount() {
        const { isFilterEventTypeCodeDataUnSolved, isFilterCompanyDataUnSolved, isFilterCountryDataUnSolved, isFilterAreaDataUnSolved, isFilterGroupIdDataUnSolved, isFilterRecordTimeUnSolved,// isFilterBatteryGroupIdDataUnSolved,//isFilterGroupNameDataUnSolved,  isFilterBatteryTypeDataUnSolved,
        } = this.props;

        // 篩選條件總數
        const list = [
            this.state.isFilterEventTypeCodeDataUnSolved.isDataList.length,
            this.state.isFilterCompanyDataUnSolved.isDataList.length,
            this.state.isFilterCountryDataUnSolved.isDataList.length,
            this.state.isFilterAreaDataUnSolved.isDataList.length,
            this.state.isFilterGroupIdDataUnSolved.isDataList.length,
        ];
        let total = 1;
        list.forEach(num => {
            total = total + num
        });
        this.setState({
            isFilterTotal: total
        })

        this.getBatteryFilter(isFilterEventTypeCodeDataUnSolved, isFilterCompanyDataUnSolved, isFilterCountryDataUnSolved, isFilterAreaDataUnSolved, isFilterGroupIdDataUnSolved, isFilterRecordTimeUnSolved,// isFilterBatteryGroupIdDataUnSolved, //isFilterGroupNameDataUnSolved,  isFilterBatteryTypeDataUnSolved,
        );//取選單資料
        this.getFilter();//篩選儲存清單API(GET)
        this.setState({
            isFilterRecordTimeUnSolved: isFilterRecordTimeUnSolved
        });

        // 判斷初始值(functionId)
    }
    componentDidUpdate(prevProps, prevState) {
        if (//更新全部的數字
            prevState.isFilterEventTypeCodeDataUnSolved.isDataList.length !== this.state.isFilterEventTypeCodeDataUnSolved.isDataList.length ||
            prevState.isFilterCompanyDataUnSolved.isDataList.length !== this.state.isFilterCompanyDataUnSolved.isDataList.length ||
            prevState.isFilterCountryDataUnSolved.isDataList.length !== this.state.isFilterCountryDataUnSolved.isDataList.length ||
            prevState.isFilterAreaDataUnSolved.isDataList.length !== this.state.isFilterAreaDataUnSolved.isDataList.length ||
            prevState.isFilterGroupIdDataUnSolved.isDataList.length !== this.state.isFilterGroupIdDataUnSolved.isDataList.length 
        ) {
            const list = [
                this.state.isFilterEventTypeCodeDataUnSolved.isDataList.length,
                this.state.isFilterCompanyDataUnSolved.isDataList.length,
                this.state.isFilterCountryDataUnSolved.isDataList.length,
                this.state.isFilterAreaDataUnSolved.isDataList.length,
                this.state.isFilterGroupIdDataUnSolved.isDataList.length,
            ];
            let total = 1;// let total = 0;
            list.forEach(num => {
                total = total + num
            });
            this.setState({
                isFilterTotal: total
            })
        }
        if (prevState.isFilterEventTypeCodeDataUnSolved.isChecked !== this.state.isFilterEventTypeCodeDataUnSolved.isChecked) {
            this.setState({
                isFilterEventTypeCodeDatUnSolveda: this.state.isFilterEventTypeCodeDataUnSolved
            })
        }
        if (prevState.isFilterCompanyDataUnSolved.isChecked !== this.state.isFilterCompanyDataUnSolved.isChecked) {
            this.setState({
                isFilterCompanyDataUnSolved: this.state.isFilterCompanyDataUnSolved
            })
        }
        if (prevState.isFilterCountryDataUnSolved.isChecked !== this.state.isFilterCountryDataUnSolved.isChecked) {
            this.setState({
                isFilterCountryDataUnSolved: this.state.isFilterCountryDataUnSolved
            })
        }
        if (prevState.isFilterAreaDataUnSolved.isChecked !== this.state.isFilterAreaDataUnSolved.isChecked) {
            this.setState({
                isFilterAreaDataUnSolved: this.state.isFilterAreaDataUnSolved
            })
        }
        if (prevState.isFilterGroupIdDataUnSolved.isChecked !== this.state.isFilterGroupIdDataUnSolved.isChecked) {
            this.setState({
                isFilterGroupIdDataUnSolved: this.state.isFilterGroupIdDataUnSolved
            })
        }
        // 數據時間
        if (prevState.isFilterRecordTimeUnSolved !== this.state.isFilterRecordTimeUnSolved) {
            this.setState({
                isFilterRecordTimeUnSolved: this.state.isFilterRecordTimeUnSolved
            })
        }
    }
    componentWillUnmount() {
        this.ajaxCancel = true;
        this.setState = (state, callback) => {
            return;
        };
    }

    render() {
        const { functionId } = this.props;
        const filterTitle = functionId.slice(0, 4);
        const { isFilterTotal, isFilterSaveList } = this.state;
        const { isFilterEventTypeCodeList, isFilterEventTypeCodeDataUnSolved } = this.state;//告警類型
        const { isFilterCompanyList, isFilterCompanyDataUnSolved } = this.state;//公司
        const { isFilterCountryList, isFilterCountryDataUnSolved } = this.state;//國家
        const { isFilterAreaList, isFilterAreaDataUnSolved } = this.state;//地域
        const { isFilterGroupIdList, isFilterGroupIdDataUnSolved } = this.state;//站台編號
        const { isFilterRecordTimeList, isFilterRecordTimeUnSolved } = this.state;//數據時間
        return (
            <Fragment>
                <div className="form-inline align-items-center justify-content-center pt-1 pb-2">
                    <i className="fas fa-filter mr-1" />
                    <Trans i18nKey={filterTitle} />
                    {/* 總計 */}
                    <span className="filter_prompt ml-1 noDetail">
                        {isFilterTotal}
                    </span>
                </div>
                <Divider />
                {/* 紀錄 */}
                <FilterSave
                    functionId={this.props.functionId} // 1301,1302
                    company={this.props.company}
                    list={isFilterSaveList}
                    onClickBookmark={(FilterConfig) => { this.onClickBookmark(FilterConfig) }}
                    onClickTrash={(FilterID) => { this.delFilter(FilterID) }}
                />
                <Divider />
                {/* 篩選器 */}
                <ul className={`filter overflowY mb-0 ${navigator.userAgent.indexOf('Windows') > 0 ? 'Windows' : 'Mac'}`}>
                    {
                        (functionId === "1301" || functionId === "1302") && (
                            <li>{/* 告警類型 */}
                                <FilterItemHeader
                                    title={"1315"}
                                    total={isFilterEventTypeCodeDataUnSolved.isDataList.length}
                                    detailList={isFilterEventTypeCodeDataUnSolved.isButtonList}
                                    isChecked={isFilterEventTypeCodeDataUnSolved.isChecked}
                                    isOpen={isFilterEventTypeCodeDataUnSolved.isOpen}
                                    onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterEventTypeCodeList', boolean)}
                                    onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterEventTypeCodeDataUnSolved, object)}
                                >
                                    <FilterItemSelect
                                        defSelectVal={isFilterEventTypeCodeDataUnSolved.isDataList}
                                        defSelectList={isFilterEventTypeCodeList}
                                        onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterEventTypeCodeList', list)}
                                        onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterEventTypeCodeDataUnSolved, object)}
                                    />
                                </FilterItemHeader>
                            </li>
                        )
                    }
                    {
                        this.props.company === "1" && (
                            <li>{/* 公司別 */}
                                {
                                    <FilterItemHeader
                                        title={"1064"}
                                        total={isFilterCompanyDataUnSolved.isDataList.length}
                                        detailList={isFilterCompanyDataUnSolved.isButtonList}
                                        isChecked={isFilterCompanyDataUnSolved.isChecked}
                                        isOpen={isFilterCompanyDataUnSolved.isOpen}
                                        onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterCompanyList', boolean)}
                                        onUpdateChainSelectList={() => this.onUpdateChainSelectList(FilterNames_API.isFilterCompanyVirtue, this.getSelectObj(isFilterCompanyList))}
                                        onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCompanyDataUnSolved, object)}
                                    >
                                        <FilterItemSelect
                                            defSelectVal={isFilterCompanyDataUnSolved.isDataList}
                                            defSelectList={isFilterCompanyList}
                                            onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterCompanyList', list)}
                                            onUpdateChainSelectList={(object) => this.onUpdateChainSelectList(FilterNames_API.isFilterCompanyVirtue, object)}
                                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCompanyDataUnSolved, object)}
                                        />
                                    </FilterItemHeader>
                                }
                            </li>
                        )
                    }

                    <li>{/* 國家 */}
                        <FilterItemHeader
                            title={"1028"}
                            total={isFilterCountryDataUnSolved.isDataList.length}
                            detailList={isFilterCountryDataUnSolved.isButtonList}
                            isChecked={isFilterCountryDataUnSolved.isChecked}
                            isOpen={isFilterCountryDataUnSolved.isOpen}
                            onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterCountryList', boolean)}
                            onUpdateChainSelectList={() => this.onUpdateChainSelectList(FilterNames_API.isFilterCountryVirtue, this.getSelectObj(isFilterCountryList))}
                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCountryDataUnSolved, object)}
                        >
                            <FilterItemSelect
                                defSelectVal={isFilterCountryDataUnSolved.isDataList}
                                defSelectList={isFilterCountryList}
                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterCountryList', list)}
                                onUpdateChainSelectList={(object) => this.onUpdateChainSelectList(FilterNames_API.isFilterCountryVirtue, object)}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCountryDataUnSolved, object)}
                            />
                        </FilterItemHeader>
                    </li>
                    <li>{/* 地域 */}
                        <FilterItemHeader
                            title={"1029"}
                            total={isFilterAreaDataUnSolved.isDataList.length}
                            detailList={isFilterAreaDataUnSolved.isButtonList}
                            isChecked={isFilterAreaDataUnSolved.isChecked}
                            isOpen={isFilterAreaDataUnSolved.isOpen}
                            onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterAreaList', boolean)}
                            onUpdateChainSelectList={() => this.onUpdateChainSelectList(FilterNames_API.isFilterAreaVirtue, this.getSelectObj(isFilterAreaList))}
                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterAreaDataUnSolved, object)}
                        >
                            <FilterItemSelect
                                defSelectVal={isFilterAreaDataUnSolved.isDataList}
                                defSelectList={isFilterAreaList}
                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterAreaList', list)}
                                onUpdateChainSelectList={(object) => this.onUpdateChainSelectList(FilterNames_API.isFilterAreaVirtue, object)}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterAreaDataUnSolved, object)}
                            />
                        </FilterItemHeader>
                    </li>
                    
                    <li>{/* 站台編號/名稱 */}
                        <FilterItemHeader
                            title={"1125"}
                            total={isFilterGroupIdDataUnSolved.isDataList.length}
                            detailList={isFilterGroupIdDataUnSolved.isButtonList}
                            isChecked={isFilterGroupIdDataUnSolved.isChecked}
                            isOpen={isFilterGroupIdDataUnSolved.isOpen}
                            onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterGroupIdList', boolean)}
                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterGroupIdDataUnSolved, object)}
                        >
                            <FilterItemSelect
                                defSelectVal={isFilterGroupIdDataUnSolved.isDataList}
                                defSelectList={isFilterGroupIdList}
                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterGroupIdList', list)}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterGroupIdDataUnSolved, object)}
                            />
                        </FilterItemHeader>
                    </li>
                    {/* 數據時間 電池歷史頁面(預設前24小時資訊) */}
                    <li>
                        <FilterItemHeader
                            title={'1036'}
                            total={1}
                            isChecked={false}
                            showCheckBox={false}
                            isOpen={true}
                        >
                            <FilterItemRadio
                                defRadioRangeMaxDay={this.state.defRadioRangeMaxDay}
                                defRadioRangeMinDay={this.state.defRadioRangeMinDay}
                                defRadioVal={isFilterRecordTimeUnSolved.Radio}
                                defRadioStart={isFilterRecordTimeUnSolved.Start}
                                defRadioEnd={isFilterRecordTimeUnSolved.End}
                                defRadioList={isFilterRecordTimeList}
                                defRadioOpenStartTime={true}
                                defRadioStartHH={isFilterRecordTimeUnSolved.StartHH}
                                defRadioStartMM={isFilterRecordTimeUnSolved.StartMM}
                                defRadioEndHH={isFilterRecordTimeUnSolved.EndHH}
                                defRadioEndMM={isFilterRecordTimeUnSolved.EndMM}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterRecordTimeUnSolved, object)}
                            />
                        </FilterItemHeader>
                    </li>

                    <Divider />
                    <div className="d-block py-3 px-4">
                        <CusMainBtnStyle
                            name={<Trans i18nKey={"1002"} />}
                            icon={"fas fa-check mr-2"}
                            fullWidth={true}
                            clickEvent={() => this.handleSumbit()}
                        />
                    </div>
                </ul>
            </Fragment>
        )
    }
    // 檢查預設篩選條件總數
    checkedIsFilterTotal = () => {return 0}
    // 書籤
    onClickBookmark = (FilterConfig) => {
        const {
            Alert: isFilterEventTypeCodeDataUnSolved,
            Company: isFilterCompanyDataUnSolved,
            Country: isFilterCountryDataUnSolved,
            Area: isFilterAreaDataUnSolved,
            GroupID: isFilterGroupIdDataUnSolved,
            RecTime: isFilterRecordTimeUnSolved,
        } = FilterConfig;
        const postData = {
            isFilterEventTypeCodeDataUnSolved: checkObject(isFilterEventTypeCodeDataUnSolved),
            isFilterCompanyDataUnSolved: checkObject(isFilterCompanyDataUnSolved),
            isFilterCountryDataUnSolved: checkObject(isFilterCountryDataUnSolved),
            isFilterAreaDataUnSolved: checkObject(isFilterAreaDataUnSolved),
            isFilterGroupIdDataUnSolved: checkObject(isFilterGroupIdDataUnSolved),
            isFilterRecordTimeUnSolved: isFilterRecordTimeUnSolved,
        };

        this.props.updateFilterData(FilterNames.isFilterEventTypeCodeDataUnSolved, checkObject(isFilterEventTypeCodeDataUnSolved));
        this.props.updateFilterData(FilterNames.isFilterCompanyDataUnSolved, checkObject(isFilterCompanyDataUnSolved));
        this.props.updateFilterData(FilterNames.isFilterCountryDataUnSolved, checkObject(isFilterCountryDataUnSolved));
        this.props.updateFilterData(FilterNames.isFilterAreaDataUnSolved, checkObject(isFilterAreaDataUnSolved));
        this.props.updateFilterData(FilterNames.isFilterGroupIdDataUnSolved, checkObject(isFilterGroupIdDataUnSolved));
        this.props.updateFilterData(FilterNames.isFilterRecordTimeUnSolved, isFilterRecordTimeUnSolved);
        this.props.submitFilterData(postData);
        this.setState({ ...postData });
    }
    //checkboxAll更改選單顯示
    onClickAllSelectList = (name, boolean) => {
        const newList = this.state[name].map(item => {
            return { ...item, selectShow: boolean }
        });
        this.setState({ [name]: [...newList] });
    }
    //更新選單list
    onUpdateSelectList = (name, list) => {
        this.setState({ [name]: [...list] });
    }
    //更新選單data
    onUpdateList = (name, object) => {
		console.log('%c UpdateList','background:yellow',name,object)
        this.setState({ [name]: { ...object } });
    }
    // 送出篩選條件
    handleSumbit = async () => {
        const { isFilterEventTypeCodeDataUnSolved, isFilterCompanyDataUnSolved, isFilterCountryDataUnSolved, isFilterAreaDataUnSolved, isFilterGroupIdDataUnSolved, isFilterGroupNameDataUnSolved,
            isFilterBatteryGroupIdDataUnSolved, isFilterBatteryTypeDataUnSolved, isFilterRecordTimeUnSolved,
            ...other } = this.state;
        await this.props.setBattInternalIDAlert('');
        await this.props.resetBatteryGroupIdUnSolved()
        await this.props.updateFilterData(FilterNames.isFilterEventTypeCodeDataUnSolved, checkObject(isFilterEventTypeCodeDataUnSolved));
        await this.props.updateFilterData(FilterNames.isFilterCompanyDataUnSolved, checkObject(isFilterCompanyDataUnSolved));
        await this.props.updateFilterData(FilterNames.isFilterCountryDataUnSolved, checkObject(isFilterCountryDataUnSolved));
        await this.props.updateFilterData(FilterNames.isFilterAreaDataUnSolved, checkObject(isFilterAreaDataUnSolved));
        await this.props.updateFilterData(FilterNames.isFilterGroupIdDataUnSolved, checkObject(isFilterGroupIdDataUnSolved));
        await this.props.updateFilterData(FilterNames.isFilterBatteryGroupIdDataUnSolved, checkObject(isFilterBatteryGroupIdDataUnSolved));
        await this.props.updateFilterData(FilterNames.isFilterRecordTimeUnSolved, isFilterRecordTimeUnSolved);
        await this.props.submitFilterData(this.state);
        this.setState({
            isFilterEventTypeCodeDataUnSolved: checkObject(isFilterEventTypeCodeDataUnSolved),
            isFilterCompanyDataUnSolved: checkObject(isFilterCompanyDataUnSolved),
            isFilterCountryDataUnSolved: checkObject(isFilterCountryDataUnSolved),
            isFilterAreaDataUnSolved: checkObject(isFilterAreaDataUnSolved),
            isFilterGroupIdDataUnSolved: checkObject(isFilterGroupIdDataUnSolved),
            isFilterRecordTimeUnSolved: isFilterRecordTimeUnSolved,
            ...other
        });

    }

    //電池數據篩選清單API:
    getBatteryFilter = (defaultEventTypeCode, defaultCompany, defaultCountry, defaultArea, defaultGroupId,
    ) => {
        this.ajaxGetAlertFilter().then((response) => {
            if (this.ajaxCancel) { new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { List, Alert } = msg;
                let AlertArr = [];
                let CompanyObj = {};
                let CountryObj = {};
                let AreaObj = {};
                let GroupIdObj = {};
                List.forEach(item => {
                    if (item.CompanyValue !== "") { CompanyObj[item.CompanyValue] = { Value: item.CompanyValue, Label: item.CompanyLabel, selectShow: true }; }
                    if (item.CountryValue !== "") { CountryObj[item.CountryValue] = { Value: item.CountryValue, Label: item.CountryLabel, selectShow: true }; }
                    if (item.AreaValue !== "") { AreaObj[item.AreaValue] = { Value: item.AreaValue, Label: item.AreaLabel, selectShow: true }; }
                    if (item.GroupValue !== "") { GroupIdObj[item.GroupValue] = { Value: item.GroupValue, Label: item.GroupLabel, selectShow: true }; }
                });
                Alert.forEach((item, idx) => {
                    AlertArr.push({ Value: item.Value, Label: item.Label, I18NCode:item.I18NCode, selectShow: true })
                });
                const CompanyList = Object.values(CompanyObj).sort(function (a, b) {
                    return (a.Label).localeCompare(b.Label, "zh-hant");
                });
                const CountryList = Object.values(CountryObj).sort(function (a, b) {
                    return (a.Label).localeCompare(b.Label, "zh-hant");
                });
                const AreaList = Object.values(AreaObj).sort(function (a, b) {
                    return (a.Label).localeCompare(b.Label, "zh-hant");
                });
                const GroupList = Object.values(GroupIdObj).sort(function (a, b) {
                    return (a.Label).localeCompare(b.Label, "zh-hant");
                });
                this.setState({
                    isFilterSelectTable: [...List],
                    isFilterEventTypeCodeList: [...AlertArr], //...Alert
                    isFilterEventTypeCodeDataUnSolved: {
                        ...defaultEventTypeCode
                    },
                    isFilterCompanyList: [...CompanyList],
                    isFilterCompanyDataUnSolved: {
                        ...defaultCompany
                    },
                    isFilterCountryList: [...CountryList],
                    isFilterCountryDataUnSolved: {
                        ...defaultCountry
                    },
                    isFilterAreaList: [...AreaList],
                    isFilterAreaDataUnSolved: {
                        ...defaultArea
                    },
                    isFilterGroupIdList: [...GroupList],
                    isFilterGroupIdDataUnSolved: {
                        ...defaultGroupId
                    },
                    isFilterRecordTimeUnSolved: this.props.isFilterRecordTimeUnSolved,
                });
                return new Promise((resolve, reject) => (resolve('')));
            } else {
                this.setState({
                    isFilterEventTypeCodeList: [],//預設告警類型
                    isFilterCompanyList: [],//預設公司別清單
                    isFilterCountryList: [],//預設國家清單
                    isFilterAreaList: [],//預設地域清單
                    isFilterGroupIdList: [],//預設站台編號清單
                    isFilterEventTypeCodeDataUnSolved: { ...initFilterSelectData },
                    isFilterCompanyDataUnSolved: { ...initFilterSelectData },
                    isFilterCountryDataUnSolved: { ...initFilterSelectData },
                    isFilterAreaDataUnSolved: { ...initFilterSelectData },
                    isFilterGroupIdDataUnSolved: { ...initFilterSelectData },
                    isFilterRecordTimeUnSolved: { ...initFilterDate },
                });
                return new Promise((resolve, reject) => (reject('')));
            }
        }).then(() => {            
            //處理已選擇項目的選單   
            if (defaultCompany.isDataList.length > 0) {
                let obj = {};
                defaultCompany.isButtonList.forEach(item => obj[item.Value] = item);
                this.onUpdateChainSelectList(FilterNames_API.isFilterCompanyVirtue, obj);
            }
            if (defaultCountry.isDataList.length > 0) {
                let obj = {};
                defaultCountry.isButtonList.forEach(item => obj[item.Value] = item);
                this.onUpdateChainSelectList(FilterNames_API.isFilterCountryVirtue, obj);
            }
            if (defaultArea.isDataList.length > 0) {
                let obj = {};
                defaultArea.isButtonList.forEach(item => obj[item.Value] = item);
                this.onUpdateChainSelectList(FilterNames_API.isFilterAreaVirtue, obj);
            }
            this.setState({
                isFilterCompanyDataUnSolved: { ...defaultCompany },
                isFilterCountryDataUnSolved: { ...defaultCountry },
                isFilterAreaDataUnSolved: { ...defaultArea },
                isFilterGroupIdDataUnSolved: { ...defaultGroupId },
            });
        });
    }

    getSelectObj = (selectList) => {
        let isSelectObject = {};
        selectList.forEach(item => {
            isSelectObject[item.Value] = item;
        });
        return isSelectObject
    }
    //選擇公司別，更新其他連動選單
    onUpdateChainSelectList = (Virtue, object) => {
        const { isFilterSelectTable } = this.state;
        const Lists = isFilterSelectTable.filter(item => {
            if (object[item[Virtue]]) {
                return true;
            } else {
                return false;
            }
        });
        let List = [];
        switch (Virtue) {
            case FilterNames_API.isFilterCompanyVirtue:
                List = [...Lists];
                break;
            case FilterNames_API.isFilterCountryVirtue:
                //過濾公司別    
                if (this.state.isFilterCompanyDataUnSolved.isDataList.length > 0) {
                    List = Lists.filter(item => (this.state.isFilterCompanyDataUnSolved.isDataList.findIndex((element) => element === item['CompanyValue']) >= 0 ? true : false));
                } else {
                    List = [...Lists];
                }
                break;
            case FilterNames_API.isFilterAreaVirtue:
                let List1 = [];
                //過濾公司別   
                if (this.state.isFilterCompanyDataUnSolved.isDataList.length > 0) {
                    List1 = Lists.filter(item => (this.state.isFilterCompanyDataUnSolved.isDataList.findIndex((element) => element === item['CompanyValue']) >= 0 ? true : false));
                } else {
                    List1 = [...Lists];
                }
                //過濾國家           
                if (this.state.isFilterCountryDataUnSolved.isDataList.length > 0) {
                    List = List1.filter(item => (this.state.isFilterCountryDataUnSolved.isDataList.findIndex((element) => element === item['CountryValue']) >= 0 ? true : false));
                } else {
                    List = [...List1];
                }
                break;
            default:
        }
        let CountryObj = {};
        let AreaObj = {};
        let GroupObj = {};
        List.forEach(item => {
            if (item.CountryValue !== "") { CountryObj[item.CountryValue] = { Value: item.CountryValue, Label: item.CountryLabel, selectShow: true }; }
            if (item.AreaValue !== "") { AreaObj[item.AreaValue] = { Value: item.AreaValue, Label: item.AreaLabel, selectShow: true }; }
            if (item.GroupValue !== "") { GroupObj[item.GroupValue] = { Value: item.GroupValue, Label: item.GroupLabel, selectShow: true }; }
        });
        const CountryList = Object.values(CountryObj).sort(function (a, b) {
            return (a.Label).localeCompare(b.Label, "zh-hant");
        });
        const AreaList = Object.values(AreaObj).sort(function (a, b) {
            return (a.Label).localeCompare(b.Label, "zh-hant");
        });
        const GroupList = Object.values(GroupObj).sort(function (a, b) {
            return (a.Label).localeCompare(b.Label, "zh-hant");
        });
        switch (Virtue) {
            case FilterNames_API.isFilterCompanyVirtue:
                this.setState({
                    isFilterCountryList: [...CountryList],
                    isFilterCountryDataUnSolved: { ...initFilterSelectData },
                    isFilterAreaList: [...AreaList],
                    isFilterAreaDataUnSolved: { ...initFilterSelectData },
                    isFilterGroupIdList: [...GroupList],
                    isFilterGroupIdDataUnSolved: { ...initFilterSelectData },
                });
                break;
            case FilterNames_API.isFilterCountryVirtue:
                this.setState({
                    isFilterAreaList: [...AreaList],
                    isFilterAreaDataUnSolved: { ...initFilterSelectData },
                    isFilterGroupIdList: [...GroupList],
                    isFilterGroupIdDataUnSolved: { ...initFilterSelectData },
                });
                break;
            case FilterNames_API.isFilterAreaVirtue:
                this.setState({
                    isFilterGroupIdList: [...GroupList],
                    isFilterGroupIdDataUnSolved: { ...initFilterSelectData },
                });
                break;
            default:
        }
    }

    //取得篩選儲存清單API(GET)
    getFilter = () => {
        this.ajaxGetFilter().then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { Filter } = msg;
                this.setState({
                    isFilterSaveList: [...Filter],
                });
            } else {
                this.setState({
                    isFilterSaveList: [],
                });
            }
        });
    }
    //刪除篩選API(GET)
    delFilter = (FilterID) => {
        this.ajaxDelFilter({ FilterID }).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.props.alertFilterMsgOpen(msg);
            } else if (code === '07') {
                this.props.alertFilterMsgOpen(msg);
            } else {
                console.error('告警未解決-篩選儲存清單', response);
                this.props.alertFilterMsgOpen(msg);
            }
        });
    }

    //取得電池數據篩選清單API(GET):
    ajaxGetAlertFilter = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getAlertFilter?eventStatus=5`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //取得參數設定篩選清單API(GET):
        //https://www.gtething.tw/battery/getAlertFilter
    }
    //取得篩選儲存清單API(GET)
    ajaxGetFilter = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const { account, functionId } = this.props;
        const url = `${apiUrl}getFilter?account=${account}&functionId=${functionId}`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //取得篩選儲存清單API(GET)
        //https://www.gtething.tw/battery/getFilter?account=admin&functionId=1400_1
    }
    //刪除篩選儲存清單API(POST):
    ajaxDelFilter = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}delFilter`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //刪除篩選儲存清單API(POST):
        //https://www.gtething.tw/battery/delFilter
    }
}

AlertUnSolvedFilter.defaultProps = {
    functionId: '0',
    isFilterEventTypeCodeDataUnSolved: {},
    isFilterCompanyDataUnSolved: {},
    isFilterCountryDataUnSolved: {},
    isFilterAreaDataUnSolved: {},
    isFilterGroupIdDataUnSolved: {},
    isFilterRecordTimeUnSolved: {},
    updateFilterData: () => { },
    submitFilterData: () => { },
    alertFilterMsgOpen: () => { },
    resetBatteryGroupIdUnSolved: () => {},
}
AlertUnSolvedFilter.propTypes = {
    functionId: PropTypes.string,
    isFilterEventTypeCodeDataUnSolved: PropTypes.object,
    isFilterCompanyDataUnSolved: PropTypes.object,
    isFilterCountryDataUnSolved: PropTypes.object,
    isFilterAreaDataUnSolved: PropTypes.object,
    isFilterGroupIdDataUnSolved: PropTypes.object,
    isFilterRecordTimeUnSolved: PropTypes.object,
    updateFilterData: PropTypes.func,
    submitFilterData: PropTypes.func,
    alertFilterMsgOpen: PropTypes.func,
    resetBatteryGroupIdUnSolved: PropTypes.func,
}
const mapStateToProps = (state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        account: state.LoginReducer.account,
        company: state.LoginReducer.company,
        curLanguage: state.LoginReducer.curLanguage,
        timeZone: state.LoginReducer.timeZone,
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        setBattInternalIDAlert: (value) => dispatch(setBattInternalIDAlert(value)),
        resetBatteryGroupIdUnSolved: () => dispatch(resetBatteryGroupIdUnSolved()),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(AlertUnSolvedFilter);


