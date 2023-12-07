import React ,{useState} from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import Alert from '@material-ui/lab/Alert';

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
    '& > * + *': {
      marginTop: theme.spacing(2),
    },
  },
}));


// 錯誤訊息
export const ErrorAlert = ({message,resetMessage}) => {
  const classes = useStyles();
  const [close, setClose] = useState(false)

  const handleClose = () => { //關閉並清空內容
    setClose(true);
    resetMessage();
  }
  
  return (
    <div>
      {
        close ? ''
        : (
          <div className={classes.root}>
            <Alert severity="error"  onClose={() => {handleClose()}}>{message}</Alert>
          </div>
        )
      }
    </div>
  );
}
ErrorAlert.defaultProps={
  message: '',
  resetMessage: ()=>{},
}

// 成功訊息
export const SuccessAlert = ({message,resetMessage}) => {
  const classes = useStyles();
  const [close, setClose] = useState(false)

  const handleClose = () => { //關閉並清空內容
    setClose(true);
    resetMessage();
  }

  return(
    <div>
    {
      close ? ''
      : (
        <div className={classes.root}>
          <Alert severity="success" onClose={() => {handleClose()}}>{message}</Alert>
        </div>
      )
    }
    </div>
  )
}
SuccessAlert.defaultProps={
  message: '',
  resetMessage: ()=>{},
}
SuccessAlert.propTypes = {
  message: PropTypes.string,
  resetMessage: PropTypes.func,
}

