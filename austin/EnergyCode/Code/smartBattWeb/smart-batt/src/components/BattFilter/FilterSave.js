import React, { useState } from 'react';
import { useTranslation } from 'react-i18next'
import PropTypes from 'prop-types';
import FilterSaveHeader from '../../components/CusBookMark/FilterSaveHeader';
import FilterSaveItem from '../../components/CusBookMark/FilterSaveItem';

const FilterSave = ({ list, onClickBookmark, onClickTrash, company , functionId }) => {
    const { t } = useTranslation();
    const [showIdx, setShowIdx] = useState([]);

    const RecTimeLabelSwtich = (FilterConfig) => {
        let label = '';
        switch(FilterConfig.RecTime.Radio) {
            case '0':   // 過去24H
                label = t('1098');
                break;
            case '1':   // 自訂義
                label =  FilterConfig?.RecTime?.isButtonList?.map(item => item.Label).join('、');
                break;
            case '3':   // 過去一個月
                label = t('1109');
                break;
            case '5':   // 過去七天
                label =  t('1111');
                break;
            default:
                label = FilterConfig?.RecTime?.isButtonList?.map(item => item.Label).join('、');
                break;
        }
        return label;
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
                                        {//國家 電池歷史第一層不顯示
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
                                        {//站台編號/名稱
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
                                        {//電池狀態 僅電池數據顯示
                                            (functionId === '1400_1' || functionId === '1400_2') && (
                                                <div className="form-inline px-2 py-1">
                                                    <div>{`${t('1021')}：`}</div>
                                                    <div>
                                                        {FilterConfig?.Status?.isChecked ?
                                                            t('1076') : FilterConfig?.Status?.isButtonList?.map(item => item.Label).join('、')
                                                        }
                                                    </div>
                                                </div>
                                            )
                                        }
                                        {//僅電池歷史顯示(電池組ID,數據時間[預設前24小時資訊])
                                            (functionId === '1600_1' || functionId === '1600_2') && (
                                                <>
                                                {/* 電池組ID */}
                                                <div className="form-inline px-2 py-1">
                                                    <div>{`${t('1026')}：`}</div>
                                                    <div>
                                                        {FilterConfig?.BatteryGroupId?.isChecked ?
                                                            t('1076') : FilterConfig?.BatteryGroupId?.isButtonList?.map(item => item.Label).join('、')
                                                        }
                                                    </div>
                                                </div>

                                                {/* 數據時間 */}
                                                <div className="form-inline px-2 py-1">
                                                    <div>{`${t('1036')}：`}</div>
                                                    <div>{RecTimeLabelSwtich(FilterConfig)}</div>
                                                </div>
                                                </>
                                            )
                                        }
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
FilterSave.propTypes = {
    list: PropTypes.array,
    company: PropTypes.string,
    onClickBookmark: PropTypes.func,
    onClickTrash: PropTypes.func,
}
export default (FilterSave);