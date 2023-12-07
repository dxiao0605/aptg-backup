import React, { useState, useEffect } from 'react';
/* i18n Functional Components */
// import { useTranslation } from 'react-i18next'

const FilterSaveItem = ({ FilterName, FilterConfig, idx, showIdx, onClickBookmark, onClickTrash, onUploadShowIdx, children }) => {
    const [showMouse, setShowMouse] = useState(false);
    useEffect(() => {
        if (Array.isArray(showIdx) && showIdx.length > 0 && showIdx.findIndex(item => item === idx) < 0) {
            setShowMouse(false);
        }
    }, [idx, showIdx, setShowMouse]);
    const onMouseEnter = (idx) => {
        onUploadShowIdx(idx);
        setShowMouse(true);
    }
    const onMouseLeave = () => {
        onUploadShowIdx([]);
        setShowMouse(false);
    }
    return (
        <>
            <div className='form-inline justify-content-between mb-1' >
                <div className="d-flex align-items-center pointer"
                    style={{minWidth:'180px'}}
                    onMouseEnter={() => onMouseEnter(idx)}
                    onClick={(e) => onClickBookmark(FilterConfig)}>
                    <span className='mx-2'>{FilterName}</span>
                </div>
                <div className="form-inline align-items-center pointer">
                    <span className='custom-control'>
                        <i className='far fa-trash-alt mr-2' onClick={(e) => onClickTrash(FilterConfig)} />
                    </span>
                </div>
                {showMouse && <>
                    <div className='filterSaveFixed' onMouseEnter={() => onMouseEnter(idx)} />
                    <div className={`filterSaveItem ${true ? '' : 'filterSaveItem_hide'}`} onMouseLeave={() => { onMouseLeave() }}>
                        {children}
                    </div>
                </>
                }
            </div>
        </>
    )

}
FilterSaveItem.defaultProps = {
    FilterName: '',
    FilterConfig: {},
    onClickBookmark: () => { },
    onClickTrash: () => { },
    onUploadShowIdx: () => { },
}
export default FilterSaveItem;