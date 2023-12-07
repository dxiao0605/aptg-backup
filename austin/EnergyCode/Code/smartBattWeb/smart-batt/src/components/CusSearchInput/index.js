import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next'
// 此為獨立功能
const CusSearchInput = ({ placeholderName, value, onClickEvent }) => {
    const { t } = useTranslation();
    const [inputVal, setInputVal] = useState(value);
    const onChangeEvent = (event) => {
        setInputVal(event.target.value);
    }
    return (
        <div className="input-group">
            <input type="text" className="form-control btn-sm-b1" placeholder={t(placeholderName)} value={inputVal} onChange={(event) => onChangeEvent(event)} style={{ height: 31 }} autoComplete="off" />
            <button className="btn-sm-b1 btn-outline-primary" onClick={(event) => { onClickEvent(inputVal) }}><i className="fas fa-search" /></button>
        </div>
    )

}
CusSearchInput.defaultProps = {
    placeholderName: '',
    value: '',
    onClickEvent: () => { },
}
CusSearchInput.propTypes = {
    placeholderName: PropTypes.string,
    value: PropTypes.string,
    onClickEvent: PropTypes.func,
}
export default CusSearchInput;