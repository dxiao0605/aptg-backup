import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import Pagination from "react-js-pagination";
import { Link } from 'react-router-dom';
// i18n
import { Trans } from 'react-i18next';
import SelectPerPage from '../../../components/CusTable/SelectPerPage';//顯示table一次幾個
import Toolbar from '../../../components/CusTable/Toolbar';//匯出
import { ArrayIsEquals } from '../../../utils/ArrayIsEquals';//判斷array是否相同的tools
import FadeMenu from '../../../components/FadeMenu';//table點點點功能



class Table extends Component {
    constructor(props) {
        super(props)
        this.state = {
            sortConfig: {
                key: null,
                direction: "ascending"
            },
            tbody: [],
            row: 10,//目前每頁顯示筆數
            menuList: ['1050'],
            selectAll: false, //checkbox全選
        };
    }
    componentDidMount() {
        const { data, perPage } = this.props;
        this.setState({
            row: perPage,
            tbody: [...data]
        });
    }
    componentDidUpdate(prevProps, prevState) {
        if (this.props.data !== undefined && ArrayIsEquals(this.props.data, prevProps.data)) {
            this.setState({
                tbody: [...this.props.data],
            });
        }
        if (this.props.perPage !== undefined && this.props.perPage !== prevProps.perPage) {
            this.setState({
                row: this.props.perPage
            })
        }
    }
    render() {      
        const { tbody, row, menuList, selectAll } = this.state;
        const { active, addEvent, deleteEvent, importExcel, exportExcel, tableErrMsg, tableHeader, limits } = this.props;
        return (
            <Fragment>
                <div className="col-12 p-0 mb-1">
                    {/* 變更表格顯示數量，預設10筆 */}
                    <div className="col-12 col-sm-4 d-inline-block p-0">
                        <SelectPerPage perPage={row} getPerPage={(row) => { this.getPerPage(row) }} />
                    </div>
                    {/* 新增、刪除、匯入、匯出表格操作列表 */}
                    <div className="col-12 col-sm-8 d-inline-block text-right p-0" style={{ verticalAlign: 'bottom' }}>
                        <Toolbar
                            isAddItem={limits.Edit === 1 ? true : false}
                            isDeleteItem={limits.Edit === 1 ? true : false}
                            isInportExcel={limits.Edit === 1 ? true : false}
                            isExportExcel={true}
                            addEvent={addEvent}
                            deleteEvent={deleteEvent}
                            importExcel={importExcel}
                            exportExcel={exportExcel} />
                    </div>
                </div>
                <div className='scrollTable'>
                    <table className="table text-center">
                        <thead>
                            <tr>
                                {limits.Edit === 1 && Array.isArray(tableHeader) && tableHeader.length > 0 &&
                                    < th style={{ minWidth: '10px', maxWidth: '50px' }}>{/* 全選 */}
                                        <input type="checkbox" onChange={(e) => { this.onSelectAllChange(e) }} checked={selectAll} />
                                    </th>
                                }
                                {
                                    Array.isArray(tableHeader) && tableHeader.map(item => {
                                        return item.active && (
                                            <th
                                                key={item.id}
                                                onClick={() => { this.requestSort(`${item.sortName}`) }}
                                            >
                                                <Trans i18nKey={`${item.id}`} /><i className='fas fa-sort' />
                                            </th>
                                        )
                                    })
                                }
                                {limits.Edit === 1 && Array.isArray(tableHeader) && tableHeader.length > 0 && < th style={{ minWidth: '10px', maxWidth: '50px' }}></th>}
                            </tr>
                        </thead>
                        <tbody className='tbody'>
                            {tbody?.length > 0 ?
                                tbody.slice(row * (active - 1), row * active)
                                    .map((item, idx) => {
                                        return (
                                            <tr key={item.Seq}>
                                                {limits.Edit === 1 && Array.isArray(tableHeader) && tableHeader.length > 0 &&
                                                    <td className="text-center">
                                                        {
                                                            item.DefaultGroup !== 0 &&
                                                            <input type="checkbox" name={item.BatteryTypeCode} checked={item.checked} onChange={(e) => { this.onCheckboxChange(e, item) }} />
                                                        }
                                                    </td>
                                                }
                                                {tableHeader[0].active && <td>{item.Company}</td>}
                                                {tableHeader[1].active && <td>{item.Country}</td>}
                                                {tableHeader[2].active && <td>{item.Area}</td>}
                                                {tableHeader[3].active && <td>{item.GroupName}</td>}
                                                {tableHeader[4].active && <td>
                                                    <Link to="/BattData" onClick={() => {
                                                        this.props.redirectURLBattLayer2({ companyCode: item.CompanyCode, company: item.Company, groupId: item.GroupInternalID, groupLabel: item.GroupLabel })
                                                    }}>{item.GroupID}</Link>
                                                </td>}
                                                {tableHeader[5].active && <td className="text-left">{item.Address}</td>}
                                                {tableHeader[6].active && <td><Link to="/BattData" onClick={() => {
                                                        this.props.redirectURLBattLayer2({ companyCode: item.CompanyCode, company: item.Company, groupId: item.GroupInternalID, groupLabel: item.GroupLabel })
                                                    }}>{item.Count}</Link></td>}
                                                {limits.Edit === 1 && Array.isArray(tableHeader) && tableHeader.length > 0 &&
                                                    <td className="text-center">
                                                        <FadeMenu
                                                            data={item}
                                                            menuList={menuList}
                                                            onMenuClick={this.onMenuClick}
                                                        />
                                                    </td>
                                                }
                                            </tr>
                                        );
                                    })
                                : <tr className="text-center td_btn-config">
                                    <td colSpan={limits.Edit === 1 ? "9" : "7"} >{tableErrMsg}</td>
                                </tr>}
                        </tbody>
                    </table>
                </div>

                {/* 頁籤 */}
                <div className="text-center">
                    <Pagination
                        prevPageText={<Trans i18nKey="1044" />}   //上一頁
                        nextPageText={<Trans i18nKey="1045" />}   //下一頁
                        firstPageText={<Trans i18nKey="1046" />}    //首頁
                        lastPageText={<Trans i18nKey="1047" />}    //末頁
                        activePage={active}
                        itemsCountPerPage={row}
                        totalItemsCount={tbody.length}
                        onChange={(idx) => { this.setCurrentPageIdx(idx) }}
                    />
                </div>
            </Fragment >
        )

    }
    // 變更每頁顯示筆數
    getPerPage = (value) => {
        this.setState({
            row: Number(value),
            // active: 1
        });
        this.props.onUpdActive(1);
    }
    //單一chkbox事件
    onCheckboxChange = (event, item) => {
        const { tbody } = this.state;
        const idx = tbody.findIndex(ite => ite === item);
        tbody[idx].checked = !tbody[idx].checked;
        this.props.onUpdateTable(tbody);
    }
    //全選chkbox事件
    onSelectAllChange = (event) => {
        const { tbody, selectAll } = this.state;
        const { row } = this.state;//第幾頁、顯示幾筆
        const { active } = this.props;
        const idxStart = 0 + Number(row) * (Number(active) - 1);//0
        const idxEnd = Number(idxStart) + Number(row) - 1;//9
        const newTable = tbody.map((item, idx) => {
            if (idx >= idxStart && idx <= idxEnd) {
                return { ...item, checked: !selectAll }
            } else {
                return item;
            }
        });
        this.setState({
            selectAll: !selectAll
        });
        this.props.onUpdateTable(newTable);
    }

