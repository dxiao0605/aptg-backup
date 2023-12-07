import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';

import {setBattInternalID} from "../../actions/BattDataAction";
import { checkObject, ajaxGetBatteryDetailFilter, ajaxDelFilter, ajaxGetFilter } from '../../components/FilterDrawer/utils';//整理checkbox狀態
import { FilterNames, initFilterSelectData, initFilterDate } from '../../components/BattFilter/InitDataFormat';//初始化格式

import Divider from '@material-ui/core/Divider';
import FilterSave from '../../components/BattFilter/FilterSave';//儲存標籤紀錄
import FilterItemHeader from '../../components/FilterDrawer/FilterItemHeader';//每筆Item的header
import FilterItemSelectSingle from '../../components/FilterDrawer/FilterItemSelectSingle';//每筆Item的Select(單選)
import FilterItemRadio from '../../components/FilterDrawer/FilterItemRadio';//每筆Item的Radio
import {CusMainBtnStyle} from '../../components/CusMainBtnStyle';


class Filter extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {
            disabledBtn: false,
            isFilterBatteryGroupIdList: [//預設電池組ID清單
                this.props.isFilterBatteryGroupIdData.isDataList
            ],
            isFilterRecordTimeList: [//預設數據時間
                { Value: "0", Label: "1098", Type: "String", show: true }, //24Hr
                { Value: "5", Label: "1111", Type: "String", show: true }, // 7D
                { Value: "3", Label: "1109", Type: "String", show: true }, // 1Month
                { Value: "1", Label: "", Type: "Date", show: true },
                { Value: "2", Label: "1083", Type: "String", show: false },
                { Value: "4", Label: "1110", Type: "String", show: false },
            ],
            isFilterBatteryGroupIdData: {//預設電池組ID
                ...initFilterSelectData
            },
            isFilterRecordTimeData: this.props.isFilterRecordTimeData,
            isFilterTotal: this.checkedIsFilterTotal(),//總數
            defRadioRangeMaxDay: '31',//往未來多少天
            defRadioRangeMinDay: '31',//往從前多少天
        }
    }
    componentDidMount() {
        const { isFilterBatteryGroupIdData, isFilterRecordTimeData } = this.props;
        this.getBatteryFilter(isFilterBatteryGroupIdData);//取選單資料
        this.getFilter();//篩選儲存清單API(GET)
        this.setState({
            isFilterRecordTimeData: isFilterRecordTimeData
        });
    }
    componentDidUpdate(prevProps, prevState) {
        if (//更新全部的數字
            prevState.isFilterBatteryGroupIdData.isDataList.length !== this.state.isFilterBatteryGroupIdData.isDataList.length
        ) {
            const list = [
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
        // 電池組ID
        if (prevState.isFilterBatteryGroupIdData.isChecked !== this.state.isFilterBatteryGroupIdData.isChecked) {
            this.setState({
                isFilterBatteryGroupIdData: this.state.isFilterBatteryGroupIdData
            })
        }
        // 數據時間
        if (prevState.isFilterRecordTimeData.Radio !== this.state.isFilterRecordTimeData.Radio) {
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
        const { functionId,battInternalId } = this.props;
        const { isFilterTotal, isFilterSaveList,disabledBtn } = this.state;
        const { isFilterBatteryGroupIdList, isFilterBatteryGroupIdData } = this.state;//電池組ID
        const { isFilterRecordTimeList, isFilterRecordTimeData } = this.state;//數據時間

        if(isFilterBatteryGroupIdData && isFilterRecordTimeData && battInternalId) {
            return (
                <Fragment>
                    <div className=" form-inline align-items-center justify-content-center pt-1 pb-2">
                        <i className="fas fa-filter mr-1" />
                        <Trans i18nKey='1600'/>
                        {/* 總計 */}
                        <span className="filter_prompt ml-1 noDetail">
                            {isFilterTotal}
                        </span>
                    </div>
                    <Divider />
                    {/* 紀錄 */}
                    <FilterSave
                        functionId={functionId} // 1400_1, 1400_2, 1600_1, 1600_2
                        company={this.props.company}
                        list={isFilterSaveList}
                        onClickBookmark={(FilterConfig) => { this.onClickBookmark(FilterConfig) }}
                        onClickTrash={(FilterID) => { this.delFilter(FilterID) }}
                    />
                    <Divider />
                    {/* 篩選器 */}
                    <ul className="filter overflowY mb-0">
                        <li>{/* 電池組ID */}
                        <FilterItemHeader
                            title={"1026"}
                            total={isFilterBatteryGroupIdData.isDataList.length}
                            detailList={isFilterBatteryGroupIdData.isButtonList}
                            showCheckBox={false}
                            isChecked={isFilterBatteryGroupIdData.isChecked}
                            isOpen={isFilterBatteryGroupIdData.isOpen}
                            onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterBatteryGroupIdData, object)}
                        >
                            <FilterItemSelectSingle
                                defSelectVal={isFilterBatteryGroupIdData.isDataList}
                                defSelectList={isFilterBatteryGroupIdList}
                                onUpdateSelectList={(list) => this.onUpdateSelectList('isFilterBatteryGroupIdList', list)}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterBatteryGroupIdData, object)}
                                onChangeDisabledBtn={this.onChangeDisabledBtn}
                            />
                        </FilterItemHeader>
                        </li>
    
                        <li>{/* 數據時間 */}
                        <FilterItemHeader
                            title={"1036"}
                            total={1}
                            showCheckBox={false}
                            isChecked={false}
                            isOpen={true}
                        >
                            <FilterItemRadio
                                defRadioVal={isFilterRecordTimeData.Radio}
                                defRadioStart={isFilterRecordTimeData.Start}
                                defRadioEnd={isFilterRecordTimeData.End}
                                defRadioList={isFilterRecordTimeList}
                                defRadioOpenStartTime={true}
                                defRadioStartHH={isFilterRecordTimeData.StartHH}
                                defRadioStartMM={isFilterRecordTimeData.StartMM}
                                defRadioEndHH={isFilterRecordTimeData.EndHH}
                                defRadioEndMM={isFilterRecordTimeData.EndMM}
                                onUpdateList={(object) => this.onUpdateList(FilterNames.isFilterRecordTimeData, object)}
                                defRadioRangeMaxDay={this.state.defRadioRangeMaxDay}
                                defRadioRangeMinDay={this.state.defRadioRangeMinDay}
                            />
                        </FilterItemHeader>
                        </li>
    
                        <Divider />
                        <div className="d-block py-3 px-4">
                            {
                                disabledBtn ? <div className="btn btn-secondary disabled w-100"><Trans i18nKey={"1002"}/></div>
                                :   <CusMainBtnStyle
                                        name={<Trans i18nKey={"1002"} />}
                                        icon={"fas fa-check mr-2"}
                                        fullWidth={true}
                                        clickEvent={() => this.handleSumbit()}
                                    />
                            }

                        </div>
                    </ul>
                </Fragment>
            )
        }else{
            return ''
        }
    }
    // 檢查預設篩選條件總數
    checkedIsFilterTotal = () => {
        return 2
    }
    onChangeDisabledBtn = (value) => {
        this.setState({
            disabledBtn:value,
        })
    }
    // 書籤
    onClickBookmark = (FilterConfig) => {
        const {
            BatteryGroupId: isFilterBatteryGroupIdData,
            RecTime: isFilterRecordTimeData,
        } = FilterConfig;
        const postData = {
            isFilterBatteryGroupIdData: checkObject(isFilterBatteryGroupIdData),
            isFilterRecordTimeData: isFilterRecordTimeData,
        };
        const battInternalId = parseInt(isFilterBatteryGroupIdData.isDataList[0])
        this.props.updateFilterData(FilterNames.isFilterBatteryGroupIdData, checkObject(isFilterBatteryGroupIdData));
        this.props.updateFilterData(FilterNames.isFilterRecordTimeData, isFilterRecordTimeData);
        this.props.submitFilterData(postData);
        this.setState({ ...postData });
        // 電池組ID BattInternalID
        this.props.setBattInternalID(battInternalId)
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
        if(name === 'isFilterBatteryGroupIdData'){// 更新battInternalId
            const battInternalId = parseInt(object.isDataList[0])
            this.props.setBattInternalID(battInternalId)
        }
    }
    // 送出篩選條件
    handleSumbit = () => {
        const { isFilterBatteryGroupIdData, isFilterRecordTimeData,
            ...other } = this.state;
        this.props.updateFilterData(FilterNames.isFilterBatteryGroupIdData, checkObject(isFilterBatteryGroupIdData));
        this.props.updateFilterData(FilterNames.isFilterRecordTimeData, isFilterRecordTimeData);
        this.props.submitFilterData(this.state);
        this.setState({
            isFilterBatteryGroupIdData: checkObject(isFilterBatteryGroupIdData),
            isFilterRecordTimeData: isFilterRecordTimeData,
            ...other
        });

    }

    //電池歷史(2)篩選清單API:
    getBatteryFilter = (defaultBatteryGroupId) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        ajaxGetBatteryDetailFilter({query}).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { List } = msg;
                let BatteryIdObj = {};
                List.forEach(item => {
                    if (item.BatteryIdObj !== "") { BatteryIdObj[item.BatteryValue] = { Value: item.BatteryValue, Label: item.BatteryLabel, selectShow: true }; }
                });
                const BatteryIdList = Object.values(BatteryIdObj).sort(function (a, b) {
                    return (a.Label).localeCompare(b.Label, "zh-hant");
                });
                this.setState({
                    isFilterSelectTable: [...List],
                    isFilterBatteryGroupIdList: [...BatteryIdList],
                    isFilterBatteryGroupIdData: {
                        ...defaultBatteryGroupId
                    },
                });
            } else {
                this.setState({
                    isFilterBatteryGroupIdList: [],//預設電池組ID清單
                    isFilterBatteryGroupIdData: { ...initFilterSelectData },
                    isFilterRecordTimeData: { ...initFilterDate },
                });
            }
        });
    }


    //取得篩選儲存清單API(GET)
    getFilter = () => {
        const { token, curLanguage, timeZone, company, account, functionId } = this.props;
        const query = { token, curLanguage, timeZone, company, account, functionId };
        ajaxGetFilter({query}).then((response) => {
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
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const postData = { FilterID };
        ajaxDelFilter({ query, postData }).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const {  msg } = response;
            if (msg) {
                this.props.alertFilterMsgOpen(msg);
            }
        });
    }
}

Filter.defaultProps = {
    functionId: '0',
    isFilterBatteryGroupIdData: {},
    isFilterRecordTimeData: {},
    updateFilterData: () => { },
    submitFilterData: () => { },
    alertFilterMsgOpen: () => { },
}
Filter.propTypes = {
    functionId: PropTypes.string,
    isFilterBatteryGroupIdData: PropTypes.object,
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
        isFilterBatteryGroupIdData: state.BattFilterReducer.isFilterBatteryGroupIdData,//電池數據電池組ID
        battInternalId: state.BattDataReducer.battInternalId,
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
		setBattInternalID: (value) => dispatch(setBattInternalID(value)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Filter);


