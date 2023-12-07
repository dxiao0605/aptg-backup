import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { apipath, ajax } from '../../../utils/ajax';
import { setSubmitKeyList, setTableHeader } from '../../../actions/CommandP1504Action';
import { setTreeFunctionId } from '../../../actions/MainNavAction';
import Filter from './Filter';//電池參數設定
import CusSearchInput from '../../../components/CusSearchInput'; //搜尋關鍵字
import Table from './Table';
import AlertMsg from '../../../components/AlertMsg';//訊息視窗
import ToggleBtnBar from '../../../components/CusTable/ToggleBtnBar';//表格欄位顯示隱藏
import { setNewTableHeader } from '../../../components/CusTable/utils';
import { filterArray } from '../../../components/CusSearchInput/utils';//Search Tool


const apiUrl = apipath();
class Page1504 extends Component {
    // 參數設定歷史
    ajaxCancel = false;//判斷ajax是否取消
    importExcelCancel = false;//判斷ajax importExcel是否取消
    constructor(props) {
        super(props)
        this.state = {
            functionId: '1504',//funcId
            pageId: '1504',//頁面Id
            limits: {//權限設定
                Edit: 0,
                Button: {}
            },
            loading: true,//總畫面讀取     
            searchInput: '',//搜尋欄位

            ajaxData: [],//ajaxTableData(原始資料)
            data: [],//tableData(顯示資料)
            tableActive: 1,//table的頁數
            tableErrMsg: '',//table錯誤訊息
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
        if (this.props.treeFunctionId !== 1504) {
            this.props.setTreeFunctionId(1504);
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
            const { tableHeader } = this.props;
            let newList = tableHeader;
            newList[0] = { id: '1064', sortName: 'Company', fixed: true, active: false };
            this.props.setTableHeader(newList);
        }
        this.setState({
            loading: true,
        });
        this.showTableData(this.props.submitKeyList);
    }
    componentDidUpdate(prevProps, prevState) {
        if (this.props.curLanguage !== undefined && this.props.curLanguage !== prevProps.curLanguage) {
            this.showTableData(this.props.submitKeyList);
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
        const { perPage, tableHeader } = this.props;
        const { loading, searchInput, data, tableErrMsg, limits } = this.state;
        if (loading) {
            return ''
        } else {
            return (
                <Fragment>
                    {/* 電池參數設定 */}
                    <Filter showTableData={this.showTableData} handleAlertMsgOpen={this.handleAlertMsgOpen} />
                    {/* 關鍵字搜索 */}
                    <div className="col-12 pl-0 pr-0">
                        <div className="d-inline-block mr-2 my-2">
                            <CusSearchInput
                                placeholderName='1037'
                                value={searchInput}
                                onClickEvent={(value) => this.onClickEvent(value)}
                            />
                        </div>
                    </div>
                    {/* ToggleBtn */}
                    <ToggleBtnBar company={this.props.company} list={tableHeader} onClickEvent={this.onToggleBtnChange} />
                    {/* table */}
                    <div className="col-12 pl-0 pr-0">
                        <Table
                            limits={limits} //設定權限，影響可看到的內容
                            perPage={perPage} //表單一次顯示幾個
                            tableHeader={tableHeader} //表單標頭
                            data={data} //顯示資料
                            active={this.state.tableActive}  
                            refreshEvent={() => this.showTableData(this.props.submitKeyList)}
                            handleAlertMsgOpen={this.handleAlertMsgOpen}
                            tableErrMsg={tableErrMsg} //錯誤訊息
                            onUpdActive={this.onUpdActive}
                        />
                    </div>

                    {/* 彈跳視窗 */}
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
        const { tableHeader } = this.props;
        const newList = setNewTableHeader(tableHeader, value);
        this.props.setTableHeader(newList);
    }
    onUpdActive = (idx) => {
        this.setState({
            tableActive: idx
        })
    }

    //關鍵字搜尋
    onClickEvent = (value) => {
        const { ajaxData } = this.state;
        const { tableHeader } =this.props;
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
    showTableData = (list) => {
        if (list === undefined || !Array.isArray(list) || list.length === 0) {
            this.setState({
                loading: false,
            });
            return;
        }
        const postData = {
            TaskId: [...list],
            Type: "",
        };
        //參數設定歷史(POST)
        this.ajaxGetCommandHistory(postData).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { Command } = msg;
                const newData = Command.map((item) => {
                    return { ...item, checked: false, slider: false }
                });
                this.setState({
                    loading: false,
                    ajaxData: [...newData],
                    data: [...newData],
                    tableErrMsg: '',
                    tableActive: 1
                })
            } else {
                this.setState({ loading: false, ajaxData: [], data: [], tableErrMsg: msg,tableActive: 1 })
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

    //關msg視窗
    handleAlertMsgClose = () => {
        this.setState({
            openAlertMsg: false,
            titleAlertMsg: '',
            textAlertMsg: '',//填寫訊息
        });
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面       
    }


    //參數設定歷史(POST):(含EXCEL)
    ajaxGetCommandHistory = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getCommandHistory`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //參數設定歷史(POST):
        //https://www.gtething.tw/battery/getCommandHistory
    }
}

Page1504.defaultProps = {
    onIsRefreshChange: () => { },
}

Page1504.propTypes = {
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
        companyToList: state.CommandP1504Reducer.companyToList,
        submitKeyList: state.CommandP1504Reducer.submitKeyList,
        tableHeader: state.CommandP1504Reducer.tableHeader,
        treeFunctionId: state.MainNavReducer.treeFunctionId, //指定目前頁面
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        setSubmitKeyList: (object) => dispatch(setSubmitKeyList(object)),
        setTableHeader: (list) => dispatch(setTableHeader(list)),
        setTreeFunctionId: (functionId) => dispatch(setTreeFunctionId(functionId)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Page1504);