import React,{Component} from 'react';
import { connect } from 'react-redux';
import { Translation } from 'react-i18next';

import { getForgetPw } from './getApi';

import { ErrorAlert, SuccessAlert } from '../../components/CusAlert';
import CusAccountInput from '../../components/CusAccountInput';
import CusEmailInput from '../../components/CusEmailInput';


class ForgetPwContent extends Component {
    constructor(props){
        super(props)
        this.state = {
            account:'',
            email: '',
            errorMsg: '',
            successMsg: '',
            disabledBtn: false,
        }
    }
    
    componentDidMount() {}

    render() {
        const { account,email,errorMsg,successMsg,disabledBtn } = this.state;
        return (
            <div className=" col-10 col-sm-3 text-center login">
                { errorMsg !== '' && <ErrorAlert message={<Translation>{(t) =><>{t(errorMsg)}</>}</Translation>} />}
                { successMsg !== '' && <SuccessAlert message={<Translation>{(t) =><>{t(successMsg)}</>}</Translation>} />}
                <div className="col-12 pt-2 pb-4">
                    <div>
                        {/* 文字-忘記密碼 */}
                        <Translation>
                            {(t) => <>{t('1105')}</>}
                        </Translation>
                    </div>
                    <div style={{fontSize: '12px'}}>
                        {/* 文字-請提供你的用戶名和電子邸件地址 */}
                        <Translation>
                            {(t) => <>{t('1107')}</>}
                        </Translation>
                    </div>
                </div>

                {/* 帳號 */}
                <label className="col-12 account">
                    <CusAccountInput 
                        placeholderName='1102' 
                        value={account} 
                        onChangeEvent={(event) => { this.changeAccount(event) }} 
                    />
                </label>
                {/* 電子郵件地址 */}
                <label className="col-12 email">
                    <CusEmailInput 
                            placeholderName='1731' 
                            value={email} 
                            onChangeEvent={(event)=>{this.changeEmail(event)}} 
                        />
                </label>

                <div className="col-12 mt-4 pt-3 align-middle" style={{borderTop:'1px solid black'}}>
                    <div className="col-6 d-inline-block text-left p-0 loginbtn--changePage disabled" onClick={()=>{this.props.changePage('login')}}>
                        {/* 文字-返回到登入畫面 */}
                        <Translation>
                            {(t) => <>{t('1108')}</>}
                        </Translation>
                    </div>
                    {
                        (account !=='' && email !== '' && disabledBtn === false) ? (
                            <div className="col-6 d-inline-block text-right p-0">
                                <button type="submit" className="btn btn-light" onClick={()=>{this.resetPwSubmitHandler()}}>
                                    {/* 文字-重置密碼 */}
                                    <Translation>
                                        {(t) => <>{t('1106')}</>}
                                    </Translation>
                                </button>
                            </div>
                        ):(
                            <div className="col-6 d-inline-block text-right p-0">
                                <button type="submit" className="btn btn-light disabled">
                                    {/* 文字-重置密碼 */}
                                    <Translation>
                                        {(t) => <>{t('1106')}</>}
                                    </Translation>
                                </button>
                            </div>
                        )
                    }
                </div>
            </div>
        )
    }


    // Function
    changeAccount = (event) => {
        let inputVal = event.target.value;
        this.setState({account: inputVal});
    }

    changeEmail = (event) => {
        let inputVal = event.target.value;
        this.setState({email: inputVal});
    }

    resetPwSubmitHandler = () => {
        const { account,email} = this.state;
        const list = {
            msg: {
                "Account": account,
                "Email": email
            }
        }
        this.setState({disabledBtn: true})
        getForgetPw({list}).then( response => {
            if(response.code === '00') {
                this.setState({successMsg: response.msg,errorMsg:'',account: '',email: ''})
                this.timer();
                clearTimeout(this.timer());
                this.setState({disabledBtn: false})
            }else {
                this.setState({successMsg:'',errorMsg: response.msg,account: '',email: ''})
                this.setState({disabledBtn: false})
            }
        })
    }

    timer = () => {
        setTimeout( () => {
            // 跳轉至登入頁
            this.props.changePage('login')
        },5000)
    }

}



const mapStateToProps = (state , ownProps) => {return {}}
export default connect(mapStateToProps)(ForgetPwContent);