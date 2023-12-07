import React, { useState } from 'react';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import Fade from '@material-ui/core/Fade';
import { useTranslation } from 'react-i18next';
import { CompareWindowns } from './InitDataFormat';//初始化格式


const FadeMenu = ({ data, menuList, onMenuClick, limits }) => {
    const { t } = useTranslation();
    const [anchorEl, setAnchorEl] = useState(null);//展開選單欄

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };
    const onClick = ({ event, data, item }) => {
        setAnchorEl(null);
        onMenuClick({ event, data, item })
    }

    return (
        <div className="d-flex justify-content-center">
            <div className="btn_icon" onClick={handleClick}>
                <i className="fas fa-ellipsis-v" />
            </div>
            <Menu
                anchorEl={anchorEl}
                keepMounted
                open={Boolean(anchorEl)}
                onClose={handleClose}
                TransitionComponent={Fade}
                getContentAnchorEl={null}
                anchorOrigin={{
                    vertical: 'center',
                    horizontal: 'left',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
            >
                {
                    menuList?.length > 0 &&
                    menuList.map((item, idx) => {
                        switch (item) {
                            case CompareWindowns.RestPwd.model:
                                return data.ResetUser && <MenuItem key={idx} onClick={(event) => onClick({ event, data, item })}>{t(item)}</MenuItem>
                            case CompareWindowns.Del.model:
                                return data.DelUser && <MenuItem key={idx} onClick={(event) => onClick({ event, data, item })}>{t(item)}</MenuItem>
                            default:
                                return <MenuItem key={idx} onClick={(event) => onClick({ event, data, item })}>{t(item)}</MenuItem>
                        }
                    })
                }
            </Menu>
        </div>
    );
}
FadeMenu.defaultProps = {
    data: {},
    menuList: [],
    onMenuClick: () => { }
}
export default FadeMenu;
