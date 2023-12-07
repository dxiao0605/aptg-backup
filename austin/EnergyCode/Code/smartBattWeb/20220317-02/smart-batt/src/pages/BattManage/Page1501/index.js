import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';
import { apipath, ajax } from '../../../utils/ajax';
import { exportExcel } from '../../../utils/exportExcel';
import {
    updateCompany, updateBatteryGroupId, updateBatteryType, updateInstallDate,
    resetAll, resetCompany, resetBatteryGroupId, resetBatteryType, resetInstallDate
} from '../../../actions/BattManageP1501Action';
import { setTreeFunctionId } from '../../../actions/MainNavAction';
import { setBattInternalID, } from "../../../actions/BattDataAction";
import { updateBatteryGroupIdBatt, resetRecordTime, } from '../../../actions/BattFilterAction';
import moment from "moment";
// components
import CusSearchInput from '../../../components/CusSearchInput'; //搜尋關鍵字
import FilterDrawer from '../../../components/FilterDrawer';   //右側篩選欄外框
import CusBookMark from '../../../components/CusBookMark'; //儲存標籤
import { FilterNames, format, initFilter, initFilterDate, CompareWindowns, initOpenData } from './InitDataFormat';//初始化格式
import FilterItemTag from './FilterItemTag';   //篩選條件Button
import Filter from './Filter';//篩選內容
import Table from './Table';
import PageAlertDialog from './PageAlertDialog';//彈跳視窗
import AlertMsg from '../../../components/AlertMsg';//訊息視窗
import ToggleBtnBar from '../../../components/CusTable/ToggleBtnBar';//表格欄位顯示隱藏
import { setNewTableHeader } from '../../../components/CusTable/utils';
import { filterArray } from '../../../components/CusSearchInput/utils';//Search Tool

const apiUrl = apipath();
class Page1501 extends Component {
    // 電池組管理
    ajaxCancel = false;//判斷ajax是否取消
    importExcelCancel = false;//判斷ajax importExcel是否取消    
    constructor(props) {
        super(props)
        this.state = {
            functionId: '1501',//funcId
            pageId: '1501',//頁面Id
            limits: {//權限設定
                Edit: 0,
                Button: {}
            },
            loading: true,//總畫面讀取     
            searchInput: '',//搜尋欄位
            //儲存標籤            
            isFilterSaveInput: '',
            //篩選視窗
            isFilterOpen: false,//開啟篩選視窗           

            ajaxData: [],//ajaxTableData(原始資料)
            data: [],//tableData(顯示資料)         
            tableHeader: [// 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
                { id: '1064', sortName: 'Company', fixed: true, active: true },     // 電池組ID
                { id: '1026', sortName: 'BatteryGroupID', fixed: false, active: true },     // 電池組ID
                { id: '1030', sortName: 'BatteryTypeName', fixed: false, active: true },        // 電池型號
                { id: '1027', sortName: 'InstallDate', fixed: false, active: true },        // 安裝日期
            ],
            tableActive: 1,//table的頁數
            tableErrMsg: '',//table錯誤訊息
            //彈跳視窗
            openDialog: false,//開啟編輯視窗
            openWindows: '',//目前開啟的視窗(1050:編輯、1051:新增、1052:刪除、1053:匯出、1054:匯入)
            openTitle: '',//視窗標頭
            openData: { ...initOpenData },//視窗資料            

            //訊息視窗            
            openAlertMsg: false,//開啟訊息視窗
            titleAlertMsg: '',//訊息視窗-標頭
            textAlertMsg: '',//訊息視窗-文字欄位
        }

    }


    // React Lifecycle
    componentDidMount() {
        //設定權限
        const { functionList } = this.props;
        const { functionId } = this.state;
        if (this.props.treeFunctionId !== 1501) {
            this.props.setTreeFunctionId(1501);
        }
        const newFList = functionList.filter(item => {
            return item.FunctionId === Number(functionId)
        });
        if (newFList.length === 1) {
            this.setState({
                limits: {
                    Edit: newFList[0].Edit,
                    Button: { ...newFList[0].Button },
                }
            });
        }
        const { company } = this.props;
        //當非管理員權限，則table不顯示公司欄位
        if (company !== "1") {
            const { tableHeader } = this.state;
            let newList = tableHeader;
            newList[0] = { id: '1064', sortName: 'Company', fixed: true, active: false };
            this.setState({
                tableHeader: newList
            });
        }
        this.setState({
            loading: true,
        });
        this.submitFilterData();
    }
    componentDidUpdate(prevProps, prevState) {
        if (this.props.curLanguage !== undefined && this.props.curLanguage !== prevProps.curLanguage) {
            this.submitFilterData();
        }
    }
    componentWillUnmount() {
        this.ajaxCancel = true;
        this.importExcelCancel = true;
        this.setState = (state, callback) => {
            return;
        };
    }

