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
            isFilterEventTypeCodeData,//告警類型
            isFilterCompanyData,//公司
            isFilterCountryData,//國家
            isFilterAreaData,//地域
            isFilterGroupIdData,//站台編號
            isFilterRecordTimeAlert,//數據時間
        } = this.props;
        return (
            <Fragment>
                <div className="d-inline-block">
                    {
                        Array.isArray(isFilterEventTypeCodeData.isButtonList) && isFilterEventTypeCodeData.isButtonList.length > 0 &&
                        isFilterEventTypeCodeData.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterEventTypeCodeData, item, idx)} />
                                {/* {item.Label} */}
                                <Trans i18nKey={item.I18NCode} />
                            </button>
                        })
                    }
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
                    {//不可刪除欄位
                        Array.isArray(isFilterRecordTimeAlert.isButtonList) && isFilterRecordTimeAlert.isButtonList.length > 0 &&
                        isFilterRecordTimeAlert.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <Trans i18nKey={item.Label} />
                            </button>
                        })
                    }
                </div>
            </Fragment>
        )
    }

}

FilterItemTag.defaultProps = {
    isFilterEventTypeCodeData: {},
    isFilterCompanyData: {},
    isFilterCountryData: {},
    isFilterAreaData: {},
    isFilterGroupIdData: {},
    isFilterRecordTimeAlert: {},
    submitFilterItemTag: () => { },
}
FilterItemTag.propTypes = {
    isFilterEventTypeCodeData: PropTypes.object,
    isFilterCompanyData: PropTypes.object,
    isFilterCountryData: PropTypes.object,
    isFilterAreaData: PropTypes.object,
    isFilterGroupIdData: PropTypes.object,
    isFilterRecordTimeAlert: PropTypes.object,
    submitFilterItemTag: PropTypes.func,
}

export default (FilterItemTag);


