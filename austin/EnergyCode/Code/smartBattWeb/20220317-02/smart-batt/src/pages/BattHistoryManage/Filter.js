import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';
import Divider from '@material-ui/core/Divider';
import { apipath, ajax } from '../../utils/ajax';
import { CusMainBtnStyle } from '../../components/CusMainBtnStyle';
import FilterSave from '../../components/BattFilter/FilterSave';//儲存標籤紀錄
import FilterItemHeader from '../../components/FilterDrawer/FilterItemHeader';//每筆Item的header
import { checkObject } from '../../components/FilterDrawer/utils';//整理checkbox狀態
import FilterItemSelect from '../../components/FilterDrawer/FilterItemSelect';//每筆Item的Select
import FilterItemRadio from '../../components/FilterDrawer/FilterItemRadio';//每筆Item的Radio
import { FilterNames, FilterNames_API, initFilterSelectData, initFilterDate } from '../../components/BattFilter/InitDataFormat';//初始化格式

const apiUrl = apipath();
class Filter extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {
            isFilterCompanyList: [],//預設公司別清單
            isFilterCountryList: [],//預設國家清單
            isFilterAreaList: [],//預設地域清單
            isFilterGroupIdList: [],//預設站台編號清單
            isFilterBatteryGroupIdList: [],//預設電池組ID清單
            isFilterBatteryStatusList: [],//預設電池狀態清單
            isFilterRecordTimeList: [//預設數據時間
                { Value: "0", Label: "1098", Type: "String", show: true },
                { Value: "5", Label: "1111", Type: "String", show: true },
                { Value: "3", Label: "1109", Type: "String", show: true },
                { Value: "1", Label: "", Type: "Date", show: true },
                { Value: "2", Label: "1083", Type: "String", show: false },
                { Value: "4", Label: "1110", Type: "String", show: false },
            ],
            defRadioRangeMaxDay: '31',//往未來多少天
            defRadioRangeMinDay: '31',//往從前多少天
            isFilterCompanyData: {//預設公司別
                ...initFilterSelectData
            },
            isFilterCountryData: {//國家
                ...initFilterSelectData
            },
            isFilterAreaData: {//預設地域
                ...initFilterSelectData
            },
            isFilterGroupIdData: {//預設站台編號
                ...initFilterSelectData
            },
            isFilterBatteryGroupIdData: {//預設電池組ID
                ...initFilterSelectData
            },
            isFilterBatteryStatusData: {//預設電池狀態
                ...initFilterSelectData
            },
            isFilterRecordTimeData: this.props.isFilterRecordTimeData,//預設數據時間
            isFilterTotal: this.checkedIsFilterTotal(),//總數
        }
    }
    componentDidMount() {
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData, 
            isFilterBatteryGroupIdData, isFilterRecordTimeData,
        } = this.props;
        this.getBatteryFilter(isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterBatteryGroupIdData,
        );//取選單資料
        this.getFilter();//篩選儲存清單API(GET)
        this.setState({
            isFilterRecordTimeData: isFilterRecordTimeData
        });
    }
    componentDidUpdate(prevProps, prevState) {
        if (//更新全部的數字
            prevState.isFilterCompanyData.isDataList.length !== this.state.isFilterCompanyData.isDataList.length ||
            prevState.isFilterCountryData.isDataList.length !== this.state.isFilterCountryData.isDataList.length ||
            prevState.isFilterAreaData.isDataList.length !== this.state.isFilterAreaData.isDataList.length ||
            prevState.isFilterGroupIdData.isDataList.length !== this.state.isFilterGroupIdData.isDataList.length ||
            prevState.isFilterBatteryGroupIdData.isDataList.length !== this.state.isFilterBatteryGroupIdData.isDataList.length
        ) {
            const list = [
                this.state.isFilterCompanyData.isDataList.length,
                this.state.isFilterCountryData.isDataList.length,
                this.state.isFilterAreaData.isDataList.length,
                this.state.isFilterGroupIdData.isDataList.length,
                this.state.isFilterBatteryGroupIdData.isDataList.length,
            ];
            let total = 1;
            list.forEach(num => {
                total = total + num
            });
            this.setState({
                isFilterTotal: total
            })
        }
        if (prevState.isFilterCompanyData.isChecked !== this.state.isFilterCompanyData.isChecked) {
            this.setState({
                isFilterCompanyData: this.state.isFilterCompanyData
            })
        }
        if (prevState.isFilterCountryData.isChecked !== this.state.isFilterCountryData.isChecked) {
            this.setState({
                isFilterCountryData: this.state.isFilterCountryData
            })
        }
        if (prevState.isFilterAreaData.isChecked !== this.state.isFilterAreaData.isChecked) {
            this.setState({
                isFilterAreaData: this.state.isFilterAreaData
            })
        }
        if (prevState.isFilterGroupIdData.isChecked !== this.state.isFilterGroupIdData.isChecked) {
            this.setState({
                isFilterGroupIdData: this.state.isFilterGroupIdData
            })
        }
        if (prevState.isFilterBatteryGroupIdData.isChecked !== this.state.isFilterBatteryGroupIdData.isChecked) {
            this.setState({
                isFilterBatteryGroupIdData: this.state.isFilterBatteryGroupIdData
            })
        }
        // 數據時間
        if (prevState.isFilterRecordTimeData !== this.state.isFilterRecordTimeData) {
            this.setState({
                isFilterRecordTimeData: this.state.isFilterRecordTimeData
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
        const { isFilterCompanyList, isFilterCompanyData } = this.state;//公司
        const { isFilterCountryList, isFilterCountryData } = this.state;//國家
        const { isFilterAreaList, isFilterAreaData } = this.state;//地域
        const { isFilterGroupIdList, isFilterGroupIdData } = this.state;//站台編號
        const { isFilterBatteryGroupIdList, isFilterBatteryGroupIdData } = this.state;//電池組ID
        const { isFilterRecordTimeList, isFilterRecordTimeData } = this.state;//數據時間
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
                    functionId={this.props.functionId} // 1400_1, 1400_2, 1600_1, 1600_2
                    company={this.props.company}
                    list={isFilterSaveList}
                    onClickBookmark={(FilterConfig) => { this.onClickBookmark(FilterConfig) }}
                    onClickTrash={(FilterID) => { this.delFilter(FilterID) }}
                />
                <Divider />
                {/* 篩選器 */}
                <ul className={`filter overflowY mb-0 ${navigator.userAgent.indexOf('Windows') > 0 ? 'Windows' : 'Mac'}`}>
                    {
                        this.props.company === "1" && (
                            <li className={functionId === '1600_2' ? 'p-0' : ''} >{/* 公司別 */}
                                {
                                    functionId !== '1600_2' && (
                                        <FilterItemHeader
                                            title={"1064"}
                                            total={isFilterCompanyData.isDataList.length}
                                            detailList={isFilterCompanyData.isButtonList}
                                            isChecked={isFilterCompanyData.isChecked}
                                            isOpen={isFilterCompanyData.isOpen}
                                            onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterCompanyList', boolean)}
                                            onUpdateChainSelectList={() => this.onUpdateChainSelectList(FilterNames_API.isFilterCompanyVirtue, this.getSelectObj(isFilterCompanyList))}
                                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCompanyData, object)}
                                        >
                                            <FilterItemSelect
                                                defSelectVal={isFilterCompanyData.isDataList}
                                                defSelectList={isFilterCompanyList}
                                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterCompanyList', list)}
                                                onUpdateChainSelectList={(object) => this.onUpdateChainSelectList(FilterNames_API.isFilterCompanyVirtue, object)}
                                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCompanyData, object)}
                                            />
                                        </FilterItemHeader>
                                    )
                                }
                            </li>
                        )
                    }

                    {
                        functionId !== '1600_2' && (
                            <li>{/* 國家 */}
                                <FilterItemHeader
                                    title={"1028"}
                                    total={isFilterCountryData.isDataList.length}
                                    detailList={isFilterCountryData.isButtonList}
                                    isChecked={isFilterCountryData.isChecked}
                                    isOpen={isFilterCountryData.isOpen}
                                    onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterCountryList', boolean)}
                                    onUpdateChainSelectList={() => this.onUpdateChainSelectList(FilterNames_API.isFilterCountryVirtue, this.getSelectObj(isFilterCountryList))}
                                    onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCountryData, object)}
                                >
                                    <FilterItemSelect
                                        defSelectVal={isFilterCountryData.isDataList}
                                        defSelectList={isFilterCountryList}
                                        onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterCountryList', list)}
                                        onUpdateChainSelectList={(object) => this.onUpdateChainSelectList(FilterNames_API.isFilterCountryVirtue, object)}
                                        onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterCountryData, object)}
                                    />
                                </FilterItemHeader>
                            </li>
                        )
                    }
                    {
                        functionId !== '1600_2' && (
                            <li>{/* 地域 */}
                                <FilterItemHeader
                                    title={"1029"}
                                    total={isFilterAreaData.isDataList.length}
                                    detailList={isFilterAreaData.isButtonList}
                                    isChecked={isFilterAreaData.isChecked}
                                    isOpen={isFilterAreaData.isOpen}
                                    onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterAreaList', boolean)}
                                    onUpdateChainSelectList={() => this.onUpdateChainSelectList(FilterNames_API.isFilterAreaVirtue, this.getSelectObj(isFilterAreaList))}
                                    onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterAreaData, object)}
                                >
                                    <FilterItemSelect
                                        defSelectVal={isFilterAreaData.isDataList}
                                        defSelectList={isFilterAreaList}
                                        onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterAreaList', list)}
                                        onUpdateChainSelectList={(object) => this.onUpdateChainSelectList(FilterNames_API.isFilterAreaVirtue, object)}
                                        onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterAreaData, object)}
                                    />
                                </FilterItemHeader>
                            </li>
                        )
                    }
                    {
                        functionId !== '1600_2' && (
                            <li>{/* 站台編號/名稱 */}
                                <FilterItemHeader
                                    title={"1125"}
                                    total={isFilterGroupIdData.isDataList.length}
                                    detailList={isFilterGroupIdData.isButtonList}
                                    isChecked={isFilterGroupIdData.isChecked}
                                    isOpen={isFilterGroupIdData.isOpen}
                                    onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterGroupIdList', boolean)}
                                    onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterGroupIdData, object)}
                                >
                                    <FilterItemSelect
                                        defSelectVal={isFilterGroupIdData.isDataList}
                                        defSelectList={isFilterGroupIdList}
                                        onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterGroupIdList', list)}
                                        onUpdateChainSelectList={(object) => this.onUpdateChainSelectList(FilterNames_API.isFilterGroupIdVirtue, object)}
                                        onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterGroupIdData, object)}
                                    />
                                </FilterItemHeader>
                            </li>
                        )
                    }

                    {/* 電池組ID 僅電池歷史顯示 */}
                    <li>
                        <FilterItemHeader
                            title={"1026"}
                            total={isFilterBatteryGroupIdData.isDataList.length}
                            detailList={isFilterBatteryGroupIdData.isButtonList}
                            isChecked={false}
                            showCheckBox={false}
                            isOpen={true}
                            onClickAllSelectList={(boolean) => this.onClickAllSelectList('isFilterBatteryGroupIdList', boolean)}
                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterBatteryGroupIdData, object)}
                        >
                            <FilterItemSelect
                                defSelectVal={isFilterBatteryGroupIdData.isDataList}
                                defSelectList={isFilterBatteryGroupIdList}
                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterBatteryGroupIdList', list)}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterBatteryGroupIdData, object)}
                            />
                        </FilterItemHeader>
                    </li>
                    {// 電池組ID 大於10筆,小於0筆訊息
                        (isFilterBatteryGroupIdData.isDataList !== [] && (isFilterBatteryGroupIdData.isDataList.length <= 0 || isFilterBatteryGroupIdData.isDataList.length > 10) )
                        && <div className="pl-3 pr-3 pt-0 pb-2 ">
                            <Trans i18nKey={'5058'} />
                            <span className="text-primary pl-1 pr-1">{isFilterBatteryGroupIdData.isDataList.length}</span>
                            <Trans i18nKey={'5059'} />
                        </div>
                    }
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
                                defRadioVal={isFilterRecordTimeData.Radio}
                                defRadioStart={isFilterRecordTimeData.Start}
                                defRadioEnd={isFilterRecordTimeData.End}
                                defRadioRangeMaxDay={this.state.defRadioRangeMaxDay}
                                defRadioRangeMinDay={this.state.defRadioRangeMinDay}
                                defRadioList={isFilterRecordTimeList}
                                defRadioOpenStartTime={true}
                                defRadioStartHH={isFilterRecordTimeData.StartHH}
                                defRadioStartMM={isFilterRecordTimeData.StartMM}
                                defRadioEndHH={isFilterRecordTimeData.EndHH}
                                defRadioEndMM={isFilterRecordTimeData.EndMM}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterRecordTimeData, object)}
                            />
                        </FilterItemHeader>
                    </li>

                    <Divider />
                    <div className="d-block py-3 px-4">
                        {
                            (isFilterBatteryGroupIdData.isDataList !== [] && (isFilterBatteryGroupIdData.isDataList.length <= 0 || isFilterBatteryGroupIdData.isDataList.length > 10) )
                                ? <div className="btn btn-secondary disabled w-100"><Trans i18nKey={"1002"} /></div>
                                : (
                                    <CusMainBtnStyle
                                        name={<Trans i18nKey={"1002"} />}
                                        icon={"fas fa-check mr-2"}
                                        fullWidth={true}
                                        clickEvent={() => this.handleSumbit()}
                                    />
                                )
                        }

                    </div>
                </ul>
            </Fragment>
        )
    }
    // 檢查預設篩選條件總數
    checkedIsFilterTotal = () => { return 1 }
    // 書籤
    onClickBookmark = (FilterConfig) => {
        const {
            Company: isFilterCompanyData,
            Country: isFilterCountryData,
            Area: isFilterAreaData,
            GroupID: isFilterGroupIdData,
            BatteryGroupId: isFilterBatteryGroupIdData,
            Status: isFilterBatteryStatusData,
            RecTime: isFilterRecordTimeData,
        } = FilterConfig;
        const postData = {
            isFilterCompanyData: checkObject(isFilterCompanyData),
            isFilterCountryData: checkObject(isFilterCountryData),
            isFilterAreaData: checkObject(isFilterAreaData),
            isFilterGroupIdData: checkObject(isFilterGroupIdData),
            isFilterBatteryGroupIdData: checkObject(isFilterBatteryGroupIdData),
            isFilterBatteryStatusData: checkObject(isFilterBatteryStatusData),
            isFilterRecordTimeData: isFilterRecordTimeData,
        };

        this.props.updateFilterData(FilterNames.isFilterCompanyData, checkObject(isFilterCompanyData));
        this.props.updateFilterData(FilterNames.isFilterCountryData, checkObject(isFilterCountryData));
        this.props.updateFilterData(FilterNames.isFilterAreaData, checkObject(isFilterAreaData));
        this.props.updateFilterData(FilterNames.isFilterGroupIdData, checkObject(isFilterGroupIdData));
        this.props.updateFilterData(FilterNames.isFilterBatteryGroupIdData, checkObject(isFilterBatteryGroupIdData));
        this.props.updateFilterData(FilterNames.isFilterBatteryStatusData, checkObject(isFilterBatteryStatusData));
        this.props.updateFilterData(FilterNames.isFilterRecordTimeData, isFilterRecordTimeData);
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
        this.setState({ [name]: { ...object } });
    }
    // 送出篩選條件
    handleSumbit = () => {
        const { isFilterCompanyData, isFilterCountryData, isFilterAreaData, isFilterGroupIdData,
            isFilterBatteryGroupIdData, isFilterBatteryStatusData, isFilterRecordTimeData,
            ...other } = this.state;
        this.props.updateFilterData(FilterNames.isFilterCompanyData, checkObject(isFilterCompanyData));
        this.props.updateFilterData(FilterNames.isFilterCountryData, checkObject(isFilterCountryData));
        this.props.updateFilterData(FilterNames.isFilterAreaData, checkObject(isFilterAreaData));
        this.props.updateFilterData(FilterNames.isFilterGroupIdData, checkObject(isFilterGroupIdData));
        this.props.updateFilterData(FilterNames.isFilterBatteryGroupIdData, checkObject(isFilterBatteryGroupIdData));
        this.props.updateFilterData(FilterNames.isFilterBatteryStatusData, checkObject(isFilterBatteryStatusData));
        this.props.updateFilterData(FilterNames.isFilterRecordTimeData, isFilterRecordTimeData);
        this.props.submitFilterData(this.state);
        this.setState({
            isFilterCompanyData: checkObject(isFilterCompanyData),
            isFilterCountryData: checkObject(isFilterCountryData),
            isFilterAreaData: checkObject(isFilterAreaData),
            isFilterGroupIdData: checkObject(isFilterGroupIdData),
            isFilterBatteryGroupIdData: checkObject(isFilterBatteryGroupIdData),
            isFilterBatteryStatusData: checkObject(isFilterBatteryStatusData),
            isFilterRecordTimeData: isFilterRecordTimeData,
            ...other
        });

    }

    //電池數據篩選清單API:
    getBatteryFilter = (defaultCompany, defaultCountry, defaultArea, defaultGroupId,
        defaultBatteryGroupId, defaultBatteryStatus,
    ) => {
        this.ajaxGetBatteryFilter().then((response) => {
            if (this.ajaxCancel) { new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { List } = msg;
                let CompanyObj = {};
                let CountryObj = {};
                let AreaObj = {};
                let GroupIdObj = {};
                let BatteryIdObj = {};
                List.forEach(item => {
                    if (item.CompanyValue !== "") { CompanyObj[item.CompanyValue] = { Value: item.CompanyValue, Label: item.CompanyLabel, selectShow: true }; }
                    if (item.CountryValue !== "") { CountryObj[item.CountryValue] = { Value: item.CountryValue, Label: item.CountryLabel, selectShow: true }; }
                    if (item.AreaValue !== "") { AreaObj[item.AreaValue] = { Value: item.AreaValue, Label: item.AreaLabel, selectShow: true }; }
                    if (item.GroupValue !== "") { GroupIdObj[item.GroupValue] = { Value: item.GroupValue, Label: item.GroupLabel, selectShow: true }; }
                    if (item.BatteryIdObj !== "") { BatteryIdObj[item.BatteryValue] = { Value: item.BatteryValue, Label: item.BatteryLabel, selectShow: true }; }
                });
                // const { Company, Country, Area, GroupID, GroupName,BatteryGroupId, BatteryType} = msg;
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
                const BatteryIdList = Object.values(BatteryIdObj).sort(function (a, b) {
                    return (a.Label).localeCompare(b.Label, "zh-hant");
                });
                this.setState({
                    isFilterSelectTable: [...List],
                    isFilterCompanyList: [...CompanyList],
                    isFilterCompanyData: {
                        ...defaultCompany
                    },
                    isFilterCountryList: [...CountryList],
                    isFilterCountryData: {
                        ...defaultCountry
                    },
                    isFilterAreaList: [...AreaList],
                    isFilterAreaData: {
                        ...defaultArea
                    },
                    isFilterGroupIdList: [...GroupList],
                    isFilterGroupIdData: {
                        ...defaultGroupId
                    },
                    isFilterBatteryGroupIdList: [...BatteryIdList],
                    isFilterBatteryGroupIdData: {
                        ...defaultBatteryGroupId
                    },
                    isFilterBatteryStatusList: [{ Value: '', Label: '' }],
                    isFilterBatteryStatusData: {
                        ...defaultBatteryStatus
                    },
                });
                return new Promise((resolve, reject) => (resolve('')));
            } else {
                console.error('電池數據CompanyList', response);
                this.setState({
                    isFilterCompanyList: [],//預設公司別清單
                    isFilterCountryList: [],//預設國家清單
                    isFilterAreaList: [],//預設地域清單
                    isFilterGroupIdList: [],//預設站台編號清單
                    isFilterGroupNameList: [],//預設站台名稱清單
                    isFilterBatteryGroupIdList: [],//預設電池組ID清單
                    isFilterBatteryTypeList: [],//預設電池型號清單
                    isFilterCompanyData: { ...initFilterSelectData },
                    isFilterCountryData: { ...initFilterSelectData },
                    isFilterAreaData: { ...initFilterSelectData },
                    isFilterGroupIdData: { ...initFilterSelectData },
                    isFilterBatteryGroupIdData: { ...initFilterSelectData },
                    isFilterBatteryStatusData: { ...initFilterSelectData },
                    isFilterRecordTimeData: { ...initFilterDate },
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
            if (defaultGroupId.isDataList.length > 0) {
                let obj = {};
                defaultGroupId.isButtonList.forEach(item => obj[item.Value] = item);
                this.onUpdateChainSelectList(FilterNames_API.isFilterGroupIdVirtue, obj);
            }
            this.setState({
                isFilterCompanyData: { ...defaultCompany },
                isFilterCountryData: { ...defaultCountry },
                isFilterAreaData: { ...defaultArea },
                isFilterGroupIdData: { ...defaultGroupId },
                isFilterBatteryGroupIdData: { ...defaultBatteryGroupId },
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
                if (this.state.isFilterCompanyData.isDataList.length > 0) {
                    List = Lists.filter(item => (this.state.isFilterCompanyData.isDataList.findIndex((element) => element === item['CompanyValue']) >= 0 ? true : false));
                } else {
                    List = [...Lists];
                }
                break;
            case FilterNames_API.isFilterAreaVirtue:
                let List1 = [];
                //過濾公司別   
                if (this.state.isFilterCompanyData.isDataList.length > 0) {
                    List1 = Lists.filter(item => (this.state.isFilterCompanyData.isDataList.findIndex((element) => element === item['CompanyValue']) >= 0 ? true : false));
                } else {
                    List1 = [...Lists];
                }
                //過濾國家           
                if (this.state.isFilterCountryData.isDataList.length > 0) {
                    List = List1.filter(item => (this.state.isFilterCountryData.isDataList.findIndex((element) => element === item['CountryValue']) >= 0 ? true : false));
                } else {
                    List = [...List1];
                }
                break;
            case FilterNames_API.isFilterGroupIdVirtue:
                let List3 = [];
                let List4 = [];
                //過濾公司別   
                if (this.state.isFilterCompanyData.isDataList.length > 0) {
                    List3 = Lists.filter(item => (this.state.isFilterCompanyData.isDataList.findIndex((element) => element === item['CompanyValue']) >= 0 ? true : false));
                } else {
                    List3 = [...Lists];
                }
                //過濾國家           
                if (this.state.isFilterCountryData.isDataList.length > 0) {
                    List4 = List3.filter(item => (this.state.isFilterCountryData.isDataList.findIndex((element) => element === item['CountryValue']) >= 0 ? true : false));
                } else {
                    List4 = [...List3];
                }
                //過濾地域
                if (this.state.isFilterAreaData.isDataList.length > 0) {
                    List = List4.filter(item => (this.state.isFilterAreaData.isDataList.findIndex((element) => element === item['AreaValue']) >= 0 ? true : false));
                } else {
                    List = [...List4];
                }
                break;
            case FilterNames_API.isFilterBatteryIdVirtue:
                let List5 = [];
                let List6 = [];
                //過濾公司別   
                if (this.state.isFilterCompanyData.isDataList.length > 0) {
                    List5 = Lists.filter(item => (this.state.isFilterCompanyData.isDataList.findIndex((element) => element === item['CompanyValue']) >= 0 ? true : false));
                } else {
                    List6 = [...Lists];
                }
                //過濾國家           
                if (this.state.isFilterCountryData.isDataList.length > 0) {
                    List5 = List5.filter(item => (this.state.isFilterCountryData.isDataList.findIndex((element) => element === item['CountryValue']) >= 0 ? true : false));
                } else {
                    List6 = [...List5];
                }
                //過濾地域
                if (this.state.isFilterAreaData.isDataList.length > 0) {
                    List = List6.filter(item => (this.state.isFilterAreaData.isDataList.findIndex((element) => element === item['AreaValue']) >= 0 ? true : false));
                } else {
                    List = [...List6];
                }
                //過濾站台
                if (this.state.isFilterGroupIdData.isDataList.length > 0) {
                    List = List6.filter(item => (this.state.isFilterGroupIdData.isDataList.findIndex((element) => element === item['GroupValue']) >= 0 ? true : false));
                } else {
                    List = [...List6];
                }
                // console.log('5',List5,'6',List6)
                break;
            default:
        }
        let CountryObj = {};
        let AreaObj = {};
        let GroupObj = {};
        let BatteryIdObj = {};
        List.forEach(item => {
            if (item.CountryValue !== "") { CountryObj[item.CountryValue] = { Value: item.CountryValue, Label: item.CountryLabel, selectShow: true }; }
            if (item.AreaValue !== "") { AreaObj[item.AreaValue] = { Value: item.AreaValue, Label: item.AreaLabel, selectShow: true }; }
            if (item.GroupValue !== "") { GroupObj[item.GroupValue] = { Value: item.GroupValue, Label: item.GroupLabel, selectShow: true }; }            
            if (item.BatteryIdObj !== "") { BatteryIdObj[item.BatteryValue] = { Value: item.BatteryValue, Label: item.BatteryLabel, selectShow: true }; }
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
        const BatteryIdList = Object.values(BatteryIdObj).sort(function (a, b) {
            return (a.Label).localeCompare(b.Label, "zh-hant");
        });
        switch (Virtue) {
            case FilterNames_API.isFilterCompanyVirtue:
                this.setState({
                    isFilterCountryList: [...CountryList],
                    isFilterCountryData: { ...initFilterSelectData },
                    isFilterAreaList: [...AreaList],
                    isFilterAreaData: { ...initFilterSelectData },
                    isFilterGroupIdList: [...GroupList],
                    isFilterGroupIdData: { ...initFilterSelectData },
                    isFilterBatteryGroupIdList: [...BatteryIdList],
                    isFilterBatteryGroupIdData: {...initFilterSelectData},
                });
                break;
            case FilterNames_API.isFilterCountryVirtue:
                this.setState({
                    isFilterAreaList: [...AreaList],
                    isFilterAreaData: { ...initFilterSelectData },
                    isFilterGroupIdList: [...GroupList],
                    isFilterGroupIdData: { ...initFilterSelectData },
                    isFilterBatteryGroupIdList: [...BatteryIdList],
                    isFilterBatteryGroupIdData: {...initFilterSelectData},
                });
                break;
            case FilterNames_API.isFilterAreaVirtue:
                this.setState({
                    isFilterGroupIdList: [...GroupList],
                    isFilterGroupIdData: { ...initFilterSelectData },
                    isFilterBatteryGroupIdList: [...BatteryIdList],
                    isFilterBatteryGroupIdData: {...initFilterSelectData},
                });
                break;
            case FilterNames_API.isFilterGroupIdVirtue:
                this.setState({
                    isFilterBatteryGroupIdList: [...BatteryIdList],
                    isFilterBatteryGroupIdData: { ...initFilterSelectData },
                })
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
            } else {
                this.props.alertFilterMsgOpen(msg);
            }
        });
    }

    //取得電池數據篩選清單API(GET):
    ajaxGetBatteryFilter = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        // const url = `${apiUrl}getBatteryFilter`;
        const url = `${apiUrl}getHistoryFilter`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //取得參數設定篩選清單API(GET):
        //https://www.gtething.tw/battery/getBatteryFilter
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

Filter.defaultProps = {
    functionId: '0',
    isFilterCompanyData: {},
    isFilterCountryData: {},
    isFilterAreaData: {},
    isFilterGroupIdData: {},
    isFilterBatteryGroupIdData: {},
    isFilterBatteryStatusData: {},
    isFilterRecordTimeData: {},
    updateFilterData: () => { },
    submitFilterData: () => { },
    alertFilterMsgOpen: () => { },
}
Filter.propTypes = {
    functionId: PropTypes.string,
    isFilterCompanyData: PropTypes.object,
    isFilterCountryData: PropTypes.object,
    isFilterAreaData: PropTypes.object,
    isFilterGroupIdData: PropTypes.object,
    isFilterBatteryGroupIdData: PropTypes.object,
    isFilterBatteryStatusData: PropTypes.object,
    isFilterRecordTimeData: PropTypes.object,
    updateFilterData: PropTypes.func,
    submitFilterData: PropTypes.func,
    alertFilterMsgOpen: PropTypes.func,
}
const mapStateToProps = (state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        account: state.LoginReducer.account,
        company: state.LoginReducer.company,
        curLanguage: state.LoginReducer.curLanguage,
        timeZone: state.LoginReducer.timeZone,
        groupInternalId: state.BattDataReducer.groupInternalId, //電池數據(第一層)站台編號
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Filter);


