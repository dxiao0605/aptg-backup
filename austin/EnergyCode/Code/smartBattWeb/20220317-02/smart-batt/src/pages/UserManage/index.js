import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { setActiveNum } from '../../actions/UserManageAction';
import { setTreeFunctionId } from '../../actions/MainNavAction';
import PageNavHeader from '../../components/PageNavHeader';
import Page1701 from './Page1701';
import Page1702 from './Page1702';

class UserManage extends Component {
    constructor(props) {
        super(props)
        this.state = {
            functionId: '1700',//funcId
            limits: {//權限設定
                Edit: 0,
                Button: {}
            },
            isRefresh: true,// 每五分鐘刷新頁面
            headerList: [
                { Name: '1701', isShow: true },//使用者
                { Name: '1702', isShow: true },//角色
            ],
            loading: true,//總畫面讀取            
        }
    }


    // React Lifecycle
    componentDidMount() {
        //更新headerList 新增 link url
        const newHeaderList = this.state.headerList.map(item => {
            const listFuncOne = this.props.functionList.filter(ite => ite.FunctionId.toString() === item.Name);
            if (listFuncOne.length > 0) {
                item.to = listFuncOne[0].Url;
                item.openLink = true;
                return item;
            } else {
                item.openLink = false;
                return item;
            }
        });
        this.setState({
            headerList: [...newHeaderList]
        });
        //選擇預設標籤及menu
        if (this.props.treeFunctionId) {
            this.state.headerList.forEach((item, idx) => {
                if (item.Name === this.props.treeFunctionId.toString() && idx !== this.props.activeNum) {
                    this.props.setActiveNum(idx);
                }
            });
        }
        this.setState({
            loading: false
        });
    }
    componentDidUpdate(prevProps, prevState) {
        if (this.props.treeFunctionId !== prevProps.treeFunctionId) {
            this.state.headerList.forEach((item, idx) => {
                if (item.Name === this.props.treeFunctionId.toString() && idx !== this.props.activeNum) {
                    this.props.setActiveNum(idx);
                }
            });
        }
    }
    componentWillUnmount() {}
    onNavClick = (idx, item) => {
        this.props.setActiveNum(idx);
        this.props.setTreeFunctionId(Number(item.Name));
    }
    render() {
        const { activeNum } = this.props;
        const { headerList, loading } = this.state;
        if (loading) { return ''} 
        else {
            return (
                <Fragment>
                    {/* 使用者管理 */}
                    <PageNavHeader
                        list={headerList}
                        activeNum={activeNum}
                        onClick={this.onNavClick}
                    />
                    <div className="container-fuild">
                        {/* filter */}
                        <div className="col-12 pb-4 pl-0 pr-0">
                            {
                                activeNum === 0 &&
                                <Page1701 onIsRefreshChange={this.onIsRefreshChange} />
                            }
                            {
                                activeNum === 1 && <Page1702 headerList={headerList} onIsRefreshChange={this.onIsRefreshChange} />
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
        activeNum: state.UserManageReducer.activeNum,
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
export default connect(mapStateToProps, mapDispatchToProps)(UserManage);