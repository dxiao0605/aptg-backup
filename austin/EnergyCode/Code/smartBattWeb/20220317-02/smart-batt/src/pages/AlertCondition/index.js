import React ,{Component, Fragment} from 'react';
import { connect } from 'react-redux';
import {setTreeFunctionId,resetTreeFunctionId} from '../../actions/MainNavAction';
import {apipath,ajax} from '../../utils/ajax';
import {fetchFilterCompanyLoad} from '../../actions/FilterCompanyAction';
// i18n
import { Translation } from 'react-i18next';
// component
import Button from '@material-ui/core/Button';
import MenuItem from '@material-ui/core/MenuItem';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import Divider from '@material-ui/core/Divider';
import PageTabHeader from '../../components/PageTabHeader';
import CusAccordion from '../../components/CusAccordion';
import { ErrorAlert,SuccessAlert } from '../../components/CusAlert';            // 成功訊息
import CusInput from '../../components/CusInput';
import {CusSelectStyle , menuUnderSelectStyle} from '../../components/CusSelectStyle';
import { CusMainBtnStyle } from '../../components/CusMainBtnStyle';

const apiUrl = apipath();
class AlertCondition extends Component {
    constructor(props){
        super(props)
        this.state={
            loading: true,
            isDisabledBtn: false,                                               // 防止重複點擊
            headerList:[                                                        // 頁面頁籤
                {Name: '1301',Url:'/AlertUnsolved',Active:false},
                {Name: '1302',Url:'/AlertSolved',Active:false},
                {Name: '1303',Url:'/AlertCondition',Active:true},
            ],
            selectedCompany: this.props.company,                                // 目前選擇的公司
            data: {},                                                           // 目前告警條件
            list: {
                msg: {
                    CompanyCode: 0,
                    IMPType: 0,
                    Alert1: 0,                                                  // 判斷數值1
                    Alert2: 0,                                                  // 判斷數值2
                    Temperature1: 0,                                             // 溫度告警值
                    Disconnect: 0,                                              // 離線時數
                    UserName: this.props.username
                }
            },
            successMsg: '',                                                     // 成功訊息
            errorMsg: '',                                                       // 錯誤訊息
        }
    }



    // React Lifecycle
    componentDidMount() {
        const {token,company} = this.props;                                     // 目前已選擇公司代碼        
        this.props.setTreeFunctionId('1303');                                   // 設定功能選單指項位置
        this.props.fetchFilterCompanyLoad({token:token,company:company});       // 篩選公司
        this.setState({loading: false})
        // 當篩選清單內只有一筆時做,查詢自己公司告警條件
        this.setState( (prevState) => ({
            list: {
                msg:{
                    ...prevState.list.msg,
                    CompanyCode: company
                }
            },
            selectedCompany: company
        }))
        // 查看所選公司目前告警條件
        this.getAlertSetup(company);
    }
    componentDidUpdate(prevProps, prevState){}
    componentWillUnmount(){}




