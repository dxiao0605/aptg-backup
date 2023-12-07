import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next'

const CusBookMark = ({ placeholderName, value, onClickEvent }) => {
    const { t } = useTranslation();
    const [isFilterSave, setIsFilterSave] = useState(false);
    const [inputVal, setInputVal] = useState(value);
    const onClickFilterSaveEvent = (active) => {
        setIsFilterSave(active);
        setInputVal('');
    }
    const onChangeEvent = (event) => {
        setInputVal(event.target.value);
    }
    return (
        <>
            <div className="d-inline-block"
                style={{ position: 'relative' }}
            // onMouseEnter={() => { setIsFilterSave(true) }}              
            >
                <button className="btn-sm-b1 btn-outline-primary mr-2" onClick={() => { onClickFilterSaveEvent(!isFilterSave) }}>
                    <i className="fas fa-bookmark" />
                </button>
                {
                    isFilterSave &&
                    <div className="bookmark">
                        <div className="input-group input-group-background">
                            <input type="text" className="form-control btn-sm-b1" placeholder={t(placeholderName)} value={inputVal} onChange={(event) => onChangeEvent(event)} style={{ height: 31 }} autoComplete="off" />
                            <button className="btn-sm-b1 btn-outline-primary" style={{ marginLeft: '1px' }} onClick={(event) => { onClickEvent(inputVal); onClickFilterSaveEvent(!isFilterSave); }}>
                                <i className="fas fa-cloud-upload-alt" />
                            </button>
                        </div>
                    </div>
                }
            </div>
        </>
    )

}
CusBookMark.defaultProps = {
    placeholderName: '',
    value: '',
    onClickEvent: () => {},
}
CusBookMark.propTypes = {
    placeholderName: PropTypes.string,
    value: PropTypes.string,
    onClickEvent: PropTypes.func,
}
export default CusBookMark;