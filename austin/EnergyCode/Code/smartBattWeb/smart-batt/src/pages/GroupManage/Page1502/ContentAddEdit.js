import React from 'react';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';

const ContentAddEdit = ({ companyList, company, inputList, data, handleSelectChange, handleInputChange, isAdd, errorMsg }) => {
    const { t } = useTranslation();
    const { GroupName, GroupID, Country, Area, Address } = inputList;
    const {
        Company,
        GroupName: GroupNameVal,
        GroupID: GroupIDVal,
        Country: CountryVal,
        Area: AreaVal,
        Address: AddressVal
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
            {/* 站台名稱 */}
            <div className="col-12 p-0 my-4">
                <div className="d-flex my-1">{t("1013")}</div>
                <div className="d-flex my-1">
                    <input type="text" className="form-control" name={GroupName} value={GroupNameVal} onChange={(event) => handleInputChange(event)} autoComplete="off" />
                </div>
            </div>
            {/* 站台編號 */}
            <div className="col-12 p-0 my-4">
                <div className="d-flex my-1">{t("1012")}</div>
                <div className="d-flex my-1">
                    <input type="text" className="form-control" name={GroupID} value={GroupIDVal} onChange={(event) => handleInputChange(event)} autoComplete="off" />
                </div>
            </div>
            {/* 國家 */}
            <div className="col-12 p-0 my-4">
                <div className="d-flex my-1">{t("1028")}</div>
                <div className="d-flex my-1">
                    <input type="text" className="form-control" name={Country} value={CountryVal} onChange={(event) => handleInputChange(event)} autoComplete="off" />
                </div>
            </div>
            {/* 地域 */}
            <div className="col-12 p-0 my-4">
                <div className="d-flex my-1">{t("1029")}</div>
                <div className="d-flex my-1">
                    <input type="text" className="form-control" name={Area} value={AreaVal} onChange={(event) => handleInputChange(event)} autoComplete="off" />
                </div>
            </div>
            {/* 地址 */}
            <div className="col-12 p-0 my-4">
                <div className="d-flex my-1">{t("1031")}</div>
                <div className="d-flex my-1">
                    <input type="text" className="form-control" name={Address} value={AddressVal} onChange={(event) => handleInputChange(event)} autoComplete="off" />
                </div>
            </div>

            {/* 更新的錯誤訊息 */}
            { errorMsg.msg && <div className="text-red">{errorMsg.msg}</div> }
        </>
    )
}
ContentAddEdit.defaultProps = {
    data: {
        Company: '',
        GroupName: '',
        GroupID: '',
        Country: '',
        Area: '',
        Address: '',
    },
    companyList: [],
    isAdd: false,
    errorMsg: {},
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


