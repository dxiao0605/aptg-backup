import React, { Fragment, useState, useEffect } from "react";
import PropTypes from 'prop-types';
import LastBB from './LastBB';
import LastBA from './LastBA';
import LastB5 from './LastB5';
import LastB3 from './LastB3';
import { checkedInputValue } from './utils';


// 篩選核取清單
const PeriodSetting = ({
    isShowPeriodSetting,
    radioVal,
    data,
    handleUpdateData,
}) => {
    const [lastData, setLastData] = useState(data);
    useEffect(() => {
        if (lastData !== data) {
            setLastData(data);
        }
    }, [lastData, data]);

    const handleInputChange = (code, e, onfocus) => {
        let value = checkedInputValue(code, e.target.value, e.target.name, onfocus);
        if (onfocus && value === undefined) {//input修改時 
            return;
        } else if (!onfocus && value === undefined) {//離開input時
            value = "";
        }
        setLastData({ ...lastData, [e.target.name]: value });
        handleUpdateData({ ...lastData, [e.target.name]: value });
    }
    const handleB3B5InputChange = (code, e, onfocus) => {
        let value = checkedInputValue(code, e.target.value, e.target.name, onfocus);
        if (onfocus && value === undefined) {//input修改時 
            return;
        } else if (!onfocus && value === undefined) {//離開input時
            value = "";
        }
        if (code === "179") {
            const newArray = lastData?.Vol ? [...lastData.Vol] : [];
            newArray[e.target.name] = value;
            setLastData({ ...lastData, Vol: newArray });
            handleUpdateData({ ...lastData, Vol: newArray });
        } else if (code === "181") {
            const newArray = lastData?.IR ? [...lastData.IR] : [];
            newArray[e.target.name] = value;
            setLastData({ ...lastData, IR: newArray });
            handleUpdateData({ ...lastData, IR: newArray });
        }
    }
    return (
        <Fragment>
            {
                isShowPeriodSetting && radioVal === "BB" &&
                <LastBB
                    data={lastData}
                    handleInputChange={(e) => { handleInputChange('187', e, true) }}
                    handleInputBlur={(e) => { handleInputChange('187', e, false) }}
                />
            }
            {
                isShowPeriodSetting && radioVal === "BA" &&
                <LastBA
                    data={lastData}
                    handleInputChange={(e) => { handleInputChange('186', e, true) }}
                    handleInputBlur={(e) => { handleInputChange('186', e, false) }}
                />
            }
            {
                isShowPeriodSetting && radioVal === "B5" &&
                <LastB5
                    data={lastData}
                    handleInputChange={(e) => { handleB3B5InputChange('179', e, true) }}
                    handleInputBlur={(e) => { handleB3B5InputChange('179', e, false) }}
                />
            }
            {
                isShowPeriodSetting && radioVal === "B3" &&
                <LastB3
                    data={lastData}
                    handleInputChange={(e) => { handleB3B5InputChange('181', e, true) }}
                    handleInputBlur={(e) => { handleB3B5InputChange('181', e, false) }}
                />
            }
        </Fragment >
    );
};
PeriodSetting.defaultProps = {
    isShowPeriodSetting: false,
    radioVal: '',
    data: {},
    handleUpdateData: () => { },
}
PeriodSetting.propTypes = {
    isShowPeriodSetting: PropTypes.bool,
    radioVal: PropTypes.string,
    data: PropTypes.object,
    handleUpdateData: PropTypes.func,
}
export default PeriodSetting;
