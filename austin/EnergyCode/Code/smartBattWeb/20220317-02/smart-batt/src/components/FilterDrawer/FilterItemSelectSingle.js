import React, { Fragment, useState, useEffect } from "react";
/* i18n Functional Components */
import PropTypes from 'prop-types';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import CusSearchInput from '../CusSearchInput'; //搜尋關鍵字
import { filterSelectArray } from '../CusSearchInput/utils';//Search Tool


// 篩選核取清單
const FilterItemSelectSingle = ({
    defInputVal,
    defSelectVal,
    defSelectList,
    onUpdateSelectList,
    onUpdateList,
    onChangeDisabledBtn,
}) => {
    const [selectVal, setSelectVal] = useState(defSelectVal); //選單設定值
    const [selectList, setSelectList] = useState(defSelectList); //選單清單
    useEffect(() => {
        if (defSelectVal.toString() !== selectVal.toString()) {
            setSelectVal(defSelectVal);
        }
    }, [selectVal, defSelectVal]);
    const onClickEvent = (value) => {
        const key = {
            dataArray: defSelectList,
            searchText: value,
        }
        const newList = filterSelectArray(key);
        setSelectList(newList);
        setSelectVal([]);
        onChangeDisabledBtn(true);
    }

    const handleChange = (event) => {
        const value = event.target.value;
        const newItem = selectList.filter(filterItem => filterItem.Value === value);
        setSelectVal(value);
        onChangeDisabledBtn(false);
        onUpdateList({
            isOpen: true,
            isChecked: false,
            isDataList: [`${value}`],
            isButtonList: newItem,
        });
    }

    return (
        <Fragment>
            <div className="col-12 mt-2 mb-1">
                <CusSearchInput
                    placeholderName='1037'
                    value={defInputVal}
                    onClickEvent={(value) => onClickEvent(value)}
                />
            </div>
            <div className="col-12 mt-1 mb-2">
                <Select
                    className="w-100"
                    value={selectVal}
                    onChange={(e)=>{handleChange(e)}}
                    // multiple
                >
                    {
                        Array.isArray(selectList) && selectList.length > 0 ?
                            selectList.map((item) => {
                                return item?.Value !== undefined && item?.selectShow && <MenuItem key={item.Value} value={item.Value}>{item.Label}</MenuItem>
                        }) :
                            <MenuItem value=''></MenuItem>
                    }
                </Select>
            </div>
        </Fragment >
    );
};
FilterItemSelectSingle.defaultProps = {
    defInputVal: '',
    defSelectVal: [],
    defSelectList: [],
    handleChange: () => { },
    onUpdateList: () => { },
    onChangeDisabledBtn: () => {}, //無填值時,不可篩選
}
FilterItemSelectSingle.propTypes = {
    defInputVal: PropTypes.string,
    defSelectList: PropTypes.array,
    handleChange: PropTypes.func,
    onUpdateList: PropTypes.func,
    onChangeDisabledBtn: PropTypes.func,
}
export default FilterItemSelectSingle;
