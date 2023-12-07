import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';

const FilterSaveHeader = ({ children }) => {
    const { t } = useTranslation();    
    const [open, setOpen] = useState(true);
    const onClick = () => {
        setOpen(!open);
    }    
    return (
        <>
            <div className="filter overflowY mb-0 pb-0">
                <div className='form-inline justify-content-between mb-1' >
                    <div className="d-flex align-items-center">
                        <span className='mx-2 font-weight-bold'>{t('1085')}</span>
                    </div>
                    <div className="form-inline align-items-center">
                        <span className='custom-control pointer'>
                            <i className={`fas ${!open ? "fa-plus" : "fa-minus"} mr-2`} onClick={() => { onClick() }} />
                        </span>
                    </div>
                </div>
            </div>
            <ul className="filter overflowY mb-0 pt-1 mh-10">
                {
                    open && children
                }
            </ul>
        </>
    )

}
FilterSaveHeader.defaultProps = {
   
}
export default FilterSaveHeader;