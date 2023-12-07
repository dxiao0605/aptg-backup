import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { updateAccount, updatePassword, fetchLoginLoad } from '../../actions/LoginAction';
import { Trans } from 'react-i18next';
import { ErrorAlert } from '../../components/CusAlert';
import CusAccountInput from '../../components/CusAccountInput';
import CusInputPassword from '../../components/CusInputPassword';




class LoginContent extends Component {

    componentDidMount() { }

    render() {
        const { account, password, errorMsg } = this.props;
        return (
            <div className=" col-10 col-sm-3 text-center login">
                { errorMsg !== '' && <ErrorAlert message={<Trans i18nKey={errorMsg} />} />}
                <div className="col-12 pt-2 pb-4">
                    {/* 文字-登入 */}
                    <Trans i18nKey="1101" />
                </div>

                <label className="col-12 account">
                    {/* 帳號 */}
                    <CusAccountInput placeholderName='1102' value={account} onChangeEvent={(event) => { this.changeAccount(event) }} />
                </label>

                <label className="col-12 password">
                    {/* 密碼 */}
                    <CusInputPassword placeholderName='1103' value={password} onChangeEvent={(event) => { this.changePassword(event) }} />
                </label>

                <div className="col-12 mt-4 pt-3 align-middle" style={{ borderTop: '1px solid black' }}>
                    <div className="col-6 d-inline-block text-left p-0 loginbtn--changePage" onClick={() => { this.props.changePage('forgetPw') }}>
                        {/* 文字-忘記密碼 */}
                        <Trans i18nKey="1105" />
                    </div>
                    <div className="col-6 d-inline-block text-right p-0">
                        <button type="submit" className="btn btn-light" onClick={() => { this.loginSubmitHandler() }}>
                            {/* 文字-登入 */}
                            <Trans i18nKey="1101" />
                        </button>
                    </div>
                </div>
            </div>
        )
    }


    // Function
    changeAccount = (event) => {
        let inputVal = event.target.value;
        this.props.updateAccount(inputVal);
    }

    changePassword = (event) => {
        let inputVal = event.target.value;
        this.props.updatePassword(inputVal);
    }

    loginSubmitHandler() {
        const { account, password } = this.props;
        const userInfo = {
            account: account,
            password: password
        }
        this.props.submit(userInfo);
    }
}



LoginContent.propTypes = {
    account: PropTypes.string,
    password: PropTypes.string,
    errorMsg: PropTypes.string,
    updateAccount: PropTypes.func,
    updatePassword: PropTypes.func,
    submit: PropTypes.func,
}



const mapStateToProps = (state, ownProps) => {
    return {
        loading: state.LoginReducer.loading,
        account: state.LoginReducer.account,
        password: state.LoginReducer.password,
        token: state.LoginReducer.token,
        errorMsg: state.LoginReducer.errorMsg,

        role: state.LoginReducer.role,					//權限
        company: state.LoginReducer.company,			//公司別
        timeZone: state.LoginReducer.timeZone,			//時區
        language: state.LoginReducer.language,			//語系
        curLanguage: state.LoginReducer.curLanguage,    //目前語系
        functionList: state.LoginReducer.functionList,	//功能選單
        data: state.LoginReducer.data
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        updateAccount: (value) => dispatch(updateAccount(value)),
        updatePassword: (value) => dispatch(updatePassword(value)),
        submit: (userObj) => dispatch(fetchLoginLoad(userObj))
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(LoginContent);