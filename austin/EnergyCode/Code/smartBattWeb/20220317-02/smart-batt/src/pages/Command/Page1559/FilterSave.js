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
                                        {/* 公司別 */}
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
                                        {/* 站台編號 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1125')}：`}</div>
                                            <div>
                                                {FilterConfig?.GroupID?.isChecked ?
                                                    t('1076') : FilterConfig?.GroupID?.isButtonList?.map(item => item.Label).join('、')
                                                }
                                            </div>
                                        </div>
                                        {/* 指令 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1566')}：`}</div>
                                            <div>
                                                {FilterConfig?.Command?.isChecked ?
                                                    t('1076') : FilterConfig?.Command?.isButtonList?.map(item => t(item.I18NCode)).join('、')
                                                }
                                            </div>
                                        </div>
                                        {/* 回應訊息 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1070')}：`}</div>
                                            <div>
                                                {FilterConfig?.Response?.isChecked ?
                                                    t('1076') : FilterConfig?.Response?.isButtonList?.map(item => t(item.I18NCode)).join('、')
                                                }
                                            </div>
                                        </div>
                                        {/* 傳送時間 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1066')}：`}</div>
                                            <div>
                                                {FilterConfig?.SendTime?.Radio === "1" ?
                                                    `${returnText(FilterConfig?.SendTime?.Start + "_" + FilterConfig?.SendTime?.StartHH + ":" + FilterConfig?.SendTime?.StartMM) +
                                                    " ~ " + returnText(FilterConfig?.SendTime?.End + "_" + FilterConfig?.SendTime?.EndHH + ":" + FilterConfig?.SendTime?.EndMM)
                                                    }` : t(FilterConfig?.SendTime?.isButtonList[0].Label)
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