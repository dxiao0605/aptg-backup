import React, { useState,useMemo, useEffect } from 'react';
import { shallowEqual, useSelector } from 'react-redux'
import PropTypes from 'prop-types';
// components
import CusPagination from '../../components/CusPagination';
import Toolbar from '../../components/CusTable/Toolbar';                //操作列(新增、刪除、匯入、匯出)
import SelectPerPage from '../../components/CusTable/SelectPerPage';
import StatusDesc from '../../components/CusTable/StatusDesc';
import CusTHeader from '../../components/CusTable/CusTHeader';
import CloseAlertDialog from './CloseAlertDialog';                      //告警詳情(單個)


const AlertSolvedTable = ({
    tableHeader,        // 表格欄位名稱
    data,               // 已解決告警表格內容
    IMPType,            // 判斷IMPType顯示名稱(20內阻、21豪電阻、22電壓)
    active,             // 目前表格顯示在第幾頁上
    isAlertDialog,      // 是否展開彈跳視窗(單一告警)
    alertDialogContent, // 目前所選單一告警資料
    // function
    getActive,          // 取得目前為第幾頁
    setIsAlertDialog,   // 變更彈跳視窗(單一告警)
    onIsRefreshChange,  // 變更頁面刷新狀態
    onSliderChange,     // 變更表格內(電壓、內阻...)滑桿展開/隱藏
    exportExcel,        // 輪出excel
    tableErrorMsg,      // 表格錯誤訊息
}) => {
    const ApiKey = useSelector( state => state.LoginReducer.Key,shallowEqual)                   // google Map Key
    const perPage = useSelector( state => state.LoginReducer.perPage,shallowEqual)              // 表格每面呈現幾筆
    const [IMPTypeIdx, setIMPTypeIdx] = useState(0)  // 目前使用的IMPType位於TableHeader的第幾個欄位 
    const [row, setRow] = useState(perPage);         // 目前每頁顯示筆數
    const [sortConfig, setSortConfig] = useState({   // 排序
		key: null,
    })
    
    
    useEffect(()=>{
        // 判斷目前使用的IMPType位於TableHeader的第幾個欄位 
        if(IMPType) {
            tableHeader.forEach( (element,idx) => {
                const IMPTypeList = ['20','21','22']
                if(IMPTypeList.includes(element.id)){
                    if(IMPType.toString() === element.id){
                        setIMPTypeIdx(idx)
                    }
                }
                return 
            })
        }
    },[tableHeader,IMPType])


    // 變更每頁顯示筆數
    const getPerPage = (value) => {
        setRow(Number(value))
        getActive(1);                               // 更新後回表格首頁
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


    //  彈跳視窗 - 告警詳倩(單一告警)
    const handleOpen = () => {
        setIsAlertDialog(true)
        onIsRefreshChange(false)    //停止5min刷新頁面
    }
    const handleClose = () => {
        setIsAlertDialog(false)
        onIsRefreshChange(true)    //重新開始5min刷新頁面
    }
    
    return (
    <div className="col-12 p-0">
        <div className="col-12 p-0 mb-1">
            {/* 變更表格顯示數量，預設10筆 */}
            <div className="col-12 col-sm-4 d-inline-block p-0">
                <SelectPerPage perPage={perPage} getPerPage={getPerPage}/>
            </div>
            {/* 新增、刪除、匯入、匯出表格操作列表 */}
            <div className="col-12 col-sm-8 d-inline-block text-right p-0" style={{verticalAlign: 'bottom'}}>
                <Toolbar isExportExcel={true} exportExcel={exportExcel}/>
            </div>
        </div>


        <div className="scrollTable text-center">
            {
                sortedTableItems ? (
                    <table className="fixedHeight">
                        <thead>
                            <tr>
                               
                                {
                                    !tableHeader ?  <th></th>
                                    : <CusTHeader tableHeader={tableHeader} IMPType={IMPType} requestSort={(value)=>{requestSort(value)}}/>
                                }
                                <th style={{minWidth: '10px',maxWidth:'50px'}}></th>
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
                                            <td className={tableHeader[0].active ? '': 'hide'}>{item.EventTypeCode ? <StatusDesc StatusCode={Number(item.EventTypeCode)}/> : ''} </td>
                                            <td className={tableHeader[1].active ? '': 'hide'}>{item.Company}</td>
                                            <td className={tableHeader[2].active ? '': 'hide'}>{item.Country}</td>
                                            <td className={tableHeader[3].active ? '': 'hide'}>{item.Area}</td>
                                            <td className={tableHeader[4].active ? '': 'hide'}>{item.GroupID}</td>
                                            <td className={tableHeader[5].active ? '': 'hide'}>{item.GroupName}</td>
                                            <td className={tableHeader[6].active ? '': 'hide'}>{item.BatteryGroupID}</td>
                                            <td className={tableHeader[7].active ? '': 'hide'}>{item.InstallDate}</td>
                                            {/* 電池型號 */}
                                            <td className={tableHeader[8].active ? '': 'hide'}>{item.BatteryType}</td>
                                            <td className={tableHeader[9].active ? '': 'hide'}>{item.RecTime}</td>
                                            <td className={(tableHeader[IMPTypeIdx].active) ? '': 'hide'}>
                                                <div className={`table-sliderbox ${item.slider === true ? 'open' : ''}`}>
                                                    <div className="table-sliderbox--contorl" onClick={(e)=>{onSliderChange(e)}}>
                                                        <i id={item.EventSeq} className={`fas ${item.slider === true ? 'fa-caret-up' : 'fa-caret-down'}`} />
                                                    </div>
                                                    {
                                                        item.IR ? item.IR.map( (ir,idx) => {
                                                            return <div key={idx}>{ir}</div>
                                                        }):''
                                                    }
                                                </div>
                                            </td>
                                            <td className={tableHeader[13].active ? '': 'hide'}>
                                                <div className={`table-sliderbox ${item.slider === true ? 'open' : ''}`}>
                                                    <div className="table-sliderbox--contorl" name={item.EventSeq} onClick={(e)=>{onSliderChange(e)}}>
                                                        <i id={item.EventSeq} className={`fas ${item.slider === true ? 'fa-caret-up' : 'fa-caret-down'}`}  />
                                                    </div>
                                                    {
                                                        item?.Vol && Array.isArray(item.Vol) && item.Vol.length>0 ? item.Vol.map( (vol,idx) => {
                                                            return <div key={idx}>{vol}</div>
                                                        }): ''
                                                    }
                                                </div>
                                            </td>
                                            <td className={tableHeader[14].active ? '': 'hide'}>{item.Temperature}</td>
                                            <td className={tableHeader[15].active ? '': 'hide'}>{item.CloseTime}</td>
                                            <td className="text-center td_btn-config" onClick={()=>{setIsAlertDialog({data:item,value:!isAlertDialog})}} ><i className="fas fa-ellipsis-v" /></td>
                                        </tr>
                                    )
                                    })
                                ): <tr><td colSpan="15">{tableErrorMsg}</td></tr> //表格錯誤訊息
                            }
                        </tbody>
                    </table>
                ): <div className="w-100 text-center">{tableErrorMsg}</div> //表格錯誤訊息

            }
        </div>



        {/* 頁籤 */}
        <CusPagination active={active} row={row} data={data} setAcitve={(idx)=>{getActive(idx)}} />


        {/* 彈跳視窗  告警詳倩(單個) */}
        {
            isAlertDialog ? (
            <CloseAlertDialog 
            data={alertDialogContent}  //目前所選單筆資料
            ApiKey={ApiKey} 
            open ={isAlertDialog}
            handleOpen ={handleOpen} 
            handleClose={handleClose}
            />)  : ''
        }




    </div>
    )
}
AlertSolvedTable.propTypes = {
    data: PropTypes.array,
    language: PropTypes.number,
}
export default AlertSolvedTable;