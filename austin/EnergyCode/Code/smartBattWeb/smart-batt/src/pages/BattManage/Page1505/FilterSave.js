import React, { useState } from 'react';
/* i18n Functional Components */
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
                                        {/* 電池型號中文 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1506')}：`}</div>
                                            <div>
                                                {FilterConfig?.BatteryTypeName?.isChecked ?
                                                    t('1076') : FilterConfig?.BatteryTypeName?.isButtonList?.map(item => item.Label).join('、')
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