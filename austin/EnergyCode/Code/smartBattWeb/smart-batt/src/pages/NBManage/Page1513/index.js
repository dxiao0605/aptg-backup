import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';

import { setTreeFunctionId } from '../../../actions/MainNavAction';
import { NBSelectNames, initOpenData, i18n1519SelectList, GetModel, ModelTable } from './InitDataFormat';
import { ajaxGetCompanyList, ajaxGetNBList, ajaxUpdNBListAllocate, ajaxUpdNBListActive } from './getApi';

import ImportCommID from './ImportCommID';//通訊序號匯入
import SimpleSelect from './SimpleSelect';//選單
import FrameLeft from './FrameLeft';//左邊框
import FrameRight from './FrameRight';//右邊框
import ArrowFloor from './ArrowFloor';//箭頭
import PageAlertDialog from './PageAlertDialog';//彈跳視窗
import AlertMsg from '../../../components/AlertMsg';//訊息視窗



class Page1513 extends Component {
    // 匯入與分配
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props)
        this.state = {
            functionId: '1503',//funcId
            pageId: '1513',//頁面Id
            limits: {//權限設定
                Edit: 0,
                Button: {}
            },
            loading: true,//總畫面讀取
            loadingSearchBtn: false,//搜尋BTN狀態
            isStart: '',//起始序號
            isEnd: '',//結束序號
            isAllocateVal: [i18n1519SelectList.i18n17.Value],//分配/未分配
            isCompanyCodeList: [],//公司清單
            isCompanyCodeVal: [""],//公司val
            isModel: GetModel.NotSent,

            ajaxData: [],//ajaxData(原始資料)
            showLeft: { ...initOpenData },//showLeft(顯示左邊資料)            
            showRight: [],//showRight(顯示右邊資料)

            //彈跳視窗
            openDialog: false,//開啟彈跳視窗

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
        if (this.props.treeFunctionId !== 1503) {
            this.props.setTreeFunctionId(1503);
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
            loading: false,
        });
        this.getCompanyList();
    }
    
    componentWillUnmount() {
        this.ajaxCancel = true;
        this.setState = (state, callback) => {
            return;
        };
    }

    render() {
        const { loading, isModel, isStart, isEnd, isAllocateVal, isCompanyCodeList, isCompanyCodeVal } = this.state;
        const { showLeft, showRight } = this.state;
        const selectData = i18n1519SelectList['i18n' + isAllocateVal[0]];
        if (loading) {
            return ''
        } else {
            return (
                <Fragment>
                    {/* 通訊序號匯入 */}
                    <ImportCommID habdleUpdateDefNBList={this.habdleUpdateDefNBList} handleAlertMsgOpen={this.handleAlertMsgOpen} />
                    {/* 通訊序號分配 */}
                    <div className="col-12 mt-4 pl-0 pr-0">
                        {/* 通訊序號分配 */}
                        <div className="font-weight-bold col-12" style={{ backgroundColor: '#525b6c', color: '#fff', lineHeight: '3' }}><Trans i18nKey={'1516'} /></div>
                        <div className="px-4 py-4 bg-white">
                            <div className="form-inline align-items-center">
                                {/* 通訊序號起迄 */}
                                <div className="form-inline ml-xl-2 w-100">
                                    <label className="mr-1 my-1" style={{ minWidth: '12em', justifyContent: 'flex-end' }}><Trans i18nKey={'1517'} />：</label>
                                    <input type='text' className="form-control btn-sm" name={NBSelectNames.StartI18nKey} value={isStart} onChange={(e) => { this.onUpdateInput(e) }} />
                                    <label className="mx-1 my-1">～</label>
                                    <input type='text' className="form-control btn-sm" name={NBSelectNames.EndI18nKey} value={isEnd} onChange={(e) => { this.onUpdateInput(e) }} />
                                </div>
                                {/* (欲查看單一序號請於起始輸入框輸入) */}
                                <div className="form-inline ml-xl-2 w-100">
                                    <label className="mr-1 my-1" style={{ minWidth: '12em', justifyContent: 'flex-end' }}></label>
                                    <label className="mr-1 my-1"><Trans i18nKey={'1518'} /></label>
                                </div>
                                {/* 分配/未分配 */}
                                <div className="form-inline ml-xl-2 w-100">
                                    <label className="mr-1 my-1" style={{ minWidth: '12em', justifyContent: 'flex-end' }}><Trans i18nKey={'1519'} />：</label>
                                    <SimpleSelect
                                        className="my-1 col-xl-1 col-12"
                                        title={NBSelectNames.AllocateI18nKey}
                                        isModel={isModel}
                                        openI18nKey={true}
                                        openSelMultiple={false}
                                        defSelectVal={isAllocateVal}
                                        defSelectList={Object.values(i18n1519SelectList)}
                                        onChangeHandler={this.onUpdateSelect}
                                    />
                                    {/* 未分配使用 查找 */}
                                    {
                                        selectData.Value === Object.values(i18n1519SelectList)[0].Value &&
                                        < button type="button" className="btn btn-sm btn-secondary exportBtnShadow col-12 col-xl-1 px-0 ml-xl-2 my-2" style={{ background: '#03c3ff', borderColor: '#03c3ff' }}
                                            onClick={() => { this.onSearchFrameList() }}
                                            disabled={this.state.loadingSearchBtn}
                                        ><Trans i18nKey={'1055'} /></button>
                                    }
                                </div>
                                {/* 選擇公司-已分配才出現 */}
                                {
                                    selectData.Value === Object.values(i18n1519SelectList)[1].Value &&
                                    <div className="form-inline ml-xl-2 w-100">
                                        <label className="mr-1 my-1" style={{ minWidth: '12em', justifyContent: 'flex-end' }}><Trans i18nKey={'1074'} />：</label>
                                        <SimpleSelect
                                            className="my-1 col-xl-1 col-12"
                                            title={NBSelectNames.CompanyCodeI18nKey}
                                            isModel={isModel}
                                            openI18nKey={false}
                                            openSelMultiple={false}
                                            defSelectVal={isCompanyCodeVal}
                                            defSelectList={isCompanyCodeList}
                                            onChangeHandler={this.onUpdateSelect}
                                        />
                                        <button type="button" className="btn btn-sm btn-secondary exportBtnShadow col-12 col-xl-1 px-0 ml-xl-2 my-2" style={{ background: '#03c3ff', borderColor: '#03c3ff' }}
                                            onClick={() => { this.onSearchFrameList() }}
                                            disabled={this.state.loadingSearchBtn}
                                        ><Trans i18nKey={'1055'} /></button>
                                    </div>
                                }
                            </div>
                            <div className="form-inline align-items-center my-3">
                                {/* 留白_對齊用 */}
                                <label className="ml-xl-2" style={{ minWidth: '3em', justifyContent: 'flex-start' }} />
                                {/* 左邊選擇框 */}
                                <div className="col-12 col-xl-5 pl-0 pr-0 pr-xl-5 pt-3" style={{ width: '339px' }}>
                                    <FrameLeft
                                        List={showLeft.NBList}
                                        showDelBtn={selectData.showDelBtn}
                                        isModel={isModel}
                                        onUpdNBList={this.onUpdNBList}
                                        onClickSetDelModel={this.onClickSetDelModel}
                                    />
                                    {/* (只有預設站台的通訊序號可改為未分配) */}
                                    {/* <div className="mt-0">(<Trans i18nKey={'1575'} />)</div> */}
                                </div>
                                {/* 箭頭 */}
                                <div className="col-12 col-xl-1 pl-0 pr-0 pr-xl-4 pb-3">
                                    <ArrowFloor
                                        text={selectData.SubmitVal}
                                        onClickHandler={this.onClickArrowFloor}
                                    />
                                </div>
                                {/* 右邊結果框 */}
                                <div className="col-12 col-xl-5 pl-0 pr-0 pl-xl-5 pt-3" style={{ width: '339px' }}>
                                    <FrameRight
                                        List={showRight}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                    {/* 彈跳視窗 */}
                    {
                        this.state.openDialog &&
                        <PageAlertDialog
                            windows={this.state.isModel} //指定開啟的視窗
                            title={ModelTable['i18n' + isAllocateVal[0]][isModel].title} //title                            
                            open={this.state.openDialog} //開啟視窗
                            data={this.state.showLeft} //視窗資料
                            companyList={this.state.isCompanyCodeList}//選擇公司別選單
                            handleClose={this.handleClose} //連動關閉視窗
                            handleSubmit={this.handleSubmit}//送出
                            onUpdDataVal={this.onUpdDataVal}//更新Data資料
                            onIsRefreshChange={(boolean) => this.props.onIsRefreshChange(boolean)}   //刷新頁面
                        />

                    }
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
                </Fragment >
            )
        }
    }
    onUpdateInput = (event) => {
        this.setState({
            ['is' + event.target.name]: event.target.value
        })
    }

    onUpdateSelect = (title, value) => {
        if (NBSelectNames.AllocateI18nKey === title) {
            if (value[0] === Object.values(i18n1519SelectList)[0].Value) {//未分配
                this.setState({
                    isCompanyCodeVal: [''],
                    isModel: GetModel.NotSent
                })
            } else {//已分配
                const { isCompanyCodeList, isCompanyCodeVal } = this.state;
                if (isCompanyCodeVal[0] === "") {
                    this.setState({
                        isCompanyCodeVal: Array.isArray(isCompanyCodeList) && isCompanyCodeList.length > 0 ? [isCompanyCodeList[0].Value] : [""],
                        isModel: GetModel.Sent
                    })
                }
            }
            this.setState({
                isAllocateVal: [i18n1519SelectList['i18n' + value[0]].Value],
                ajaxData: [],//ajaxData(原始資料)
                showLeft: { ...initOpenData },//showLeft(顯示左邊資料)
                showRight: [],//showRight(顯示右邊資料)
            })
        } else if (NBSelectNames.CompanyCodeI18nKey === title) {
            this.setState({
                isCompanyCodeVal: [...value],
                ajaxData: [],//ajaxData(原始資料)
                showLeft: { ...initOpenData },//showLeft(顯示左邊資料)
                showRight: [],//showRight(顯示右邊資料)
            })
        }
    }
    //初始化左邊資料
    habdleUpdateDefNBList = (list) => {
        this.setState({
            ajaxData: [...list],//ajaxData(原始資料)
            showLeft: { NBList: [...list], Company: '', Remark: '' },//showLeft(顯示左邊資料)
            showRight: [],//showRight(顯示右邊資料)
        })
    }
    //更新左邊資料
    onUpdNBList = (list) => {
        this.setState({
            showLeft: { NBList: [...list], Company: '', Remark: '' },//showLeft(顯示左邊資料)   
            showRight: [],//showRight(顯示右邊資料)       
        })
    }
    onUpdDataVal = (name, value) => {
        this.setState((prevState, props) => ({
            showLeft: {
                ...prevState.showLeft,
                [name]: value,
            }
        }))
    }
    handleSubmit = (data) => {//彈跳視窗-確認送出
        const { isModel } = this.state;
        if (isModel === GetModel.NotSent || isModel === GetModel.Sent) {
            this.handleNotSentAndSentSumit(data); // 匯入與分配 - 分配按鈕
        } else if (isModel === GetModel.Del) {
            this.handleDelSubmit(data);
        }
    }
    // 匯入與分配 - 分配按鈕
    handleNotSentAndSentSumit = (data) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { isModel, isAllocateVal } = this.state;
        //未分配情況，選單不可為空白
        if (isAllocateVal[0] === Object.values(i18n1519SelectList)[0].Value && (ModelTable.i18n17[isModel] === undefined || data?.Company === "")) { return new Promise((resolve, reject) => (reject(''))); }
        //確認有資料
        if (data?.NBList === undefined || !Array.isArray(data.NBList)) { return new Promise((resolve, reject) => (reject(''))); }
        const filterList = data.NBList.filter(item => item.checked);
        if (filterList.length === 0) { return new Promise((resolve, reject) => (reject(''))); }
        const list = Object.values(i18n1519SelectList).filter(item => item.Value === isAllocateVal[0]);
        if (list.length === 0) { return new Promise((resolve, reject) => (reject(''))); }
        const postData = {
            NBList: [...(filterList.map(item => item.NBID))],
            Company: data.Company,
            Allocate: list[0].SentModel,
            Remark: data.Remark,
            UserName: this.props.username

        };
        // 分配通訊序號(POST):
        return ajaxUpdNBListAllocate({query,postData}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { NBList, Message } = msg;
                const newData = data.NBList.filter(item => !item.checked);
                this.setState({
                    ajaxData: [...newData],
                    showLeft: { NBList: [...newData], Remark: '', Company: '' },//showLeft(顯示左邊資料)  
                    showRight: [...NBList],//showRight(顯示右邊資料)  
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: Message,//填寫訊息  
                })
                this.handleClose();
            } else if (code === '07') {
                const { Message } = msg;
                this.setState((prevState, prevProps) => ({
                    ajaxData: [...prevState.ajaxData],
                    showLeft: { ...prevState.showLeft, NBList: [...prevState.ajaxData], },
                    showRight: [],//showRight(顯示右邊資料)
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: Message,//填寫訊息
                }));
                this.handleClose();
            } else {
                console.error('匯入與分配 ajaxUpdNBListAllocate', response)
                const { Message } = msg;
                this.setState((prevState, prevProps) => ({
                    ajaxData: [...prevState.ajaxData],
                    showLeft: { ...prevState.showLeft, NBList: [...prevState.ajaxData] },
                    showRight: [],//showRight(顯示右邊資料)
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: Message,//填寫訊息
                }))
                this.handleClose();
            }
        });
    }
    // 匯入與分配-刪除按鈕
    handleDelSubmit = (data) => {//左邊視窗刪除功能
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { isModel, isAllocateVal } = this.state;
        //未分配情況，選單不可為空白
        if (isAllocateVal[0] === Object.values(i18n1519SelectList)[0].Value && ModelTable.i18n17[isModel] === undefined) { return new Promise((resolve, reject) => (reject(''))); }
        //已分配情況，不可有刪除
        if (isAllocateVal[0] === Object.values(i18n1519SelectList)[1].Value) { return new Promise((resolve, reject) => (reject(''))); }
        //確認有資料
        if (data?.NBList === undefined || !Array.isArray(data.NBList)) { return new Promise((resolve, reject) => (reject(''))); }
        const filterList = data.NBList.filter(item => item.checked);
        if (filterList.length === 0) { return new Promise((resolve, reject) => (reject(''))); }
        const postData = {
            NBList: [...filterList],
            Active: "15",
            Remark: data.Remark,
            UserName: this.props.username
        };
        // 修改通訊序號狀態/刪除通訊序號(POST):
        return ajaxUpdNBListActive({query,postData}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { Message } = msg;
                const newData = data.NBList.filter(item => !item.checked);
                this.setState({
                    ajaxData: [...newData],
                    showLeft: { NBList: [...newData], Remark: '', Company: '' },//showLeft(顯示左邊資料)  
                    showRight: [],//showRight(顯示右邊資料)  
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: Message,//填寫訊息  
                })
                this.handleClose();
            } else if (code === '07') {
                const { Message } = msg;
                this.setState((prevState, prevProps) => ({
                    ajaxData: [...prevState.ajaxData],
                    showLeft: { ...prevState.showLeft, NBList: [...prevState.ajaxData], },
                    showRight: [],//showRight(顯示右邊資料)
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: Message,//填寫訊息
                }));
                this.handleClose();
            } else {
                console.error('匯入與分配 ajaxUpdNBListActive', response)
                const { Message } = msg;
                this.setState((prevState, prevProps) => ({
                    ajaxData: [...prevState.ajaxData],
                    showLeft: { ...prevState.showLeft, NBList: [...prevState.ajaxData] },
                    showRight: [],//showRight(顯示右邊資料)
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: Message,//填寫訊息
                }))
                this.handleClose();
            }
        });
    }

    // 匯入與分配-查找按鈕
    onSearchFrameList = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { isCompanyCodeVal, isAllocateVal, isStart, isEnd } = this.state;
        this.setState({ 
            loadingSearchBtn: true,
            showLeft: { NBList: [], Remark: '', Company: '' },//showLeft(顯示左邊資料)  
            showRight: [],//showRight(顯示右邊資料)  
         });
        const data = {
            companyCode: isCompanyCodeVal,
            allocate: isAllocateVal[0],
            active: '',
            start: isStart,
            end: isEnd,
        };
        // 通訊序號列表(GET):
        return ajaxGetNBList({query,data}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { NBList } = msg;
                const newData = NBList.map((item) => {
                    return { ...item, checked: false }
                });
                this.setState({
                    ajaxData: [...newData],
                    showLeft: { NBList: newData, Remark: '', Company: '' },//showLeft(顯示左邊資料)  
                    showRight: [],//showRight(顯示右邊資料)    
                })
            } else if (code === '07') {
                this.setState({
                    ajaxData: [],
                    showLeft: { ...initOpenData },
                    showRight: [],//showRight(顯示右邊資料)
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                })
            } else {
                console.error('匯入與分配 ajaxGetNBList', response)
                this.setState({
                    ajaxData: [],
                    showLeft: { ...initOpenData },
                    showRight: [],//showRight(顯示右邊資料)
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                })
            }
            this.setState({ loadingSearchBtn: false });
        });
    }
    //取選擇公司清單(GET):
    getCompanyList = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        ajaxGetCompanyList({query}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { List } = msg;
                const { isAllocateVal } = this.state;
                this.setState({
                    isCompanyCodeList: [...List],
                    isCompanyCodeVal: [isAllocateVal[0] === Object.values(i18n1519SelectList)[1].Value && Array.isArray(List) && List?.length > 0 ? List[0].Value.toString() : ""]
                })
            } else {
                this.setState({ isCompanyCodeList: [], isCompanyCodeVal: [''] });
            }
        });
    }
    //箭頭功能
    onClickArrowFloor = () => {
        const { showLeft, isAllocateVal } = this.state;
        if (showLeft?.NBList === undefined || !Array.isArray(showLeft.NBList)) { return; }
        const filterList = showLeft.NBList.filter(item => item.checked);
        if (filterList.length > 0) {
            this.setState({
                openDialog: true,
                isModel: isAllocateVal[0] === Object.values(i18n1519SelectList)[0].Value ? GetModel.NotSent : GetModel.Sent,
            });
            this.props.onIsRefreshChange(false);   //停止5min刷新頁面
        } else {
            this.handleClose();
        }
    }
    //設定成刪除模式功能
    onClickSetDelModel = () => {
        const { showLeft } = this.state;
        if (showLeft?.NBList === undefined || !Array.isArray(showLeft.NBList)) { return; }
        const filterList = showLeft.NBList.filter(item => item.checked);
        if (filterList.length > 0) {
            this.setState({
                openDialog: true,
                isModel: GetModel.Del
            });
            this.props.onIsRefreshChange(false);   //停止5min刷新頁面
        } else {
            this.handleClose();
        }
    }

    // 彈跳視窗
    handleClose = () => {
        const { isAllocateVal } = this.state;
        this.setState((prevState, prevProps) => ({
            openDialog: false,
            isModel: isAllocateVal[0] === Object.values(i18n1519SelectList)[0].Value ? GetModel.NotSent : GetModel.Sent,
            showLeft: {
                ...prevState.showLeft,
                Remark: '',
            }
        }));
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面
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

}

Page1513.defaultProps = {
    onIsRefreshChange: () => { },
}

Page1513.propTypes = {
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
        setTreeFunctionId: (functionId) => dispatch(setTreeFunctionId(functionId)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Page1513);