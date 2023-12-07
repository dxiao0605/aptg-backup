import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';

import { NBSelectNames } from './InitDataFormat';
import { ajaxGetIMPTypeCompany, ajaxUpdIMPType } from './getApi';

import SimpleSelect from './SimpleSelect';//選單
import AlertMsg from '../../../components/AlertMsg';//訊息視窗


class Page1801 extends Component {
    // 匯入與分配
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props)
        this.state = {
            functionId: '1800',//funcId
            pageId: '1801',//頁面Id
            limits: {//權限設定
                Edit: 0,
                Button: {}
            },
            loading: true,//總畫面讀取
            isIMPTypeList: [],//內阻List
            isIMPTypeVal: [''],//內阻
            isCompanyCodeList: [],//公司清單
            isCompanyCodeVal: [""],//公司val
            isMappingList: [],//公司別與內阻的mapping表

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
        this.getIMPTypeCompany([], []);
    }
    componentWillUnmount() {
        this.ajaxCancel = true;
        this.setState = (state, callback) => {
            return;
        };
    }

    render() {
        const { loading, limits, isIMPTypeList, isIMPTypeVal, isCompanyCodeList, isCompanyCodeVal } = this.state;
        if (loading) {
            return ''
        } else {
            return (
                <Fragment>
                    {/* 通訊序號分配 */}
                    <div className="col-12 col-xl-6 mt-4 pl-0 pr-0">
                        {/* 通訊序號分配 */}
                        <div className="font-weight-bold col-12" style={{ backgroundColor: '#525b6c', color: '#fff', lineHeight: '3' }}><Trans i18nKey={'1803'} /></div>
                        <div className="px-4 py-4 bg-white">
                            <div className="form-inline align-items-center">
                                {/* 選擇公司 */}
                                {
                                    this.props.company === "1" &&
                                    <div className="form-inline ml-xl-2 w-100 my-2">
                                        <label className="mr-1 my-1" ><Trans i18nKey={'1074'} />：</label>
                                        <SimpleSelect
                                            className="my-1 col-xl-1 col-12"
                                            title={NBSelectNames.CompanyCodeI18nKey}
                                            openI18nKey={false}
                                            openSelMultiple={false}
                                            defSelectVal={isCompanyCodeVal}
                                            defSelectList={isCompanyCodeList}
                                            onChangeHandler={this.onUpdateCompanySelect}
                                            disabled={limits.Button.P1801 === 1 ? false : true}
                                        />
                                    </div>
                                }
                                {/* 選擇以內阻[µΩ/mΩ]或電導值[S]呈現 */}
                                <div className="form-inline ml-xl-2 w-100 my-2">
                                    <label className="mr-1 my-1" ><Trans i18nKey={'1804'} />：</label>
                                    <SimpleSelect
                                        className="my-1 col-xl-1 col-12"
                                        title={NBSelectNames.IMPTypeI18nKey}
                                        openI18nKey={false}
                                        openSelMultiple={false}
                                        defSelectVal={isIMPTypeVal}
                                        defSelectList={isIMPTypeList}
                                        onChangeHandler={this.onUpdateIMPTypeSelect}
                                        disabled={limits.Button.P1801 === 1 ? false : true}
                                    />
                                </div>
                                {/* 確認 */}
                                {
                                    limits.Button.P1801 === 1 &&
                                    <div className="form-inline ml-xl-2 w-100 my-2 justify-content-end">
                                        < button type="button" className="btn btn-sm btn-secondary exportBtnShadow col-12 col-xl-2 px-0" style={{ background: '#03c3ff', borderColor: '#03c3ff' }}
                                            onClick={() => { this.handleSubmit() }}
                                        ><Trans i18nKey={'1010'} /></button>
                                    </div>
                                }
                            </div>
                        </div>
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
                </Fragment >
            )
        }
    }
    onUpdateCompanySelect = (title, value) => {
        if (value.length === 0) { return }
        const { isMappingList, isIMPTypeList } = this.state;
        const isIMPTypeVal = isMappingList.filter(item => item.CompanyCode.toString() === value[0].toString());
        this.setState({
            isCompanyCodeVal: value,
            isIMPTypeVal: [isIMPTypeVal.length === 1 ? isIMPTypeVal[0].IMPType.toString() : isIMPTypeList[0].Value.toString()],
        })
    }

    onUpdateIMPTypeSelect = (title, value) => {
        if (value.length === 0) { return }
        this.setState({
            isIMPTypeVal: value,
        })
    }

    handleSubmit = () => {//確認送出
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        const { limits } = this.state;
        const { isCompanyCodeVal, isIMPTypeVal } = this.state;
        if (limits?.Button === undefined && limits.Button.P1801 !== 1) { return new Promise((resolve, reject) => (reject(''))); }
        const postData = {
            Company: this.props.company === "1" ? isCompanyCodeVal[0] : this.props.company,
            IMPType: isIMPTypeVal[0],
            UserName: this.props.username
        };
        return ajaxUpdIMPType({query,postData}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.setState((prevState, prevProps) => ({
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                }));
                this.getIMPTypeCompany(isCompanyCodeVal, isIMPTypeVal);
            } else if (code === '07') {
                this.setState((prevState, prevProps) => ({
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                }));
            } else {
                console.error('內阻/電導值 ajaxUpdIMPType', response)
                this.setState((prevState, prevProps) => ({
                    openAlertMsg: true,//開啟訊息視窗
                    titleAlertMsg: '1089',
                    textAlertMsg: msg,//填寫訊息
                }))
            }
        });
    }


    //公司清單及內阻呈現下拉選單API(GET):
    getIMPTypeCompany = (isCompanyCodeVal, isIMPTypeVal) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const query = { token, curLanguage, timeZone, company };
        this.setState({ isCompanyCodeList: [], isIMPTypeList: [], isMappingList: [] });
        ajaxGetIMPTypeCompany({query}).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { Company, IMPType, Mapping } = msg;
                const isIMPType = Mapping.filter(item => item.CompanyCode.toString() === Company[0].Value.toString());
                this.setState({
                    isCompanyCodeList: [...Company],
                    isCompanyCodeVal: [isCompanyCodeVal.length > 0 ? isCompanyCodeVal[0] : Company[0].Value.toString()],
                    isIMPTypeList: [...IMPType],
                    isIMPTypeVal: [isIMPTypeVal.length > 0 ? isIMPTypeVal[0] : isIMPType[0].IMPType.toString()],
                    isMappingList: [...Mapping]
                })
            } else {
                this.setState({ isCompanyCodeList: [], isCompanyCodeVal: [''], isIMPTypeList: [], isIMPTypeVal: [''], isMappingList: [] });
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
}

Page1801.defaultProps = {
    onIsRefreshChange: () => { },
}

Page1801.propTypes = {
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
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Page1801);