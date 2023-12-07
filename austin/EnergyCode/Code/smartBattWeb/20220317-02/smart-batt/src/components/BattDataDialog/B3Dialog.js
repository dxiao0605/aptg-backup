import React,{Fragment} from 'react';
import { useTranslation } from 'react-i18next';
import Divider from '@material-ui/core/Divider';
import CusInput from '../CusInput';
import Button from '@material-ui/core/Button';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import CusDialog from '../CusDialog';
import { DialogTitleStyle, CloseBtnStyle, DialogContentStyle, DialogActionsStyle } from '../CusDialog/style';
import { CusMainBtnStyle } from '../CusMainBtnStyle';
import SelectedTable from './SelectedTable';

const B3Dialog = ({data,list,lastDate,perPage,role,onDialogClose,handleInputChange,handleSubmit}) => {
    const { t } = useTranslation();



    const {IR} = list;
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
                        {t('1414')} {data ? data.length : 0} {t('1023')}
                    </div>

                    <SelectedTable data={data} perPage={perPage} />

                </div> 
                <Divider />
                    <div className="pt-2 pb-2">
                    {/* 命令 */}
                    <div>
                        <div className="col-2 d-inline-block p-0 align-top">{t('1065')}</div> 
                        <div className="col-10 d-inline-block p-0 align-top">{t('179')}</div>
                    </div>    
                    {/* 參數: */}
                    <div>
                        <div className="col-2 d-inline-block p-0 align-top">{t('1552')}</div> 
                        <div className="col-10 d-inline-block p-0 align-top">
                        {
                            IR ? 
                            IR.map( (item,idx) => {
                                return(
                                    <Fragment key={idx}>
                                        <div>CH{idx+1} {t('1565')}</div>
                                        <CusInput
                                            name={idx}
                                            value={item}
                                            isFullWidth={true}
                                            disabled={role === 2 ? true : false}
                                            onChangeEvent={(e)=>{handleInputChange(e)}} />
                                    </Fragment>
                                )
                            }):''
                        }
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
B3Dialog.defaultProps = {
    data: [],
    list: {},
    onDialogClose: () => {},
    handleInputChange: () => {},
    handleSubmit: () => {},
    getListData: () => {},          //前一次資料(預設值)
}
export default B3Dialog;