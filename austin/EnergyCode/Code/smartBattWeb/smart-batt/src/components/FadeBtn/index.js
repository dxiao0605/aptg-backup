import React from 'react';
import PropTypes from 'prop-types';

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
FadeBtn.propTypes = {
    data: PropTypes.object,
    menuList: PropTypes.array,
    onMenuClick: PropTypes.func,
}
export default FadeBtn;
