import React, { useState,useMemo} from 'react';
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next'                          // i18n Functional Components
// component
import CusPagination from '../../components/CusPagination';
import Toolbar from '../../components/CusTable/Toolbar';                // 操作列(新增、刪除、匯入、匯出)
import SelectPerPage from '../../components/CusTable/SelectPerPage';
import CusTHeader from '../../components/CusTable/CusTHeader';
import StatusDesc from '../../components/CusTable/StatusDesc';

const DataTable = ({data,IMPType,tableHeader,perPage,exportExcel}) => {
    const { t } = useTranslation();
    const [row, setRow] = useState(perPage);                            // 目前每頁顯示筆數
    const [active ,setAcitve] = useState(1);                            // 目前頁數
    const [sortConfig, setSortConfig] = useState({                      // 排序
		key: null,
    })



    // 變更每頁顯示筆數
    const getPerPage = (value) => {
        setRow(Number(value))
        setAcitve(1);                                                   // 更新後回首頁
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
    <div className="col-12 p-0">
        <div className="col-12 p-0">
            <div className="col-12 col-sm-6 d-inline-block text-left">
                <SelectPerPage perPage={perPage} getPerPage={getPerPage}/>{/* 變更表格顯示數量，預設10筆 */}
            </div>
            <div className="col-12 col-sm-6 d-inline-block text-right">
                <Toolbar exportExcel={exportExcel} />
            </div>
        </div>

        <div className="scrollTable mt-0 mr-3 ml-3 mb-3">

            {
                data ? (
                    <table className="fixedHeight" style={{width: '100%'}}>
                        <thead>
                            <tr>
                                {
                                    !tableHeader ?  <th></th>
                                    : <CusTHeader tableHeader={tableHeader} IMPType={IMPType} requestSort={(value)=>{requestSort(value)}}/>
                                }
                            </tr>
                        </thead>
                        <tbody>
                            {
                                sortedTableItems && sortedTableItems.length > 0 ? (
                                    sortedTableItems
                                    .slice(row * (active - 1), row * active)
                                    .map( (item) => {
                                        return (
                                            <tr key={item.Seq}>
                                                <td>{item.RecTime}</td>
                                                <td>
                                                    {
                                                        item.IR ? item.IR.map( (ir,idx) => {
                                                            return <div key={idx}>{ir}</div>
                                                        }):''
                                                    }
                                                </td>
                                                <td>
                                                    {
                                                        item.Vol ? item.Vol.map( (vol,idx) => {
                                                            return <div key={idx}>{vol}</div>
                                                        }): ''
                                                    }
                                                </td>
                                                <td>{item.Temperature}</td>
                                                <td>
                                                    {
                                                        item.StatusCode ? item.StatusCode.map( (code,idx) => {
                                                            return <StatusDesc key={idx} StatusCode={code} />
                                                        }) : ''
                                                    }
                                                </td>
                                            </tr>
                                        )
                                    })
                                ): ''
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
DataTable.propTypes = {
    data: PropTypes.array,
}
export default DataTable;