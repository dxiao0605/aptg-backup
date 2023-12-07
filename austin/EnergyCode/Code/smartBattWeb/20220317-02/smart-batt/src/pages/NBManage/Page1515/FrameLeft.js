import React, { Fragment, useState } from "react";
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';


// 左邊框
const FrameLeft = ({
    List,
    onUpdNBList,
}) => {
    const { t } = useTranslation();
    const [checkboxAll, setCheckboxAll] = useState(false);
    const onCheckBoxAllEvent = (e) => {
        const newList = List.map(item => {

            item.checked = !checkboxAll;
            return item;
        });
        setCheckboxAll(!checkboxAll);
        onUpdNBList(newList);
    }
    const onCheckBoxEvent = (e, item) => {
        const idx = List.findIndex(ite => ite === item);
        if (idx < 0) { return; }
        item.checked = e.target.checked;
        List[idx] = item;
        onUpdNBList(List);
        setCheckboxAll(false);
    }
    return (
        <Fragment>
            <div className="justify-content-center w-100  text-center" style={{ height: '1.5em' }}></div>
            <div className="justify-content-center w-100">
                <div className="w-100 text-center">
                    <div className="header border border-dark" style={{ overflowY: 'hidden', overflowX: 'auto' }}>
                        <div className="d-flex align-items-center py-1 pr-3" style={{ lineHeight: 2, justifyContent: 'space-around' }}>
                            <div className="px-0 ml-3 mr-3 form-check arrowCheckbox">
                                <input type='checkbox' className="form-check-input" style={{ zoom: '140%', marginTop: '0', marginLeft: '0', minWidth: '1em' }}
                                    onChange={(e) => { onCheckBoxAllEvent(e) }}
                                    checked={checkboxAll}
                                />
                            </div>
                            <div className="px-0" style={{ minWidth: '9em' }}>{t('1064')}</div>
                            <div className="px-0" style={{ minWidth: '8em' }}>{t('1057')}</div>
                        </div>
                    </div>
                    <div className="body border border-dark scrollStyle" style={{ overflow: 'auto', height: '26vh', minHeight: '2.5em' }}>
                        {
                            Array.isArray(List) && List?.length > 0 && List.map((item, idx) => {
                                return (
                                    <div key={idx} className="d-flex align-items-center" style={{ lineHeight: 2, justifyContent: 'space-around' }}>
                                        <div className="px-0 ml-3 mr-3 form-check arrowCheckbox">
                                            <input type='checkbox' className="form-check-input" style={{ zoom: '140%', marginTop: '0', marginLeft: '0', minWidth: '1em' }}
                                                id={'P' + idx}
                                                onChange={(e) => { onCheckBoxEvent(e, item) }}
                                                checked={item.checked}
                                            />
                                        </div>
                                        <div className="px-0" style={{ minWidth: '9em' }}>
                                            <label className="d-flex align-items-center justify-content-center my-0" htmlFor={'P' + idx}>{item.Company}</label>
                                        </div>
                                        <div className="px-0" style={{ minWidth: '8em' }}>
                                            <label className="d-flex align-items-center justify-content-center my-0" htmlFor={'P' + idx}>{item.NBID}</label>
                                        </div>
                                    </div>
                                )
                            })
                        }
                    </div>
                </div>
            </div>
            <div className="d-flex my-1  w-100" style={{ justifyContent: 'space-between', minHeight: '36px' }}>
                <div className="py-1">{t('1092') + " " + List.filter(item => item.checked === true).length + " " + t('1093') + " " + List.length + " " + t('1094')}</div>
            </div>
        </Fragment >
    );
};
FrameLeft.defaultProps = {
    List: [],
    showDelBtn: false,
    onClickHanlder: () => { },
    onClickSetDelModel: () => { },
}
FrameLeft.propTypes = {
    List: PropTypes.array,
    showDelBtn: PropTypes.bool,
    onClickHanlder: PropTypes.func,
    onClickSetDelModel: PropTypes.func,
}
export default FrameLeft;
