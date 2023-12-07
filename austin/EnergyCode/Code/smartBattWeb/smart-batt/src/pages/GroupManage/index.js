import React, { Component, Fragment } from 'react';
import { connect } from 'react-redux';
import { setActiveNum } from '../../actions/GroupManageAction';
import PageNavHeader from '../../components/PageNavHeader';
import Page1502 from './Page1502';

class GroupManage extends Component {
    constructor(props) {
        super(props)
        this.state = {
            isRefresh: true,// 每五分鐘刷新頁面
            headerList: [
                { Name: '1502', isShow: true },
            ],
            loading: true,//總畫面讀取            
        }
    }


    // React Lifecycle
    componentDidMount() { 
        this.setState({
            loading: false
        });
    }
    componentDidUpdate(prevProps, prevState) { }
    componentWillUnmount() { }
    render() {
        const { activeNum } = this.props;
        const { headerList, loading } = this.state;
        if (loading) {return ''} 
        else {
            return (
                <Fragment>
                    {/* 站台管理 */}
                    <PageNavHeader
                        list={headerList}
                        activeNum={activeNum}
                        onClick={this.onNavClick}
                    />
                    <div className="container-fuild">
                        {/* filter */}
                        <div className="col-12 pb-4 pl-0 pr-0">
                            {
                                activeNum === 0 && <Page1502 onIsRefreshChange={this.onIsRefreshChange} />
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
        activeNum: state.GroupManageReducer.activeNum,
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        setActiveNum: (int) => dispatch(setActiveNum(int)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(GroupManage);