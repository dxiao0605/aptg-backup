import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { apipath, ajax } from '../../../utils/ajax';
import { Trans } from 'react-i18next';
import AlertMsg from '../../../components/AlertMsg';//訊息視窗
import { initImportData, fileFormat } from './InitDataFormat';
import SimpleSelect from './SimpleSelect';//選單
import { updateLogo } from '../../../actions/MainNavAction';


const apiUrl = apipath();
class Page1802 extends Component {
    // 匯入與分配
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props)
        this.state = {
            functionId: '1800',//funcId
            pageId: '1802',//頁面Id
            limits: {//權限設定
                Edit: 0,
                Button: {}
            },
            loading: true,//總畫面讀取

            isShowName: initImportData.isShowName,//顯示名稱(寬)
            isShortName: initImportData.isShowName,//顯示名稱(窄)
            isPngName: initImportData.FileName,//公司logo  
            isCompanyCodeList: [],//公司別清單
            isCompanyCodeVal: [this.props.company],//公司別Value

            //訊息視窗            
            openAlertMsg: false,//開啟訊息視窗
            titleAlertMsg: '',//訊息視窗-標頭
            textAlertMsg: '',//訊息視窗-文字欄位
            closeAlertMsgRefresh: false,//關閉訊息視窗時，啟動重新整理功能
        }
        this.fileInput = React.createRef();
        this.defLogoBig = React.createRef();
        this.newLogoBig = React.createRef();

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
            closeAlertMsgRefresh: false,
        });
        this.getIMPTypeCompany().then(() => {
            this.getCompanyLogo(this.state.isCompanyCodeVal[0]);
        });
    }
    // componentDidUpdate(prevProps, prevState) {
    //     if (this.props.curLanguage !== undefined && this.props.curLanguage !== prevProps.curLanguage) {}
    // }
    componentWillUnmount() {
        this.ajaxCancel = true;
        this.setState = (state, callback) => {
            return;
        };
    }

    render() {
        const { loading, limits, isShowName, isShortName } = this.state;
        const { isCompanyCodeVal, isCompanyCodeList } = this.state;
        if (loading) {
            return ''
        } else {
            return (
                <Fragment>
                    {/* 通訊序號分配 */}
                    <div className="col-12 col-xl-6 mt-4 pl-0 pr-0">
                        {/* 通訊序號分配 */}
                        <div className="font-weight-bold col-12" style={{ backgroundColor: '#525b6c', color: '#fff', lineHeight: '3' }}><Trans i18nKey={'1805'} /></div>
                        <div className="px-4 py-4 bg-white">
                            <div className="form-inline align-items-center">
                                {/* 選擇公司 */}
                                {
                                    this.props.company === "1" &&
                                    <div className="form-inline ml-xl-2 w-100 my-2">
                                        <label className="mr-1 my-1" ><Trans i18nKey={'1074'} />：</label>
                                        <SimpleSelect
                                            className="my-1 col-xl-1 col-12"
                                            title={'isCompanyCodeVal'}
                                            openI18nKey={false}
                                            openSelMultiple={false}
                                            defSelectVal={isCompanyCodeVal}
                                            defSelectList={isCompanyCodeList}
                                            onChangeHandler={this.onUpdateCompanySelect}
                                            disabled={false}
                                        />
                                    </div>
                                }
                                {/* 公司顯示名稱 */}
                                <div className="d-flex align-items-center ml-xl-2 w-100 my-2" style={{ flexFlow: 'row wrap' }}>
                                    <label className="mr-1 my-1 col-12 px-0" style={{ justifyContent: 'start' }}><Trans i18nKey={'1806'} />：</label>
                                    <label className="mr-1 my-1 col-12 px-0" style={{ justifyContent: 'start' }}><Trans i18nKey={"1812"} /></label>
                                    <input type="text my-1" className="px-2 col-xl-6 col-12" name={'isShowName'} value={isShowName} maxLength="10" onChange={(e) => { this.HandleInputChange(e) }} disabled={limits.Button.P1802 === 1 ? false : true} />
                                    <label className="mr-1 my-1 col-12 px-0" style={{ justifyContent: 'start' }}><Trans i18nKey={"1808"} /></label>
                                    <input type="text my-1" className="px-2 col-xl-6 col-12" name={'isShortName'} value={isShortName} maxLength="5" onChange={(e) => { this.HandleInputChange(e) }} disabled={limits.Button.P1802 === 1 ? false : true} />
                                </div>
                                {/* 公司logo圖檔 */}
                                <div className="d-flex align-items-center ml-xl-2 w-100 my-2" style={{ flexFlow: 'row wrap' }}>
                                    {/* 公司顯示名稱 */}
                                    <label className="my-1 col-12 px-0" style={{ justifyContent: 'start' }}><Trans i18nKey={"1811"} /></label>
                                    {/* 圖 */}
                                    <div className="p-2 col-xl-6 col-12 rounded border border-secondary">
                                        <div className="form-inline my-1" style={{ justifyContent: 'space-around' }}>
                                            <div style={{ width: '5em' }}><Trans i18nKey={'1813'} /></div>
                                            <div className="logo_bg rounded" style={{ width: "250px", padding: "1rem" }}>
                                                <div className="mainNav_logo-top-big" style={{ justifyContent: 'space-around',height: "75px", width: "200px" }} ref={this.defLogoBig}>
                                                    <div className=""  />
                                                </div>
                                            </div>
                                        </div>
                                        {limits.Button.P1802 === 1 &&
                                            <div className="form-inline my-1" style={{ justifyContent: 'space-around' }}>
                                                <div style={{ width: '5em' }}><Trans i18nKey={'1814'} /></div>
                                                <div className="logo_bg rounded" style={{  width: "250px", padding: "1rem" }}>
                                                    <div className="mainNav_logo-top-big" style={{ justifyContent: 'space-around',height: "75px", width: "200px" }}  ref={this.newLogoBig}>
                                                        <div className="" />
                                                    </div>
                                                </div>
                                            </div>
                                        }
                                    </div>
                                    {/* 瀏覽 */}
                                    {limits.Button.P1802 === 1 &&
                                        <div className="my-1 col-xl-6 col-12 px-0 form-inline">
                                            <div className="col-12 col-xl-5 exportBtn btn-secondary exportBtnShadow ml-xl-3 my-2 cursor-pointer" style={{ background: '#03c3ff', borderColor: '#03c3ff', borderRadius:'0.25rem' }}>
                                                <div className="systemSetting-uploader">
                                                    <input type="file" className="custom-file-input" style={{height: '30px',cursor: 'pointer'}} data-target="file-uploader" id="file-uploader" accept={fileFormat.png.name} ref={this.fileInput} onChange={(e) => { this.habdleFileChange(e) }} />
                                                    <label className="pointer" htmlFor="file-uploader" style={{whiteSpace:'nowrap',lineHeight:'auto'}}>
                                                        <div className='systemSetting-uploader-btn'><Trans i18nKey={"1547"} /></div>
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    }
                                </div>
                                {/* 取消logo設定/確認 */}
                                {
                                    limits.Button.P1802 === 1 &&
                                    <div className="form-inline ml-xl-2 w-100 my-2 justify-content-end">
                                        <button type="button" className="btn btn-sm btn-secondary exportBtnShadow col-12 col-xl-2 px-0 my-2" style={{ background: '#03c3ff', borderColor: '#03c3ff', minWidth: '11em' }}
                                            onClick={() => { this.handleRevert() }}
                                        ><Trans i18nKey={'1810'} /></button>
                                        <button type="button" className="btn btn-sm btn-secondary exportBtnShadow col-12 col-xl-2 px-0 ml-2 my-2" style={{ background: '#03c3ff', borderColor: '#03c3ff', minWidth: '11em' }}
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
    //input onChange
    HandleInputChange = (e) => {
        this.setState({
            [e.target.name]: e.target.value,
        })
    }
    //取消logo設定
    handleRevert = () => {
        if (this.state.isCompanyCodeVal[0] === "") { return; }
        const postData = {
            "Company": this.state.isCompanyCodeVal[0],
            "UserName": this.props.username,
        };
        this.ajaxDelLogo(postData).then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                this.handleAlertMsgOpen('1089', msg);
                this.setState({ closeAlertMsgRefresh: true });
                this.onInit(this.state.isCompanyCodeVal[0]);
            } else {
                console.error('公司設定 ajaxDelLogo', response)
                this.handleAlertMsgOpen('1089', msg);
            }
        });
    }
    //初始化
    onInit = (companyCode) => {
        this.getCompanyLogo(companyCode);
        this.fileInput.current.value = initImportData.FileValue;
        this.fileInput.current.files = initImportData.File;
        this.newLogoBig.current.innerHTML = '';
    }
    //select onChange
    onUpdateCompanySelect = (title, value) => {
        if (value.length === 0) { return }
        this.setState({
            [title]: value,
        });
        this.onInit(value[0]);
    }

    //瀏覽
    habdleFileChange = (event) => {
        if (event.target.files?.length > 0) {
            const files = event.target.files[0];
            if (files.type !== fileFormat.png.type) { return; }
            this.setState({
                isPngName: files.name
            });
            if (FileReader) {
                var fr = new FileReader();
                fr.onload = () => {
                    this.newLogoBig.current.innerHTML = '';
                    //寬版               
                    let imgBig = document.createElement('img');
                    imgBig.src = fr.result;
                    this.newLogoBig.current.appendChild(imgBig);
                }
                fr.readAsDataURL(files);
            }
        } else {
            this.fileInput.current.files = initImportData.File;
            this.fileInput.current.value = initImportData.FileValue;
            this.setState({ isPngName: initImportData.FileName });
            this.newLogoBig.current.innerHTML = '';
        }
    }

    //上傳公司Logo API(POST):
    //https://www.gtething.tw/battery/uploadLogo
    handleSubmit = () => {//確認送出        
        if (this.state.isCompanyCodeVal[0] === '') { return }
        const { username, token, curLanguage, timeZone, company } = this.props;
        let formData = new FormData();
        formData.append('username', username);
        formData.append('showName', this.state.isShowName);
        formData.append('shortName', this.state.isShortName);
        formData.append('Company', this.state.isCompanyCodeVal[0]);
        formData.append('files', this.fileInput?.current?.files[0] === undefined ? null : this.fileInput.current.files[0]);
        let xhr = new XMLHttpRequest();  // 先new 一個XMLHttpRequest。
        xhr.open('POST', `${apiUrl}uploadLogo`);   // 設置xhr得請求方式和url。
        xhr.setRequestHeader('token', token);
        xhr.setRequestHeader('language', curLanguage);
        xhr.setRequestHeader('timezone', timeZone);
        xhr.setRequestHeader('company', company);
        xhr.onreadystatechange = (event) => {   // 等待ajax請求完成。
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面	
            if (xhr.status === 200) {
                if (xhr.responseText === "") { return; }
                console.log('File upload => Server Status:' + xhr.status + ' 伺服器已接收');
                const { msg, code } = JSON.parse(xhr.responseText);
                if (code === '00') {
                    console.log('File upload => Server Response Code:' + code + ' 上傳成功');
                    this.fileInput.current.files = initImportData.File;
                    this.setState({ filesName: initImportData.FileName, closeAlertMsgRefresh: true });
                    this.handleAlertMsgOpen('1089', msg);
                } else {
                    console.log('File upload => Server Response Code:' + code + ' 上傳失敗');
                    this.fileInput.current.files = initImportData.File;
                    this.setState({ filesName: initImportData.FileName });
                    this.handleAlertMsgOpen('1089', msg);
                }
            } else {
                console.log('File upload => Server Status:' + xhr.status + ' 伺服器發生錯誤');
                this.fileInput.current.files = initImportData.File;
                this.setState({ filesName: initImportData.FileName });
                this.handleAlertMsgOpen('1089', 'File upload => Server Status:' + xhr.status);
            }
        };
        // 獲取上傳進度
        xhr.upload.onprogress = (event) => {
            if (event.lengthComputable) {
                // var percent = Math.floor(event.loaded / event.total * 100);
                // document.querySelector("#progress .progress-item").style.width = percent + "%";
                // 設置進度顯示
                // this.setState({
                //     openAlertMsg: true,
                //     textAlertMsg: percent + "%"
                // })
            }
        };
        xhr.send(formData);
    }

    //公司清單及內阻呈現下拉選單API(GET):
    getIMPTypeCompany = () => {
        this.setState({ isCompanyCodeList: [], isCompanyCodeVal: [''] });
        return this.ajaxGetIMPTypeCompany().then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { Company, } = msg;
                this.setState({
                    isCompanyCodeList: [...Company],
                    isCompanyCodeVal: [Company[0].Value.toString()],
                });
            } else {
                this.setState({ isCompanyCodeList: [], isCompanyCodeVal: [''] });
            }
        });
    }

    //查詢公司Logo Name API(GET):
    getCompanyLogo = (companyCode) => {
        if (companyCode === '') { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
        this.ajaxGetCompanyLogo(companyCode).then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { ShowName, ShortName, ImagesFlag } = msg;
                this.setState({
                    isShowName: ShowName ? ShowName : initImportData.isShowName,
                    isShortName: ShortName ? ShortName : initImportData.isShowName
                });
                if (ImagesFlag === 1) {
                    this.getLogoImages(companyCode);
                    this.newLogoBig.current.innerHTML = '';
                } else {
                    this.fileInput.current.value = initImportData.FileValue;
                    this.fileInput.current.files = initImportData.File;
                    this.newLogoBig.current.innerHTML = '';
                    this.defLogoBig.current.innerHTML = '';
                }
            } else if (code === '07') {
                this.setState({ isShowName: initImportData.isShowName, isShortName: initImportData.isShowName, });
                this.fileInput.current.value = initImportData.FileValue;
                this.fileInput.current.files = initImportData.File;
                this.newLogoBig.current.innerHTML = '';
                this.defLogoBig.current.innerHTML = '';
            } else {
                console.error('公司設定', response)
                this.setState({ isShowName: initImportData.isShowName, isShortName: initImportData.isShowName, });
            }
        });
    }

    //公司Logo API(GET):
    getLogoImages = (companyCode) => {
        if (companyCode === '') { return }
        this.ajaxGetLogoImages(companyCode).then(response => response.blob())
            .then(images => {
                this.defLogoBig.current.innerHTML = '';
                //圖片URL
                const outside = URL.createObjectURL(images);
                //目前LOGO               
                let imgBig = document.createElement('img');
                imgBig.src = outside;
                this.defLogoBig.current.appendChild(imgBig);
            })
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
        if (this.state.closeAlertMsgRefresh) {
            this.props.updateLogo(true);
            const { isCompanyCodeVal } = this.state;
            this.getCompanyLogo(isCompanyCodeVal[0]);
        }
        this.props.onIsRefreshChange(true);    //重新開始5min刷新頁面       
    }

    //公司清單及內阻呈現下拉選單API(GET):
    ajaxGetIMPTypeCompany = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getIMPTypeCompany`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //內阻呈現下拉選單API(GET):
        //https://www.gtething.tw/battery/getIMPTypeCompany
    }

    //查詢公司Logo API(GET):
    ajaxGetCompanyLogo = (companyCode) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getCompanyLogo?companyCode=${companyCode}`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //查詢公司Logo API(GET):
        //https://www.gtething.tw/battery/getCompanyLogo?companyCode=
    }
    //公司Logo API(GET):
    ajaxGetLogoImages = (companyCode) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getLogoImages?companyCode=${companyCode}`;
        return fetch(url, {
            method: 'GET',
            headers: new Headers({
                'Accept': '*/*',
                'Content-Type': 'application/json',
                'token': token,
                'language': curLanguage,
                'timezone': timeZone,
                'company': company,
            }),
        })
        //公司Logo API(GET):
        //https://www.gtething.tw/battery/getLogoImages?companyCode=
    }
    //取消Logo設定API(POST):
    ajaxDelLogo = (postData) => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}delLogo`;
        return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
        //取消Logo設定API(POST):
        //https://www.gtething.tw/battery/delLogo
    }

}

Page1802.defaultProps = {
    onIsRefreshChange: () => { },
}

Page1802.propTypes = {
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
        updateLogo: (boolean) => dispatch(updateLogo(boolean)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Page1802);