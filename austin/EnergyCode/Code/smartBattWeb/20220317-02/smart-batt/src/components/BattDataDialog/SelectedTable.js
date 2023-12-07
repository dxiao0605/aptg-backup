import React ,{useState,useMemo} from 'react';
import CusPagination from '../../components/CusPagination';
import SelectPerPageCmd from '../../components/CusTable/SelectPerPageCmd';
import { useTranslation } from 'react-i18next';

const SelectedTable = ({
    data,
    perPage,
}) => {
    const { t } = useTranslation();
    const [row, setRow] = useState(perPage);            //目前每頁顯示筆數
    const [active ,setAcitve] = useState(1);            //目前頁數
    const [sortConfig, setSortConfig] = useState({      //排序
		key: null,
    })
    
    

    // 變更每頁顯示筆數
    const getPerPage = (value) => {
        setRow(Number(value))
        setAcitve(1);                               // 更新後回表格首頁
    }

    // 排序
	const sortedTableItems = useMemo(() => {
		if(data) {
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
        }
	}, [data, sortConfig]);

	const requestSort = (key) => {
		let direction = "ascending";
		if (sortConfig.key === key && sortConfig.direction === "ascending") {
			direction = "descending";
		}
		setSortConfig({ key, direction });
    };
    


    return(
        <>
            {/* 變更表格顯示數量，預設10筆 */}
            <div className="col-12 col-sm-4 d-inline-block p-0">
                <SelectPerPageCmd perPage={perPage} getPerPage={getPerPage} />
            </div>
            
            <div className="scrollTable">
                {
                    sortedTableItems ? (
                        <table className="fixedHeight w-100">
                            <thead>
                                <tr>
                                    <th className="text-center" onClick={()=>{requestSort('GroupID')}}>{t('1012')}</th>
                                    <th className="text-center" onClick={()=>{requestSort('BatteryGroupID')}}>{t('1026')}</th>
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
                                                <td className="text-center">{item.GroupID}</td>
                                                <td className="text-center">
                                                    <div style={{position:'relative',overflow:'hidden',overflowY:'auto',maxHeight:'100px'}}>
                                                        {Array.isArray(item.BatteryGroupID) ?  (
                                                            item.BatteryGroupID.map( (item,idx) => {
                                                                return <div key={idx}>{item}</div>
                                                            })
                                                        ):item.BatteryGroupID }
                                                    </div>
                                                </td>
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
        </>
    )
}
SelectedTable.defaultProps = {
    source: '',
}
export default SelectedTable;