import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { Trans } from 'react-i18next';
import { FilterNames } from './InitDataFormat';//初始化格式


class FilterItemTag extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props);
        this.state = {}
    }
    render() {
        const {
            isFilterCompanyData,//公司
            isFilterCountryData,//國家
            isFilterAreaData,//地域
            isFilterGroupIdData,//站台
            isFilterPreviousNBIDData,//(接續)通訊序號
            isFilterStartTimeData,//(接續)開始時間
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
                        Array.isArray(isFilterPreviousNBIDData.isButtonList) && isFilterPreviousNBIDData.isButtonList.length > 0 &&
                        isFilterPreviousNBIDData.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterPreviousNBIDData, item, idx)} />{item.Label}
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterStartTimeData.isButtonList) && isFilterStartTimeData.isButtonList.length > 0 &&
                        isFilterStartTimeData.isButtonList.map((item, idx) => {
                            return item.LabelShow && <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                {item.ButtonShow && <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterStartTimeData, item, idx)} />}
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
    isFilterPreviousNBIDData: {},
    isFilterStartData: {},
    submitFilterItemTag: () => {},
}
FilterItemTag.propTypes = {
    isFilterCompanyData: PropTypes.object,
    isFilterCountryData: PropTypes.object,
    isFilterAreaData: PropTypes.object,
    isFilterGroupIdData: PropTypes.object,
    isFilterPreviousNBIDData: PropTypes.object,
    isFilterStartTimeData: PropTypes.object,
    submitFilterItemTag: PropTypes.func,
}

export default (FilterItemTag);


