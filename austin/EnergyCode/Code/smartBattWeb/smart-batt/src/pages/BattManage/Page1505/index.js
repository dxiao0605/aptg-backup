import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';

import { exportExcel } from '../../../utils/exportExcel';
import {
    updateCompany, updateBatteryTypeName,
    resetAll, resetCompany, resetBatteryTypeName,
} from '../../../actions/BattManageP1505Action';
import { setTreeFunctionId } from '../../../actions/MainNavAction';
import { setNewTableHeader } from '../../../components/CusTable/utils';
import { filterArray } from '../../../components/CusSearchInput/utils';//Search Tool
import { ajaxSaveFilter } from '../../../components/FilterDrawer/utils';
import { FilterNames, initOpenData, initFilterSelectData, CompareWindowns } from './InitDataFormat';//初始化格式
import { ajaxGetBatteryTypeList, ajaxAddBatteryType, ajaxUpdBatteryType, ajaxDelBatteryType } from './getApi';

import CusSearchInput from '../../../components/CusSearchInput'; //搜尋關鍵字
import FilterItemTag from './FilterItemTag';   //篩選條件Button
import Table from './Table';
import PageAlertDialog from './PageAlertDialog';//彈跳視窗
import AlertMsg from '../../../components/AlertMsg';//訊息視窗
import ToggleBtnBar from '../../../components/CusTable/ToggleBtnBar';//表格欄位顯示隱藏


