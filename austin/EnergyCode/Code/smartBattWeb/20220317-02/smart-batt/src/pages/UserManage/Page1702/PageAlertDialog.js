import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';
import AlertDialog from '../../../components/AlertDialog';//彈跳視窗
import { apipath, ajax } from '../../../utils/ajax';
//內容
import ContentAdd from './ContentAdd';
import ContentAuthority from './ContentAuthority';
import ContentText from './ContentText';
import { CompareWindowns, inputList, inputAuthorityList } from './InitDataFormat';//初始化格式

const apiUrl = apipath();
class PageAlertDialog extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {
            RoleList: [],
        }
    }
    componentDidMount() {
        switch (this.props.windows) {
            case CompareWindowns.Add.model:
                //取選單資料
                this.getRoleList().then(() => {}).catch();
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
    handleSelectChange = (name, value, disabled) => {
        if (disabled) { return; }
        this.props.onUpdData(name, value);
    }

    render() {
        const { windows, title, titleMore, open, handleClose, openTitleI18n } = this.props;
        return (
            <AlertDialog
                openTitleI18n={openTitleI18n}//判斷title是否為i18n
                title={title} //title
                titleMore={titleMore}
                open={open} //開啟視窗                
                handleClose={handleClose} //連棟關閉視窗
                handleSubmit={this.handleSubmit}
                onIsRefreshChange={(boolean) => this.props.onIsRefreshChange(boolean)}   //刷新頁面
            >
                {//新增
                    (CompareWindowns.Add.model === windows) &&
                    <ContentAdd
                        data={this.props.data}
                        inputParameter={CompareWindowns.Add}
                        inputList={inputList}
                        selectMenu={this.state}
                        handleInputChange={this.handleInputChange}
                        handleSelectChange={this.handleSelectChange}
                    />
                }
                {//權限
                    (CompareWindowns.Authority.model === windows) &&
                    <ContentAuthority
                        limits={this.props.limits}
                        data={this.props.authoritydata}
                        inputParameter={CompareWindowns.Authority}
                        inputList={inputAuthorityList}
                        selectMenu={this.state}
                        onUpdAuthorityData={this.props.onUpdAuthorityData}
                    />
                }
                {//刪除
                    (CompareWindowns.Del.model === windows) &&
                    <ContentText>
                        {<Trans i18nKey="1726" />}{/* 您確定您想要刪除此角色?刪除之後無法復原(角色之使用者需先刪除方可刪除角色) */}
                        <br />
                        <br />
                        {this.props.data.RoleName}
                    </ContentText>
                }
            </AlertDialog>
        )
    }
    handleSubmit = () => {
        if (this.props.limits?.Edit === 1 && this.props.limits?.Button?.RolePrivilege === 1) {
            this.props.handleSubmit();
        } else {
            this.props.handleClose();
        }
    }
    // get Data
    getRoleList = () => {
        return this.ajaxGetRoleList().then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { List, } = msg;
                this.setState({
                    RoleList: [...List],
                })
            } else {
                this.setState({
                    RoleList: [],
                });
                this.props.onRestData();
            }
        });
    }

    //角色選單API(GET):
    ajaxGetRoleList = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getRoleList`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //角色選單API(GET):
        //https://www.gtething.tw/battery/getRoleList
    }

}

PageAlertDialog.defaultProps = {
    openTitleI18n: true,
    windows: '',
    title: '',
    titleMore: '',
    open: false,
    data: {},
    authoritydata: {},
    handleOpen: () => { },
    handleClose: () => { },
    handleSubmit: () => { },
    onRestData: () => { },
    onUpdAuthorityData: () => { },
    onIsRefreshChange: () => { },
}
PageAlertDialog.propTypes = {
    openTitleI18n: PropTypes.bool,
    windows: PropTypes.string,
    title: PropTypes.string,
    titleMore: PropTypes.string,
    open: PropTypes.bool,
    data: PropTypes.object,
    authoritydata: PropTypes.object,
    handleOpen: PropTypes.func,
    handleClose: PropTypes.func,
    handleSubmit: PropTypes.func,
    onRestData: PropTypes.func,
    onUpdAuthorityData: PropTypes.func,
    onIsRefreshChange: PropTypes.func,
}
const mapStateToProps = (state, ownProps) => {
    return {
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


