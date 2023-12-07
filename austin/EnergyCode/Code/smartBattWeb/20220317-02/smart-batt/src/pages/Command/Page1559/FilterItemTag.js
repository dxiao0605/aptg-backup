import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';
import { FilterNames } from './InitDataFormat';//初始化格式


class FilterItemTag extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {

        }
    }
    render() {
        const {
            isFilterCompanyData,//公司
            isFilterCountryData,//國家
            isFilterAreaData,//地域
            isFilterGroupIdData,//站台編號
            isFilterCommandData,//指令
            isFilterResponseData,//回應訊息
            isFilterSendTimeData,//傳送時間
        } = this.props;
        return (
            <Fragment>
                <div className="d-inline-block">
                    {
                        Array.isArray(isFilterCompanyData.isButtonList) && isFilterCompanyData.isButtonList.length > 0 &&
                        isFilterCompanyData.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterCompanyData, item, idx)} />{item.Label}
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterCountryData.isButtonList) && isFilterCountryData.isButtonList.length > 0 &&
                        isFilterCountryData.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterCountryData, item, idx)} />{item.Label}
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterAreaData.isButtonList) && isFilterAreaData.isButtonList.length > 0 &&
                        isFilterAreaData.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterAreaData, item, idx)} />{item.Label}
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterGroupIdData.isButtonList) && isFilterGroupIdData.isButtonList.length > 0 &&
                        isFilterGroupIdData.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterGroupIdData, item, idx)} />{item.Label}
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterCommandData.isButtonList) && isFilterCommandData.isButtonList.length > 0 &&
                        isFilterCommandData.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterCommandData, item, idx)} /><Trans i18nKey={item.I18NCode} />
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterResponseData.isButtonList) && isFilterResponseData.isButtonList.length > 0 &&
                        isFilterResponseData.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterResponseData, item, idx)} /><Trans i18nKey={item.I18NCode} />
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterSendTimeData.isButtonList) && isFilterSendTimeData.isButtonList.length > 0 &&
                        isFilterSendTimeData.isButtonList.map((item, idx) => {              
                            return item.LabelShow && <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                {item.ButtonShow && <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterSendTimeData, item, idx)} />}
                                {item.Value !== "1" ? <Trans i18nKey={item.Label} /> : item.Label}
                            </button>
                        })
                    }
                </div>
            </Fragment>
        )
    }

}

FilterItemTag.defaultProps = {
    isFilterCompanyData: {},
    isFilterCountryData: {},
    isFilterAreaData: {},
    isFilterGroupIdData: {},
    isFilterCommandData: {},
    isFilterResponseData: {},
    isFilterSendTimeData: {},
    submitFilterItemTag: () => { },
}
FilterItemTag.propTypes = {
    isFilterCompanyData: PropTypes.object,
    isFilterCountryData: PropTypes.object,
    isFilterAreaData: PropTypes.object,
    isFilterGroupIdData: PropTypes.object,
    isFilterCommandData: PropTypes.object,
    isFilterResponseData: PropTypes.object,
    isFilterSendTimeData: PropTypes.object,
    submitFilterItemTag: PropTypes.func,
}

export default (FilterItemTag);