    render() {
        const { perPage } = this.props;
        const { loading, pageId, searchInput, isFilterOpen, data, tableErrMsg, openDialog, openWindows, openTitle, openData, tableHeader, limits } = this.state;
        const { isFilterCompanyData, isFilterBatteryGroupIdData,isFilterInstallDateData } = this.props;//篩選      
        if (loading) {
            return ''
        } else {
            return (
                <Fragment>
                    {/* filter */}
                    <div className="col-12 pb-4 pl-0 pr-0">
                        <div className="d-inline-block mr-2 my-2">
                            {/* 關鍵字搜索 */}
                            <CusSearchInput
                                placeholderName='1037'
                                value={searchInput}
                                onClickEvent={(value) => this.onClickEvent(value)}
                            />
                        </div>
                        {/* 篩選 */}
                        <FilterDrawer
                            isOpen={isFilterOpen}  //顯示/隱藏篩選清單(true/false)
                            setIsOpen={(value) => { this.setOpenIsFilter(value) }}  //顯示/隱藏篩選清單
                            resetEvent={this.resetFilterData}    //清空所有欄位
                        >
                            <Filter
                                functionId={pageId}
                                isFilterCompanyData={isFilterCompanyData}
                                isFilterBatteryGroupIdData={isFilterBatteryGroupIdData}
                                isFilterInstallDateData={isFilterInstallDateData}
                                updateFilterData={this.updateFilterData}
                                submitFilterData={this.submitFilterData}
                                alertFilterMsgOpen={this.alertFilterMsgOpen}
                            />
                        </FilterDrawer>
                        {/* 標籤儲存 */}
                        <CusBookMark
                            value={this.state.isFilterSaveInput}
                            placeholderName={'1087'}
                            onClickEvent={this.saveFilterData}
                        />
                        {/* 重置 */}
                        <div className="d-inline-block">
                            <button className="btn-sm-b1 btn-outline-primary mr-2" onClick={this.resetFilterData}>
                                <Trans i18nKey="1038" />
                            </button>
                        </div>
                        <FilterItemTag
                            isFilterCompanyData={isFilterCompanyData}
                            isFilterBatteryGroupIdData={isFilterBatteryGroupIdData}
                            isFilterInstallDateData={isFilterInstallDateData}
                            submitFilterItemTag={this.submitFilterItemTag}
                        />
                    </div>
                    {false && <ToggleBtnBar list={tableHeader} onClickEvent={this.onToggleBtnChange} />}
                    {/* table */}
                    <div className="col-12 pb-4 pl-0 pr-0">
                        <Table
                            limits={limits}
                            perPage={perPage}
                            tableHeader={tableHeader}
                            data={data}
                            active={this.state.tableActive}
                            exportExcel={this.onExportExcel}
                            tableErrMsg={tableErrMsg}
                            getEditData={this.getEditData}
                            onUpdActive={this.onUpdActive}
                            redirectURLHistory={this.redirectURLHistory}//跳轉至電池歷史(第二層)
                        />
                    </div>

                    {/* 彈跳視窗  編輯 */}
                    {
                        limits.Edit === 1 && openDialog &&
                        <PageAlertDialog
                            windows={openWindows} //指定開啟的視窗
                            title={openTitle} //title
                            open={openDialog} //開啟視窗
                            data={openData} //視窗資料
                            handleClose={this.handleClose} //連動關閉視窗
                            handleEditSubmit={this.handleEditSubmit}//編輯送出
                            onIsRefreshChange={(boolean) => this.props.onIsRefreshChange(boolean)}   //刷新頁面
                        />
                    }
                    <AlertMsg
                        msgTitle={this.state.titleAlertMsg}//Title
                        open={this.state.openAlertMsg} //開啟視窗
                        handleClose={this.handleAlertMsgClose} //連動關閉視窗
                        onIsRefreshChange={(boolean) => this.props.onIsRefreshChange(boolean)}   //刷新頁面
                        isDisabledBtn={false}
                    >
                        {/* 視窗資料 */}
                        <div className="col-12 p-0 my-4">
                            <div className="my-1">{this.state.textAlertMsg}</div>
                        </div>
                    </AlertMsg>
                </Fragment>
            )
        }
    }
    // 變更表格顯示隱藏欄位
    onToggleBtnChange = (value) => {
        const { tableHeader } = this.state;
        const newList = setNewTableHeader(tableHeader, value);
        this.setState({
            tableHeader: newList,
        })
    }
    onUpdActive = (idx) => {
        this.setState({
            tableActive: idx
        })
    }
    // 取得某一行資料
    getEditData = ({ data }) => {
        const InstallDate = data.InstallDate.match(/(\d+[-]\d+[-]\d+)/g);
        this.setState({
            openDialog: true,
            openWindows: CompareWindowns.Edit.model,
            openTitle: CompareWindowns.Edit.title,
            openData: { ...data, InstallDate: InstallDate !== null ? InstallDate[0] : moment(new Date()).format(format) },
        });
    }
    //  彈跳視窗 - 編輯   
    handleClose = () => {
        this.setState({
            openDialog: false,
            openWindows: '',
            openTitle: '',
            openData: { ...initOpenData },
        });
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面
    }
    // 彈跳視窗 - 編輯送出
    handleEditSubmit = (data) => {
        const { date, BatteryGroupIDValue, BatteryTypeJSON } = data;
        const InstallDate = moment(date).format(format);
        const { username } = this.props;
        const postData = {
            "msg": {
                "BatteryGroupID": BatteryGroupIDValue,
                "BatteryType": BatteryTypeJSON,
                "InstallDate": InstallDate,
                "UserName": username
            }
        };
        this.ajaxUpdBattery(postData).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    openDialog: false,
                    openData: {},
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
                this.submitFilterData();
            } else {
                this.setState({
                    openDialog: false,
                    openData: {},
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            }
        });
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面
    }

    //匯出Excel
    onExportExcel = () => {
        this.setState({
            openAlertMsg: true,//開啟訊息視窗
            titleAlertMsg: '1089',
            textAlertMsg: <Trans i18nKey="1091" />,//填寫訊息
        });
        const postData = this.returnPostData(this.props, "check");
        this.ajaxGetBattManageExcel(postData).then((response) => {
            if (this.ajaxCancel || this.importExcelCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00') {
                const excelName = msg;
                const url = `${apiUrl}getBattManage`;
                const data = this.returnPostData(this.props, "csv");
                const { token, curLanguage, timeZone, company } = this.props;
                exportExcel(token, curLanguage, timeZone, company, url, excelName, data, () => {
                    this.setState({
                        openAlertMsg: true,//開啟訊息視窗
                        titleAlertMsg: '1089',
                        textAlertMsg: <Trans i18nKey="1090" />,//填寫訊息
                    });
                }, 'POST',()=>{
                    this.setState({
                        openMsg: true,          // 開啟訊息視窗
                        message: "5003",        // 填寫訊息
                        isDisabledBtn: false,   // 防止重複點擊
                    })
                });

            }else {
                this.setState({
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            }
        });
    }

    //關鍵字搜尋
    onClickEvent = (value) => {
        const { ajaxData, tableHeader } = this.state;
        const allowArray = [];
        tableHeader.forEach(item => {
            if (!item.active) { return; }
            allowArray.push(item.sortName);
        });
        const key = {
            dataArray: ajaxData,
            searchText: value,
            allowArray: [...allowArray],
        };
        const newTableData = filterArray(key);
        this.setState({
            data: newTableData,
            tableActive: 1
        })
    }
    //BUTTON標籤 取消功能
    submitFilterItemTag = (name, item, idx) => {
        const { isFilterCompanyData, isFilterBatteryGroupIdData,isFilterInstallDateData } = FilterNames;
        const { isFilterInstallDateData: InstallDateData, } = this.props;
        if (name === isFilterInstallDateData && InstallDateData.Radio === "0") { return; }
        let postData = {
            isFilterCompanyData: this.props.isFilterCompanyData,
            isFilterBatteryGroupIdData: this.props.isFilterBatteryGroupIdData,
            isFilterInstallDateData: this.props.isFilterInstallDateData,
        }
        if (name === isFilterInstallDateData) {
            this.props.resetInstallDate();
            postData.isFilterInstallDateData = initFilterDate;
        } else {
            const { isOpen, isDataList, isButtonList } = this.props[name];
            const newIsDataList = isDataList.filter(ite => ite !== item.Value);
            const newIsButtonList = isButtonList.filter(ite => ite !== item);
            const object = {
                isOpen: isOpen,
                isChecked: newIsDataList.length === 0 ? true : false,
                isDataList: [...newIsDataList],
                isButtonList: [...newIsButtonList],
            }
            postData[name] = object;
            switch (name) {
                case isFilterCompanyData:
                    this.props.updateCompany(object);
                    break;
                case isFilterBatteryGroupIdData:
                    this.props.updateBatteryGroupId(object);
                    break;
                default:
            }
        }
        this.submitFilterData(postData);
    }


    //打開右邊篩選
    setOpenIsFilter = (value) => {
        this.setState({
            isFilterOpen: value
        })
    }
    updateFilterData = (name, object) => {//更新右邊篩選值內容
        const { isFilterCompanyData, isFilterBatteryGroupIdData,isFilterInstallDateData } = FilterNames;
        switch (name) {
            case isFilterCompanyData:
                this.props.updateCompany(object);
                break;
            case isFilterBatteryGroupIdData:
                this.props.updateBatteryGroupId(object);
                break;
            case isFilterInstallDateData:
                this.props.updateInstallDate(object);
                break;
            default:
        }
    }
    saveFilterData = (FilterName) => {//右邊篩選全部儲存
        const postData = {
            "Account": this.props.account,
            "FunctionId": this.state.pageId,
            "FilterName": FilterName,
            "FilterConfig": {
                "Company": { ...this.props.isFilterCompanyData },
                "BatteryGroupId": { ...this.props.isFilterBatteryGroupIdData },
                "InstallDate": { ...this.props.isFilterInstallDateData },
            }
        }
        this.ajaxSaveFilter(postData).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { msg } = response;
            this.setState({
                openAlertMsg: true,//開啟訊息視窗
                titleAlertMsg: '1089',
                textAlertMsg: msg,//填寫訊息
            });
            // const { code, msg } = response;
            // if (code === '00' && msg) {
            //     this.setState({
            //         openAlertMsg: true,//開啟訊息視窗
            //         titleAlertMsg: '1089',
            //         textAlertMsg: msg,//填寫訊息
            //     });
            // } else {
            //     this.setState({
            //         openAlertMsg: true,//開啟訊息視窗
            //         titleAlertMsg: '1089',
            //         textAlertMsg: msg,//填寫訊息
            //     });
            // }
        });
    }
    resetFilterData = () => {//右邊篩選全部初始化
        this.props.resetAll();
        let postData = {
            isFilterCompanyData: initFilter,
            isFilterBatteryGroupIdData: initFilter,
            isFilterInstallDateData: initFilterDate,
        }
        this.submitFilterData(postData);
    }

    // get Data
    submitFilterData = (props) => {
        // reset
        this.setState({
            loading: true,
            ajaxData: [],
            data: [],
            tableErrMsg: '',
            openDialog: false,//開啟編輯視窗
            openData: {},//視窗資料
        });
        let postData;
        if (props === undefined) {
            postData = this.returnPostData(this.props, "");
        } else {
            postData = this.returnPostData(props, "");
        }
        // 電池組管理API
        this.ajaxGetBattManage(postData).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { BatteryGroupID } = msg;
                this.setState({
                    loading: false,
                    ajaxData: [...BatteryGroupID],
                    data: [...BatteryGroupID],
                    tableErrMsg: '',
                    isFilterOpen: false,
                    tableActive: 1
                });
            } else {
                this.setState({ loading: false, ajaxData: [], data: [], tableErrMsg: msg, isFilterOpen: false, tableActive: 1 });
            }
        });
    }
    returnPostData = (state, type) => {
        const { isFilterCompanyData, isFilterBatteryGroupIdData,isFilterInstallDateData } = state;
        return {
            "Company": {
                "All": isFilterCompanyData.isChecked ? "1" : "0",
                "List": [...isFilterCompanyData.isDataList]
            },
            "BatteryGroupId": {
                "All": isFilterBatteryGroupIdData.isChecked ? "1" : "0",
                "List": [...isFilterBatteryGroupIdData.isDataList]
            },
            "InstallDate": { ...isFilterInstallDateData },
            "Type": type

        }
    }

    //刪除成功後顯示msg視窗
    alertFilterMsgOpen = (title, msg) => {
        this.setState({
            isFilterOpen: false,
            openAlertMsg: true,
            titleAlertMsg: title,
            textAlertMsg: msg,//填寫訊息
        });
        this.props.onIsRefreshChange(false);    //重新開始5min刷新頁面       
    }

    //開關msg視窗
    handleAlertMsgClose = () => {
        this.setState({
            openAlertMsg: false,
            titleAlertMsg: '',
            textAlertMsg: '',//填寫訊息
        });
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面       
    }

    // 跳轉至電池歷史(第二層)
    redirectURLHistory = async (data) => {
        await this.props.setBattInternalID(data.battInternalId)
        const batteryGroupId = {
            isOpen: true,
            isChecked: false,
            isDataList: [data.battInternalId],
            isButtonList: [{ Value: data.battInternalId, Label: data.batteryGroupId }],
        }
        await this.props.updateBatteryGroupIdBatt(batteryGroupId);
        await this.props.resetRecordTime();
    }

    //電池組管理API(Get):
    ajaxGetBattManage = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getBattManage`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //電池組管理API(Get):
        //https://www.gtething.tw/battery/getBattManage
    }
    //電池組編輯API(POST):
    ajaxUpdBattery = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}updBattery`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //電池組編輯API(POST):
        //https://www.gtething.tw/battery/updBattery
    }
    //電池組管理取EXCEL NAME API(Get):
    ajaxGetBattManageExcel = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getBattManage`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //電池組管理取EXCEL NAME API(Get):
        //https://www.gtething.tw/battery/getBattManage
    }
    //儲存篩選API(POST):
    ajaxSaveFilter = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}saveFilter`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //儲存篩選API(POST):
        //https://www.gtething.tw/battery/saveFilter
    }
}
Page1501.defaultProps = {
    onIsRefreshChange: () => {},
}

