import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { setActiveNum } from '../../actions/SystemSettingsAction';
import { setTreeFunctionId } from '../../actions/MainNavAction';
import PageNavHeader from '../../components/PageNavHeader';
import Page1801 from './Page1801';
import Page1802 from './Page1802';
import Page1815 from './Page1815';


class SystemSettings extends Component {
    constructor(props) {
        super(props)
        this.state = {
            functionId: '1800',//funcId
            limits: {//權限設定
                Edit: 0,
                Button: {}
            },
            isRefresh: true,// 每五分鐘刷新頁面
            headerList: [
                { Name: '1801', isShow: false },//內組/電導值
                { Name: '1802', isShow: false },//公司設定
                { Name: '1815', isShow: false },//指令限制 1815
            ],
            loading: true,//總畫面讀取            
        }
    }


    // React Lifecycle
    componentDidMount() {
        //設定權限
        const { functionList } = this.props;
        const { functionId } = this.state;
        if (this.props.treeFunctionId !== 1800) {
            this.props.setTreeFunctionId(1800);
        }
        const newFList = functionList.filter(item => {
            return item.FunctionId === Number(functionId)
        });
        if (newFList.length === 1) {
            this.setState({
                limits: {
                    Edit: newFList[0].Edit,
                    Button: { ...newFList[0].Button },
                }
            });
            const { headerList } = this.state;
            let activeList = [];
            const newHeaderList = headerList.map((item, idx) => {
                let newItem = item;
                if (newFList[0]?.Button === undefined) {
                    return newItem;
                }
                if (newFList[0].Button['P' + item.Name] === 1 || newFList[0].Button['P' + item.Name] === 2) {
                    newItem.isShow = true
                    activeList.push(idx);
                    return newItem;
                }
                newItem.isShow = false
                return newItem;
            });
            this.setState({
                headerList: [...newHeaderList],
            });
            if (activeList.findIndex(item => item === this.props.activeNum) < 0) {
                this.props.setActiveNum(activeList[0]);
            }
        }
        this.setState({
            loading: false
        });
    }
    componentDidUpdate(prevProps, prevState) {}
    componentWillUnmount() {}
    onNavClick = (idx) => {
        this.props.setActiveNum(idx);
    }
    render() {
        const { activeNum } = this.props;
        const { headerList, loading } = this.state;
        if (loading) {return ''} 
        else {
            return (
                <Fragment>
                    {/* 系統設定 */}
                    <PageNavHeader
                        list={headerList}
                        activeNum={activeNum}
                        onClick={this.onNavClick}
                    />
                    <div className="container-fuild">
                        {/* filter */}
                        <div className="col-12 pb-4 pl-0 pr-0">
                            {
                                activeNum === 0 && <Page1801 onIsRefreshChange={this.onIsRefreshChange} />
                            }
                            {
                                activeNum === 1 && <Page1802 onIsRefreshChange={this.onIsRefreshChange} />
                            }
                            {
                                activeNum === 2 && <Page1815 onIsRefreshChange={this.onIsRefreshChange} />
                            }
                        </div>
                    </div>

                </Fragment>
            )
        }
    }
    //Function
    // 變更頁面刷新狀態
    onIsRefreshChange = (boolean) => {
        this.setState({
            isRefresh: boolean
        })
    }

}



const mapStateToProps = (state, ownProps) => {
    return {
        activeNum: state.SystemSettingsReducer.activeNum,
        functionList: state.LoginReducer.functionList,
        treeFunctionId: state.MainNavReducer.treeFunctionId, //指定目前頁面
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        setActiveNum: (int) => dispatch(setActiveNum(int)),
        setTreeFunctionId: (functionId) => dispatch(setTreeFunctionId(functionId)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(SystemSettings);