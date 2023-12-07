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
                                    <div className="">
                                        <div className="form-inline border-bottom px-2 py-1">{item.FilterName}</div>
                                        {/* 公司別 */}
                                        {
                                            company === "1" && <div className="form-inline px-2 py-1">
                                                <div>{`${t('1064')}：`}</div>
                                                <div>
                                                    {item.FilterConfig?.Company?.isChecked ?
                                                        t('1076') : item.FilterConfig?.Company?.isButtonList?.map(item => item.Label).join('、')
                                                    }
                                                </div>
                                            </div>
                                        }
                                        {/* 電池組ID */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1026')}：`}</div>
                                            <div>
                                                {item.FilterConfig?.BatteryGroupId?.isChecked ?
                                                    t('1076') : item.FilterConfig?.BatteryGroupId?.isButtonList?.map(item => item.Label).join('、')
                                                }
                                            </div>
                                        </div>
                                        {/* 安裝日期 */}
                                        <div className="form-inline px-2 py-1">
                                            <div>{`${t('1027')}：`}</div>
                                            <div>
                                                {item.FilterConfig?.InstallDate?.Radio === "1" ?
                                                    `${returnText(item.FilterConfig?.InstallDate?.Start) +
                                                    " ~ " + returnText(item.FilterConfig?.InstallDate?.End)
                                                    }` : t(item.FilterConfig?.InstallDate?.isButtonList[0].Label)
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