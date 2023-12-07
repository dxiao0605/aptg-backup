import React from 'react';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';

const ContentAddEdit = ({ companyList, company, inputList, data, handleSelectChange, handleInputChange, isAdd }) => {
    const { t } = useTranslation();
    const { BatteryTypeName } = inputList;
    const {
        Company,
        BatteryTypeName: BatteryTypeNameVal,
    } = data;
    return (
        <>
            {/* 公司別 */}
            {
                company === "1" && isAdd &&
                <div className="col-12 p-0 my-4">
                    <div className="d-flex my-1">{t("1064")}</div>
                    <div className="d-flex my-1">
                        <select className="form-control" value={Company} onChange={(event) => handleSelectChange(event)} >
                            {
                                Array.isArray(companyList) && companyList.length > 0 && companyList.map(item => {
                                    return <option key={item.Value} value={item.Value}>{item.Label}</option>
                                })
                            }
                        </select>
                    </div>
                </div>
            }
            {/* 電池型號中文 */}
            <div className="col-12 p-0 my-4">
                <div className="d-flex my-1">{t("1506")}</div>
                <div className="d-flex my-1">
                    <input type="text" className="form-control" name={BatteryTypeName} value={BatteryTypeNameVal} onChange={(event) => handleInputChange(event)}  autoComplete="off" />
                </div>
            </div>
        </>
    )
}
ContentAddEdit.defaultProps = {
    data: {
        Company: '',
        BatteryTypeName: '',
    },
    companyList: [],
    isAdd: false,
    handleSelectChange: () => { },
    handleInputChange: () => { }
}
ContentAddEdit.propTypes = {
    data: PropTypes.object,
    isAdd: PropTypes.bool,
    companyList: PropTypes.array,
    inputList: PropTypes.object,
    handleSelectChange: PropTypes.func,
    handleInputChange: PropTypes.func
}
export default ContentAddEdit;