    render() {
        const {filterCompany,filterCompanyError,functionList} = this.props;
        const {loading,isDisabledBtn,selectedCompany,successMsg,errorMsg} = this.state;
        const { Alert1,Alert2,Temperature1,Disconnect } = this.state.list.msg;
        const pageInfo = functionList.filter(element => element.FunctionId === 1303)[0];
        if(!loading){
            const {headerList} = this.state;
            return(
                <Fragment>
                    {/* 告警條件 */}
                    <PageTabHeader list={headerList} /> 
                    {/* 成功/失敗訊息 */}
                    {
                        (successMsg === '') ?  ""
                        : (
                            <SuccessAlert  message={successMsg} resetMessage={()=>{this.resetSuccessMessage()}}/> 
                        )
                    }
                    {
                        (errorMsg === '') ?  ""
                        : (
                            <ErrorAlert  message={errorMsg} resetMessage={()=>{this.resetErrorMessage()}}/>
                        )
                    }
    
    
                    <div className=" col-12 col-md-6 m-auto">
                        <div className="mt-4">
                            {/* 選擇公司 */}
                            {
                                filterCompany.length <= 1 ? ''
                                : (
                                    <CusAccordion 
                                        title={
                                            <Translation>
                                                {(t) => <>{t('1074')}</>}
                                            </Translation>
                                        } >
                                        <FormControl className="col-12">
                                            <Select
                                                labelId='language-select-label'
                                                id='language-select'
                                                value={`${selectedCompany}`}
                                                onChange={(e)=>{this.onChangeCompany(e)}}
                                                MenuProps={menuUnderSelectStyle}    //選單從下方顯示
                                                input={<CusSelectStyle />}
                                            >
                                                {
                                                    filterCompany.length <= 0 ? ''
                                                    : (
                                                        filterCompany.map( (item) => {
                                                            return (
                                                                <MenuItem key={item.Value} value={item.Value}>{item.Label}</MenuItem>
                                                            )
                                                        })
                                                    ) 
                                                }
                                            </Select>
                                            {filterCompanyError !== '' && <FormHelperText>{filterCompanyError}</FormHelperText>}
                                        </FormControl>
            
                                    </CusAccordion>
                                )
                            }
                            

                            {/* 選擇公司後再顯示該公司目前設定值 */}
                            {
                                (selectedCompany && selectedCompany !== 0) ? (
                                    <Fragment>
                                        {/* 判斷數值 */}
                                        <CusAccordion title={
                                            <Translation>
                                                {(t) => <>{t('1318')}</>} 
                                                </Translation>
                                            } >
                                            {/* 判定值1 */}
                                            <div>
                                                <div className="pt-2">
                                                    <Translation>{(t) => <>{t('1032')}</>}</Translation>
                                                </div>
                                                <div className="pb-2">
                                                    <CusInput name="Alert1" value={Alert1} isFullWidth={true} onChangeEvent={(e)=>{this.onAlertInputChange(e)}} disabled={pageInfo.Edit === 1 ? false : true} />
                                                </div>
                                            </div>
                                            <Divider/>
                                            {/* 判定值2 */}
                                            <div>
                                                <div className="pt-2">
                                                    <Translation>{(t) => <>{t('1033')}</>}</Translation>
                                                </div>
                                                <div className="pb-2">
                                                    <CusInput name="Alert2" value={Alert2} isFullWidth={true} onChangeEvent={(e)=>{this.onAlertInputChange(e)}} disabled={pageInfo.Edit === 1 ? false : true} />
                                                </div>
                                            </div>
                                        </CusAccordion>
                                        {/* 溫度告警值 */}
                                        <CusAccordion 
                                            title={
                                                <Translation>
                                                    {(t) => <>{t('1323')}</>}
                                                </Translation>
                                            } >
                                            {/* 溫度告警值 */}
                                            <div className="pt-2">
                                                <Translation>{(t) => <>{t('1324')}</>}</Translation>
                                            </div>
                                            <div className="pb-2">
                                                <CusInput name='Temperature1' value={Temperature1} isFullWidth={true} onChangeEvent={(e)=>{this.onInputChange(e)}} disabled={pageInfo.Edit === 1 ? false : true} />
                                            </div>
                                        </CusAccordion>
                                        {/* 離線時數 */}
                                        <CusAccordion 
                                            title={
                                                <Translation>
                                                    {(t) => <>{t('1319')}</>}
                                                </Translation>
                                            } >
                                            {/* 小時數 */}
                                            <div className="pt-2">
                                                <Translation>{(t) => <>{t('1321')}</>}</Translation>
                                                <Translation>{(t) => <>{t('1326')}</>}</Translation>
                                            </div>
                                            <div className="pb-2">
                                                <CusInput name='Disconnect' value={Disconnect} isFullWidth={true} onChangeEvent={(e)=>{this.onInputChange(e)}} disabled={pageInfo.Edit === 1 ? false : true} />
                                            </div>
                                        </CusAccordion>



                                        <div className="text-right">
                                            {
                                                (isDisabledBtn || pageInfo.Edit !== 1) ? <Button variant="contained" disabled ><Translation>{(t) => <>{t('1001')}</>}</Translation></Button>
                                                : (
                                                    <CusMainBtnStyle 
                                                        name={
                                                            <Translation>
                                                                {(t) => <>{t('1001')}</>}
                                                            </Translation>
                                                        }  
                                                        icon="fas fa-check"
                                                        clickEvent={(e)=>{this.handleSubmit(e)}} />
                                                )
                                            }
                                        </div>
                                    </Fragment>
                                ) : ''
                            }
                        </div>


                    </div>
                    
                </Fragment>
            )
        }else {
            return ''
        }
    }
        

