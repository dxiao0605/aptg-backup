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
import SelectedTable from './SelectedTable';
import { DialogTitleStyle, CloseBtnStyle, DialogContentStyle, DialogActionsStyle } from '../CusDialog/style';
import { CusMainBtnStyle } from '../CusMainBtnStyle';

const BBDialog = ({data,list,lastDate,perPage,role,source,onDialogClose,handleInputChange,handleSubmit}) => {
    const { t } = useTranslation();
    const total = data.map(element => element.BatteryGroupID.length).reduce((a,b) => a+b,0);


    const {IRTestTime,BatteryCapacity,CorrectionValue,Resistance} = list;
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
                            <div className="col-10 d-inline-block p-0 align-top">{t('187')}</div>
                        </div>    
                        {/* 參數: */}
                        <div>
                            <div className="col-2 d-inline-block p-0 align-top">{t('1552')}</div> 
                            <div className="col-10 d-inline-block p-0 align-top">
                                <div>{t('1548')}</div>
                                <CusInput 
                                    name="IRTestTime" 
                                    value={(data.length > 1 && IRTestTime === 0) ? '' : IRTestTime}
                                    isFullWidth={true}
                                    disabled={role === 2 ? true : false}
                                    onChangeEvent={(e)=>{handleInputChange(e)}} />
                                <div>{t('1549')}</div>
                                <CusInput 
                                    name="BatteryCapacity" 
                                    value={(data.length > 1 && BatteryCapacity === 0) ? '' : BatteryCapacity}
                                    isFullWidth={true}
                                    disabled={role === 2 ? true : false}
                                    onChangeEvent={(e)=>{handleInputChange(e)}} />
                                <div>{t('1550')}</div>
                                <CusInput 
                                    name="CorrectionValue" 
                                    value={(data.length > 1 && CorrectionValue === 0) ? '' : CorrectionValue}
                                    isFullWidth={true}
                                    disabled={role === 2 ? true : false} 
                                    onChangeEvent={(e)=>{handleInputChange(e)}} />
                                <div>{t('1553')}</div>
                                <CusInput
                                    name="Resistance"
                                    value={(data.length > 1 && Resistance === 0 )? '' : Resistance}
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
BBDialog.defaultProps = {
    data: [],
    list: {},
    role: 1,
    onDialogClose: () => {},
    handleInputChange: () => {},
    handleSubmit: () => {},
    getListData: () => {},          //前一次資料(預設值)
}
BBDialog.propTypes = {
    data:PropTypes.array,
    list: PropTypes.object,
    role: PropTypes.number,
    onDialogClose: PropTypes.func,
    handleInputChange: PropTypes.func,
    handleSubmit: PropTypes.func,
    getListData: PropTypes.func,
}
export default BBDialog;