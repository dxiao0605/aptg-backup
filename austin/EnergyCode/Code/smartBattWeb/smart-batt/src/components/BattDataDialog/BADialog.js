import React from 'react';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';
import Divider from '@material-ui/core/Divider';
import Button from '@material-ui/core/Button';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import CusInput from '../CusInput';
import CusDialog from '../CusDialog';
import { DialogTitleStyle, CloseBtnStyle, DialogContentStyle, DialogActionsStyle } from '../CusDialog/style';
import { CusMainBtnStyle } from '../CusMainBtnStyle';
import SelectedTable from './SelectedTable';

const BADialog = ({data,list,lastDate,perPage,role,source,onDialogClose,handleInputChange,handleSubmit}) => {
    const { t } = useTranslation();
    const total = data.map(element => element.BatteryGroupID.length).reduce((a,b) => a+b,0);
    const {UploadCycle,IRCycle,CommunicationCycle} = list;
    return (
        <CusDialog
            open={true}
            maxWidth='sm'
            handleClose={onDialogClose}
        >
            <DialogTitle id="alert-dialog-slide-title" style={DialogTitleStyle}>
                <div className="col-6 p-0 d-inline-block">{t('1412')}</div>
                <div className="col-6 m-0 p-0 d-inline-block text-right">
                    <i className="fas fa-times" style={CloseBtnStyle} onClick={onDialogClose} />
                </div>
            </DialogTitle>
            <DialogContent className="overflowY" style={DialogContentStyle}>
                {/* 表格(站台號碼+電池組ID) */}
                <div>
                    <div>
                        {/* 將傳送指令至下列啟用的電池組ID，共 data.length 個 */}
                        {t('1414')} {data ? (source ==='BattGroup' ? total : data.length) : 0} {t('1023')}
                    </div>

                    <SelectedTable data={data} perPage={perPage} />

                </div> 
                <Divider />
                    <div className="pt-2 pb-2">
                        {/* 命令 */}
                        <div>
                            <div className="col-2 d-inline-block p-0 align-top">{t('1065')}</div> 
                            <div className="col-10 d-inline-block p-0 align-top">{t('186')}</div>
                        </div>    
                        {/* 參數: */}
                        <div>
                            <div className="col-2 d-inline-block p-0 align-top">{t('1552')}</div> 
                            <div className="col-10 d-inline-block p-0 align-top">
                                <div>{t('1555')}</div>
                                <CusInput 
                                    name="UploadCycle" 
                                    value={(data.length > 1 && UploadCycle === 0) ? '': UploadCycle} 
                                    isFullWidth={true} 
                                    disabled={role === 2 ? true : false} 
                                    onChangeEvent={(e)=>{handleInputChange(e)}} />
                                <div>{t('1556')}</div>
                                <CusInput 
                                    name="IRCycle" 
                                    value={(data.length > 1 && IRCycle === 0) ? '' : IRCycle}
                                    isFullWidth={true}
                                    disabled={role === 2 ? true : false}
                                    onChangeEvent={(e)=>{handleInputChange(e)}} />
                                <div>{t('1557')}</div>
                                <CusInput 
                                    name="CommunicationCycle"
                                    value={(data.length > 1 && CommunicationCycle === 0) ? '': CommunicationCycle}
                                    isFullWidth={true}
                                    disabled={role === 2 ? true : false} 
                                    onChangeEvent={(e)=>{handleInputChange(e)}} />
                            </div>
                        </div>

                        {/* 最後設定時間 */}
                        { data.length === 1 && <div className="pt-2">{t('1417')}{lastDate}</div>}
                    </div>
                <Divider />

            </DialogContent>
            <DialogActions style={DialogActionsStyle}>
                {/* 取消 */}
                <Button onClick={onDialogClose}>{t('1003')}</Button>
                {/* 確認 */}
                <CusMainBtnStyle name={t('1554')} icon="fas fa-check" clickEvent={handleSubmit} />
            </DialogActions>
        </CusDialog>
    )
}
BADialog.defaultProps = {
    data: [],
    list: {},
    onDialogClose: () => {},
    handleInputChange: () => {},
    handleSubmit: () => {},
    getListData: () => {},          //前一次資料(預設值)
}
BADialog.propTypes = {
    data: PropTypes.array,
    list: PropTypes.object,
    onDialogClose: PropTypes.func,
    handleInputChange: PropTypes.func,
    handleSubmit: PropTypes.func,
    getListData: PropTypes.func,
}
export default BADialog;