class Page1505 extends Component {
    // 電池型號管理
    ajaxCancel = false;//判斷ajax是否取消
    importExcelCancel = false;//判斷ajax importExcel是否取消
    constructor(props) {
        super(props)
        this.state = {
            functionId: '1501',//funcId
            pageId: '1505',//頁面Id
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
            tableHeader: [ // 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
                { id: '1064', sortName: 'Company', fixed: true, active: true },// 公司別
                { id: '1506', sortName: 'BatteryTypeName', fixed: false, active: true },// 電池型號中文
            ],
            tableActive: 1,//table的頁數
            tableErrMsg: '',//table錯誤訊息
            //彈跳視窗
            openDialog: false,//開啟新增、編輯、刪除視窗
            openWindows: '',//目前開啟的視窗(1050:編輯、1051:新增、1052:刪除、1053:匯出、1054:匯入)
            openTitle: '',//視窗標頭
            openData: { ...initOpenData, Company: this.props.company },//視窗資料
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
        const { loading,searchInput,
            data, tableErrMsg, openDialog, openWindows, openTitle, openData, tableHeader, limits } = this.state;
        const { isFilterCompanyData, isFilterBatteryTypeNameData } = this.props;//篩選      
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
                        {/* 重置 */}
                        <div className="d-inline-block">
                            <button className="btn-sm-b1 btn-outline-primary mr-2" onClick={this.resetFilterData}>
                                <Trans i18nKey="1038" />
                            </button>
                        </div>
                        <FilterItemTag
                            isFilterCompanyData={isFilterCompanyData}
                            isFilterBatteryTypeNameData={isFilterBatteryTypeNameData}
                            submitFilterItemTag={this.submitFilterItemTag}
                        />
                    </div>
                    {false && <ToggleBtnBar list={tableHeader} onClickEvent={this.onToggleBtnChange} />}
                    {/* table */}
                    <div className="col-12 pb-4 pl-0 pr-0">
                        <Table
                            limits={limits} //設定權限，影響可看到的內容
                            perPage={perPage} //表單一次顯示幾個
                            tableHeader={tableHeader} //表單標頭
                            data={data} //顯示資料   
                            active={this.state.tableActive}
                            addEvent={this.addEvent} //新增功能
                            deleteEvent={this.deleteEvent} //刪除功能
                            exportExcel={this.onExportExcel} //匯出表單功能
                            tableErrMsg={tableErrMsg} //錯誤訊息
                            getEditData={this.getEditData} //編輯功能
                            onUpdateTable={this.onUpdateTable} //checkbox功能
                            onUpdActive={this.onUpdActive}
                        />
                    </div>

                    {/* 彈跳視窗 */}
                    {
                        limits.Edit === 1 && openDialog &&
                        <PageAlertDialog
                            windows={openWindows} //指定開啟的視窗
                            title={openTitle} //title
                            open={openDialog} //開啟視窗
                            data={openData} //視窗資料
                            handleClose={this.handleClose} //連動關閉視窗
                            handleEditSubmit={this.handleEditSubmit}//編輯送出
                            handleAddSubmit={this.handleAddSubmit}//新增送出
                            handleDelSubmit={this.handleDelSubmit}//刪除送出
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
    // 更新table資訊
    onUpdateTable = (data) => {
        this.setState({
            data: [...data]
        });
    }
    onUpdActive = (idx) => {
        this.setState({
            tableActive: idx
        })
    }
    // 取得編輯某一行資料
    getEditData = ({ data }) => {
        this.setState({
            openDialog: true,
            openWindows: CompareWindowns.Edit.model,
            openTitle: CompareWindowns.Edit.title,
            openData: { ...data },
        });
        this.props.onIsRefreshChange(false);   //停止5min刷新頁面
    }
    //新增功能
    addEvent = () => {
        this.setState({
            openDialog: true,
            openWindows: CompareWindowns.Add.model,
            openTitle: CompareWindowns.Add.title,
            openData: { ...initOpenData, Company: this.props.company }
        });
        this.props.onIsRefreshChange(false);   //停止5min刷新頁面
    }

    //刪除功能
    deleteEvent = () => {
        const { data } = this.state;
        const batteryTypeCode = [];
        data.forEach(item => {
            if (item.checked) {
                batteryTypeCode.push(item.BatteryTypeCode);
            }
        })
        if (batteryTypeCode.length === 0) { return; }
        this.setState({
            openDialog: true,
            openWindows: CompareWindowns.Del.model,
            openTitle: CompareWindowns.Del.title,
            openData: { ...initOpenData, Company: this.props.company }
        });
        this.props.onIsRefreshChange(false);   //停止5min刷新頁面
    }
    //匯出Excel
    onExportExcel = () => {
        this.setState({
            openAlertMsg: true,//開啟訊息視窗
            titleAlertMsg: '1089',
            textAlertMsg: <Trans i18nKey="1091" />,//填寫訊息
        });
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const postData = this.returnPostData(this.props, "check");
        ajaxGetBatteryTypeList({query,postData}).then((response) => {
            if (this.ajaxCancel || this.importExcelCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00') {
                const excelName = msg;
                const url = `getBatteryTypeList`;
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

            } else {
                this.setState({
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            }
        });
    }
    //  彈跳視窗 - 編輯

    handleClose = () => {
        this.setState({
            openDialog: false,
            openWindows: '',
            openTitle: '',
            openData: { ...initOpenData, Company: this.props.company },
        });
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面
    }
    //新增送出
    handleAddSubmit = (data) => {
        const { BatteryTypeCode, BatteryTypeName, Company, } = data;
        const { username } = this.props;
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const postData = {
            "msg":
            {
                "CompanyCode": Company,
                "BatteryTypeCode": BatteryTypeCode,
                "BatteryTypeName": BatteryTypeName,
                "UserName": username
            }
        }
        ajaxAddBatteryType({query,postData}).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData, Company: this.props.company },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
                this.submitFilterData();
            } else {
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData, Company: this.props.company },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            }
        });
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面
    }
    //編輯送出
    handleEditSubmit = (data) => {
        const { BatteryTypeCode, BatteryTypeName, Company, } = data;
        const { username } = this.props;
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const postData = {
            "msg":
            {
                "CompanyCode": Company,
                "BatteryTypeCode": BatteryTypeCode,
                "BatteryTypeName": BatteryTypeName,
                "UserName": username
            }
        }
        ajaxUpdBatteryType({query,postData}).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData, Company: this.props.company },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
                this.submitFilterData();
            } else {
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData, Company: this.props.company },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            }
        });
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面
    }
    //刪除送出
    handleDelSubmit = () => {
        const { username } = this.props;
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { data } = this.state;
        const batteryTypeCode = [];
        data.forEach(item => {
            if (item.checked) {
                batteryTypeCode.push(item.BatteryTypeCode);
            }
        })
        if (batteryTypeCode.length === 0) { return; }
        const postData = {
            "msg":
            {
                "BatteryTypeCode": [...batteryTypeCode],
                "UserName": username
            }
        }
        ajaxDelBatteryType({query,postData}).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData, Company: this.props.company },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
                this.submitFilterData();
            } else {
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData, Company: this.props.company },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            }
        });
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面
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
        const { isFilterCompanyData, isFilterBatteryTypeNameData, } = FilterNames;
        let postData = {
            isFilterCompanyData: this.props.isFilterCompanyData,
            isFilterBatteryTypeNameData: this.props.isFilterBatteryTypeNameData,
        }

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
            case isFilterBatteryTypeNameData:
                this.props.updateBatteryTypeName(object);
                break;
            default:
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
        const { isFilterCompanyData, isFilterBatteryTypeNameData, } = FilterNames;
        switch (name) {
            case isFilterCompanyData:
                this.props.updateCompany(object);
                break;
            case isFilterBatteryTypeNameData:
                this.props.updateBatteryTypeName(object);
                break;
            default:
        }
    }
    saveFilterData = (FilterName) => {//右邊篩選全部儲存
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const postData = {
            "Account": this.props.account,
            "FunctionId": this.state.pageId,
            "FilterName": FilterName,
            "FilterConfig": {
                "Company": { ...this.props.isFilterCompanyData },
                "BatteryTypeName": { ...this.props.isFilterBatteryTypeNameData },
            }
        }
        ajaxSaveFilter({query,postData}).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            } else {
                this.setState({
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            }
        });
    }
    resetFilterData = () => {//右邊篩選全部初始化        
        this.props.resetAll();
        let postData = {
            isFilterCompanyData: initFilterSelectData,
            isFilterBatteryTypeNameData: initFilterSelectData,
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
            openTitle: '',//視窗標頭
            openData: { ...initOpenData, Company: this.props.company },//視窗資料
        });
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        let postData;
        if (props === undefined) {
            postData = this.returnPostData(this.props, "");
        } else {
            postData = this.returnPostData(props, "");
        }
        // 電池型號管理API
        ajaxGetBatteryTypeList({query,postData}).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { BatteryTypeList } = msg;
                const newData = BatteryTypeList.map((item) => {
                    return { ...item, checked: false, slider: false }
                });
                this.setState({
                    loading: false,
                    ajaxData: [...newData],
                    data: [...newData],
                    tableErrMsg: '',
                    isFilterOpen: false,
                    tableActive: 1
                })
            } else {
                this.setState({ loading: false, ajaxData: [], data: [], tableErrMsg: msg, isFilterOpen: false, tableActive: 1 })
            }
        });
    }
    returnPostData = (state, type) => {
        const { isFilterCompanyData, isFilterBatteryTypeNameData, } = state;
        return {
            "Company": {
                "All": isFilterCompanyData.isChecked ? "1" : "0",
                "List": [...isFilterCompanyData.isDataList]
            },
            "BattTypeList": {
                "All": isFilterBatteryTypeNameData.isChecked ? "1" : "0",
                "List": [...isFilterBatteryTypeNameData.isDataList]
            },
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

}
Page1505.defaultProps = {
    onIsRefreshChange: () => { },
}

Page1505.propTypes = {
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
        isFilterCompanyData: state.BattManageP1505Reducer.isFilterCompanyData,
        isFilterBatteryTypeNameData: state.BattManageP1505Reducer.isFilterBatteryTypeNameData,
        treeFunctionId: state.MainNavReducer.treeFunctionId, //指定目前頁面
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        updateCompany: (object) => dispatch(updateCompany(object)),
        updateBatteryTypeName: (object) => dispatch(updateBatteryTypeName(object)),
        resetCompany: () => dispatch(resetCompany()),
        resetBatteryTypeName: () => dispatch(resetBatteryTypeName()),
        resetAll: () => dispatch(resetAll()),
        setTreeFunctionId: (functionId) => dispatch(setTreeFunctionId(functionId)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Page1505);