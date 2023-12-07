import React from 'react';
// import Menu from '@material-ui/core/Menu';
// import MenuItem from '@material-ui/core/MenuItem';
// import Fade from '@material-ui/core/Fade';

//單純[...]樣式以及onclick功能
const FadeBtn = ({ data, onMenuClick }) => {   
    const onClick = ({ event, data }) => {
        onMenuClick({ event, data })
    }

    return (
        <div className="d-flex justify-content-center">
            <div className="btn_icon" onClick={(event) => onClick({ event, data })}>
                <i className="fas fa-ellipsis-v" />
            </div>            
        </div>
    );
}
FadeBtn.defaultProps = {
    data: {},
    menuList: [],
    onMenuClick: () => { }
}
export default FadeBtn;
