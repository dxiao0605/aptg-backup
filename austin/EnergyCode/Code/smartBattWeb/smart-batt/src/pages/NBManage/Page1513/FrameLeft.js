import React, { Fragment, useState } from "react";
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';
import { CusMainBtnStyle } from '../../../components/CusMainBtnStyle';


// 左邊框
const FrameLeft = ({
    List,
    showDelBtn,
    // isModel,//判斷目前為 分配(Sent)/未分配(NotSent)
    onUpdNBList,
    onClickSetDelModel,
}) => {
    const { t } = useTranslation();
    const [checkboxAll, setCheckboxAll] = useState(false);
    const onCheckBoxAllEvent = (e) => {
        const newList = List.map(item => {
            item.checked = !checkboxAll;

            // 判斷是否是預設站台 DefaultGroup (0:預設站台, 1:非預設站台)
            // 預設站台的通訊序號才可改為未分配
            // if((isModel === 'Sent') && (item.DefaultGroup === 1)) {
            //     item.checked = false;
            // }
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
                    <div className="header border border-dark">
                        <div className="form-inline py-1 pr-3" style={{ lineHeight: 2, justifyContent: 'space-around' }}>
                            <div className="col-2 px-0 form-check arrowCheckbox" style={{ justifyContent: 'flex-end' }}>
                                <input type='checkbox' className="form-check-input" style={{ zoom: '140%', marginTop: '0', marginLeft: '0' }}
                                    onChange={(e) => { onCheckBoxAllEvent(e) }}
                                    checked={checkboxAll}
                                />
                            </div>
                            <div className="col-10 px-0">{t('1057')}</div>
                        </div>
                    </div>
                    <div className="body border border-dark scrollStyle" style={{ overflowY: 'scroll', height: '26vh', minHeight: '2.5em' }}>
                        {
                            Array.isArray(List) && List?.length > 0 && List.map((item, idx) => {
                                return (
                                    <div key={idx} className="form-inline" style={{ lineHeight: 2, justifyContent: 'space-around' }}>
                                        <div className="col-2 px-0 form-check arrowCheckbox" style={{ justifyContent: 'flex-end' }}>
                                            <input type='checkbox' className="form-check-input" style={{ zoom: '140%', marginTop: '0', marginLeft: '0' }} id={'P' + idx}
                                                onChange={(e) => { onCheckBoxEvent(e, item) }}
                                                checked={item.checked}
                                                // disabled={ ((isModel === 'Sent') && (item.DefaultGroup === 1)) ? true : false}   //DefaultGroup(0:預設站台, 1:非預設站台)
                                            />
                                        </div>
                                        <div className="col-10 px-0">
                                            {/* <label className={`d-flex align-items-center justify-content-center my-0 ${((isModel === 'Sent') && (item.DefaultGroup === 1)) ? 'text-gray': ''}`} htmlFor={'P' + idx}>{item.NBID}</label> */}
                                            <label className={`d-flex align-items-center justify-content-center my-0`} htmlFor={'P' + idx}>{item.NBID}</label>
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
                {
                    showDelBtn &&
                    <CusMainBtnStyle name={t('1052')} disabled={false} icon="" clickEvent={() => onClickSetDelModel()} />
                }
            </div>
        </Fragment >
    );
};
FrameLeft.defaultProps = {
    List: [],
    showDelBtn: false,
    isModel: 'Sent',
    onClickHanlder: () => { },
    onClickSetDelModel: () => { },
}
FrameLeft.propTypes = {
    List: PropTypes.array,
    showDelBtn: PropTypes.bool,
    isModel: PropTypes.string,
    onClickHanlder: PropTypes.func,
    onClickSetDelModel: PropTypes.func,
}
export default FrameLeft;
