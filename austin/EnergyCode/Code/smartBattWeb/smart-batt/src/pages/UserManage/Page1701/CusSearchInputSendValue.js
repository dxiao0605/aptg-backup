import React, { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
// 此為獨立功能
const CusSearchInput = ({ placeholderName, value, onUpdInput, onClickEvent }) => {
    const { t } = useTranslation();
    const [inputVal, setInputVal] = useState(value);
    const onChangeEvent = (event) => {
        setInputVal(event.target.value);
        onUpdInput(event.target.value);
    }
    useEffect(() => {
        if (inputVal !== value) {
            setInputVal(value);
        }
    }, [inputVal, value])
    return (
        <div className="input-group">
            <input type="text" className="form-control btn-sm-b1" placeholder={t(placeholderName)} value={inputVal} onChange={(event) => onChangeEvent(event)} style={{ height: 31 }} autoComplete="off" />
            <button className="btn-sm-b1 btn-outline-primary mr-2" onClick={(event) => { onClickEvent(inputVal) }}><i className="fas fa-search" /></button>
            <button className="btn-sm-b1 btn-outline-primary" onClick={(event) => { onClickEvent(''); onUpdInput(''); }}>{t('1038')}</button>
        </div>
    )

}
CusSearchInput.defaultProps = {
    placeholderName: '',
    value: '',
    onClickEvent: () => { },
}
export default CusSearchInput;