import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
// components
import { setActiveNum } from '../../actions/BattManageAction';
// import CusLoader from '../../components/CusLoader';
import PageNavHeader from '../../components/PageNavHeader';
import Page1501 from './Page1501';
import Page1505 from './Page1505';

class BattManage extends Component {
    constructor(props) {
        super(props)
        this.state = {
            isRefresh: true,// 每五分鐘刷新頁面
            headerList: [
                { Name: '1501', isShow: true }, // 電池組管理
                { Name: '1505', isShow: true }, // 電池型號管理
            ],
            loading: true,//總畫面讀取
        }
    }


    // React Lifecycle
    componentDidMount() {
        this.setState({ loading: false });
    }
    componentDidUpdate(prevProps, prevState) { }
    componentWillUnmount() {}

    onNavClick = (idx) => {
        this.props.setActiveNum(idx);
    }
    render() {
        const { activeNum } = this.props;
        const { headerList, loading } = this.state;
        if (loading) {return '' } 
        else {
            return (
                <Fragment>
                    {/* 電池管理 */}
                    <PageNavHeader
                        list={headerList}
                        activeNum={activeNum}
                        onClick={this.onNavClick}
                    />
                    <div className="container-fuild">
                        <div className="col-12 pt-4 pb-4 pl-0 pr-0">
                            {
                                activeNum === 0 && <Page1501 onIsRefreshChange={this.onIsRefreshChange} />
                            }
                            {
                                activeNum === 1 && <Page1505 onIsRefreshChange={this.onIsRefreshChange} />
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
        activeNum: state.BattManageReducer.activeNum,
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        setActiveNum: (int) => dispatch(setActiveNum(int)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(BattManage);