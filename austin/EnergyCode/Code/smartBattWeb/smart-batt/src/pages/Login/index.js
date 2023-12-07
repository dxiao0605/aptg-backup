import React,{Component} from 'react';
import { Redirect } from 'react-router-dom';
import { connect } from 'react-redux';
import classnames from 'classnames';
import LoginContent from './LoginContent';
import ForgetPwContent from './ForgetPwContent';
import './login.scss';
class Login extends Component {
    constructor(props){
        super(props)
        this.state = {
            loaded: false,
            visible: 'login',
        }
    }
    
    componentDidMount() {
        if(this.state.loaded === false) { this.setState({loaded:true})}
    }

    render() {
        const { token,redirectToReferrer } = this.props;
        const { loaded, visible } = this.state;
        const isLoading = classnames({
            'loading': loaded === false 
        })
        if( token !== '' && redirectToReferrer ) { return <Redirect to='/' /> }



        return (
            <div className="container-fuild">
                <div className={`login_container ${isLoading}`} style={{height: '100vh'}}>
                    <div className={`col-12 logo-warp ${isLoading}`} />
                    <div className="col-12 text-center login_content">
                    {
                        (
                            ()=>{
                                switch(visible){
                                    case 'login':
                                        return <LoginContent changePage={this.changePage} />
                                    case 'forgetPw':
                                        return <ForgetPwContent changePage={this.changePage} />
                                    default:
                                        return <LoginContent changePage={this.changePage} />
                                }
                            }
                        )()
                    }
                    </div>
                </div>
            </div>
        )
    }


    // Function
    // 變更頁面(登入、忘記密碼)
    changePage = (value) => {
        this.setState({visible:value})
    }

}






const mapStateToProps = (state , ownProps) => {
    return {
        loading: state.LoginReducer.loading,
        token: state.LoginReducer.token,
        errorMsg: state.LoginReducer.errorMsg,
        redirectToReferrer: state.LoginReducer.redirectToReferrer,
    }
}
export default connect(mapStateToProps)(Login);