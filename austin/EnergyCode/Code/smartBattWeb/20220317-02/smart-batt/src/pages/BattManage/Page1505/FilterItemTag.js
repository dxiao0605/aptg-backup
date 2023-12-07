import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
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
            isFilterBatteryTypeNameData,//電池型號中文
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
                        Array.isArray(isFilterBatteryTypeNameData.isButtonList) && isFilterBatteryTypeNameData.isButtonList.length > 0 &&
                        isFilterBatteryTypeNameData.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterBatteryTypeNameData, item, idx)} />{item.Label}
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
    isFilterBatteryTypeNameData: {},
    isFilterBatteryTypeNameEData: {},
    isFilterBatteryTypeNameJData: {},
    submitFilterItemTag: () => { },
}
FilterItemTag.propTypes = {
    isFilterCompanyData: PropTypes.object,
    isFilterBatteryTypeNameData: PropTypes.object,
    isFilterBatteryTypeNameEData: PropTypes.object,
    isFilterBatteryTypeNameJData: PropTypes.object,
    submitFilterItemTag: PropTypes.func,
}

export default (FilterItemTag);


