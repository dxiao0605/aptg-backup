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
            isFilterBatteryGroupIdData,//電池組ID
            isFilterInstallDateData,//安裝日期
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
                        Array.isArray(isFilterBatteryGroupIdData.isButtonList) && isFilterBatteryGroupIdData.isButtonList.length > 0 &&
                        isFilterBatteryGroupIdData.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterBatteryGroupIdData, item, idx)} />{item.Label}
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterInstallDateData.isButtonList) && isFilterInstallDateData.isButtonList.length > 0 &&
                        isFilterInstallDateData.isButtonList.map((item, idx) => {   
                            return item.LabelShow && <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                { item.ButtonShow && <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterInstallDateData, item, idx)} /> }                                
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
    isFilterBatteryGroupIdData: {},
    isFilterInstallDateData: {},
    submitFilterItemTag: () => { },
}
FilterItemTag.propTypes = {
    isFilterCompanyData: PropTypes.object,
    isFilterBatteryGroupIdData: PropTypes.object,
    isFilterInstallDateData: PropTypes.object,
    submitFilterItemTag: PropTypes.func,
}

export default (FilterItemTag);


