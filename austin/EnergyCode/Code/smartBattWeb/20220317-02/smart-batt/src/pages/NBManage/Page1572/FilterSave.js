import React, { useState } from 'react';
/* i18n Functional Components */
import { useTranslation } from 'react-i18next'
import FilterSaveHeader from '../../../components/CusBookMark/FilterSaveHeader';
import FilterSaveItem from '../../../components/CusBookMark/FilterSaveItem';

const FilterSave = ({ list, onClickBookmark, onClickTrash, company }) => {
    const { t } = useTranslation();
    const [showIdx, setShowIdx] = useState([]);
    const returnText = (text) => {
        return (text !== undefined ? text : '')
    }
    return (
        <>
            <FilterSaveHeader>
                {
                    Array.isArray(list) && list.length > 0 &&
                    list.map((item, idx) => {
                        const { FilterConfig } = item;
                        return (
                            <li key={idx} className="pt-0">
                                <FilterSaveItem
                                    FilterName={item.FilterName}
                                    FilterConfig={item.FilterConfig}
                                    idx={idx.toString()}
                                    showIdx={showIdx}
                                    onClickBookmark={(FilterConfig) => { onClickBookmark(FilterConfig) }}
                                    onClickTrash={() => { onClickTrash(item.FilterID) }}
                                    onUploadShowIdx={(idx) => setShowIdx([idx])}
                                >
                                    {/* 顯示移動框內容 */}
                                    <div className="">
                                        <div className="form-inline border-bottom px-2 py-1">{item.FilterName}</div>
                                        {/* 公司 */}
                                        {
                                            company === "1" &&
                                            <div className="form-inline px-2 py-1">
                                                <div>{`${t('1064')}：`}</div>
                                                <div>
                                                    {FilterConfig?.Company?.isChecked ?
                                                        t('1076') : FilterConfig?.Company?.isButtonList?.map(item => item.Label).join('、')
                                                    }
                                                </div>
                                            </div>
                                        }
                                        {/* 國家 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1028')}：`}</div>
                                            <div>
                                                {FilterConfig?.Country?.isChecked ?
                                                    t('1076') : FilterConfig?.Country?.isButtonList?.map(item => item.Label).join('、')
                                                }
                                            </div>
                                        </div>
                                        {/* 地域 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1029')}：`}</div>
                                            <div>
                                                {FilterConfig?.Area?.isChecked ?
                                                    t('1076') : FilterConfig?.Area?.isButtonList?.map(item => item.Label).join('、')
                                                }
                                            </div>
                                        </div>
                                        {/* 站台編號/名稱 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1125')}：`}</div>
                                            <div>
                                                {FilterConfig?.GroupId?.isChecked ?
                                                    t('1076') : FilterConfig?.GroupID?.isButtonList?.map(item => item.Label).join('、')
                                                }
                                            </div>
                                        </div>
                                        {/* (接續)通訊序號 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1573')}：`}</div>
                                            <div>
                                                {FilterConfig?.NBID?.isChecked ?
                                                    t('1076') : FilterConfig?.NBID?.isButtonList?.map(item => item.Label).join('、')
                                                }
                                            </div>
                                        </div>
                                        {/* (接續)開始時間 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1574')}：`}</div>
                                            <div>
                                                {FilterConfig?.PreviousTime?.Radio === "1" ?
                                                    `${returnText(FilterConfig?.PreviousTime?.Start + "_" + FilterConfig?.PreviousTime?.StartHH + ":" + FilterConfig?.PreviousTime?.StartMM) +
                                                    " ~ " + returnText(FilterConfig?.PreviousTime?.End + "_" + FilterConfig?.PreviousTime?.EndHH + ":" + FilterConfig?.PreviousTime?.EndMM)
                                                    }` : t(FilterConfig?.PreviousTime?.isButtonList[0].Label)
                                                }
                                            </div>
                                        </div>
                                    </div>
                                </FilterSaveItem>
                            </li>
                        )
                    })
                }
            </FilterSaveHeader>
        </>
    )

}
FilterSave.defaultProps = {
    list: [],
    company: "1",
    onClickBookmark: () => { },
    onClickTrash: () => { },
}
export default (FilterSave);