import React, { useState } from 'react';
import { useTranslation } from 'react-i18next'
import FilterSaveHeader from '../../../components/CusBookMark/FilterSaveHeader';
import FilterSaveItem from '../../../components/CusBookMark/FilterSaveItem';

const FilterSave = ({ list, onClickBookmark, onClickTrash, company , functionId }) => {
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
                                        {/* 告警類型 */}
                                        {
                                            (functionId === '1301' || functionId === '1302') && (
                                                <div className="form-inline px-2 py-1">
                                                    <div>{`${t('1315')}：`}</div>
                                                    <div>
                                                        {FilterConfig?.Alert?.isChecked ?
                                                            t('1076') : FilterConfig?.Alert?.isButtonList?.map(item => item.Label).join('、')
                                                        }
                                                    </div>
                                                </div>
                                            )
                                        }

                                        {/* 公司別 */}
                                        {
                                            company === "1" && (
                                                <>
                                                {//電池歷史第一層不顯示
                                                    functionId !== '1600_2' && (
                                                        <div className="form-inline px-2 py-1">
                                                            <div>{`${t('1064')}：`}</div>
                                                            <div>
                                                                {FilterConfig?.Company?.isChecked ?
                                                                    t('1076') : FilterConfig?.Company?.isButtonList?.map(item => item.Label).join('、')
                                                                }
                                                            </div>
                                                        </div>
                                                    )
                                                }
                                                </>
                                            )
                                        }
                                        {/* 國家 */}
                                        {//電池歷史第一層不顯示
                                            functionId !== '1600_2' && (
                                            <div className="form-inline px-2 py-1">
                                                <div>{`${t('1028')}：`}</div>
                                                <div>
                                                    {FilterConfig?.Country?.isChecked ?
                                                        t('1076') : FilterConfig?.Country?.isButtonList?.map(item => item.Label).join('、')
                                                    }
                                                </div>
                                            </div>
                                            )
                                        }
                                        
                                        {/* 地域 */}
                                        {
                                            functionId !== '1600_2' && (
                                                <div className="form-inline px-2 py-1">
                                                    <div>{`${t('1029')}：`}</div>
                                                    <div>
                                                        {FilterConfig?.Area?.isChecked ?
                                                            t('1076') : FilterConfig?.Area?.isButtonList?.map(item => item.Label).join('、')
                                                        }
                                                    </div>
                                                </div>
                                            )
                                        }
                                        {/* 站台編號/名稱 */}
                                        {
                                            functionId !== '1600_2' && (
                                                <div className="form-inline px-2 py-1">
                                                    <div>{`${t('1125')}：`}</div>
                                                    <div>
                                                        {FilterConfig?.GroupID?.isChecked ?
                                                            t('1076') : FilterConfig?.GroupID?.isButtonList?.map(item => item.Label).join('、')
                                                        }
                                                    </div>
                                                </div>
                                            )
                                        }
                                        {/* 數據時間 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1036')}：`}</div>
                                            <div>
                                                {FilterConfig?.RecTime?.Radio === '4' ?
                                                    t('1110') : FilterConfig?.RecTime?.isButtonList?.map(item => item.Label).join('、')
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