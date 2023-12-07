import React from 'react';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';
import CommonSelect from './CommonSelect';

const ContentAddEdit = ({ inputParameter, inputList, data, selectMenu, handleInputChange, handleSelectChange }) => {
    const { t } = useTranslation();
    const { RoleList } = selectMenu;
    const {
        RoleName,//角色名稱
        CopyRoleId,//複製權限
        RoleDesc,//說明(中文)
        RoleDescE,//說明(英文)
        RoleDescJ,//說明(日文)
    } = inputList;
    const handleRoleNameChk = (event, disabled) => {
        const value = event.target.value.replace(/[^a-zA-Z0-9 _-]*/g, '');           
        if (value !== event.target.value){
            event.target.value = data.RoleName;
        }else{
            handleInputChange(event, disabled);
        }
    }
    return (
        <div className="py-3" style={{ flexFlow: 'row wrap', display: 'flex' }}>
            <div className="d-inline-block px-0 col-12 col-xl-6">
                {/* 角色名稱 */}
                {
                    inputParameter?.Inpt[RoleName] && inputParameter.Inpt[RoleName].isShow &&
                    <div className="d-inline-block col-12 p-0 my-2">
                        <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[RoleName].i18nKey)}*</div>
                        <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                            <input type="text" className="form-control" autoComplete="off"
                                name={RoleName}
                                value={data[RoleName]}
                                disabled={inputParameter.Inpt[RoleName].disabled}
                                maxLength={inputParameter.Inpt[RoleName].maxLength}
                                onChange={(event) => { handleRoleNameChk(event, inputParameter.Inpt[RoleName].disabled) }}
                            />
                        </div>
                    </div>
                }
                {/* 權限 */}
                {
                    inputParameter?.Inpt[CopyRoleId] && inputParameter.Inpt[CopyRoleId].isShow &&
                    <div className="d-inline-block col-12 p-0 my-2">
                        <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[CopyRoleId].i18nKey)}*</div>
                        <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                            <CommonSelect
                                className="form-control SelectBorder0"
                                openI18nKey={false}
                                openSelMultiple={false}
                                defSelectVal={data[CopyRoleId]}
                                defSelectList={RoleList}
                                onChangeHandler={(value) => { handleSelectChange(CopyRoleId, value, inputParameter.Inpt[CopyRoleId].disabled) }}
                                disabled={inputParameter.Inpt[CopyRoleId].disabled}
                            />
                        </div>
                    </div>
                }
            </div>
            <div className="d-inline-block px-0 col-12 col-xl-6">
                {/* 說明(中文) */}
                {
                    inputParameter?.Inpt[RoleDesc] && inputParameter?.Inpt[RoleDesc].isShow &&
                    <div className="d-inline-block col-12 p-0 my-2">
                        <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[RoleDesc].i18nKey)}*</div>
                        <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                            <input type="text" className="form-control" autoComplete="off"
                                name={RoleDesc}
                                value={data[RoleDesc]}
                                disabled={inputParameter.Inpt[RoleDesc].disabled}
                                maxLength={inputParameter?.Inpt[RoleDesc].maxLength}
                                onChange={(event) => { handleInputChange(event, inputParameter.Inpt[RoleDesc].disabled) }}
                            />
                        </div>
                    </div>
                }
                {/* 說明(英文) */}
                {
                    inputParameter?.Inpt[RoleDescE] && inputParameter?.Inpt[RoleDescE].isShow &&
                    <div className="d-inline-block col-12 p-0 my-2">
                        <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[RoleDescE].i18nKey)}*</div>
                        <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                            <input type="text" className="form-control" autoComplete="off"
                                name={RoleDescE}
                                value={data[RoleDescE]}
                                disabled={inputParameter.Inpt[RoleDescE].disabled}
                                maxLength={inputParameter?.Inpt[RoleDescE].maxLength}
                                onChange={(event) => { handleInputChange(event, inputParameter.Inpt[RoleDescE].disabled) }}
                            />
                        </div>
                    </div>
                }
                {/* 說明(日文) */}
                {
                    inputParameter?.Inpt[RoleDescJ] && inputParameter?.Inpt[RoleDescJ].isShow &&
                    <div className="d-inline-block col-12 p-0 my-2">
                        <div className="d-flex my-1 pl-xl-3">{t(inputParameter.Inpt[RoleDescJ].i18nKey)}*</div>
                        <div className="d-flex my-1 pl-xl-3 pr-xl-3">
                            <input type="text" className="form-control" autoComplete="off"
                                name={RoleDescJ}
                                value={data[RoleDescJ]}
                                disabled={inputParameter.Inpt[RoleDescJ].disabled}
                                maxLength={inputParameter?.Inpt[RoleDescJ].maxLength}
                                onChange={(event) => { handleInputChange(event, inputParameter.Inpt[RoleDescJ].disabled) }}
                            />
                        </div>
                    </div>
                }
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


