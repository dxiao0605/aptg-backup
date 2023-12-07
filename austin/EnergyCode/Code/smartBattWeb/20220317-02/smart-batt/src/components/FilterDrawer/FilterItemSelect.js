import React, { Fragment, useState, useEffect } from "react";
/* i18n Functional Components */
import PropTypes from 'prop-types';
import Select from '@material-ui/core/Select';
import MenuItem from '@material-ui/core/MenuItem';
import CusSearchInput from '../CusSearchInput'; //搜尋關鍵字
import { filterSelectArray } from '../CusSearchInput/utils';//Search Tool
import { ArrayIsEquals } from '../../utils/ArrayIsEquals';//陣列比對

// 篩選核取清單
const FilterItemSelect = ({
    defInputVal,
    defSelectVal,
    defSelectList,
    onUpdateSelectList,
    onUpdateChainSelectList,
    onUpdateList,
}) => {
    const [selectVal, setSelectVal] = useState(defSelectVal); //選單設定值
    const [selectList, setSelectList] = useState(defSelectList); //選單清單
    useEffect(() => {
        if (ArrayIsEquals(defSelectVal, selectVal)) {
            setSelectVal(defSelectVal);
        }
        if (ArrayIsEquals(defSelectList, selectList)) {
            setSelectList(defSelectList);
        }
    }, [selectVal, defSelectVal, selectList, defSelectList]);
    const onClickEvent = (value) => {
        const key = {
            dataArray: selectList,
            searchText: value,
        }
        const newList = filterSelectArray(key);
        onUpdateSelectList(newList);
        setSelectVal([]);
    }    
    const handleChange = (event) => {
        const vlist = event.target.value;
        const list = vlist.filter(item => item !== "").sort(function (a, b) {            
            return a - b;
        });
        let isButtonList = [];
        let isSelectObject = {};
        if (list.length > 0) {
            selectList.forEach(item => {
                list.forEach(vitem => {
                    if (item.Value === vitem) {
                        isButtonList.push(item);
                        isSelectObject[item.Value] = item;
                    }
                });
            });
        }
        setSelectVal(list);
        onUpdateChainSelectList(isSelectObject);//選單連動=>公司->國家->地域
        onUpdateList({
            isOpen: true,
            isChecked: false,
            isDataList: list,
            isButtonList: isButtonList,
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
                    onChange={handleChange}
                    multiple
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
FilterItemSelect.defaultProps = {
    defInputVal: '',
    defSelectVal: [],
    defSelectList: [],
    handleChange: () => { },
    onUpdateSelectList: () => { },
    onUpdateChainSelectList: () => { },
    onUpdateList: () => { }
}
FilterItemSelect.propTypes = {
    defInputVal: PropTypes.string,
    defSelectList: PropTypes.array,
    handleChange: PropTypes.func,
    onUpdateSelectList: PropTypes.func,
    onUpdateChainSelectList: PropTypes.func,
    onUpdateList: PropTypes.func,
}
export default FilterItemSelect;
