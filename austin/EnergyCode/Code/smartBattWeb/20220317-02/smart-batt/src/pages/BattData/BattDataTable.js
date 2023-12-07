import React, { useState,useMemo,useEffect} from 'react';
import { shallowEqual, useSelector } from 'react-redux';
// import {checkedInputValue} from '../../components/BattDataDialog/utils';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next'                                      // i18n Functional Components
//  component
// import CusLoader from '../../components/CusLoader';
import CusPagination from '../../components/CusPagination';
import Toolbar from '../../components/CusTable/Toolbar';                            //操作列(新增、刪除、匯入、匯出)
import SelectPerPage from '../../components/CusTable/SelectPerPage';
import StatusDesc from '../../components/CusTable/StatusDesc';
import CusTHeader from '../../components/CusTable/CusTHeader';
import BatchCmdBar from '../../components/BattDataDialog/BatchCmdBar';               //多選操作按鈕(下方列SELECT)
import SingleOptionsMenu from '../../components/BattDataDialog/SingleOptionsMenu';  //單筆操作清單
import BattDataDialog from '../../components/BattDataDialog';                       //判斷顯示的彈跳視窗(BB,BA,B3,B5,...)


const BattDataTable = ({
    isDisabledBtn,          // 防止重複點擊問題
    buttonControlList,      // 電池數據按鈕權限清單
    // 表格
    selectAll,              // 表格全選
    selectedList,           // 己選取項目清單
    checkedBatchCmdItem,    // 判斷下方下行指令顯示欄位(B3,B5僅BatteryID不為零時顯示)
    tableHeader,            // 表格欄位名稱
    data,                   // 表格內容
    active,                 // 目前第幾頁
    IMPType,                // 判斷IMPType顯示名稱(20內阻、21豪電阻、22電壓)
    // 視窗
    dialogId,               // 選則顯示彈跳視窗(批次)(BA,BB,...)
    redirectURLHistory,     // 跳轉至電池歷史第二層
    redirectURLAlert,       // 跳轉至未解決告警取得(站台編號,電池群組ID)
    // function
    getActive,              // 取得目前表格第幾頁
    onIsRefreshChange,      // 變更頁面刷新狀態(停止每五分鐘更新/重新開始每五分鐘更新)
    exportExcel,            // 輪出excel
    onSliderChange,         // 變更表格(內阻,電壓)展開
    onSelectAllChange,      // 變更data資料(變更checkbox全選)
    cancelSelectedAll,      // 取消全選
    onCheckboxChange,       // 變更data資料(各別checkbox狀態)
    setOpenDialog,          // 顯示打開(BB、BA)
    handleSubmit,           // 發送(批次)BA,(批次)BB,B3,B5,Group,Battery指令
}) => {
    const { t } = useTranslation();
    const perPage = useSelector( state => state.LoginReducer.perPage,shallowEqual)  // 表格每面呈現幾筆
    const [row, setRow] = useState(perPage);            // 目前每頁顯示筆數
    const [IMPTypeIdx, setIMPTypeIdx] = useState(0)     // 目前使用的IMPType位於TableHeader的第幾個欄位 
    const [selectedData,setSelectedData] = useState([]);// 目前所選則的資料
    const [sortConfig, setSortConfig] = useState({      // 排序
		key: null,
    })
    // 下行指令權限
    const BatchCmdRole = buttonControlList['BatchCmd'];



    
	// 相當於componentDidMount
    useEffect(()=>{
        // 判斷目前使用的IMPType位於TableHeader的第幾個欄位 
        if(IMPType) {
            tableHeader.forEach( (element,idx) => {
                if(element.id === '20' || element.id === '21' || element.id === '22'){
                    if(IMPType.toString() === element.id){
                        return setIMPTypeIdx(idx)
                    }
                }
                return 
            })
        }
    },[tableHeader,IMPType])





    /* 表格 */
    // 變更每頁顯示筆數
    const getPerPage = (value) => {
        setRow(Number(value))
        getActive(1)                                    // 取得目前為第幾頁
        cancelSelectedAll();                            // 取消checkbox全選
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
        


    /* 視窗 */
    // 操作顯示隱藏
    const getDialogOpen = ({id,data}) => {
        // 取消全選
        cancelSelectedAll();                            //清空原有已checked
        setSelectedData([]);                            //清空原有選取內容
        // 視窗內容
        setOpenDialog(id);                              //選擇開起的指令[BA(186),BB(187),B3(179),B5(181),Group(1416),Battery(1551)]
        setSelectedData([data]);                        //取得選取的資料內容
        onIsRefreshChange(false)                        //停止5min刷新頁面
    }
    // 關閉視窗
    const onDialogClose = () => {
        setOpenDialog('');                              //選擇開起的指令[BA(186),BB(187)]
        onIsRefreshChange(true)                         //5min刷新頁面
    }
    // 顯示視窗
    const getMultiOpenDialog = (id) => {
        setSelectedData([]);                            //清空原有選取內容
        setOpenDialog(id)                               //選擇開起的指令視窗[BA(186),BB(187)]
        // 當多選BA、BB指令且為電池數據第二層時,取得新的selectedList,內僅BatteryID為零的
        if(selectedList.length > 1 && selectedList[0].BatteryID){
            if(id === '187' || id === '186' ) {
                const newList = selectedList.filter( filterItem => filterItem.BatteryID === '0');
                setSelectedData(newList)
            }
        }else{setSelectedData(selectedList);}
        
        //停止5min刷新頁面
        (id && id !== '') ? onIsRefreshChange(false) : onIsRefreshChange(true)
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
    
            {/* 表格 */}
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
                                        .map( (item) => {
                                        return (
                                            <tr key={item.Seq}>
                                                <td className="text-center">
                                                    <input type="checkbox" name={item.Seq} checked={item.checked} onChange={(e)=>{onCheckboxChange(e)}} />
                                                </td>
                                                <td className={tableHeader[0].active ? '': 'hide'}>{item.Company}</td>
                                                <td className={tableHeader[1].active ? '': 'hide'}>{item.Country}</td>
                                                <td className={tableHeader[2].active ? '': 'hide'}>{item.Area}</td>
                                                <td className={`${tableHeader[3].active ? '': 'hide'} text-left`}>{item.Address}</td>
                                                <td className={tableHeader[4].active ? '': 'hide'}>{item.GroupID}</td>
                                                <td className={tableHeader[5].active ? '': 'hide'}>{item.GroupName}</td>
                                                <td className={tableHeader[6].active ? '': 'hide'}>
                                                    <Link to="/BattHistory" onClick={()=>{redirectURLHistory({
                                                        batteryGroupId: item.BatteryGroupID,
                                                        battInternalId: item.BattInternalID
                                                        })}}>
                                                        {item.BatteryGroupID}
                                                    </Link>
                                                </td>
                                                <td className={tableHeader[7].active ? '': 'hide'}>{item.InstallDate}</td>
                                                <td className={tableHeader[8].active ? '': 'hide'}>{item.BatteryType}</td>
                                                <td className={tableHeader[9].active ? '': 'hide'}>{item.RecTime}</td>
                                                {/* IMPType */}
                                                <td className={tableHeader[IMPTypeIdx].active  ? '': 'hide'}>
                                                    <div className={`table-sliderbox ${item.slider === true ? 'open' : ''}`}>
                                                        <div className="table-sliderbox--contorl" onClick={(e)=>{onSliderChange(e)}}>
                                                            <i id={item.Seq} className={`fas ${item.slider === true ? 'fa-caret-up' : 'fa-caret-down'}`} />
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
                                                        <div className="table-sliderbox--contorl" name={item.Seq} onClick={(e)=>{onSliderChange(e)}}>
                                                            <i id={item.Seq} className={`fas ${item.slider === true ? 'fa-caret-up' : 'fa-caret-down'}`}  />
                                                        </div>
                                                        {
                                                            item.Vol ? item.Vol.map( (vol,idx) => {
                                                                return <div key={idx}>{vol}</div>
                                                            }): ''
                                                        }
                                                    </div>
                                                </td>
                                                <td className={tableHeader[14].active ? '': 'hide'}>{item.Temperature}</td>
                                                <td className={tableHeader[15].active ? '': 'hide'}><StatusDesc StatusCode={item.StatusCode} /></td>
                                                <td className="text-center td_btn-config">
                                                    {/* 單筆資料操作選單 */}
                                                    <SingleOptionsMenu
                                                        layer={2}                               // 電池數據第幾層(判斷顯示隱藏欄位)
                                                        BatteryID={item.BatteryID}              // 電池組ID
                                                        buttonControlList={buttonControlList}   // 電池數據按鈕權限清單
                                                        data={item}                             // 資料
                                                        setOpenDialogId={getDialogOpen}         // 判斷彈跳視窗內容[BA(186),BB(187),B3(179),B5(181),Group(1416),Battery(1551)]
                                                        redirectURLAlert={()=>{
                                                            redirectURLAlert({                  // 跳轉至未解決頁面,代入篩選條件(站台編號,電池組ID)
                                                                groupId:item.GroupInternalID,
                                                                groupName:`${item.GroupName}${item.GroupID}`,
                                                                groupLabel:item.GroupLabel,
                                                                batteryGroupId: item.BatteryGroupID,
                                                                battInternalID: item.BattInternalID,
                                                                })
                                                            }
                                                        }
                                                    />
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
            <CusPagination active={active} row={row} data={data} setAcitve={(idx)=>{setActive(idx)}} />
            

            {/* 下方操作列表(下行指令操作欄) */}
            {
                (BatchCmdRole === 1 && selectedList.length > 0) ? (
                    <BatchCmdBar
                        layer={2}// 目前在電池數據第幾層
                        selected={dialogId}// 目前所選指令代號(186,187,181,179,...)
                        list={selectedList}// 已選擇清單
                        getSelectOpen={getMultiOpenDialog}// 開啟指令視窗
                        checkedItem={checkedBatchCmdItem()}// 判斷下行指令BB,BA,B3,B5 是否顯示(已選擇清單中BatteryID不等於0的總數)
                    />
                ): ''
            }
    


        {/* 彈跳視窗 */}
        <BattDataDialog 
            perPage={perPage}
            dialogId={dialogId}
            source="BattData"
            selectedData={selectedData}
            onDialogClose={onDialogClose}
            handleSubmit={handleSubmit} />
    </div>
    )
}

BattDataTable.defaultProps = {
    checkedBatchCmdItem: () => {},
}
BattDataTable.propTypes = {
    data: PropTypes.array,
    language: PropTypes.number,
}
export default BattDataTable;