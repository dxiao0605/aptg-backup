import React, { useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import clsx from 'clsx';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import Icon from '@material-ui/core/Icon';
import Close from '@material-ui/icons/Close';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles({
  list: {
    width: 300,
    height: 'auto',
    padding: 10,
  },
  fullList: {
    width: 'auto',
  },
});

export default function FilterDrawer({ children, isOpen, setIsOpen, resetEvent }) {
  const { t } = useTranslation();
  const classes = useStyles();
  const [state, setState] = React.useState(isOpen);

  const toggleDrawer = (open) => (event) => {
    if (event && event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
      return;
    }
    setState(open);
    setIsOpen(open); //變更顯示隱藏
  };




  // React Lifecycle
  useEffect(() => {
    if (state !== isOpen) {
      setState(isOpen)
    }
  }, [isOpen, state])




  return (
    <div className="d-inline-block">
      {/* 篩選 */}
      <button className="btn-sm-b1 btn-outline-primary mr-2" onClick={toggleDrawer(true)}>
        <i className="fas fa-filter" /> {t('1002')} <i className="fas fa-angle-right" />
      </button>
      {
        state !== undefined ? (
          <Drawer
            anchor={'right'}
            variant="temporary"
            open={state}
            onClose={toggleDrawer(false)}
          >
            <div
              className={clsx(classes.list)}
              role="presentation"
            >
              <List>
                <div className="d-flex p-0 my-2 align-items-center">
                  <div className="col-6 d-inline-block pl-2">
                    <Close
                      fontSize="large"
                      color="action"
                      style={{ cursor: 'pointer' }}
                      onClick={toggleDrawer(false)}
                      onKeyDown={toggleDrawer(false)} />
                  </div>
                  {/* 清空篩選資料 */}
                  <div className="col-6 d-inline-block pr-3 text-right">
                    <Icon
                      className="fas fa-trash"
                      color="action"
                      style={{ cursor: 'pointer' }}
                      onClick={() => { resetEvent() }}
                      onKeyDown={() => { resetEvent() }} />
                  </div>
                </div>
              </List>
              {/* 篩選內容 */}
              {children}
            </div>
          </Drawer>
        ) : ''
      }
    </div>
  );
}