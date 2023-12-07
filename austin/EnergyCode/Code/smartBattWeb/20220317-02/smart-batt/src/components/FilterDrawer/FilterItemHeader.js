import React, { Fragment, useState, useEffect } from "react";
/* i18n Functional Components */
import PropTypes from 'prop-types';
import { useTranslation } from "react-i18next";



// 篩選核取清單
const FilterItemHeader = ({
    title,
    total,
    detailList,
    showCheckBox,
    isChecked,
    isOpen,
    onClickAllSelectList,
    onUpdateChainSelectList,
    onUpdateList,
    children,
}) => {
    const { t } = useTranslation();
    const [open, setIsOpen] = useState(isOpen);

    useEffect(() => {
        setIsOpen(isOpen)
        //當全選時隱藏下方欄位
        isChecked ? setIsOpen(false) : setIsOpen(true)

    }, [isOpen, isChecked])

    //變更open狀態
    const onClickOpen = (e) => {
        setIsOpen(!open);
    };
    //變更checkbox狀態
    const onClickSelectAll = (e) => {
        setIsOpen(!e.target.checked);
        onClickAllSelectList(!e.target.checked);
        onUpdateChainSelectList();
        onUpdateList({
            isOpen: open,
            isChecked: e.target.checked,
            isDataList: [],
            isButtonList: [],
        });
    };
    // 已選擇詳細清單
    const getDetailList = (array) => {
        const list = [];
        array.forEach(element => {
            list.push(element.Label)
        });
        
        // return list.join('\n');
        return list.join(',\n');
    }
    // console.log("return", checked, counter);
    return (
        <Fragment>
            <div className='form-inline justify-content-between py-2' style={{ boxShadow: '0px 7px 15px -15px #000, 0px -7px 15px -15px #000' }}>
                <div className="d-flex align-items-center" style={{position:'relative'}}>
                    <span className='mx-2'>{t(title)}</span>
                    <span className={`filter_prompt mr-2 ${detailList && detailList.length > 0 ? '' : 'noDetail'}`} 
                        data-list={detailList && detailList.length > 0 ? getDetailList(detailList) : ''}>
                        {total}
                    </span>
                </div>
                {
                    showCheckBox &&
                    <div className="form-inline align-items-center">
                        <span className='custom-control custom-checkbox'>
                            <input type='checkbox' className="custom-control-input " id={'P' + title} onChange={(e) => { onClickSelectAll(e) }} checked={isChecked} />
                            <label className="custom-control-label" htmlFor={'P' + title}>{t('1076')}</label>
                        </span>
                        {
                            !isChecked &&
                            <span className='custom-control custom-checkbox pl-2 mr-1 pointer' onClick={(e) => { onClickOpen(e) }}>
                                <i className={`fas ${!open ? "fa-plus" : "fa-minus"}`} />
                            </span>
                        }
                    </div>
                }
            </div>
            {
                open &&
                < div className='form-inline align-items-center w-100 mt-1 py-2' >
                    {children}
                </div>
            }
        </Fragment >
    );
};
FilterItemHeader.defaultProps = {
    total: 0,
    detailList: [],
    showCheckBox: true,
    isOpen: false,
    isChecked: true,
    onClickAllSelectList: () => { },
    onUpdateChainSelectList: () => { },
    onUpdateList: () => { },
}
FilterItemHeader.propTypes = {
    total: PropTypes.number,
    showCheckBox: PropTypes.bool,
    isOpen: PropTypes.bool,
    isChecked: PropTypes.bool,
    onClickAllSelectList: PropTypes.func,
    onUpdateChainSelectList: PropTypes.func,
    onUpdateList: PropTypes.func,
}
export default FilterItemHeader;
