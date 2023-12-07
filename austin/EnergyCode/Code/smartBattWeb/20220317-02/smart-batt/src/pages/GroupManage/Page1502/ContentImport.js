import React from 'react';
import { useTranslation } from 'react-i18next';
import '../../../style/importTools.scss';

const ContentImport = ({ companyList, company, data, handleSelectChange, handleFileChange }) => {
    const { t } = useTranslation();
    const {
        Company,
        FileName,
    } = data;
    return (
        <>
            {/* 公司別 */}
            {
                company === "1" &&
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
            {/* File import */}
            <div className="col-12 p-0 my-4 form-inline">
                <div className="d-inline-flex col-xl-8 mb-xl-0 mb-3" style={{ color: "#007bff", fontWeight: "bold" }}>{FileName === "" ? t("1507") : FileName}</div>
                <div className="d-inline-flex col-xl-4 exportBtn exportBtnShadow align-items-center cursor-pointer">
                    <div className="custom-file">
                        <input type="file" className="custom-file-input" data-target="file-uploader" id="file-uploader" accept=".xlsx" onChange={(e) => handleFileChange(e)} />
                        <label htmlFor="file-uploader">
                            <i className="fas fa-file-upload" />
                            <div className='mx-1'>{t("1054")}</div>
                        </label>
                    </div>
                </div>
            </div>
        </>
    )
}
export default ContentImport;


