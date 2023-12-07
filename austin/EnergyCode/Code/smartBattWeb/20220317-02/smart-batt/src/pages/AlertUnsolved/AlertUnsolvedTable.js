import React, { useState,useMemo,useEffect} from 'react';
import { shallowEqual, useSelector } from 'react-redux';
import PropTypes from 'prop-types';
import CusPagination from '../../components/CusPagination';
import { useTranslation } from 'react-i18next'
// components
import Toolbar from '../../components/CusTable/Toolbar';                //操作列(新增、刪除、匯入、匯出)
import SelectPerPage from '../../components/CusTable/SelectPerPage';
import StatusDesc from '../../components/CusTable/StatusDesc';
import CusTHeader from '../../components/CusTable/CusTHeader';
import CloseMultiAlertDialog from './CloseMultiAlertDialog';            //批次解決告警(多個)
import CloseAlertDialog from './CloseAlertDialog';                      //告警詳情(單個)


const AlertUnsolvedTable = ({
    isDisabledBtn,          // 防止重複點擊問題
    isEdit,                 // 是否有編輯權限
    selectAll,              // 未解決告警表格全選
    selectedList,           // 己選取未解決項目清單
    tableHeader,            // 表格欄位名稱
    data,                   // 未解決告警表格內容
    active,                 // 目前表格顯示在第幾頁上
    IMPType,                // 判斷IMPType顯示名稱(20內阻、21豪電阻、22電壓)
    errorMsg,               // 失敗訊息
    isAlertDialog,          // 是否展開彈跳視窗(單一告警)
    isMultiAlertDialog,     // 是否展開彈跳視窗(批次告警)
    alertDialogContent,     // 目前所選單一告警資料
    tableErrorMsg,          // 表格錯誤訊息
    // function
    onIsRefreshChange,      // 變更頁面刷新狀態(停止每五分鐘更新/重新開始每五分鐘更新)
    getActive,              // 取得目前為第幾頁
    onSliderChange,         // 變更表格內(電壓、內阻...)滑桿展開/隱藏
    onSelectAllChange,      // 變更data資料(變更checkbox全選)
    onCheckboxChange,       // 變更data資料(各別checkbox狀態)
    cancelSelectedAll,      // 取消全選
    exportExcel,            // 輪出excel
    resetErrorMessage,      // 清空失敗訊息
    setIsAlertDialog,       // 變更彈跳視窗(單一告警)
    setIsMultiAlertDialog,  // 變更彈跳視窗(批次告警)
    onCloseAlertSumbit,     // 送出解決告警方案
}) => {
    const { t } = useTranslation();
    const username = useSelector(state => state.LoginReducer.username,shallowEqual)             // 使用者名稱
    const ApiKey = useSelector( state => state.LoginReducer.Key,shallowEqual)                   // google Map Key
    const perPage = useSelector( state => state.LoginReducer.perPage,shallowEqual)              // 表格每面呈現幾筆
    const [IMPTypeIdx, setIMPTypeIdx] = useState(0)     // 目前使用的IMPType位於TableHeader的第幾個欄位 
    const [row, setRow] = useState(perPage);            // 目前每頁顯示筆數
    const [sortConfig, setSortConfig] = useState({      // 排序
		key: null,
    })



    useEffect(()=>{
        // 判斷目前使用的IMPType位於TableHeader的第幾個欄位 
        if(IMPType) {
            tableHeader.forEach( (element,idx) => {
                const IMPTypeList = ['20','21','22'];
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
        cancelSelectedAll();                        // 取消checkbox全選
    }
    // 變更頁數
    const setActive = (value) => {
        getActive(value);
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


    // 彈跳視窗 - 批次解決告警(多筆)
    const onCloseMultiAlertSubmit = (list) => {
        onCloseAlertSumbit(list)
    }
    const onMultiAlertDialogOpen = () => {
        setIsMultiAlertDialog(true)
        onIsRefreshChange(false)    //停止5min刷新頁面
    }
    const onMultiAlertDialogClose = () => {
        setIsMultiAlertDialog(false)
        onIsRefreshChange(true)    //停止5min刷新頁面
    }

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
                <Toolbar isExportExcel={true} exportExcel={exportExcel} />
            </div>
        </div>


        <div className="scrollTable text-center">
            {
                sortedTableItems ? (
                    <table className="fixedHeight">
                        <thead>
                            <tr>
                                <th style={{minWidth: '10px',maxWidth:'50px'}}>{/* 全選 */}
                                    <input type="checkbox" onChange={(e)=>{onSelectAllChange(e,active,row,sortedTableItems)}} checked={selectAll} />
                                </th>
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
                                            <td className="text-center">
                                                <input type="checkbox" name={item.EventSeq} checked={item.checked} onChange={(e)=>{onCheckboxChange(e)}} />
                                            </td>
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
                                            {/* IMPType */}
                                            <td className={tableHeader[IMPTypeIdx].active  ? '': 'hide'}>
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
                                                        item.Vol ? item.Vol.map( (vol,idx) => {
                                                            return <div key={idx}>{vol}</div>
                                                        }): ''
                                                    }
                                                </div>
                                            </td>
                                            <td className={tableHeader[14].active ? '': 'hide'}>{item.Temperature}</td>
                                            <td className="text-center td_btn-config" onClick={()=>{setIsAlertDialog({data:item,value:!isAlertDialog})}} ><i className="fas fa-ellipsis-v" /></td>
                                        </tr>
                                    )
                                    })
                                ): <tr><td colSpan="14">{tableErrorMsg}</td></tr> //表格錯誤訊息
                            }
                        </tbody>
                    </table>
                ): <div className="w-100 text-center">{tableErrorMsg}</div> //表格錯誤訊息

            }
        </div>
        {/* 批次解決告警下方操作列表 */}
        {
            (selectedList.length > 0 && isEdit === 1) ? (
                <div className={`page_multiSelectOperateBar ${isMultiAlertDialog ? 'open' : ''}`}>
                    ({t('1022')}{selectedList.length}{t('1023')})
                    <div className="btn btn btn-primary ml-2" onClick={onMultiAlertDialogOpen}>
                        {t('1309')}
                    </div>
                </div>
            ): ''
        }

        {/* 頁籤 */}
        <CusPagination active={active} row={row} data={data} setAcitve={(idx)=>{setActive(idx)}} />

        {/* 彈跳視窗  告警詳倩(單個) */}
        {
            isAlertDialog ? (
            <CloseAlertDialog 
            isDisabledBtn={isDisabledBtn}
            isEdit={isEdit}                                         //判斷是否有可編輯權限
            username={username} 
            data={alertDialogContent}                               //目前所選資料
            ApiKey={ApiKey} 
            open ={isAlertDialog}
            errorMsg={errorMsg}                                     //失敗訊息
            resetErrorMessage={resetErrorMessage}                   //清空失敗訊息
            handleOpen ={handleOpen} 
            handleClose={handleClose}
            handleSubmit={onCloseAlertSumbit}                       //解決告警 標記為[己解決]按鈕
            />)  : ''
        }

        {/* 批次解決告警(多個) */}
        {
            isMultiAlertDialog ? ( 
                <CloseMultiAlertDialog 
                    isDisabledBtn={isDisabledBtn}                   //防止重複點擊問題
                    username={username}                             //使用者名稱
                    open={isMultiAlertDialog}                       //顯示影藏批次解決告警
                    data={selectedList}                             //批次解決告警已選擇項目資訊(表格)
                    errorMsg={errorMsg}                             //失敗訊息
                    resetErrorMessage={resetErrorMessage}           //清空失敗訊息
                    perPage={perPage}
                    handleSubmit={onCloseMultiAlertSubmit}          //批次解決告警 標記為[己解決]按鈕
                    handleOpen={onMultiAlertDialogOpen}             //開啟批次解決告警視窗
                    handleClose={onMultiAlertDialogClose}           //關閉顯示影藏批次解決告警視窗
                />
            ) : ''
        }

    </div>
    )
}
AlertUnsolvedTable.defaultProps ={
    tableErrorMsg: '',
}
AlertUnsolvedTable.propTypes = {
    isEdit: PropTypes.number,
    data: PropTypes.array,
    language: PropTypes.number,
}
export default AlertUnsolvedTable;