import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';

import { exportExcel } from '../../../utils/exportExcel';
import { setTreeFunctionId } from '../../../actions/MainNavAction';
import { reset_USERMANAGE } from '../../../actions/UserManageP1702Action';
import { CompareWindowns, initOpenData } from './InitDataFormat';//初始化格式
import { setNewTableHeader } from '../../../components/CusTable/utils';
import { filterArray,filterArray_LinkToUsers } from '../../../components/CusSearchInput/utils';//Search Tool
import { ajaxGetUserInfo, ajaxGetUserInfoCheckExcel, ajaxAddUser, ajaxUpdUser, ajaxDelUser, ajaxResetPassword } from './getApi';

import CusSearchInput from './CusSearchInputSendValue'; //搜尋關鍵字
import Table from './Table';
import PageAlertDialog from './PageAlertDialog';//彈跳視窗
import AlertMsg from '../../../components/AlertMsg';//訊息視窗
import ToggleBtnBar from '../../../components/CusTable/ToggleBtnBar';//表格欄位顯示隱藏

class Page1701 extends Component {
    // 使用者
    ajaxCancel = false;//判斷ajax是否取消
    importExcelCancel = false;//判斷ajax importExcel是否取消    
    constructor(props) {
        super(props)
        this.state = {
            functionId: '1701',//funcId
            pageId: '1701',//頁面Id
            limits: {//權限設定
                Edit: 0,
                Button: {}
            },
            loading: true,//總畫面讀取     
            searchInput: '',//搜尋欄位        

            ajaxData: [],//ajaxTableData(原始資料)
            data: [],//tableData(顯示資料)         
            tableHeader: [// 表格欄位名稱(fixed 固定欄位不可隱藏 | active 顯示隱藏)
                { id: '1724', sortName: 'UserName', fixed: true, active: true },     // 名稱
                { id: '1727', sortName: 'Account', fixed: false, active: true },     // 帳號
                { id: '1064', sortName: 'CompanyName', fixed: false, active: true },        // 公司名稱
                { id: '1702', sortName: 'RoleName', fixed: false, active: true },        // 角色
                { id: '1728', sortName: 'LastLogin', fixed: false, active: true },        // 最後登入時間
            ],
            tableActive: 1,//table的頁數
            tableErrMsg: '',//table錯誤訊息
            //彈跳視窗
            openDialog: false,//開啟編輯視窗
            openWindows: '',//目前開啟的視窗(1050:編輯、1051:新增、1052:刪除、1053:匯出、1054:匯入)
            openTitle: '',//視窗標頭
            openData: { ...initOpenData },//視窗資料            
            openSubmitDisabled: false,//視窗sumbit開關

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
        if (this.props.treeFunctionId !== 1701) {
            this.props.setTreeFunctionId(1701);
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
        this.submitFilterData().then(() => {
            //角色頁面傳送資訊到使用者頁面用
            if (this.props.forward === false) { return }
            const { ajaxData } = this.state;
            const key = {
                dataArray: ajaxData,
                searchText: this.props.searchInput?.toString(),
                allowArray: ['RoleId',],
            };
            const newTableData = filterArray_LinkToUsers(key);
            this.setState({
                data: newTableData
            });
        });
    }
    componentDidUpdate(prevProps, prevState) {
        if (this.props.curLanguage !== undefined && this.props.curLanguage !== prevProps.curLanguage) {
            this.submitFilterData();
        }
    }
    componentWillUnmount() {
        this.props.reset_USERMANAGE();
        this.ajaxCancel = true;
        this.importExcelCancel = true;
        this.setState = (state, callback) => {
            return;
        };
    }

    render() {
        const { perPage } = this.props;
        const { loading, searchInput, data, tableErrMsg, openDialog, openWindows, openTitle, openData, tableHeader, limits } = this.state;
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
                            onUpdActive={this.onUpdActive}
                        />
                    </div>

                    {/* 彈跳視窗  編輯 */}
                    {
                        limits.Edit === 1 && openDialog &&
                        <PageAlertDialog
                            disabled={this.state.openSubmitDisabled}
                            limits={limits}
                            windows={openWindows} //指定開啟的視窗
                            title={openTitle} //title
                            open={openDialog} //開啟視窗
                            data={openData} //視窗資料
                            onUpdData={this.onUpdData}//更新資料
                            onRestData={this.onRestData}//重置資料
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
    // 取得某一行資料
    getData = ({ model, data, title }) => {
        this.setState({
            openDialog: true,
            openWindows: model,
            openTitle: title,
            openData: {
                ...data,
                Company: { Label: data.CompanyName, Value: data.CompanyCode },
                RoleId: Array.isArray(data.RoleId) ? [...data.RoleId] : [data.RoleId],
                Language: Array.isArray(data.Language) ? [...data.Language] : [data.Language],
                TimeZone: Array.isArray(data.TimeZone) ? [...data.TimeZone] : [data.TimeZone],
                Name: data.UserName
            }
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
    onRestData = () => {
        this.setState({
            openData: {
                ...initOpenData,
                CompanyName: this.props.companyName,
                CompanyCode: this.props.company,
            }
        })
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
    handleEditSubmit = () => {
        const {openWindows} = this.state;
        this.setState({
            openSubmitDisabled: true,
        })
        switch (openWindows) {
            case CompareWindowns.Add.model:
                this.HandleAddSumbit().then(() => {
                    this.setState({
                        openSubmitDisabled: false
                    })
                });
                break;
            case CompareWindowns.Edit.model:
                this.HandleEditSumbit().then(() => {
                    this.setState({
                        openSubmitDisabled: false
                    })
                })
                break;
            case CompareWindowns.Del.model:
                this.HandleDelSumbit().then(() => {
                    this.setState({
                        openSubmitDisabled: false
                    })
                })
                break;
            case CompareWindowns.RestPwd.model:
                this.HandleRestPwdSumbit().then(() => {
                    this.setState({
                        openSubmitDisabled: false
                    })
                })
                break;
            default:
                this.setState({
                    openSubmitDisabled: false
                })
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
            Account: openData.Account,
            Company: openData.Company,
            Name: openData.Name,
            Password: openData.Password,
            PasswordTWO: openData.PasswordTWO,
            RoleId: openData.RoleId.length === 0 || (openData.RoleId.length === 1 && openData.RoleId[0] === '') ? '' : openData.RoleId[0],
            Email: openData.Email,
            Language: openData.Language.length === 0 || (openData.Language.length === 1 && openData.Language[0] === '') ? '' : openData.Language[0],
            TimeZone: openData.TimeZone.length === 0 || (openData.TimeZone.length === 1 && openData.TimeZone[0] === '') ? '' : openData.TimeZone[0],
            Mobile: openData.Mobile,
            UserName: username
        };
        return ajaxAddUser({query,postData}).then((response) => {
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
                console.error('使用者 ajaxAddUser', response)
                this.setState({
                    openDialog: true,
                    // openData: {},
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                });
            }
        });
    }

    //Sumbit Edit
    HandleEditSumbit = () => {
        const { username } = this.props;
        const { token, curLanguage, timeZone, company, account } = this.props;
        const query = { token, curLanguage, timeZone, company, account };
        const { openData } = this.state;
        const postData = {
            Account: openData.Account,
            Name: openData.Name,
            RoleId: openData.RoleId.length === 0 || (openData.RoleId.length === 1 && openData.RoleId[0] === '') ? '' : openData.RoleId[0],
            Email: openData.Email,
            Language: openData.Language.length === 0 || (openData.Language.length === 1 && openData.Language[0] === '') ? '' : openData.Language[0],
            TimeZone: openData.TimeZone.length === 0 || (openData.TimeZone.length === 1 && openData.TimeZone[0] === '') ? '' : openData.TimeZone[0],
            Mobile: openData.Mobile,
            UserName: username
        };
        return ajaxUpdUser({query,postData}).then((response) => {
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
                console.error('使用者 ajaxUpdUser', response)
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
    //Sumbit Del
    HandleDelSumbit = () => {
        const { username } = this.props;
        const { token, curLanguage, timeZone, company, account } = this.props;
        const query = { token, curLanguage, timeZone, company, account };
        const { openData } = this.state;
        const postData = {
            DeleteAccount: openData.Account,
            Account: this.props.account,
            UserName: username
        }
        return ajaxDelUser({query,postData}).then((response) => {
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
                console.error('使用者 ajaxDelUser', response)
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

    //Sumbit RestPwd
    HandleRestPwdSumbit = () => {
        const { username } = this.props;
        const { token, curLanguage, timeZone, company, account } = this.props;
        const query = { token, curLanguage, timeZone, company, account };
        const { openData } = this.state;
        const postData = {
            Account: openData.Account,
            UserName: username
        }
        return ajaxResetPassword({query,postData}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { msg } = response;
            if(msg) {
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


    //新增
    onOpenAddEvent = () => {
        const sendData = {
            model: CompareWindowns.Add.model,
            data: {
                ...initOpenData,
                CompanyName: this.props.companyName,
                CompanyCode: this.props.company,
            },
            title: CompareWindowns.Add.title,
        };
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
        ajaxGetUserInfoCheckExcel({query}).then((response) => {
            if (this.ajaxCancel || this.importExcelCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00') {
                // const { token, curLanguage, timeZone, company, account } = this.props;
                const excelName = msg;
                const url = `getUserInfo?account=${account}&type=${"csv"}`;
                const data = '';
                exportExcel(token, curLanguage, timeZone, company, url, excelName, data, () => {
                    this.setState({
                        openAlertMsg: true,//開啟訊息視窗
                        titleAlertMsg: '1089',
                        textAlertMsg: <Trans i18nKey="1090" />,//填寫訊息
                    });
                }, 'GET',()=>{
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
            openData: { ...initOpenData },//視窗資料
        });
        // 使用者資訊API(GET):
        return ajaxGetUserInfo({query}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { User } = msg;
                this.setState({
                    loading: false,
                    ajaxData: [...User],
                    data: [...User],
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
Page1701.defaultProps = {
    forward: false,
    onIsRefreshChange: () => { },
}

Page1701.propTypes = {
    forward: PropTypes.bool,
    onIsRefreshChange: PropTypes.func,
    perPage: PropTypes.number,
}

const mapStateToProps = (state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        account: state.LoginReducer.account,
        username: state.LoginReducer.username,
        company: state.LoginReducer.company,
        companyName: state.LoginReducer.companyName,			//公司名稱
        curLanguage: state.LoginReducer.curLanguage,
        timeZone: state.LoginReducer.timeZone,
        perPage: state.LoginReducer.perPage,
        functionList: state.LoginReducer.functionList,
        treeFunctionId: state.MainNavReducer.treeFunctionId, //指定目前頁面
        searchInput: state.UserManageP1701Reducer.searchInput, //查看使用者ID
        forward: state.UserManageP1701Reducer.forward, //查看使用者功能是否啟動
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        setTreeFunctionId: (functionId) => dispatch(setTreeFunctionId(functionId)),
        reset_USERMANAGE: (functionId) => dispatch(reset_USERMANAGE(functionId)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Page1701);