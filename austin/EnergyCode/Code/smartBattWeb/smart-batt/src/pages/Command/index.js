import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { setActiveNum } from '../../actions/CommandAction';
import PageNavHeader from '../../components/PageNavHeader';
import Page1504 from './Page1504';
import Page1559 from './Page1559';

class Command extends Component {
    constructor(props) {
        super(props)
        this.state = {
            functionId: '1504',//funcId
            limits: {//權限設定
                Edit: 0,
                Button: {}
            },
            isRefresh: true,// 每五分鐘刷新頁面
            headerList: [
                { Name: '1504', isShow: false },//電池參數設定
                { Name: '1559', isShow: false },//參數設定歷史
            ],
            loading: true,//總畫面讀取            
        }
    }


    // React Lifecycle
    componentDidMount() {
        //設定權限
        const { functionList } = this.props;
        const { functionId } = this.state;
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
                if (newFList[0].Button['P' + item.Name] === 1) {
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
    componentDidUpdate(prevProps, prevState) { }
    componentWillUnmount() { }
    onNavClick = (idx) => {
        this.props.setActiveNum(idx);
    }
    render() {
        const { activeNum } = this.props;
        const { headerList, loading, limits } = this.state;
        if (loading) { return ''} 
        else {
            return (
                <Fragment>
                    {/* 指令 */}
                    <PageNavHeader
                        list={headerList}
                        activeNum={activeNum}
                        onClick={this.onNavClick}
                    />
                    <div className="container-fuild">
                        {/* filter */}
                        <div className="col-12 pb-4 pl-0 pr-0">
                            {
                                limits?.Button?.P1504 === 1 && activeNum === 0 && <Page1504 onIsRefreshChange={this.onIsRefreshChange} />
                            }
                            {
                                limits?.Button?.P1559 === 1 && activeNum === 1 && <Page1559 onIsRefreshChange={this.onIsRefreshChange} />
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
        activeNum: state.CommandReducer.activeNum,
        functionList: state.LoginReducer.functionList,
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        setActiveNum: (int) => dispatch(setActiveNum(int)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(Command);