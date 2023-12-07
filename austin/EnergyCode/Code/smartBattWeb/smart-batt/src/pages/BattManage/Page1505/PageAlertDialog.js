import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import AlertDialog from '../../../components/AlertDialog';//彈跳視窗
import { ajax } from '../../../utils/ajax';
//內容
import ContentAddEdit from './ContentAddEdit';
import ContentDel from './ContentDel';
import { CompareWindowns, inputList } from './InitDataFormat';//初始化格式

class PageAlertDialog extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {
            data: {
                BatteryTypeCode: '',//電池型號
                BatteryTypeName: '',//中文
                Company: '',//公司別
            },
            companyList: [],
        }
    }
    componentDidMount() {
        this.getCompanyList().then(() => {
            this.setState({
                data: {
                    ...this.props.data,
                },
            });
        });//取選單資料
    }
    componentWillUnmount() {
        this.ajaxCancel = true;
        this.setState = (state, callback) => {
            return;
        };
    }
    handleSelectChange = (event) => {
        this.setState((prevState, props) => ({
            data: {
                ...prevState.data,
                Company: event.target.value,
            }
        }));
    }
    handleInputChange = (event) => {
        this.setState((prevState, props) => ({
            data: {
                ...prevState.data,
                [event.target.name]: event.target.value,
            }
        }));
    }

    render() {
        const { company, windows, title, open, handleClose } = this.props;
        const { companyList, data } = this.state;
        return (
            <AlertDialog
                title={title} //title
                open={open} //開啟視窗                
                handleClose={handleClose} //連棟關閉視窗
                handleSubmit={this.handleSubmit}
                onIsRefreshChange={(boolean) => this.props.onIsRefreshChange(boolean)}   //刷新頁面
            >
                {//新增
                    (CompareWindowns.Add.model === windows) &&
                    <ContentAddEdit
                        data={data}
                        companyList={companyList}
                        company={company}
                        inputList={inputList}
                        isAdd={true}
                        handleSelectChange={(event) => this.handleSelectChange(event)}
                        handleInputChange={(event) => this.handleInputChange(event)}
                    />
                }
                {//編輯
                    (CompareWindowns.Edit.model === windows) &&
                    <ContentAddEdit
                        data={data}
                        companyList={companyList}
                        company={company}
                        inputList={inputList}
                        handleSelectChange={(event) => this.handleSelectChange(event)}
                        handleInputChange={(event) => this.handleInputChange(event)}
                    />
                }
                {//刪除
                    (CompareWindowns.Del.model === windows) && <ContentDel />
                }
            </AlertDialog>
        )
    }
    handleSubmit = () => {
        const { windows, handleEditSubmit, handleAddSubmit, handleDelSubmit } = this.props;
        const { data } = this.state;
        switch (windows) {
            case CompareWindowns.Add.model://新增                
                handleAddSubmit(data);
                break;
            case CompareWindowns.Edit.model://編輯                
                handleEditSubmit(data);
                break;
            case CompareWindowns.Del.model://刪除
                handleDelSubmit();
                break;
            default:
        }
    }
    // get Data
    getCompanyList = () => {
        return this.ajaxGetCompanyList().then((response) => {
            if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面
            if (Object.keys(response).length === 0) { return new Promise((resolve, reject) => (reject(''))); }
            const { code, msg } = response;
            if (code === '00' && msg) {
                const { List } = msg;
                this.setState({
                    companyList: [...List],
                    data: {
                        BatteryTypeCode: '',//電池型號
                        BatteryTypeName: '',//中文
                        Company: Array.isArray(List) && List.length > 0 ? List[0].Value : '',//公司別
                    },
                })
            } else {
                this.setState({
                    companyList: [],
                    data: {
                        BatteryTypeCode: '',//電池型號
                        BatteryTypeName: '',//中文
                        Company: '',//公司別
                    },
                });
            }
        });
    }

    //公司下拉選單API(GET):
    ajaxGetCompanyList = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `getCompanyList`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //公司下拉選單API(GET):
        //https://www.gtething.tw/battery/getCompanyList
    }

}

PageAlertDialog.defaultProps = {
    windows: '',
    title: '',
    open: false,
    data: {},
    handleOpen: () => { },
    handleClose: () => { },
    handleSubmit: () => { },
    handleImportSubmit: () => { },
    onIsRefreshChange: () => { },
}
PageAlertDialog.propTypes = {
    windows: PropTypes.string,
    title: PropTypes.string,
    open: PropTypes.bool,
    data: PropTypes.object,
    handleOpen: PropTypes.func,
    handleClose: PropTypes.func,
    handleSubmit: PropTypes.func,
    handleImportSubmit: PropTypes.func,
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


