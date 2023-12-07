import React from 'react';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';
import CommonSelect from './CommonSelect';

const ContentAddEdit = ({ inputParameter, inputList, data, selectMenu, handleInputChange, handleInputListChange, handleSelectChange, limits, model }) => {
    const { t } = useTranslation();
    const { CompanyList, LanguageList, RoleList, TimeZoneList, } = selectMenu;
    const {
        Name,//顯示名稱
        Company,//公司
        Mobile,//行動電話
        Email,//電子郵件
        Account,//帳號
        RoleId,//角色
        Password,//密碼
        PasswordTWO,//重新輸入密碼
        Language,//語系
        TimeZone,//時區
        CreateTime,//建立日期
    } = inputList;
    const handlePasswordChk = (e) => {
        if (data.Password === "") { e.target.className = "form-control border-danger"; return; }
        if (data.Password.length < e.target.minLength) { e.target.className = "form-control border-danger"; return; }
        const regex = new RegExp(/[A-Za-z0-9\\-_!@#$%^&*()_+=,.<>:;]*/);
        const Exec = regex.exec(data.Password);
        if ((Exec.length > 0 && Exec[0] !== Exec.input) || Exec?.length === 0) {
            e.target.value = "";
            handleInputChange(e, inputParameter.Inpt[Password].disabled);
            e.target.className = "form-control border-danger";
        } else {
            e.target.className = "form-control";
        }
    }
    const handlePasswordTwoChk = (e) => {
        if (data.PasswordTWO.length < e.target.minLength) { e.target.className = "form-control border-danger"; return; }
        if (data.Password === "" || data.PasswordTWO === "" || data.Password !== data.PasswordTWO) {
            e.target.value = "";
            handleInputChange(e, inputParameter.Inpt[PasswordTWO].disabled);
            e.target.className = "form-control border-danger";
        } else {
            e.target.className = "form-control";
        }
    }
    const handleAccountChk = (event, disabled) => {
        const value = event.target.value.replace(/[^a-zA-Z0-9_-]/g, '');        
        event.target.value = value;
        handleInputChange(event, disabled);
    }
    const handleMobileChk = (event, disabled) => {
        const value = event.target.value.replace(/\D*/g, '');
        event.target.value = value;
        handleInputChange(event, disabled);
    }
    const handleEmailChk = (e) => {
        if (data.Email === "" || data.Email.indexOf('@') <= 0) {
            e.target.value = "";
            handleInputChange(e, inputParameter.Inpt[Email].disabled);
            e.target.className = "form-control border-danger";
        } else {
            e.target.className = "form-control";
        }
    }
    return (
        <div className="py-3">
            {/* 顯示名稱 */}
            {
                inputParameter?.Inpt[Name] && inputParameter.Inpt[Name].isShow &&
                <div className="d-inline-block col-xl-6 col-12 p-0 my-2">
                    <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[Name].i18nKey)}*</div>
                    <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                        <input type="text" className="form-control" autoComplete="off"
                            name={Name}
                            value={data[Name]}
                            disabled={inputParameter.Inpt[Name].disabled || !(limits?.Button?.UserEdit === 1 && data.EditUser)}//limits = functionList控制，EditUser是table每筆的資訊
                            maxLength={inputParameter.Inpt[Name].maxLength}
                            onChange={(event) => { handleInputChange(event, inputParameter.Inpt[Name].disabled) }}
                        />
                    </div>
                </div>
            }
            {/* 公司-新增 */}
            {
                model === "Add" && inputParameter?.Inpt[Company] && inputParameter?.Inpt[Company].isShow &&
                <div className="d-inline-block col-xl-6 col-12 p-0 my-2">
                    <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[Company].i18nKey)}*</div>
                    <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                        <input className="form-control" autoComplete="off"
                            name={Company} list={Company}
                            disabled={inputParameter.Inpt[Company].disabled || !(limits?.Button?.AddCompany === 1)}
                            value={data[Company].Label}
                            onChange={(event) => handleInputListChange(event, inputParameter.Inpt[Company].disabled)}
                        />
                        <datalist id={Company} >
                            {
                                Array.isArray(CompanyList) && CompanyList.length > 0 && CompanyList.map(item => {
                                    return <option key={item.Value.toString()} value={item.Label} />
                                })
                            }
                        </datalist>
                    </div>
                </div>
            }
            {/* 公司-編輯 */}
            {
                model === "Edit" && inputParameter?.Inpt[Company] && inputParameter?.Inpt[Company].isShow &&
                <div className="d-inline-block col-xl-6 col-12 p-0 my-2">
                    <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[Company].i18nKey)}*</div>
                    <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                        <input className="form-control" autoComplete="off"
                            name={Company} list={Company}
                            disabled={inputParameter.Inpt[Company].disabled || !(limits?.Button?.UserEdit === 1 && data?.EditUser)}
                            value={data[Company].Label}
                            onChange={(event) => handleInputListChange(event, inputParameter.Inpt[Company].disabled)}
                        />
                        <datalist id={Company} >
                            {
                                Array.isArray(CompanyList) && CompanyList.length > 0 && CompanyList.map(item => {
                                    return <option key={item.Value.toString()} value={item.Label} />
                                })
                            }
                        </datalist>
                    </div>
                </div>
            }
            {/* 行動電話 */}
            {
                inputParameter?.Inpt[Mobile] && inputParameter?.Inpt[Mobile].isShow &&
                <div className="d-inline-block col-xl-6 col-12 p-0 my-2">
                    <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[Mobile].i18nKey)}</div>
                    <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                        <input type="text" className="form-control" autoComplete="off"
                            name={Mobile}
                            value={data[Mobile]}
                            disabled={inputParameter.Inpt[Mobile].disabled || !(limits?.Button?.UserEdit === 1 && data?.EditUser)}
                            maxLength={inputParameter?.Inpt[Mobile].maxLength}
                            onChange={(event) => { handleMobileChk(event, inputParameter.Inpt[Mobile].disabled) }}
                        />
                    </div>
                </div>
            }
            {/* 電子郵件 */}
            {
                inputParameter?.Inpt[Email] && inputParameter?.Inpt[Email].isShow &&
                <div className="d-inline-block col-xl-6 col-12 p-0 my-2">
                    <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[Email].i18nKey)}*</div>
                    <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                        <input type="text" className="form-control" autoComplete="off"
                            name={Email}
                            value={data[Email]}
                            disabled={inputParameter.Inpt[Email].disabled || !(limits?.Button?.UserEdit === 1 && data?.EditUser)}
                            maxLength={inputParameter?.Inpt[Email].maxLength}
                            onChange={(event) => { handleInputChange(event, inputParameter.Inpt[Email].disabled) }}
                            onBlur={(e) => handleEmailChk(e)}
                        />
                    </div>
                </div>
            }
            {/* 帳號 */}
            {
                inputParameter?.Inpt[Account] && inputParameter?.Inpt[Account].isShow &&
                <div className="d-inline-block col-xl-6 col-12 p-0 my-2">
                    <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[Account].i18nKey)}*</div>
                    <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                        <input type="text" className="form-control" autoComplete="off"
                            name={Account}
                            value={data[Account]}
                            disabled={inputParameter.Inpt[Account].disabled || !(limits?.Button?.UserEdit === 1 && data?.EditUser)}
                            maxLength={inputParameter?.Inpt[Account].maxLength}
                            onChange={(event) => { handleAccountChk(event, inputParameter.Inpt[Account].disabled) }}
                        />
                    </div>
                </div>
            }
            {/* 角色 */}
            {
                inputParameter?.Inpt[RoleId] && inputParameter?.Inpt[RoleId].isShow &&
                <div className="d-inline-block col-xl-6 col-12 p-0 my-2">
                    <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[RoleId].i18nKey)}*</div>
                    <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                        <CommonSelect
                            className="form-control SelectBorder0"
                            openI18nKey={false}
                            openSelMultiple={false}
                            defSelectVal={data[RoleId]}
                            defSelectList={RoleList}
                            onChangeHandler={(value) => { handleSelectChange(RoleId, value, inputParameter.Inpt[RoleId].disabled) }}
                            disabled={inputParameter.Inpt[RoleId].disabled || !(limits?.Button?.UserEdit === 1 && data?.EditUser)}
                        />
                    </div>
                </div>
            }
            {/* 密碼 */}
            {
                inputParameter?.Inpt[Password] && inputParameter?.Inpt[Password].isShow &&
                <div className="d-inline-block col-xl-6 col-12 p-0 my-2">
                    <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[Password].i18nKey)}*</div>
                    <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                        <input type="password" className={"form-control"} autoComplete="off"
                            name={Password}
                            value={data[Password]}
                            disabled={inputParameter.Inpt[Password].disabled || !(limits?.Button?.UserEdit === 1 && data?.EditUser)}
                            maxLength={inputParameter?.Inpt[Password].maxLength}
                            minLength={inputParameter?.Inpt[Password].minLength}
                            onChange={(event) => { handleInputChange(event, inputParameter.Inpt[Password].disabled) }}
                            onBlur={(e) => handlePasswordChk(e)}
                        />
                    </div>
                </div>
            }
            {/* 重新輸入密碼 */}
            {
                inputParameter?.Inpt[PasswordTWO] && inputParameter?.Inpt[PasswordTWO].isShow &&
                <div className="d-inline-block col-xl-6 col-12 p-0 my-2">
                    <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[PasswordTWO].i18nKey)}*</div>
                    <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                        <input type="password" className={"form-control"} autoComplete="off"
                            name={PasswordTWO}
                            value={data[PasswordTWO]}
                            disabled={inputParameter.Inpt[PasswordTWO].disabled || !(limits?.Button?.UserEdit === 1 && data?.EditUser)}
                            maxLength={inputParameter?.Inpt[PasswordTWO].maxLength}
                            minLength={inputParameter?.Inpt[PasswordTWO].minLength}
                            onChange={(event) => { handleInputChange(event, inputParameter.Inpt[PasswordTWO].disabled) }}
                            onBlur={(e) => handlePasswordTwoChk(e)}
                        />
                    </div>
                </div>
            }
            {/* 語系 */}
            {
                inputParameter?.Inpt[Language] && inputParameter?.Inpt[Language].isShow &&
                <div className="d-inline-block col-xl-6 col-12 p-0 my-2">
                    <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[Language].i18nKey)}*</div>
                    <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                        <CommonSelect
                            className="form-control SelectBorder0"
                            openI18nKey={false}
                            openSelMultiple={false}
                            defSelectVal={data[Language]}
                            defSelectList={LanguageList}
                            onChangeHandler={(value) => { handleSelectChange(Language, value, inputParameter.Inpt[Language].disabled) }}
                            disabled={inputParameter.Inpt[Language].disabled || !(limits?.Button?.UserEdit === 1 && data?.EditUser)}
                        />
                    </div>
                </div>
            }
            {/* 時區 */}
            {
                inputParameter?.Inpt[TimeZone] && inputParameter?.Inpt[TimeZone].isShow &&
                <div className="d-inline-block col-xl-6 col-12 p-0 my-2">
                    <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[TimeZone].i18nKey)}*</div>
                    <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                        <CommonSelect
                            className="form-control SelectBorder0"
                            openI18nKey={false}
                            openSelMultiple={false}
                            defSelectVal={data[TimeZone]}
                            defSelectList={TimeZoneList}
                            onChangeHandler={(value) => { handleSelectChange(TimeZone, value, inputParameter.Inpt[TimeZone].disabled) }}
                            disabled={inputParameter.Inpt[TimeZone].disabled || !(limits?.Button?.UserEdit === 1 && data?.EditUser)}//
                        />
                    </div>
                </div>
            }
            {/* 建立日期 */}
            {
                inputParameter?.Inpt[CreateTime] && inputParameter?.Inpt[CreateTime].isShow &&
                <div className="d-inline-block col-xl-6 col-12 p-0 my-2">
                    <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[CreateTime].i18nKey)}*</div>
                    <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                        <input type="text" className="form-control" autoComplete="off"
                            name={CreateTime}
                            value={data[CreateTime]}
                            disabled={inputParameter.Inpt[CreateTime].disabled || !(limits?.Button?.UserEdit === 1 && data?.EditUser)}
                            onChange={() => { }}
                        />
                    </div>
                </div>
            }

        </div>
    )
}
ContentAddEdit.defaultProps = {
    limits: {
        Button: {
            UserEdit: 1
        }
    },
    data: {
        date: new Date(),
        BatteryGroupIDValue: '',
        BatteryTypeValue: '',
        EditUser: false,
    },
    selectMenu: {},
    inputParameter: { disabled: true, isShow: false, minLength: 0, maxLength: 0 },
    handleSelectChange: () => { },
    handleInputChange: () => { },
    handleInputListChange: () => { },
}
ContentAddEdit.propTypes = {
    limits: PropTypes.object,
    data: PropTypes.object,
    selectMenu: PropTypes.object,
    inputParameter: PropTypes.object,
    inputList: PropTypes.object,
    handleSelectChange: PropTypes.func,
    handleInputChange: PropTypes.func,
    handleInputListChange: PropTypes.func,
}
export default ContentAddEdit;