    // 清空成功訊息
    resetSuccessMessage = (e) => {
        const {successMsg} = this.state;
        if(successMsg !== ''){
            this.setState({successMsg: ''})
        }
        return 
    }
    // 清空錯誤訊息
    resetErrorMessage = (e) => {
        const {errorMsg} = this.state;
        if(errorMsg !== ''){
            this.setState({errorMsg: ''})
        }
        return 
    }

    // 變更所選公司
    onChangeCompany = (e) => {
        // 清空訊息
        this.setState({
            successMsg: '',
            errorMsg: '',
        })
        this.setState( (prevState) => ({
            list: {
                msg:{
                    ...prevState.list.msg,
                    CompanyCode: e.target.value
                }
            },
            selectedCompany: e.target.value
        }))

        // 查看所選公司目前告警條件
        this.getAlertSetup(e.target.value);
    }
    // 變更Input
    onInputChange = (e) => {
        const onlyNumber = e.target.value.replace(/[^\d]/,'');
        // 清空訊息
        this.setState({
            successMsg: '',
            errorMsg: '',
        })
        this.setState( (prevState) => ({
            list: {
                msg:{
                    ...prevState.list.msg,
                    [e.target.name]: onlyNumber
                }
            }
        }))
    }
    // 變更內組/電導判斷值1、2
    onAlertInputChange = (e) => {
        const { IMPType } = this.state.list.msg;
        const onlyNumber = e.target.value.replace(/[^\d]/,'');
        const floatNumber = e.target.value.replace(/[^0-9\.\s]/,'');
        // 清空訊息
        this.setState({
            successMsg: '',
            errorMsg: '',
        })

        switch (IMPType) {
            case 21:
                const str = e.target.value.toString();
                if(str.includes('.')){
                    const afterDot = str.substring(str.indexOf('.')+1,str.length );
                    if(afterDot.length <= 3) {
                        this.setState( (prevState) => ({
                            list: {
                                msg:{
                                    ...prevState.list.msg,
                                    [e.target.name]: floatNumber
                                }
                            }
                        }))
                    }
                }else {
                    this.setState( (prevState) => ({
                        list: {
                            msg:{
                                ...prevState.list.msg,
                                [e.target.name]: floatNumber
                            }
                        }
                    }))
                }
                break;
        
            default:
                this.setState( (prevState) => ({
                    list: {
                        msg:{
                            ...prevState.list.msg,
                            [e.target.name]: onlyNumber
                        }
                    }
                }));
                break;
        }

        

    }


    // 取得告警條件
    getAlertSetup = (value) => {
        // 查看所選公司目前告警條件
        this.fetchAlertSetup(value).then((response) => {
            if(response.code === '00' && response.msg) {
                this.setState((prevState) => ({
                    loading: false,
                    data: response.msg.AlertSetup,
                    list: {
                        msg: {
                            ...prevState.list.msg,
                            IMPType: response.msg.AlertSetup.IMPType,
                            Alert1: response.msg.AlertSetup.Alert1,             // 判斷數值1
                            Alert2: response.msg.AlertSetup.Alert2,             // 判斷數值2
                            Temperature1: response.msg.AlertSetup.Temperature1,   // 溫度告警值
                            Disconnect: response.msg.AlertSetup.Disconnect,     // 離線時數
                            UserName: this.props.username
                        }
                    },
                    errorMsg: '',
                }))
            }else {
                this.setState({loading:false,data:{},errorMsg: response.msg})
            }
        })
    }

