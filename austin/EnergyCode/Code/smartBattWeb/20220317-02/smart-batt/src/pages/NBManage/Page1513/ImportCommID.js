import React, { Fragment, useRef, useState } from 'react';
import { useSelector } from 'react-redux';
import PropTypes from 'prop-types';
import { apipath } from '../../../utils/ajax';
import { initImportData } from './InitDataFormat';
import { useTranslation } from 'react-i18next';



const apiUrl = apipath();

const ImportCommID = ({habdleUpdateDefNBList,handleAlertMsgOpen}) => {
    const fileInput = useRef();
    const username = useSelector(state => state.LoginReducer.username); 
    const token = useSelector(state => state.LoginReducer.token); 
    const curLanguage = useSelector(state => state.LoginReducer.curLanguage); 
    const timeZone = useSelector(state => state.LoginReducer.timeZone); 
    const company = useSelector(state => state.LoginReducer.company);
    const { t } = useTranslation();
    const [filesName,setFilesName] = useState(initImportData.FileName);

    const habdleFileChange = (event) => {
        if (event.target.files?.length > 0) {
            setFilesName(event.target.files[0].name)
        } else {
            fileInput.current.value = initImportData.FileValue;
            fileInput.current.files = initImportData.File;
            setFilesName(initImportData.FileName);
        }
    }

    //通訊序號匯入(POST):
    //https://www.gtething.tw/battery/importNBID
    const handleFileUpdate = () => {
        const ProcessingText = t('1091');
        handleAlertMsgOpen('1089', ProcessingText);
        let formData = new FormData();
        formData.append('username', username);
        formData.append('files', fileInput.current.files[0]);
        let xhr = new XMLHttpRequest();  // 先new 一個XMLHttpRequest。
        xhr.open('POST', `${apiUrl}importNBID`);   // 設置xhr得請求方式和url。
        xhr.setRequestHeader('token', token);
        xhr.setRequestHeader('language', curLanguage);
        xhr.setRequestHeader('timezone', timeZone);
        xhr.setRequestHeader('company', company);
        xhr.onreadystatechange = (event) => {   // 等待ajax請求完成。
            // if (this.ajaxCancel) { return new Promise((resolve, reject) => (reject(''))); }//強制結束頁面	
            if (xhr.status === 200) {
                if (xhr.responseText === "") { return; }
                // console.log('File upload => Server Status:' + xhr.status + ' 伺服器已接收');
                const { msg, code } = JSON.parse(xhr.responseText);
                if (code === '00' && msg?.NBList && Array.isArray(msg.NBList)) {
                    // console.log('File upload => Server Response Code:' + code + ' 上傳成功');
                    handleAlertMsgOpen('1089', msg.Message);
                    const { NBList } = msg;
                    const list = NBList.map(item => {
                        item.checked = false;
                        return item;
                    });
                    habdleUpdateDefNBList(list);
                } else {
                    // console.log('File upload => Server Response Code:' + code + ' 上傳失敗');
                    handleAlertMsgOpen('1089', msg.Message);
                    habdleUpdateDefNBList(msg.NBList);
                }
            } else {
                // console.log('File upload => Server Status:' + xhr.status + ' 伺服器發生錯誤');
                handleAlertMsgOpen('1089', 'File upload => Server Status:' + xhr.status);
            }
        };
        xhr.onloadend = () => {
            fileInput.current.value = initImportData.FileValue;
            fileInput.current.files = initImportData.File;
            setFilesName(initImportData.FileName)
            // this.setState({ filesName: initImportData.FileName });
        }
        // 獲取上傳進度
        xhr.upload.onprogress = (event) => {
            if (event.lengthComputable) {
                // var percent = Math.floor(event.loaded / event.total * 100);
                // document.querySelector("#progress .progress-item").style.width = percent + "%";
                // 設置進度顯示
                // this.setState({
                //     openAlertMsg: true,
                //     textAlertMsg: percent + "%"
                // })
            }
        };
        xhr.send(formData);
    }

    return (
        <Fragment>
            {/* 通訊序號匯入 */}
            <div className="col-12 my-4 pl-0 pr-0">
                {/* 通訊序號匯入 */}
                <div className="font-weight-bold col-12" style={{ backgroundColor: '#525b6c', color: '#fff', lineHeight: '3' }}>{t('1514')}</div>
                {/* 限制匯入格式為CSV檔案，筆數上限1000筆 */}
                <div className="px-4 py-4 bg-white">
                    <div className="form-inline align-items-center">
                        <div className="col-12 col-xl-6 px-0 my-1 ml-xl-2 mr-xl-5" style={{ border: '1px solid #333333' }}>
                            <div className="col-12 my-4 font-weight-bold text-center">
                                {
                                    filesName === "" ? t('1540') : filesName
                                }
                                {/* <Trans i18nKey={"1540"} /> */}
                            </div>
                        </div>
                        <div className="d-inline-flex col-12 col-xl-1 exportBtn btn-primary exportBtnShadow mr-xl-3 my-2 cursor-pointer" style={{ background: '#03c3ff', borderColor: '#03c3ff' }}>
                            <div className="InA-uploader custom-file">
                                <input type="file" className="custom-file-input" data-target="file-uploader" id="file-uploader" accept=".xlsx" ref={fileInput} onChange={(e) => { habdleFileChange(e) }} />
                                <label className="pointer" htmlFor="file-uploader" style={{whiteSpace:'nowrap',lineHeight:'auto'}}>
                                    <div className='InA-uploader-btn'>{t('1547')}</div>
                                </label>
                            </div>
                        </div>
                        <button type="button" className="btn btn-primary exportBtnShadow col-12 col-xl-1 mr-xl-3 px-0 my-2" style={{ background: '#03c3ff', borderColor: '#03c3ff' }}
                            onClick={handleFileUpdate}
                        >{t('1054')}</button>
                    </div>
                </div>
            </div>
        </Fragment>
    )
}

ImportCommID.defaultProps = {
    habdleUpdateDefNBList: () => { },
    handleAlertMsgOpen: () => { },
}

ImportCommID.propTypes = {
    habdleUpdateDefNBList: PropTypes.func,
    handleAlertMsgOpen: PropTypes.func,
}

export default ImportCommID;