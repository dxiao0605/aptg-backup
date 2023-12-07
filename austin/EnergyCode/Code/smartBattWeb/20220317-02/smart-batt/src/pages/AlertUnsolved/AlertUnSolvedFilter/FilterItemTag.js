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
            isFilterEventTypeCodeDataUnSolved,//告警類型
            isFilterCompanyDataUnSolved,//公司
            isFilterCountryDataUnSolved,//國家
            isFilterAreaDataUnSolved,//地域
            isFilterGroupIdDataUnSolved,//站台編號
            isFilterBatteryGroupIdDataUnSolved,//電池組ID
            isFilterRecordTimeUnSolved,//數據時間
        } = this.props;
        return (
            <Fragment>
                <div className="d-inline-block">
                    {
                        Array.isArray(isFilterEventTypeCodeDataUnSolved.isButtonList) && isFilterEventTypeCodeDataUnSolved.isButtonList.length > 0 &&
                        isFilterEventTypeCodeDataUnSolved.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterEventTypeCodeDataUnSolved, item, idx)} />
                                <Trans i18nKey={item.I18NCode} />
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterCompanyDataUnSolved.isButtonList) && isFilterCompanyDataUnSolved.isButtonList.length > 0 &&
                        isFilterCompanyDataUnSolved.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterCompanyDataUnSolved, item, idx)} />{item.Label}
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterCountryDataUnSolved.isButtonList) && isFilterCountryDataUnSolved.isButtonList.length > 0 &&
                        isFilterCountryDataUnSolved.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterCountryDataUnSolved, item, idx)} />{item.Label}
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterAreaDataUnSolved.isButtonList) && isFilterAreaDataUnSolved.isButtonList.length > 0 &&
                        isFilterAreaDataUnSolved.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterAreaDataUnSolved, item, idx)} />{item.Label}
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterGroupIdDataUnSolved.isButtonList) && isFilterGroupIdDataUnSolved.isButtonList.length > 0 &&
                        isFilterGroupIdDataUnSolved.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterGroupIdDataUnSolved, item, idx)} />{item.Label}
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterBatteryGroupIdDataUnSolved.isButtonList) && isFilterBatteryGroupIdDataUnSolved.isButtonList.length > 0 &&
                        isFilterBatteryGroupIdDataUnSolved.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                {item.Label}
                            </button>
                        })
                    }
                    {//不可刪除欄位
                        Array.isArray(isFilterRecordTimeUnSolved.isButtonList) && isFilterRecordTimeUnSolved.isButtonList.length > 0 &&
                        isFilterRecordTimeUnSolved.isButtonList.map((item, idx) => {
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
    isFilterEventTypeCodeDataUnSolved: {},
    isFilterCompanyDataUnSolved: {},
    isFilterCountryDataUnSolved: {},
    isFilterAreaDataUnSolved: {},
    isFilterGroupIdDataUnSolved: {},
    isFilterBatteryGroupIdDataUnSolved: {},
    isFilterRecordTimeUnSolved: {},
    submitFilterItemTag: () => { },
}
FilterItemTag.propTypes = {
    isFilterEventTypeCodeDataUnSolved: PropTypes.object,
    isFilterCompanyDataUnSolved: PropTypes.object,
    isFilterCountryDataUnSolved: PropTypes.object,
    isFilterAreaDataUnSolved: PropTypes.object,
    isFilterGroupIdDataUnSolved: PropTypes.object,
    isFilterBatteryGroupIdDataUnSolved: PropTypes.object,
    isFilterRecordTimeUnSolved: PropTypes.object,
    submitFilterItemTag: PropTypes.func,
}

export default (FilterItemTag);


