import React, { Fragment } from 'react';
import { Link } from 'react-router-dom';
/* i18n Functional Components */
import { useTranslation } from 'react-i18next'



const SubItem = ({ data, hasAlert, alertTotal, setCurrentSubIdx }) => {
    const { t } = useTranslation();
    return (
        <Fragment>
            {
                data.map(subItem => {
                    return (
                        <Fragment key={subItem.FunctionId}>
                            {
                                (
                                    () => {
                                        switch (subItem.Type) {
                                            case 'text':
                                                return (
                                                    // <div className='mainNav_item_subItemGroup disabled' key={subItem.FunctionId}>{t(subItem.FunctionId)}</div>
                                                    <div className='mainNav_item_subItemGroup disabled' key={subItem.FunctionId}>
                                                        {
                                                            (
                                                                () => {
                                                                    //判斷如果為告警 未解決
                                                                    if (subItem.FunctionId === 1301) {
                                                                        return (
                                                                            <div className='mainNav_item_subItem'>
                                                                                {t(subItem.FunctionId)}
                                                                                <div className={hasAlert ? "prompt sub" : "d-none"}>
                                                                                    {alertTotal}
                                                                                </div>
                                                                            </div>
                                                                        )
                                                                    } else {
                                                                        return <div>{t(subItem.FunctionId)}</div>
                                                                    }
                                                                }
                                                            )()
                                                        }
                                                    </div>
                                                )
                                            case 'url':
                                                return (
                                                    <div className={`mainNav_item_subItemGroup ${subItem.active ? "active" : ""}`} key={subItem.FunctionId} onClick={
                                                        () => setCurrentSubIdx(subItem.FunctionId)
                                                    }>
                                                        <Link to={subItem.Url}>
                                                            {
                                                                (
                                                                    () => {
                                                                        //判斷如果為告警 未解決
                                                                        if (subItem.FunctionId === 1301) {
                                                                            return (
                                                                                <div className='d-flex mainNav_item_subItem'>
                                                                                    <div style={{ width: '48px', height: '24px' }} />
                                                                                    {t(subItem.FunctionId)}
                                                                                    <div className={hasAlert ? "prompt sub" : "d-none"}>
                                                                                        {alertTotal}
                                                                                    </div>
                                                                                </div>
                                                                            )
                                                                        } else {
                                                                            return (
                                                                                <div className='d-flex mainNav_item_subItem'>
                                                                                    <div style={{ width: '48px', height: '24px' }} />
                                                                                    {t(subItem.FunctionId)}
                                                                                </div>
                                                                            )
                                                                        }
                                                                    }
                                                                )()
                                                            }
                                                        </Link>
                                                    </div>
                                                )
                                            default:
                                                return (
                                                    <div className={`mainNav_item_subItemGroup ${subItem.active ? "active" : ""}`} key={subItem.FunctionId} onClick={() => setCurrentSubIdx(subItem.FunctionId)}><Link to={subItem.Url}>{t(subItem.FunctionId)}</Link></div>
                                                )
                                        }
                                    }
                                )()
                            }
                        </Fragment>
                    )
                })
            }
        </Fragment>
    )
}
SubItem.defaultProps = {
    data: [],
}
export default SubItem;