    // 變更告警條件
    handleSubmit = (e) => {
        const { list } = this.state;
        const postData = {
            msg: {
                CompanyCode: list.msg.CompanyCode,
                IMPType: list.msg.IMPType,
                Alert1: parseFloat(list.msg.Alert1),        // 判斷數值1
                Alert2: parseFloat(list.msg.Alert2),        // 判斷數值2
                Temperature1: list.msg.Temperature1,        // 溫度告警值
                Disconnect: list.msg.Disconnect,            // 離線時數
                UserName: list.msg.username
            }
        }
        // 清空訊息
        this.setState({successMsg: '', errorMsg: '',})
        this.fetchUpdAlertSetup(postData).then( (response) => {
            if(response.code === '00'){
                this.setState({loading: false,successMsg: response.msg, errorMsg: '', })
            }else{
                this.setState({
                    loading: false,
                    list: {
                        msg: {
                            CompanyCode: response.msg.CompanyCode,
                            IMPType: response.msg.IMPType,
                            Alert1: response.msg.Alert1,                                // 判斷數值1
                            Alert2: response.msg.Alert2,                                // 判斷數值2
                            Temperature1: response.msg.Temperature1,                    // 溫度告警值
                            Disconnect: response.msg.Disconnect,                        // 離線時數
                            UserName: this.props.username
                        }
                    },
                    successMsg: '',
                    errorMsg: response.msg.Message
                })
            }
        })
    }


    // get api
    // 告警條件API(GET):
    fetchAlertSetup = (companyCode) => {
        const {token,curLanguage,timeZone} = this.props;
        const url = `${apiUrl}getAlertSetup`;
        this.setState({loading: true})
        return ajax(url,'GET',token,curLanguage,timeZone,companyCode)
        // https://www.gtething.tw/battery/getAlertSetup?companyCode=aptg
        /**
         * CompanyCode	公司代碼
         * IMPType	0: 內阻值 1:電導值 2:毫內阻
         * Alert1	警告值1
         * Alert2	警告值2
         * Disconnect	判斷斷線時間(小時)
         */
    }

    // 修改告警條件API(POST):
    fetchUpdAlertSetup = (list) => {
        const {token,curLanguage,timeZone} = this.props;
        const {selectedCompany} = this.state;
        const url = `${apiUrl}updAlertSetup`;
        this.setState({loading: true})
        return ajax(url,'POST',token,curLanguage,timeZone,selectedCompany,list)
        // https://www.gtething.tw/battery/getAlertSetup?companyCode=aptg
        /**
         * CompanyCode	公司代碼
         * IMPType	0: 內阻值 1:電導值 2:毫內阻
         * Alert1	警告值1
         * Alert2	警告值2
         * Temperature1 溫度告警值
         * Disconnect	判斷斷線時間(小時)
         */
    }






}
const mapStateToProps = ( state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        account:state.LoginReducer.account,
        username: state.LoginReducer.username,
        role: state.LoginReducer.role,
        company: state.LoginReducer.company,
        curLanguage: state.LoginReducer.curLanguage,                        // 目前語系
        timeZone: state.LoginReducer.timeZone,
        functionList: state.LoginReducer.functionList,
        filterCompany: state.FilterCompanyReducer.filterCompany,            // 篩選公司
        filterCompanyError: state.FilterCompanyReducer.filterCompanyError,  // 篩選公司錯誤

    }
}
const mapDispatchToProps = (dispatch,ownProps) => {
    return {
        //左側功能選單
        setTreeFunctionId: (value) => dispatch(setTreeFunctionId(value)),
        resetTreeFunctionId: () => dispatch(resetTreeFunctionId()),
        //篩選列表
        fetchFilterCompanyLoad: (data) => dispatch(fetchFilterCompanyLoad(data)),
    }
}
export default connect(mapStateToProps,mapDispatchToProps)(AlertCondition);