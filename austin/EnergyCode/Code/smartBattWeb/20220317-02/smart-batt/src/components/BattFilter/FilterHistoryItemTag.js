import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';
import { FilterNames } from './InitDataFormat';//初始化格式


class FilterHistoryItemTag extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {

        }
    }
    render() {
        const {
            company,
            isFilterCompanyData,//公司
            isFilterCountryData,//國家
            isFilterAreaData,//地域
            isFilterGroupIdData,//站台編號
            isFilterBatteryGroupIdData,//電池組ID
            isFilterBatteryStatusData,//電池狀態
            isFilterRecordTimeData,//數據時間
        } = this.props;
        return (
            <Fragment>
                <div className="d-inline-block">
                    {
                        Array.isArray(isFilterCompanyData.isButtonList) && isFilterCompanyData.isButtonList.length > 0 && company === "1" &&
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
                        Array.isArray(isFilterBatteryGroupIdData.isButtonList) && isFilterBatteryGroupIdData.isButtonList.length > 0 &&
                        isFilterBatteryGroupIdData.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">{item.Label}</button>
                        })
                    }
                    {
                        Array.isArray(isFilterBatteryStatusData.isButtonList) && isFilterBatteryStatusData.isButtonList.length > 0 &&
                        isFilterBatteryStatusData.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterBatteryStatusData, item, idx)} />
                                <Trans i18nKey={item.I18NCode} />
                            </button>
                        })
                    }
                    {//不可刪除欄位
                        Array.isArray(isFilterRecordTimeData.isButtonList) && isFilterRecordTimeData.isButtonList.length > 0 &&
                        isFilterRecordTimeData.isButtonList.map((item, idx) => {
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

FilterHistoryItemTag.defaultProps = {
    company: '0',
    isFilterCompanyData: {},
    isFilterCountryData: {},
    isFilterAreaData: {},
    isFilterGroupIdData: {},
    isFilterGroupNameData: {},
    isFilterBatteryGroupIdData: {},
    isFilterBatteryTypeData: {},
    isFilterBatteryStatusData: {},
    isFilterRecordTimeData: {},
    submitFilterItemTag: () => { },
}
FilterHistoryItemTag.propTypes = {
    isFilterCompanyData: PropTypes.object,
    isFilterCountryData: PropTypes.object,
    isFilterAreaData: PropTypes.object,
    isFilterGroupIdData: PropTypes.object,
    isFilterGroupNameData: PropTypes.object,
    isFilterBatteryGroupIdData: PropTypes.object,
    isFilterBatteryTypeData: PropTypes.object,
    isFilterBatteryStatusData: PropTypes.object,
    isFilterRecordTimeData: PropTypes.object,
    submitFilterItemTag: PropTypes.func,
}

export default (FilterHistoryItemTag);


