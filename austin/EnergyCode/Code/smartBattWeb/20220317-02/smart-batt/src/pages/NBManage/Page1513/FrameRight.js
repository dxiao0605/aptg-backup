import React, { Fragment, } from "react";
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next';


// 右邊框
const FrameRight = ({
    List,
}) => {
    const { t } = useTranslation();
    return (
        <Fragment>
            <div className="justify-content-center w-100 text-center" style={{ height: '1.5em' }}>{t('1522')}</div>
            <div className="justify-content-center w-100">
                <div className="w-100 text-center">
                    <div className="header border border-dark">
                        <div className="form-inline py-1 pr-3" style={{ lineHeight: 2, justifyContent: 'space-around' }}>
                            <div className="col-6 px-0">{t('1064')}</div>
                            <div className="col-6 px-0">{t('1057')}</div>
                        </div>
                    </div>
                    <div className="body border border-dark scrollStyle" style={{ overflowY: 'scroll', height: '26vh', minHeight: '2.5em' }}>
                        {
                            Array.isArray(List) && List?.length > 0 &&  List.map((item, idx) => {
                                return (
                                    <div key={idx} className="form-inline" style={{ lineHeight: 2, justifyContent: 'space-around' }}>
                                        <div className="col-6 px-0">{item.CompanyName}</div>
                                        <div className="col-6 px-0">{item.NBID}</div>
                                    </div>
                                )
                            })
                        }
                    </div>
                </div>
            </div>
            <div className="d-flex my-1  w-100" style={{ justifyContent: 'start', minHeight: '36px' }}>
                <div className="py-1">{t('1095') + " " + List.length + " " + t('1094')}</div>
            </div>
        </Fragment>
    );
};
FrameRight.defaultProps = {
    List: [],
}
FrameRight.propTypes = {
    List: PropTypes.array,
}
export default FrameRight;
