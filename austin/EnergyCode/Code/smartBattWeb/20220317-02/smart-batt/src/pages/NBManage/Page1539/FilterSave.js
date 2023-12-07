import React, { useState } from 'react';
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
                                        {/* 異動者公司 */}
                                        {
                                            company === "1" &&
                                            <div className="form-inline px-2 py-1">
                                                <div>{`${t('1538')}：`}</div>
                                                <div>
                                                    {FilterConfig?.Company?.isChecked ?
                                                        t('1076') : FilterConfig?.Company?.isButtonList?.map(item => item.Label).join('、')
                                                    }
                                                </div>
                                            </div>
                                        }
                                        {/* 異動項目 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1059')}：`}</div>
                                            <div>
                                                {FilterConfig?.ModifyItem?.isChecked ?
                                                    t('1076') : FilterConfig?.ModifyItem?.isButtonList?.map(item => t(item.I18NCode)).join('、')
                                                }
                                            </div>
                                        </div>
                                        {/* 通訊序號 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1057')}：`}</div>
                                            <div>
                                                {FilterConfig?.NBID?.isChecked ?
                                                    t('1076') : FilterConfig?.NBID?.isButtonList?.map(item => item.Label).join('、')
                                                }
                                            </div>
                                        </div>
                                        {/* 傳送時間 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1060')}：`}</div>
                                            <div>
                                                {FilterConfig?.ModifyTime?.Radio === "1" ?
                                                    `${returnText(FilterConfig?.ModifyTime?.Start + "_" + FilterConfig?.ModifyTime?.StartHH + ":" + FilterConfig?.ModifyTime?.StartMM) +
                                                    " ~ " + returnText(FilterConfig?.ModifyTime?.End + "_" + FilterConfig?.ModifyTime?.EndHH + ":" + FilterConfig?.ModifyTime?.EndMM)
                                                    }` : t(FilterConfig?.ModifyTime?.isButtonList[0].Label)
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