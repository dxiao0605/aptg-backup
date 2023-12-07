import React , {Component} from "react";
import { connect } from "react-redux";
import { changeCurLanguage,changeLanguage } from '../../actions/LoginAction';
import { fetchLanguageListLoad } from '../../actions/LanguageListAction';
import { fetchTimeZoneListLoad } from '../../actions/TimeZoneListAction';
import SelectLanguage from '../SelectLanguage';
import UserConfig from '../UserConfig';


class Header extends Component {
	constructor(props) {
		super(props)
		this.state = {
			curLanguage: 1,
		}
	}

	componentDidMount() {
        const { token } = this.props;
		this.props.fetchLanguageListLoad(token);	//取得語系清單
		this.props.fetchTimeZoneListLoad(token);	//取得時間清單
	}
	
	componentDidUpdate(prevProps, prevState){
		if (this.props.curLanguage !== prevState.curLanguage) {
			this.setState({curLanguage: this.props.curLanguage})
		}
	}


	render(){
		const { languageList,languageListErrorMsg } = this.props;
		const { curLanguage } = this.state;
		return (
			<>
				{/* 語系選擇(目前頁面上語系，下次登入時不會記錄) */}
				<div className='page_settingTool'>
					<SelectLanguage 
						currentLocale={curLanguage} 	//目前語系
						languageList={languageList} 	//語系清單
						languageListErrorMsg={languageListErrorMsg}	//語系清單錯誤訊息
						getCurLocale={this.fetchUpdAccount} 	//變更目前語系
					/>
				</div>
				{/* 使用者設定 (變更預設語系、時區、使用者密碼、登出) */}
				<div className='page_settingTool'>
					<UserConfig />{" "}
				</div>
			</>
		);
	}

	// get API 更新使用者資訊
	fetchUpdAccount = (locale) => {
		const {token,account,curTimeZone} = this.props;
		const url = `updAccount`;
		const list = {
			msg: {
				Account: account,
				TimeZone: curTimeZone,
				Language: locale,
			}
		}
		fetch(url,{
			method: 'POST',
			headers: new Headers({
				'Accept': '*/*',
				'Content-Type': 'application/json',
				'token': token,
			}),
			body: JSON.stringify(list)
		}).then(response => {
			if(response.status === 200) {
				return response.json()
			}else {
				return {}
			}
		})
		.then( response => {
			if(response.code === '00'){
				this.props.changeLanguage(locale);
				this.props.changeCurLanguage(locale);
			}
		})
	}
	
};

const mapStateToProps = ( state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        account:state.LoginReducer.account,
        username: state.LoginReducer.username,					//用戶名
        role: state.LoginReducer.role,							//權限
        company: state.LoginReducer.company,					//公司別
		timeZone: state.LoginReducer.timeZone,					//時區
		language: state.LoginReducer.language,					//語系
        curLanguage: state.LoginReducer.curLanguage,    		//目前語系
        functionList: state.LoginReducer.functionList,			//功能選單
        languageList: state.LanguageListReducer.languageList,	//語系清單
		languageListErrorMsg: state.LanguageListReducer.languageListErrorMsg,	//語系清單錯誤訊息
		timeZoneList: state.TimeZoneListReducer.timeZoneList,	//時區清單
        timeZoneListErrorMsg: state.TimeZoneListReducer.timeZoneListErrorMsg,	//時區清單錯誤訊息
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
	return {
		changeLanguage: (value) => dispatch(changeLanguage(value)),				//變更預設語系
		changeCurLanguage: (value) => dispatch(changeCurLanguage(value)),		//變更目前語系
		fetchLanguageListLoad: (data) => dispatch(fetchLanguageListLoad(data)),	//取得語系清單
		fetchTimeZoneListLoad: (data) => dispatch(fetchTimeZoneListLoad(data)),	//取得語系清單
	};
};
export default connect(mapStateToProps,mapDispatchToProps)(Header);
