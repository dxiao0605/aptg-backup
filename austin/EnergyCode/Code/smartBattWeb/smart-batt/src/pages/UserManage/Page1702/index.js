import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';

import { setActiveNum } from '../../../actions/UserManageAction';
import { setTreeFunctionId } from '../../../actions/MainNavAction';
import { updateUSERMANAGE } from '../../../actions/UserManageP1702Action';
import { exportExcel } from '../../../utils/exportExcel';
import { setNewTableHeader } from '../../../components/CusTable/utils';
import { filterArray } from '../../../components/CusSearchInput/utils';//Search Tool
import { CompareWindowns, initOpenData, initOpenAuthorityData } from './InitDataFormat';//初始化格式
import { 
    ajaxGetRoleInfo, ajaxGetRoleInfoCheckExcel, 
    ajaxAddRole, ajaxDelRole, 
    ajaxGetRoleAuthority, ajaxUpdRoleAuthority
} from './getApi';

import CusSearchInput from './CusSearchInputSendValue'; //搜尋關鍵字
import Table from './Table';
import PageAlertDialog from './PageAlertDialog';//彈跳視窗
import AlertMsg from '../../../components/AlertMsg';//訊息視窗
import ToggleBtnBar from '../../../components/CusTable/ToggleBtnBar';//表格欄位顯示隱藏