Page1501.propTypes = {
    onIsRefreshChange: PropTypes.func,
    perPage: PropTypes.number,
}

const mapStateToProps = (state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        account: state.LoginReducer.account,
        username: state.LoginReducer.username,
        company: state.LoginReducer.company,
        curLanguage: state.LoginReducer.curLanguage,
        timeZone: state.LoginReducer.timeZone,
        perPage: state.LoginReducer.perPage,
        functionList: state.LoginReducer.functionList,
        isFilterCompanyData: state.BattManageP1501Reducer.isFilterCompanyData,
        isFilterBatteryGroupIdData: state.BattManageP1501Reducer.isFilterBatteryGroupIdData,
        isFilterBatteryTypeData: state.BattManageP1501Reducer.isFilterBatteryTypeData,
        isFilterInstallDateData: state.BattManageP1501Reducer.isFilterInstallDateData,
        treeFunctionId: state.MainNavReducer.treeFunctionId, //指定目前頁面
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        updateCompany: (object) => dispatch(updateCompany(object)),
        updateBatteryGroupId: (object) => dispatch(updateBatteryGroupId(object)),
        updateBatteryType: (object) => dispatch(updateBatteryType(object)),
        updateInstallDate: (object) => dispatch(updateInstallDate(object)),
        resetCompany: () => dispatch(resetCompany()),
        resetBatteryGroupId: () => dispatch(resetBatteryGroupId()),
        resetBatteryType: () => dispatch(resetBatteryType()),
        resetInstallDate: () => dispatch(resetInstallDate()),
        resetAll: () => dispatch(resetAll()),
        setTreeFunctionId: (functionId) => dispatch(setTreeFunctionId(functionId)),
        setBattInternalID: (value) => dispatch(setBattInternalID(value)),
        updateBatteryGroupIdBatt: (object) => dispatch(updateBatteryGroupIdBatt(object)),
        resetRecordTime: () => dispatch(resetRecordTime()),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Page1501);