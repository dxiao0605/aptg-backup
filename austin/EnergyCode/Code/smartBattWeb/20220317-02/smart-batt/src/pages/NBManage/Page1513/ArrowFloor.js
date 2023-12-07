import React, { Fragment, useState } from "react";
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';


// 箭頭
const ArrowFloor = ({
    text,
    onClickHandler,
}) => {
    const { t } = useTranslation();
    const [hover, setHover] = useState(false);

    return (
        <Fragment>
            <div className="d-flex arrowFloor align-items-center justify-content-center" onClick={() => { onClickHandler() }}>
                <div className="arrowFloor__oneFloor my-2 font-weight-bold cursor-pointer"
                    onMouseEnter={() => setHover(true)}
                    onMouseLeave={() => setHover(false)}
                >{text === "" ? "" : t(text)}</div>
                <div className="arrowFloor__twoFloor my-2 cursor-pointer">
                    <div className={`arrow ${hover && 'active'}`} />
                </div>
            </div>
        </Fragment>
    );
};
ArrowFloor.defaultProps = {
    text: '',
    onClickHandler: () => { },
}
ArrowFloor.propTypes = {
    text: PropTypes.string,
    onClickHandler: PropTypes.func,
}
export default ArrowFloor;
