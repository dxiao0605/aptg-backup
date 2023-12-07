import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';
import AlertDialog from '../../../components/AlertDialog';//彈跳視窗
import { apipath, ajax } from '../../../utils/ajax';
import ContentAddEdit from './ContentAddEdit';
import ContentText from './ContentText';
import { CompareWindowns, inputList } from './InitDataFormat';//初始化格式

const apiUrl = apipath();
class PageAlertDialog extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {
            CompanyList: [],
            LanguageList: [],
            RoleList: [],
            TimeZoneList: [],
        }
    }
    componentDidMount() {
        switch (this.props.windows) {
            case CompareWindowns.Add.model:
            case CompareWindowns.Edit.model:
                //取選單資料
                this.getUserList().then(() => { }).catch();
                break;
            default:
        }
    }
    componentWillUnmount() {
        this.ajaxCancel = true;
        this.setState = (state, callback) => {
            return;
        };
    }
    handleInputChange = (event, disabled) => {
        if (disabled) { return; }
        this.props.onUpdData(event.target.name, event.target.value);
    }
    handleInputListChange = (event, disabled) => {
        if (disabled) { return; }
        const { CompanyList } = this.state;
        const keyList = CompanyList.filter(item => item.Label === event.target.value);
        const data = {
            Value: keyList.length === 0 ? "" : keyList[0].Value,
            Label: keyList.length === 0 ? event.target.value : keyList[0].Label
        }
        this.props.onUpdData("Company", data);
    }
    handleSelectChange = (name, value, disabled) => {
        if (disabled) { return; }
        this.props.onUpdData(name, value);
    }

    render() {
        const { windows, title, open, handleClose } = this.props;
        return (
            <AlertDialog
                title={title} //title
                open={open} //開啟視窗   
                disabled={this.props.disabled}
                handleClose={handleClose} //連棟關閉視窗
                handleSubmit={this.handleSubmit}
                onIsRefreshChange={(boolean) => this.props.onIsRefreshChange(boolean)}   //刷新頁面
            >
                {//新增
                    (CompareWindowns.Add.model === windows) &&
                    <ContentAddEdit
                        limits={this.props.limits}
                        data={this.props.data}
                        inputParameter={CompareWindowns.Add}
                        inputList={inputList}
                        selectMenu={this.state}
                        handleInputChange={this.handleInputChange}
                        handleInputListChange={this.handleInputListChange}
                        handleSelectChange={this.handleSelectChange}
                        model={'Add'}
                    />
                }
                {//編輯
                    (CompareWindowns.Edit.model === windows) &&
                    <ContentAddEdit
                        limits={this.props.limits}
                        data={this.props.data}
                        inputParameter={CompareWindowns.Edit}
                        inputList={inputList}
                        selectMenu={this.state}
                        handleInputChange={this.handleInputChange}
                        handleInputListChange={this.handleInputListChange}
                        handleSelectChange={this.handleSelectChange}
                        model={'Edit'}
                    />
                }
                {//刪除
                    (CompareWindowns.Del.model === windows) &&
                    <ContentText>
                        {<Trans i18nKey="1736" />}{/* 確認要刪除所選擇的站台? */}
                        <br />
                        <br />
                        {this.props.data.Name}{/* 提醒:....(省略不打) */}
                    </ContentText>
                }
                {//重設密碼
                    (CompareWindowns.RestPwd.model === windows) &&
                    <ContentText>
                        {<Trans i18nKey="1739" />}{/* 確認要重置密碼? */}
                    </ContentText>
                }
            </AlertDialog>
        )
    }
    handleSubmit = () => {
        if (this.props.data.EditUser) {
            this.props.handleEditSubmit();
        }else{
            this.props.handleClose();
        }
    }
    // get Data
    getUserList = () => {
        return this.ajaxGetUserList().then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { CompanyList, LanguageList, RoleList, TimeZoneList, } = msg;
                this.setState({
                    CompanyList: [...CompanyList],
                    LanguageList: [...LanguageList],
                    RoleList: [...RoleList],
                    TimeZoneList: [...TimeZoneList],
                })
            } else {
                this.setState({
                    CompanyList: [],
                    LanguageList: [],
                    RoleList: [],
                    TimeZoneList: [],
                });
                this.props.onRestData();
            }
        });
    }

    //使用者頁面選單API(GET):
    ajaxGetUserList = () => {
        const { token, curLanguage, timeZone, company, account } = this.props;
        const url = `${apiUrl}getUserList?account=${account}`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //使用者頁面選單API(GET):
        //https://www.gtething.tw/battery/getUserList?account=
    }

}

PageAlertDialog.defaultProps = {
    windows: '',
    title: '',
    open: false,
    data: {},
    handleOpen: () => { },
    handleClose: () => { },
    onUpdData: () => { },
    onRestData: () => { },
    handleEditSubmit: () => { },
    onIsRefreshChange: () => { },
}
PageAlertDialog.propTypes = {
    windows: PropTypes.string,
    title: PropTypes.string,
    open: PropTypes.bool,
    data: PropTypes.object,
    handleOpen: PropTypes.func,
    handleClose: PropTypes.func,
    onUpdData: PropTypes.func,
    onRestData: PropTypes.func,
    handleEditSubmit: PropTypes.func,
    onIsRefreshChange: PropTypes.func,
}
const mapStateToProps = (state, ownProps) => {
    return {
        account: state.LoginReducer.account,
        token: state.LoginReducer.token,
        company: state.LoginReducer.company,
        curLanguage: state.LoginReducer.curLanguage,
        timeZone: state.LoginReducer.timeZone,
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(PageAlertDialog);