    //點點點的click事件
    onMenuClick = ({ event, data, item }) => {
        const { menuList } = this.state;
        if (item === menuList[0]) {//編輯
            this.props.getEditData({ data });
        }
    }

    // 取得目前頁籤
    setCurrentPageIdx = (idx) => {
        this.setState({ selectAll: false });
        this.props.onUpdActive(idx);
        //取消全選chkbox事件
        const { tbody } = this.state;
        const newTable = tbody.map((item, idx) => {
            return { ...item, checked: false }
        });
        this.props.onUpdateTable(newTable);

    }

    // 排序，點第一次為正序，第二次為反序
    requestSort = (key) => {
        const { sortConfig, tbody } = this.state;
        let sortedItems = [...tbody];
        let direction = "ascending";
        if (sortConfig.key === key) {
            if (sortConfig.direction === "ascending") {
                direction = "descending";
            }
            sortedItems.sort((a, b) => {
                if (a[sortConfig.key] > b[sortConfig.key]) {
                    return sortConfig.direction === "ascending" ? -1 : 1;
                }
                if (a[sortConfig.key] < b[sortConfig.key]) {
                    return sortConfig.direction === "ascending" ? 1 : -1;
                }
                return 0;
            });
        }
        this.setState({
            sortConfig: {
                key: key,
                direction: direction,
            },
            tbody: [...sortedItems],
        });
    };
}



Table.defaultProps = {
    active: 1,
    data: [],
    tableHeader: [],
    tableErrMsg: '',
    addEvent: () => { },
    deleteEvent: () => { },
    exportExcel: () => { },
    onUpdActive: () => { },
}
Table.propTypes = {
    actvie: PropTypes.number,
    data: PropTypes.array,
    perPage: PropTypes.number,
    tableHeader: PropTypes.array,
    tableErrMsg: PropTypes.string,
    addEvent: PropTypes.func,
    deleteEvent: PropTypes.func,
    exportExcel: PropTypes.func,
    onUpdActive: PropTypes.func,
}
export default Table;