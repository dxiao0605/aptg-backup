import React, { Component } from 'react';
import { connect } from 'react-redux';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';
import AlertDialog from '../../../components/AlertDialog';//彈跳視窗

//內容
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import { NBSelectNames, GetModel } from './InitDataFormat';//初始化格式


class PageAlertDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
        }
    }
    componentDidMount() {
        //未分配-公司別選單        
        if (this.props.windows === GetModel.NotSent && this.props.data.Company === "" && this.props.companyList.length > 0) {
            this.props.onUpdDataVal(NBSelectNames.Company, this.props.companyList[0].Value)
        }
    }
    componentWillUnmount() {
        this.setState = (state, callback) => {
            return;
        };
    }
    handleSelectChange = (event) => {
        this.props.onUpdDataVal(event.target.name, event.target.value);
    }
    handleTextareaChange = (event) => {
        this.props.onUpdDataVal(event.target.name, event.target.value);
    }
    render() {
        const { windows, title, open, handleClose } = this.props;
        return (
            <AlertDialog
                title={title} //title
                open={open} //開啟視窗                
                handleClose={handleClose} //連棟關閉視窗
                handleSubmit={this.handleSubmit}
                onIsRefreshChange={(boolean) => this.props.onIsRefreshChange(boolean)}   //刷新頁面
            >
                {//未分配改分配
                    (windows === GetModel.NotSent) &&
                    <div className="col-12 p-0 my-3">
                        <div className="form-inline my-2"><Trans i18nKey="1523" /></div>
                        <div className="form-inline my-2">
                            <div className="col-2 px-0 mr-xl-1"><Trans i18nKey="1074" />：</div>
                            <Select
                                name={NBSelectNames.Company}
                                value={this.props.data.Company}
                                onChange={(e) => this.handleSelectChange(e)}
                                multiple={false}
                                style={{ minWidth: '180px', textAlign: 'center', top: '-5px' }}
                            >
                                {
                                    Array.isArray(this.props.companyList) && this.props.companyList.length > 0 ?
                                        this.props.companyList.map((item) => {
                                            return item?.Value !== undefined &&
                                                <MenuItem key={item.Value.toString()} value={item.Value.toString()}>{item.Label}</MenuItem>;
                                        }) :
                                        <MenuItem value=''></MenuItem>
                                }
                            </Select>
                        </div>
                        <div className="form-inline my-2 align-items-start">
                            <div className="col-12 col-xl-2 px-0 mr-xl-1"><Trans i18nKey="1057" />：</div>
                            <div className="col-12 col-xl-8 px-0 ">
                                <div className="border border_ccc w-100 arrowBoxShadow">
                                    <div className="body py-1 pr-1">
                                        <div style={{ overflowY: 'scroll', height: '30vh' }}>
                                            {
                                                this.props.data?.NBList && Array.isArray(this.props.data.NBList) && this.props.data.NBList.map((item, idx) => {
                                                    return item.checked &&
                                                        <div key={idx} className="form-inline" style={{ lineHeight: 2 }}>
                                                            <div className="col-10 pl-3 px-0">{item.NBID}</div>
                                                        </div>
                                                })
                                            }
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="form-inline my-2 align-items-start">
                            <div className="col-12 col-xl-2 px-0 mr-xl-1"><Trans i18nKey="1062" />：</div>
                            <div className="col-12 col-xl-8 px-0 ">
                                <textarea className="border border_ccc arrowBoxShadow w-100 px-3 py-2"
                                    name={NBSelectNames.Remark}
                                    style={{ minHeight: '10vh', maxHeight: '10vh', height: '10vh' }}
                                    value={this.props.data.Remark}
                                    onChange={(e) => this.handleTextareaChange(e)}
                                />
                            </div>
                        </div>
                    </div>
                }
                {//分配改未分配
                    (windows === GetModel.Sent) &&
                    <div className="col-12 p-0 my-3">
                        <div className="form-inline my-2"><Trans i18nKey="1525" /></div>
                        <div className="form-inline my-2 align-items-start">
                            <div className="col-12 col-xl-2 px-0 mr-xl-1"><Trans i18nKey="1057" />：</div>
                            <div className="col-12 col-xl-8 px-0 ">
                                <div className="border border_ccc w-100 arrowBoxShadow">
                                    <div className="header py-1 pr-1">
                                        <div className="form-inline pr-3" style={{ lineHeight: 2 }}>
                                            <div className="col-4 px-0 pl-3"><Trans i18nKey="1064" /></div>
                                            <div className="col-8 px-0"><Trans i18nKey="1057" /></div>
                                        </div>
                                    </div>
                                    <div className="body py-1 pr-1">
                                        <div style={{ overflowY: 'scroll', height: '30vh' }}>
                                            {
                                                this.props.data?.NBList && Array.isArray(this.props.data.NBList) && this.props.data.NBList.map((item, idx) => {
                                                    return item.checked &&
                                                        <div key={idx} className="form-inline" style={{ lineHeight: 2 }}>
                                                            <div className="col-4 px-0 pl-3">{item.Company}</div>
                                                            <div className="col-8 px-0">{item.NBID}</div>
                                                        </div>
                                                })
                                            }
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="form-inline my-2 align-items-start">
                            <div className="col-12 col-xl-2 px-0 mr-xl-1"><Trans i18nKey="1062" />：</div>
                            <div className="col-12 col-xl-8 px-0 ">
                                <textarea className="border border_ccc arrowBoxShadow w-100 px-3 py-2"
                                    name={NBSelectNames.Remark}
                                    style={{ minHeight: '10vh', maxHeight: '10vh', height: '10vh' }}
                                    value={this.props.data.Remark}
                                    onChange={(e) => this.handleTextareaChange(e)}
                                />
                            </div>
                        </div>
                    </div>
                }

                {//刪除畫面
                    (windows === GetModel.Del) &&
                    <div className="col-12 p-0 my-3">
                        <div className="form-inline my-2"><Trans i18nKey="1527" /></div>
                        <div className="form-inline my-2 align-items-start">
                            <div className="col-12 col-xl-2 px-0 mr-xl-1"><Trans i18nKey="1057" />：</div>
                            <div className="col-12 col-xl-8 px-0 ">
                                <div className="border border_ccc w-100 arrowBoxShadow">
                                    <div className="header py-1 pr-1">
                                        <div className="form-inline pr-3" style={{ lineHeight: 2 }}>
                                            <div className="col-4 px-0 pl-3"><Trans i18nKey="1064" /></div>
                                            <div className="col-8 px-0"><Trans i18nKey="1057" /></div>
                                        </div>
                                    </div>
                                    <div className="body py-1 pr-1">
                                        <div style={{ overflowY: 'scroll', height: '30vh' }}>
                                            {
                                                this.props.data?.NBList && Array.isArray(this.props.data.NBList) && this.props.data.NBList.map((item, idx) => {
                                                    return item.checked &&
                                                        <div key={idx} className="form-inline" style={{ lineHeight: 2 }}>
                                                            <div className="col-4 px-0 pl-3">{item.Company}</div>
                                                            <div className="col-8 px-0">{item.NBID}</div>
                                                        </div>
                                                })
                                            }
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="form-inline my-2 align-items-start">
                            <div className="col-12 col-xl-2 px-0 mr-xl-1"><Trans i18nKey="1062" />：</div>
                            <div className="col-12 col-xl-8 px-0 ">
                                <textarea className="border border_ccc arrowBoxShadow w-100 px-3 py-2"
                                    name={NBSelectNames.Remark}
                                    style={{ minHeight: '10vh', maxHeight: '10vh', height: '10vh' }}
                                    value={this.props.data.Remark}
                                    onChange={(e) => this.handleTextareaChange(e)}
                                />
                            </div>
                        </div>
                        <div className="form-inline my-2 align-items-start">
                            <div className="col-12 col-xl-2 px-0 mr-xl-1"></div>
                            <div className="col-12 col-xl-8 px-0 ">
                                <Trans i18nKey="1569" />
                            </div>
                        </div>
                    </div>
                }
            </AlertDialog>
        )
    }
    handleSubmit = () => {
        this.props.handleSubmit(this.props.data);
    }
}

PageAlertDialog.defaultProps = {
    windows: '',
    title: '',
    open: false,
    data: {},
    companyList: [],
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
    companyList: PropTypes.array,
    handleClose: PropTypes.func,
    handleSubmit: PropTypes.func,
    handleImportSubmit: PropTypes.func,
    onIsRefreshChange: PropTypes.func,
}
const mapStateToProps = (state, ownProps) => {
    return { }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {}
}
export default connect(mapStateToProps, mapDispatchToProps)(PageAlertDialog);


