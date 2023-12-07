import React from 'react';
import { useTranslation } from 'react-i18next';
import { makeStyles } from "@material-ui/core/styles";
import PropTypes from 'prop-types';

const useStyles = makeStyles((theme) => ({
    checkBox: {
        top: '1px',
    },
}));

const ContentAddEdit = ({ limits, inputParameter, inputList, data, onUpdAuthorityData }) => {
    const { t } = useTranslation();
    const classes = useStyles([]);
    const {
        //P1200,//總攬
        P1300,//告警
        P1400,//電池數據
        P1501,//電池組管理
        P1502,//站台管理
        P1503,//通訊序號
        P1504,//電池參數設定
        P1600,//電池歷史
        P1700,//使用者管理
        P1800,//系統設定
    } = inputList;
    const onCheckBoxEvent = (e) => {
        const split = e.target.name.split('_');
        if (split.length === 0) { return; }
        const checked = e.target.checked;
        if (split[1] === "View") {
            if (checked) {
                const data = {
                    page: split[0],
                    name: split[1],
                    value: checked,
                }
                onUpdAuthorityData(data);
            } else {
                Object.keys(inputParameter.Inpt[split[0]]).forEach(item => {
                    const data = {
                        page: split[0],
                        name: item,
                        value: checked,
                    }
                    onUpdAuthorityData(data);
                });
            }            
        } else {//後面的選項
            if (checked) {//有勾，要連View一起勾選
                const data_View = {
                    page: split[0],
                    name: "View",
                    value: checked,
                }
                const data = {
                    page: split[0],
                    name: split[1],
                    value: checked,
                }
                onUpdAuthorityData(data_View);
                onUpdAuthorityData(data);
            } else {
                const data = {
                    page: split[0],
                    name: split[1],
                    value: checked,
                }
                onUpdAuthorityData(data);
            }
        }

    }
    return (
        <div className="py-3">
            {/* 告警 */}
            <div className="d-flex align-items-start col-12 p-0 my-2">
                <div className="px-0 col-xl-3 col-5 d-inline-block">{t(P1300.replace('P', ''))}：</div>
                <div className="px-0 col-xl-8 col-7 d-inline-block">
                    {
                        Object.keys(data[P1300]).map((item, idx) => (
                            <div key={idx} className="d-inline-block" style={{ minWidth: "12em" }}>
                                <span className={classes.checkBox + ' form-check'}>
                                    <input type='checkbox' className="form-check-input" style={{ zoom: '140%', margin: '2.5px -16px' }} id={P1300 + item}
                                        name={P1300 + "_" + item}
                                        checked={data[P1300][item]}
                                        onChange={(e) => { onCheckBoxEvent(e) }}
                                        disabled={limits?.Edit === 1 && limits?.Button?.RolePrivilege === 1  ? false : true}
                                    />
                                    <label className="form-check-label" htmlFor={P1300 + item}>{t(inputParameter.Inpt[P1300][item])}</label>
                                </span>
                            </div>
                        ))
                    }
                </div>
            </div>
            {/* 電池數據 */}
            <div className="d-flex align-items-start col-12 p-0 my-2">
                <div className="px-0 col-xl-3 col-5 d-inline-block">{t(P1400.replace('P', ''))}：</div>
                <div className="px-0 col-xl-8 col-7 d-inline-block">
                    {
                        Object.keys(data[P1400]).map((item, idx) => (
                            <div key={idx} className="d-inline-block" style={{ minWidth: "12em" }}>
                                <span className={classes.checkBox + ' form-check'}>
                                    <input type='checkbox' className="form-check-input" style={{ zoom: '140%', margin: '2.5px -16px' }} id={P1400 + item}
                                        name={P1400 + "_" + item}
                                        checked={data[P1400][item]}
                                        onChange={(e) => { onCheckBoxEvent(e) }}
                                        disabled={limits?.Edit === 1 && limits?.Button?.RolePrivilege === 1  ? false : true}
                                    />
                                    <label className="form-check-label" htmlFor={P1400 + item}>{t(inputParameter.Inpt[P1400][item])}</label>
                                </span>
                            </div>
                        ))
                    }
                </div>
            </div>
            {/* 電池管理 */}
            <div>
                <div className="px-0 d-inline-block">{t(1500)}：</div>
                {/* 電池組管理 */}
                <div className="d-flex align-items-start col-12 p-0 my-2">
                    <div className="px-0 col-xl-3 col-5 d-inline-block pl-3">{t(P1501.replace('P', ''))}：</div>
                    <div className="px-0 col-xl-8 col-7 d-inline-block">
                        {
                            Object.keys(data[P1501]).map((item, idx) => (
                                <div key={idx} className="d-inline-block" style={{ minWidth: "12em" }}>
                                    <span className={classes.checkBox + ' form-check'}>
                                        <input type='checkbox' className="form-check-input" style={{ zoom: '140%', margin: '2.5px -16px' }} id={P1501 + item}
                                            name={P1501 + "_" + item}
                                            checked={data[P1501][item]}
                                            onChange={(e) => { onCheckBoxEvent(e) }}
                                            disabled={limits?.Edit === 1 && limits?.Button?.RolePrivilege === 1  ? false : true}
                                        />
                                        <label className="form-check-label" htmlFor={P1501 + item}>{t(inputParameter.Inpt[P1501][item])}</label>
                                    </span>
                                </div>
                            ))
                        }
                    </div>
                </div>
                {/* 站台管理 */}
                <div className="d-flex align-items-start col-12 p-0 my-2">
                    <div className="px-0 col-xl-3 col-5 d-inline-block pl-3">{t(P1502.replace('P', ''))}：</div>
                    <div className="px-0 col-xl-8 col-7 d-inline-block">
                        {
                            Object.keys(data[P1502]).map((item, idx) => (
                                <div key={idx} className="d-inline-block" style={{ minWidth: "12em" }}>
                                    <span className={classes.checkBox + ' form-check'}>
                                        <input type='checkbox' className="form-check-input" style={{ zoom: '140%', margin: '2.5px -16px' }} id={P1502 + item}
                                            name={P1502 + "_" + item}
                                            checked={data[P1502][item]}
                                            onChange={(e) => { onCheckBoxEvent(e) }}
                                            disabled={limits?.Edit === 1 && limits?.Button?.RolePrivilege === 1  ? false : true}
                                        />
                                        <label className="form-check-label" htmlFor={P1502 + item}>{t(inputParameter.Inpt[P1502][item])}</label>
                                    </span>
                                </div>
                            ))
                        }
                    </div>
                </div>
                {/* 電池歷史 */}
                <div className="d-flex align-items-start col-12 p-0 my-2">
                    <div className="px-0 col-xl-3 col-5 d-inline-block pl-3">{t(P1503.replace('P', ''))}：</div>
                    <div className="px-0 col-xl-8 col-7 d-inline-block">
                        {
                            Object.keys(data[P1503]).map((item, idx) => (
                                <div key={idx} className="d-inline-block" style={{ minWidth: "12em" }}>
                                    <span className={classes.checkBox + ' form-check'}>
                                        <input type='checkbox' className="form-check-input" style={{ zoom: '140%', margin: '2.5px -16px' }} id={P1503 + item}
                                            name={P1503 + "_" + item}
                                            checked={data[P1503][item]}
                                            onChange={(e) => { onCheckBoxEvent(e) }}
                                            disabled={limits?.Edit === 1 && limits?.Button?.RolePrivilege === 1  ? false : true}
                                        />
                                        <label className="form-check-label" htmlFor={P1503 + item}>{t(inputParameter.Inpt[P1503][item])}</label>
                                    </span>
                                </div>
                            ))
                        }
                    </div>
                </div>
                {/* 電池參數 */}
                <div className="d-flex align-items-start col-12 p-0 my-2">
                    <div className="px-0 col-xl-3 col-5 d-inline-block pl-3">{t(P1504.replace('P', ''))}：</div>
                    <div className="px-0 col-xl-8 col-7 d-inline-block">
                        {
                            Object.keys(data[P1504]).map((item, idx) => (
                                <div key={idx} className="d-inline-block" style={{ minWidth: "12em" }}>
                                    <span className={classes.checkBox + ' form-check'}>
                                        <input type='checkbox' className="form-check-input" style={{ zoom: '140%', margin: '2.5px -16px' }} id={P1504 + item}
                                            name={P1504 + "_" + item}
                                            checked={data[P1504][item]}
                                            onChange={(e) => { onCheckBoxEvent(e) }}
                                            disabled={limits?.Edit === 1 && limits?.Button?.RolePrivilege === 1  ? false : true}
                                        />
                                        <label className="form-check-label" htmlFor={P1504 + item}>{t(inputParameter.Inpt[P1504][item])}</label>
                                    </span>
                                </div>
                            ))
                        }
                    </div>
                </div>
            </div>

            {/* 電池歷史 */}
            <div className="d-flex align-items-start col-12 p-0 my-2">
                <div className="px-0 col-xl-3 col-5 d-inline-block">{t(P1600.replace('P', ''))}：</div>
                <div className="px-0 col-xl-8 col-7 d-inline-block">
                    {
                        Object.keys(data[P1600]).map((item, idx) => (
                            <div key={idx} className="d-inline-block" style={{ minWidth: "12em" }}>
                                <span className={classes.checkBox + ' form-check'}>
                                    <input type='checkbox' className="form-check-input" style={{ zoom: '140%', margin: '2.5px -16px' }} id={P1600 + item}
                                        name={P1600 + "_" + item}
                                        checked={data[P1600][item]}
                                        onChange={(e) => { onCheckBoxEvent(e) }}
                                        disabled={limits?.Edit === 1 && limits?.Button?.RolePrivilege === 1  ? false : true}
                                    />
                                    <label className="form-check-label" htmlFor={P1600 + item}>{t(inputParameter.Inpt[P1600][item])}</label>
                                </span>
                            </div>
                        ))
                    }
                </div>
            </div>

            {/* 使用者管理 */}
            <div className="d-flex align-items-start col-12 p-0 my-2">
                <div className="px-0 col-xl-3 col-5 d-inline-block">{t(P1700.replace('P', ''))}：</div>
                <div className="px-0 col-xl-8 col-7 d-inline-block">
                    {
                        Object.keys(data[P1700]).map((item, idx) => (
                            <div key={idx} className="d-inline-block" style={{ minWidth: "12em" }}>
                                <span className={classes.checkBox + ' form-check'}>
                                    <input type='checkbox' className="form-check-input" style={{ zoom: '140%', margin: '2.5px -16px' }} id={P1700 + item}
                                        name={P1700 + "_" + item}
                                        checked={data[P1700][item]}
                                        onChange={(e) => { onCheckBoxEvent(e) }}
                                        disabled={limits?.Edit === 1 && limits?.Button?.RolePrivilege === 1  ? false : true}
                                    />
                                    <label className="form-check-label" htmlFor={P1700 + item}>{t(inputParameter.Inpt[P1700][item])}</label>
                                </span>
                            </div>
                        ))
                    }
                </div>
            </div>

            {/* 系統設定 */}
            <div className="d-flex align-items-start col-12 p-0 my-2">
                <div className="px-0 col-xl-3 col-5 d-inline-block">{t(P1800.replace('P', ''))}：</div>
                <div className="px-0 col-xl-8 col-7 d-inline-block">
                    {
                        Object.keys(data[P1800]).map((item, idx) => (
                            <div key={idx} className="d-inline-block" style={{ minWidth: "12em" }}>
                                <span className={classes.checkBox + ' form-check'}>
                                    <input type='checkbox' className="form-check-input" style={{ zoom: '140%', margin: '2.5px -16px' }} id={P1800 + item}
                                        name={P1800 + "_" + item}
                                        checked={data[P1800][item]}
                                        onChange={(e) => { onCheckBoxEvent(e) }}
                                        disabled={limits?.Edit === 1 && limits?.Button?.RolePrivilege === 1  ? false : true}
                                    />
                                    <label className="form-check-label" htmlFor={P1800 + item}>{t(inputParameter.Inpt[P1800][item])}</label>
                                </span>
                            </div>
                        ))
                    }
                </div>
            </div>


        </div>
    )
}
ContentAddEdit.defaultProps = {
    data: {
        date: new Date(),
        BatteryGroupIDValue: '',
        BatteryTypeValue: '',
    },
    selectMenu: {},
    inputParameter: { disabled: true, isShow: false, minLength: 0, maxLength: 0 },
    handleSelectChange: () => { },
    handleInputChange: () => { },
    handleInputListChange: () => { },
}
ContentAddEdit.propTypes = {
    data: PropTypes.object,
    selectMenu: PropTypes.object,
    inputParameter: PropTypes.object,
    inputList: PropTypes.object,
    handleSelectChange: PropTypes.func,
    handleInputChange: PropTypes.func,
    handleInputListChange: PropTypes.func,
}
export default ContentAddEdit;


