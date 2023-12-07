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
            isFilterNBIDData,//通訊序號
            isFilterModifyItemData,//異動項目
            isFilterModifyTimeData,//異動時間
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
                        Array.isArray(isFilterNBIDData.isButtonList) && isFilterNBIDData.isButtonList.length > 0 &&
                        isFilterNBIDData.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterNBIDData, item, idx)} />{item.Label}
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterModifyItemData.isButtonList) && isFilterModifyItemData.isButtonList.length > 0 &&
                        isFilterModifyItemData.isButtonList.map((item, idx) => {
                            return <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterModifyItemData, item, idx)} /><Trans i18nKey={item.I18NCode} />
                            </button>
                        })
                    }
                    {
                        Array.isArray(isFilterModifyTimeData.isButtonList) && isFilterModifyTimeData.isButtonList.length > 0 &&
                        isFilterModifyTimeData.isButtonList.map((item, idx) => {
                            return item.LabelShow && <button key={item.Value} className="btn-sm-b1 btn-outline-primary mr-2">
                                {item.ButtonShow && <i className="fas fa-window-close mr-1" onClick={() => this.props.submitFilterItemTag(FilterNames.isFilterModifyTimeData, item, idx)} />}
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
    isFilterNBIDData: {},
    isFilterModifyItemData: {},
    isFilterModifyTimeData: {},
    submitFilterItemTag: () => { },
}
FilterItemTag.propTypes = {
    isFilterCompanyData: PropTypes.object,
    isFilterNBIDData: PropTypes.object,
    isFilterModifyItemData: PropTypes.object,
    isFilterModifyTimeData: PropTypes.object,
    submitFilterItemTag: PropTypes.func,
}

export default (FilterItemTag);


