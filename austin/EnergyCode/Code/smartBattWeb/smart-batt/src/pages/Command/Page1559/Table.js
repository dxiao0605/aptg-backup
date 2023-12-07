import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import Pagination from "react-js-pagination";
import { Trans } from 'react-i18next';
import SelectPerPage from '../../../components/CusTable/SelectPerPage';//顯示table一次幾個
import Toolbar from '../../../components/CusTable/Toolbar';//匯出
import { ArrayIsEquals } from '../../../utils/ArrayIsEquals';//判斷array是否相同的tools




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
        const { tbody, row } = this.state;
        const { active, exportExcel, refreshEvent, tableErrMsg, tableHeader, limits } = this.props;
        return (
            <Fragment>
                <div className="col-12 p-0 mb-1">
                    {/* 變更表格顯示數量，預設10筆 */}
                    <div className="col-12 col-sm-4 d-inline-block p-0">
                        <SelectPerPage perPage={row} getPerPage={(row) => { this.getPerPage(row) }} />
                    </div>
                    {/* 新增、刪除、匯入、匯出表格操作列表 */}
                    <div className="col-12 col-sm-8 d-inline-block text-right p-0" style={{verticalAlign: 'bottom'}}>
                        <Toolbar
                            isExportExcel={true}
                            isRefresh={true}
                            exportExcel={exportExcel}
                            refreshEvent={refreshEvent}
                        />
                    </div>
                </div>
                <div className='scrollTable'>
                    <table className="table text-center">
                        <thead>
                            <tr>
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
                            </tr>
                        </thead>
                        <tbody className='tbody'>
                            {tbody?.length > 0 ?
                                tbody.slice(row * (active - 1), row * active)
                                    .map((item, idx) => {
                                        return (
                                            <tr key={item.Seq}>
                                                { tableHeader[0].active && <td>{item.Company}</td>}
                                                { tableHeader[1].active && <td>{item.Country}</td>}
                                                { tableHeader[2].active && <td>{item.Area}</td>}
                                                { tableHeader[3].active && <td>{item.GroupID}</td>}
                                                { tableHeader[4].active && <td>{item.GroupName}</td>}
                                                { tableHeader[5].active && <td>{item.BatteryTypeName}</td>}
                                                { tableHeader[6].active && <td>{item.BatteryGroupID}</td>}
                                                { tableHeader[7].active && <td>{item.Command}</td>}
                                                { tableHeader[8].active && <td>{item.SendTime}</td>}
                                                { tableHeader[9].active && <td>{item.PublishTime}</td>}
                                                { tableHeader[10].active && <td>{item.AckTime}</td>}
                                                { tableHeader[11].active && <td>{item.ResponseTime}</td>}
                                                { tableHeader[12].active && <td>{item.Response}</td>}
                                                { tableHeader[13].active && <td>{item.Config}</td>}
                                            </tr>
                                        );
                                    })
                                : <tr className="text-center td_btn-config">
                                    <td colSpan={limits.Edit === 1 ? "14" : "14"} >{tableErrMsg}</td>
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
        });
        this.setCurrentPageIdx(1);
    }

    // 取得目前頁籤
    setCurrentPageIdx = (idx) => {
        this.props.onUpdActive(idx);
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
    refreshEvent: () => { },
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
    refreshEvent: PropTypes.func,
    onUpdActive: PropTypes.func,
}
export default Table;