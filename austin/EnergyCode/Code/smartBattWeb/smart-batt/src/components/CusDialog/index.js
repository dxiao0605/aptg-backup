import React from 'react';
import PropTypes from 'prop-types';
import Dialog from '@material-ui/core/Dialog';
import Slide from '@material-ui/core/Slide';



const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="up" ref={ref} {...props} />;
});

const CusDialog = ({ open, handleClose, children, maxWidth, style }) => {
  return (
    <Dialog
      open={open}
      fullWidth={true}
      maxWidth={maxWidth}
      TransitionComponent={Transition}
      keepMounted
      onClose={handleClose}
      aria-labelledby="alert-dialog-slide-title"
      aria-describedby="alert-dialog-slide-description"
      style={style}
    >
      {children}
    </Dialog>
  );
}

CusDialog.defaultProps = {
  title: '',
  style: '',
  open: false,
  maxWidth: 'sm',
  handleClose: () => { },
}
CusDialog.propTypes = {
  title: PropTypes.string,
  open: PropTypes.bool,
  maxWidth: PropTypes.string,
  handleClose: PropTypes.func,
}
export default CusDialog;