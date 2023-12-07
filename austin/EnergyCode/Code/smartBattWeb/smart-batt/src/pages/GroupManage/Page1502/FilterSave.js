import React, { useState } from 'react';
import { useTranslation } from 'react-i18next'
import FilterSaveHeader from '../../../components/CusBookMark/FilterSaveHeader';
import FilterSaveItem from '../../../components/CusBookMark/FilterSaveItem';

const FilterSave = ({ list, onClickBookmark, onClickTrash, company }) => {
    const { t } = useTranslation();
    const [showIdx, setShowIdx] = useState([]);
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
                                        {/* 站台名稱/號碼 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1125')}：`}</div>
                                            <div>
                                                {FilterConfig?.GroupID?.isChecked ?
                                                    t('1076') : FilterConfig?.GroupID?.isButtonList?.map(item => item.Label).join('、')
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