import React, { useState,useMemo} from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next'
// components
import CusPagination from '../../components/CusPagination';
import SelectPerPage from '../../components/CusTable/SelectPerPage';
import StatusDesc from '../../components/CusTable/StatusDesc';


const CloseMultiAlertTable = ({data,perPage}) => {
    const { t } = useTranslation();
    const [row, setRow] = useState(perPage);    //目前每頁顯示筆數
    const [active ,setAcitve] = useState(1);    //目前頁數
    const [sortConfig, setSortConfig] = useState({  //排序
		key: null,
    })



    // 變更每頁顯示筆數
    const getPerPage = (value) => {
        setRow(Number(value))
        setAcitve(1);// 更新後回表格首頁
    }
    // 排序
	const sortedTableItems = useMemo(() => {
		let sortedItems = [...data];
		if (sortConfig.key !== null) {
			sortedItems.sort((a, b) => {
				if (a[sortConfig.key] < b[sortConfig.key]) {
					return sortConfig.direction === "ascending" ? -1 : 1;
				}
				if (a[sortConfig.key] > b[sortConfig.key]) {
					return sortConfig.direction === "ascending" ? 1 : -1;
				}
				return 0;
			});
		}
		return sortedItems;
	}, [data, sortConfig]);

	const requestSort = (key) => {
		let direction = "ascending";
		if (sortConfig.key === key && sortConfig.direction === "ascending") {
			direction = "descending";
		}
		setSortConfig({ key, direction });
    };



    
    return (
    <div className="col-12 pb-0 pl-0 pr-0">
        {/* 變更表格顯示數量，預設10筆 */}
        <div className="col-12 p-0 mb-2">
            <div className="col-12 col-sm-6 d-inline-block">
                <SelectPerPage perPage={perPage} getPerPage={getPerPage}/>
            </div>
        </div>

        <div className="scrollTable">
            {
                sortedTableItems ? (
                    <table>
                        <thead>
                            <tr>
                                <th onClick={() => {requestSort("EventTypeCode");}} style={{minWidth:'80px',maxWidth:'80px'}}>{t('1305')}<i className='fas fa-sort'/></th>{/* 告警類型 */}
                                <th onClick={() => {requestSort("BatteryGroupID");}}>{t('1026')}<i className='fas fa-sort'/></th>{/* 電池組ID */}
                                <th onClick={() => {requestSort("RecTime");}}>{t('1036')}<i className='fas fa-sort'/></th>{/* 數據時間 */}
                                <th onClick={() => {requestSort("Alert1");}}>{t('1032')}<i className='fas fa-sort'/></th>{/* 判斷值1 */}
                                <th onClick={() => {requestSort("Alert2");}}>{t('1033')}<i className='fas fa-sort'/></th>{/* 判斷值2 */}
                            </tr>
                        </thead>
                        <tbody>
                            {
                                sortedTableItems && sortedTableItems.length > 0 ? (
                                    sortedTableItems
                                    .slice(row * (active - 1), row * active)
                                    .map( (item,idx) => {
                                    return (
                                        <tr key={idx}>
                                            <td>{item.EventTypeCode ? <StatusDesc StatusCode={Number(item.EventTypeCode)}/> : ''} </td>
                                            <td>{item.BatteryGroupID}</td>
                                            <td>{item.RecTime}</td>
                                            <td>
                                                {/* EventTypeCode 3需更換 4離線 5溫度告警值 */}
                                                {
                                                item.EventTypeCode === '3' 
                                                ? item.Alert1 
                                                : (item.EventTypeCode === '4' ? item.Disconnect: item.Temperature1) }
                                                {item.EventTypeCode === '4' && <>{t('1035')}</>}
                                                {item.EventTypeCode === '25' && <>℃</>}
                                            </td>
                                            <td>{item.Alert2.slice(0,1) === '0' ? '' : item.Alert2 }</td>
                                        </tr>
                                    )
                                    })
                                ): <tr><td></td></tr>
                            }
                        </tbody>
                    </table>
                ): <div className="w-100 text-center">{t('1048')}</div> //查無資料
            }
        </div>


        {/* 頁籤 */}
        <CusPagination active={active} row={row} data={data} setAcitve={(idx)=>{setAcitve(idx)}} />
    </div>
    )
}
CloseMultiAlertTable.propTypes = {
    data: PropTypes.array,
    language: PropTypes.number,
}
export default CloseMultiAlertTable;