class Page1702 extends Component {
    // 角色
    ajaxCancel = false;//判斷ajax是否取消
    importExcelCancel = false;//判斷ajax importExcel是否取消    
    constructor(props) {
        super(props)
        this.state = {
            functionId: '1702',//funcId
            pageId: '1702',//頁面Id
            limits: {//權限設定
                Edit: 0,
                Button: {}
            },
            loading: true,//總畫面讀取     
            searchInput: '',//搜尋欄位        

            ajaxData: [],//ajaxTableData(原始資料)
            data: [],//tableData(顯示資料)         
            tableHeader: [// 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
                { id: '1703', sortName: 'RoleName', fixed: true, active: true },      // 角色名稱
                { id: '1704', sortName: 'RoleDesc', fixed: false, active: true },     // 角色中文說明
                { id: '1705', sortName: 'RoleDescE', fixed: false, active: true },    // 角色英文說明
                { id: '1706', sortName: 'RoleDescJ', fixed: false, active: true },    // 角色日文說明
                { id: '1723', sortName: 'Count', fixed: false, active: true },        // 用戶數量
            ],
            tableActive: 1,//table的頁數
            tableErrMsg: '',//table錯誤訊息
            //彈跳視窗
            openDialog: false,//開啟編輯視窗
            openWindows: '',//目前開啟的視窗(1050:編輯、1051:新增、1052:刪除、1053:匯出、1054:匯入)
            openTitleI18n: true,
            openTitle: '',//視窗標頭
            openTitleMore: '',
            openData: { ...initOpenData },//新增角色資料
            openAuthorityData: { ...initOpenAuthorityData },//權限資料

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
        if (this.props.treeFunctionId !== 1702) {
            this.props.setTreeFunctionId(1702);
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
        const { loading, searchInput, data, tableErrMsg, openDialog, openWindows, openTitleI18n, openTitle, openTitleMore, openData, openAuthorityData, tableHeader, limits } = this.state;
        if (loading) {
            return ''
        } else {
            return (
                <Fragment>
                    {/* filter */}
                    <div className="col-12 pt-4 pb-4 pl-0 pr-0">
                        <div className="d-inline-block mr-2 my-2">
                            {/* 關鍵字搜索 */}
                            <CusSearchInput
                                placeholderName='1037'
                                value={searchInput}
                                onUpdInput={this.onUpdInput}
                                onClickEvent={(value) => this.onClickEvent(value)}
                            />
                        </div>
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
                            addEvent={this.onOpenAddEvent}
                            exportExcel={this.onExportExcel}
                            tableErrMsg={tableErrMsg}
                            getData={this.getData}
                            HandleforwardUserSumbit={this.HandleforwardUserSumbit}//forwed到使用者畫面
                            HandleAuthorityClick={this.HandleAuthorityClick}//取角色權限
                            onUpdActive={this.onUpdActive}
                        />
                    </div>

                    {/* 彈跳視窗  編輯 */}
                    {
                        openDialog &&
                        <PageAlertDialog
                            limits={limits}
                            windows={openWindows} //指定開啟的視窗
                            openTitleI18n={openTitleI18n}//判斷title是否為i18n
                            title={openTitle} //title
                            titleMore={openTitleMore}
                            open={openDialog} //開啟視窗
                            data={openData} //視窗資料
                            authoritydata={openAuthorityData} //視窗資料
                            onUpdData={this.onUpdData}//更新資料
                            onUpdAuthorityData={this.onUpdAuthorityData}//更新資料
                            onRestData={this.onRestData}//重置資料
                            handleClose={this.handleClose} //連動關閉視窗                            
                            handleSubmit={this.handleSubmit}//送出
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
    // 取得某一行資料
    getData = ({ model, data, title }) => {
        this.setState({
            openDialog: true,
            openWindows: model,
            openTitle: title,
            openData: { ...data, }
        });
    }

    //更新資料
    onUpdData = (name, value) => {
        this.setState((prevState, props) => ({
            openData: {
                ...prevState.openData,
                [name]: value
            }
        }));
    }
    //重設資料
    onRestData = () => {
        this.setState({
            openData: { ...initOpenData }
        })
    }

    //  彈跳視窗 - 編輯   
    handleClose = () => {
        this.setState({
            openDialog: false,
            openWindows: '',
            openTitleI18n: true,
            openTitle: '',
            openTitleMore: '',
            openData: { ...initOpenData },
        });
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面
    }
    // 彈跳視窗 - 送出
    handleSubmit = () => {
        const { openWindows } = this.state;
        switch (openWindows) {
            case CompareWindowns.Add.model:
                this.HandleAddSumbit();
                break;
            case CompareWindowns.Authority.model:
                this.HandleAuthoritySumbit();
                break;
            case CompareWindowns.Del.model:
                this.HandleDelSumbit();
                break;
            default:
        }
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面
    }


    //Sumbit Add
    HandleAddSumbit = () => {
        const { username } = this.props;
        const { token, curLanguage, timeZone, company, account } = this.props;
        const query = { token, curLanguage, timeZone, company, account };
        const { openData } = this.state;
        const postData = {
            RoleName: openData.RoleName,
            CopyRoleId: openData.CopyRoleId.length > 0 ? openData.CopyRoleId[0] : '',
            RoleDesc: openData.RoleDesc,
            RoleDescE: openData.RoleDescE,
            RoleDescJ: openData.RoleDescJ,
            UserName: username,
        };
        return ajaxAddRole({query,postData}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
                this.submitFilterData();
            } else if (code === '07') {
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            } else {
                console.error('角色 ajaxAddRole', response)
                this.setState({
                    openDialog: true,
                    // openData: { ...initOpenData },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            }
        });
    }

    //Sumbit Authority
    HandleAuthoritySumbit = () => {
        const { username } = this.props;
        const { token, curLanguage, timeZone, company, account } = this.props;
        const query = { token, curLanguage, timeZone, company, account };
        const { openAuthorityData } = this.state;
        const postData = {
            ...openAuthorityData,
            UserName: username
        }
        return ajaxUpdRoleAuthority({query,postData}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    openDialog: false,
                    openAuthorityData: { ...initOpenAuthorityData },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
                this.submitFilterData();
            } else if (code === '07') {
                this.setState({
                    openDialog: false,
                    openAuthorityData: { ...initOpenAuthorityData },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            } else {
                console.error('角色 ajaxUpdRoleAuthority', response)
                this.setState({
                    openDialog: true,
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            }
        });
    }
    //Sumbit forwardUser
    HandleforwardUserSumbit = ({ event, data, item }) => {
        this.props.headerList.forEach((ite, idx) => {
            if (ite.to === data.UserUrl) {
                this.props.updateUSERMANAGE({ searchInput: data.RoleId, forward: true, });
                this.props.setActiveNum(idx);
                this.props.setTreeFunctionId(ite.Name);
            }
        });
    }

    //Sumbit Del
    HandleDelSumbit = () => {
        const { username } = this.props;
        const { token, curLanguage, timeZone, company, account } = this.props;
        const query = { token, curLanguage, timeZone, company, account };
        const { openData } = this.state;
        const postData = {
            RoleId: openData.RoleId,
            UserName: username
        }

        return ajaxDelRole({query,postData}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
                this.submitFilterData();
            } else if (code === '07') {
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            } else {
                console.error('角色 ajaxDelRole', response)
                this.setState({
                    openDialog: false,
                    openData: { ...initOpenData },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            }
        });
    }

    //取角色權限
    HandleAuthorityClick = ({ event, data, item: model }) => {
        const { token, curLanguage, timeZone, company, account } = this.props;
        const query = { token, curLanguage, timeZone, company, account };
        this.setState({
            openDialog: false,
        });
        ajaxGetRoleAuthority({query,roleId:data.RoleId}).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { RoleDesc, RoleName } = msg;
                this.setState({
                    openDialog: true,
                    openWindows: model,
                    openTitleI18n: false,
                    openTitle: CompareWindowns.Authority.title,
                    openTitleMore: `：${RoleDesc} ( ${RoleName} )`,
                    openAuthorityData: { ...msg, RoleId: data.RoleId }
                });
            } else if (code === '07') {
                this.setState({
                    openDialog: false,
                    openTitleI18n: true,
                    openTitle: CompareWindowns.Authority.title,
                    openTitleMore: '',
                    openAuthorityData: { ...initOpenAuthorityData },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            } else {
                console.error('角色 ajaxGetRoleAuthority', response)
                this.setState({
                    openDialog: false,
                    openTitleI18n: true,
                    openTitle: CompareWindowns.Authority.title,
                    openTitleMore: '',
                    openAuthorityData: { ...initOpenAuthorityData },
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            }
        });
    }

    //更新角色權限
    onUpdAuthorityData = ({ page, name, value }) => {
        this.setState((prevState, props) => ({
            openAuthorityData: {
                ...prevState.openAuthorityData,
                [page]: {
                    ...prevState.openAuthorityData[page],
                    [name]: value
                }
            }
        }));
    }



    //新增
    onOpenAddEvent = () => {
        const sendData = {
            model: CompareWindowns.Add.model,
            data: { ...initOpenData },
            title: CompareWindowns.Add.title,
        }
        this.getData(sendData);
    }
    //匯出Excel
    onExportExcel = () => {
        const { token, curLanguage, timeZone, company, account } = this.props;
        const query = { token, curLanguage, timeZone, company, account };
        this.setState({
            openAlertMsg: true,//開啟訊息視窗
            titleAlertMsg: '1089',
            textAlertMsg: <Trans i18nKey="1091" />,//填寫訊息
        });
        ajaxGetRoleInfoCheckExcel({query}).then((response) => {
            if (this.ajaxCancel || this.importExcelCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00') {
                // const { token, curLanguage, timeZone, company, account } = this.props;
                const excelName = msg;
                const url = `getRoleInfo?account=${account}&type=${"csv"}`;
                const data = '';
                exportExcel(token, curLanguage, timeZone, company, url, excelName, data, () => {
                    this.setState({
                        openAlertMsg: true,//開啟訊息視窗
                        titleAlertMsg: '1089',
                        textAlertMsg: <Trans i18nKey="1090" />,//填寫訊息
                    });
                }, 'GET', () => {
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

    onUpdInput = (value) => {
        this.setState({
            searchInput: value
        })
    }
    onUpdActive = (idx) => {
        this.setState({
            tableActive: idx
        })
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

    // get Data
    submitFilterData = (props) => {
        const { token, curLanguage, timeZone, company, account } = this.props;
        const query = { token, curLanguage, timeZone, company, account };
        // reset
        this.setState({
            loading: true,
            ajaxData: [],
            data: [],
            tableErrMsg: '',
            openDialog: false,//開啟編輯視窗
            openData: {},//視窗資料
        });
        // 角色資訊API(GET):
        return ajaxGetRoleInfo({query}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { Role } = msg;
                this.setState({
                    loading: false,
                    ajaxData: [...Role],
                    data: [...Role],
                    tableErrMsg: '',
                    tableActive: 1
                });
            } else {
                this.setState({ loading: false, ajaxData: [], data: [], tableErrMsg: msg, tableActive: 1 });
            }
        });
    }

    //開msg視窗
    handleAlertMsgOpen = (title, msg) => {
        this.setState({
            openAlertMsg: true,
            titleAlertMsg: title,
            textAlertMsg: msg,//填寫訊息
        });
        this.props.onIsRefreshChange(false);    //關閉5min刷新頁面       
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
Page1702.defaultProps = {
    onIsRefreshChange: () => { },
}

Page1702.propTypes = {
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
        treeFunctionId: state.MainNavReducer.treeFunctionId, //指定目前頁面
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        setActiveNum: (int) => dispatch(setActiveNum(int)),
        setTreeFunctionId: (functionId) => dispatch(setTreeFunctionId(functionId)),
        updateUSERMANAGE: (functionId) => dispatch(updateUSERMANAGE(functionId)),        
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Page1702);