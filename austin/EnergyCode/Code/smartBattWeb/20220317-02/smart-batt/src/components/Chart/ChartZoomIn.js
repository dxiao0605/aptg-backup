import React, { Fragment } from 'react';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import {DialogTitleStyle,CloseBtnStyle,DialogContentStyle,DialogActionsStyle} from '../CusDialog/style';





const ChartZoomIn = ({title,children}) => {
    const [open, setOpen] = React.useState(false);
  
    const handleClickOpen = () => {
      setOpen(true);
    };
  
    const handleClose = () => {
      setOpen(false);
    };
  
    return (
        <Fragment>
            <div className="btn_icon" onClick={handleClickOpen}>
                <i className='fas fa-expand' />
            </div>

            <Dialog
                fullWidth={true}
                maxWidth={'md'}
                open={open}
                onClose={handleClose}
                aria-labelledby="max-width-dialog-title"
            >
                <DialogTitle id="max-width-dialog-title" style={DialogTitleStyle}>
                    <div className="col-6 p-0 d-inline-block">{title}</div>
                    <div className="col-6 m-0 p-0 d-inline-block text-right">
                        <i className="fas fa-times" style={CloseBtnStyle} onClick={handleClose} />
                    </div>
                </DialogTitle>
                <DialogContent style={DialogContentStyle}>
                    {children}
                </DialogContent>
                
                <DialogActions style={DialogActionsStyle}>
                </DialogActions>
            </Dialog>
        </Fragment>

    )
}
export default ChartZoomIn;