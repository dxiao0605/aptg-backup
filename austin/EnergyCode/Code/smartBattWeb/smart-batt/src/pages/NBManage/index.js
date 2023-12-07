import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { setActiveNum } from '../../actions/NBManageAction';
import PageNavHeader from '../../components/PageNavHeader';
import Page1513 from './Page1513';
import Page1515 from './Page1515';
import Page1539 from './Page1539';
import Page1572 from './Page1572';
class NBManage extends Component {
    constructor(props) {
        super(props)
        this.state = {
            functionId: '1503',//funcId
            limits: {//權限設定
                Edit: 0,
                Button: {}
            },
            isRefresh: true,// 每五分鐘刷新頁面
            headerList: [
                { Name: '1513', isShow: false },//匯入與分配
                { Name: '1515', isShow: false },//啟用與停用
                { Name: '1539', isShow: false },//異動紀錄
                { Name: '1572', isShow: false },//接續歷史
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
    componentDidUpdate(prevProps, prevState) {}
    componentWillUnmount() {}

    
    render() {
        const { activeNum } = this.props;
        const { headerList, loading, limits } = this.state;
        if (loading) {return ''} 
        else {
            return (
                <Fragment>
                    {/* 通續序號 */}
                    <PageNavHeader
                        list={headerList}
                        activeNum={activeNum}
                        onClick={this.onNavClick}
                        extraStyle="extra"
                    />
                    <div className="container-fuild">
                        {/* filter */}
                        <div className="col-12 pb-4 pl-0 pr-0">
                            {
                                limits?.Button?.P1513 === 1 && activeNum === 0 && <Page1513 onIsRefreshChange={this.onIsRefreshChange} />
                            }
                            {
                                limits?.Button?.P1515 === 1 && activeNum === 1 && <Page1515 onIsRefreshChange={this.onIsRefreshChange} />
                            }
                            {
                                limits?.Button?.P1539 === 1 && activeNum === 2 && <Page1539 onIsRefreshChange={this.onIsRefreshChange} />
                            }
                            {
                                limits?.Button?.P1572 === 1 && activeNum === 3 && <Page1572 onIsRefreshChange={this.onIsRefreshChange} />
                            }
                        </div>
                    </div>

                </Fragment>
            )
        }
    }
    //Function
    onNavClick = (idx) => {
        this.props.setActiveNum(idx);
    }
    // 變更頁面刷新狀態
    onIsRefreshChange = (boolean) => {
        this.setState({
            isRefresh: boolean
        })
    }

}



const mapStateToProps = (state, ownProps) => {
    return {
        activeNum: state.NBManageReducer.activeNum,
        functionList: state.LoginReducer.functionList,
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        setActiveNum: (int) => dispatch(setActiveNum(int)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(NBManage);