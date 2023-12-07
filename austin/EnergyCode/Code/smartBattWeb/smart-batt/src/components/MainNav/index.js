import React, { Fragment, Component } from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { Link } from 'react-router-dom';
import { connect } from 'react-redux';
import { apipath, ajax } from '../../utils/ajax';
import { fetchAlertCountLoad } from '../../actions/AlertCountAction';
import { setTreeFunctionId,updateLogo } from '../../actions/MainNavAction';
import NavItem from './NavItem';
import NavItemIcon from './NavItemIcon';
import './mainNav.scss';
import logoLg from '../../image/rce_smartbatt logo.png';



const apiUrl = apipath();
class MainNav extends Component {
    ajaxCancel = false;//判斷ajax是否取消
    constructor(props) {
        super(props)
        this.state = {
            loaded: false,
            hasAlert: true,                 //是否有告警提示
            alertTotal: 222,                //目前有幾筆告警            
            tree: {},                       //更新MENU物件            
        }
        this.myLogoBig = React.createRef();     //logo 寬版物件
        this.myLogoSml = React.createRef();     //logo 窄版物件
    }

    componentDidMount() {
        const { token, company, alertCount } = this.props;
        const { loaded } = this.state;
        // lazyload
        if(loaded === false) { this.setState({loaded: true})};
        //取得告警數
        this.props.fetchAlertCountLoad({ token: token, company: company })
        alertCount <= 0 ? this.setState({ hasAlert: false, alertTotal: 0 }) : this.setState({ hasAlert: true, alertTotal: alertCount })        
        
        //更新MENU物件
        let newFun = {};
        this.props.functionList.forEach(item => {
            item.active = false;
            item.openMenu = false;
            newFun[item.FunctionId] = item;
        });
        const NewTree = this.HandleUpdTree(newFun, this.props.treeFunctionId);
        this.setState({
            tree: { ...NewTree }
        });
        //取得logo
        this.getCompanyLogo();
    }

    componentDidUpdate(prevProps, prevState) {
        if (this.props.treeFunctionId !== prevProps.treeFunctionId) {//更新tree
            const { tree } = this.state;
            const NewTree = this.HandleUpdTree(tree, this.props.treeFunctionId);
            this.setState({
                tree: { ...(NewTree === undefined ? tree : NewTree) }
            });
            if (NewTree === undefined) { this.props.setTreeFunctionId(prevProps.treeFunctionId) }
        }
        if (this.props.alertCount !== prevProps.alertCount) {
            const { alertCount } = this.props;
            alertCount <= 0 ? this.setState({ hasAlert: false, alertTotal: 0 }) : this.setState({ hasAlert: true, alertTotal: alertCount })
        }
        if (this.props.updLogo !== prevProps.updLogo) {
            //取得logo
            this.getCompanyLogo();
            this.props.updateLogo(false);
        }
    }
    componentWillUnmount() {
        this.ajaxCancel = true;
        clearInterval(this.intervalID)
        this.setState = (state, callback) => {
            return;
        };
    }

    HandleUpdTree = (tree, funcId) => {
        if (funcId === undefined) { return }
        if (Object.keys(tree).length === 0) { return }
        let newFun = {};
        //全部取消
        Object.values(tree).forEach(item => {
            item.active = false;
            item.openMenu = false;
            newFun[item.FunctionId] = item;
        });
        //選擇新的頁面
        let item = newFun[funcId];
        if (item) {
            //自己的那頁
            item.active = true;
            newFun[funcId] = item;
            if (item.ParentId !== "") {
                //點選子層，連同父層一起勾選
                let itemParent = newFun[item.ParentId];
                itemParent.active = true;
                itemParent.openMenu = true;
                newFun[item.ParentId] = itemParent;
            }
        }
        return newFun;
    }

    render() {
        const { isShow } = this.props;
        const { loaded, hasAlert, alertTotal, tree } = this.state;
        // style
        const isLoading = classNames({
            'loading': loaded === false,
        })
        const classCheckedNav = classNames({ //是否顯示功能選單
            'open': isShow === true,
        })
        const classCheckedDisabled = classNames({   //true時,顯示(行動裝置)功能選單內容
            'd-block': isShow === false,
            'd-none': isShow === true,
        })
        const classCheckVisible = classNames({   //true時,顯示(PC)功能選單內容
            'd-block': isShow === true,
            'd-none': isShow === false
        })
        const classImgVisible = classNames({   //顯示底下logo圖示
            'd-inline-block': isShow === true,
            'd-none': isShow === false
        })
        const TreeList = Object.values(tree);
        return (
            <Fragment>
                <nav className={`mainNav align-top ${classCheckedNav} ${isLoading}`}>
                    {/* 展開時(寛版) */}
                    <dl className={` mainNav_content ${classCheckVisible}`}>
                        <Link to="/">
                            <div className="" ref={this.myLogoBig} />
                        </Link>

                        {
                            // 判斷是否為根節點
                            Array.isArray(TreeList) && TreeList.length > 0 && TreeList.filter(filterItem => filterItem.ParentId === '').map(item => {
                                // 判斷是否有子清單列表
                                const hasSub = TreeList.filter(elem => elem.ParentId.toString() === item.FunctionId.toString());
                                return (
                                    <Fragment key={item.FunctionId}>
                                        {/* 選單項目 */}
                                        <NavItem item={item} hasSub={hasSub} hasAlert={hasAlert} alertTotal={alertTotal} setCurrentIdx={this.setCurrentIdx} setOpenMenu={this.setOpenMenu} />
                                    </Fragment>
                                )
                            })
                        }
                    </dl>
                    {/* 收合時(窄版) */}
                    <div className={`mainNavIcon ${classCheckedDisabled}`}>
                        <div className="mainNavIcon_logo-item">
                            <Link to="/">
                                <div className="w-100" ref={this.myLogoSml} />
                            </Link>
                        </div>
                        {
                            // 判斷是否為根節點
                            Array.isArray(TreeList) && TreeList.length > 0 && TreeList.filter(filterItem => filterItem.ParentId === '').map(item => {
                                // 判斷是否有子清單列表
                                const hasSub = TreeList.filter(elem => elem.ParentId.toString() === item.FunctionId.toString());

                                return (
                                    <Fragment key={item.FunctionId}>
                                        {/* 選單項目 */}
                                        <NavItemIcon item={item} hasSub={hasSub} hasAlert={hasAlert} alertTotal={alertTotal} setCurrentIdx={this.setDisabledCurrentIdx} />
                                    </Fragment>
                                )
                            })
                        }
                    </div>

                    {/* 下方logo */}
                    <div className={`bottom_group ${isShow ? '' : 'text-center'}`}>
                        <img src={logoLg} className={`mainNav_logo-bottom align-bottom ${classImgVisible}`} alt="rce_smartbatt" />
                        {/* 展開按鈕 */}
                        <i className={`fas ${isShow ? 'fa-angle-double-left' : 'fa-angle-double-right'} mainNav_toggleBtn align-bottom`} onClick={this.handleNav} />
                    </div>
                </nav>
            </Fragment>

        )
    }

    //取得logo
    getCompanyLogo = () => {
        this.ajaxGetCompanyLogo().then((response) => {
            if (this.ajaxCancel) { return; }//強制結束頁面
            if (Object.keys(response).length === 0) { return; }
            const { code, msg } = response;
            if (code === '00' && msg) {
                if (msg.ImagesFlag === 0) {//文字模式
                    this.myLogoBig.current.innerHTML = '';
                    this.myLogoSml.current.innerHTML = '';
                    //寬版
                    let divBig = document.createElement('div'); // is a node
                    divBig.className = 'mainNav_logo-text-top-big';
                    divBig.innerHTML = msg.ShowName;
                    this.myLogoBig.current.appendChild(divBig);
                    //窄版
                    let divSml = document.createElement('div'); // is a node
                    divSml.className = 'mainNav_logo-text-top-sml';
                    divSml.innerHTML = msg.ShortName;
                    this.myLogoSml.current.appendChild(divSml);
                } else if (msg.ImagesFlag === 1) {//圖片
                    // const width = msg.Width;
                    this.ajaxGetLogoImages()
                        .then(response => response.blob())
                        .then(images => {
                            this.myLogoBig.current.innerHTML = '';
                            this.myLogoSml.current.innerHTML = '';
                            //圖片URL
                            const outside = URL.createObjectURL(images);
                            //寬版
                            let divBig = document.createElement('div'); // is a node
                            divBig.className = 'mainNav_logo-top-big';
                            divBig.style = `width:${'200px'};height:75px;`;
                            let imgBig = document.createElement('img');
                            imgBig.className = "";
                            imgBig.src = outside;
                            imgBig.setAttribute("alt","logo");
                            divBig.appendChild(imgBig);
                            this.myLogoBig.current.appendChild(divBig);
                            //窄版
                            let divSml = document.createElement('div'); // is a node
                            divSml.className = 'mainNav_logo-top-sml';
                            let imgSml = document.createElement('img');
                            imgSml.src = outside;
                            imgSml.setAttribute("alt","logo");
                            imgSml.style = 'max-width:100%';
                            divSml.appendChild(imgSml);
                            this.myLogoSml.current.appendChild(divSml);
                        })
                }
            } else {
                console.error('MainNav ajaxGetCompanyLogo', response)
            }
        });
    }

    //查詢公司Logo API(GET):
    ajaxGetCompanyLogo = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `getCompanyLogo`;
        return ajax(url, 'GET', token, curLanguage, timeZone, company)
        //查詢公司Logo API(GET):
        //https://www.gtething.tw/battery/getCompanyLogo
    }
    //公司Logo API(GET):
    ajaxGetLogoImages = () => {
        const { token, curLanguage, timeZone, company } = this.props;
        const url = `${apiUrl}getLogoImages`;
        return fetch(url, {
            method: 'GET',
            headers: new Headers({
                'Accept': '*/*',
                'Content-Type': 'application/json',
                'token': token,
                'language': curLanguage,
                'timezone': timeZone,
                'company': company,
            }),
        })
        //公司Logo API(GET):
        //https://www.gtething.tw/battery/getLogoImages
    }



    // 顯示/隱藏功能選單
    handleNav = () => {
        const { isShow } = this.props;
        this.props.getIsShow(!isShow);
    }
    //設定openMenu
    setOpenMenu = (funcId) => {
        const { tree } = this.state;
        if (funcId === undefined || Object.keys(tree).length === 0) {
            let newFun = {};
            //全部取消
            Object.values(tree).forEach(item => {
                item.openMenu = false;
                newFun[item.FunctionId] = item;
            });
            this.setState({
                tree: { ...newFun }
            });
            return;
        }
        //更新資料
        switch (funcId) {
            case 1300:
            case 1500:
            case 1700:
                const oldOpenMenu = tree[funcId].openMenu;
                let newFun = {};
                //全部取消
                Object.values(tree).forEach(item => {
                    item.openMenu = false;
                    newFun[item.FunctionId] = item;
                });
                let item = newFun[funcId];
                if (item) {
                    //自己的那頁
                    item.openMenu = !oldOpenMenu;
                    newFun[funcId] = item;
                }
                this.setState({
                    tree: { ...newFun }
                });
                break;
            default:
        }
    }

    // 目前展開下拉清單
    setCurrentIdx = (value) => {
        this.props.setTreeFunctionId(value);
    }
    // 目前展開下拉清單(收合用)
    setDisabledCurrentIdx = (value) => {
        switch (value) {
            case 1300:
                this.props.setTreeFunctionId(1301);
                break;
            case 1500:
                this.props.setTreeFunctionId(1501);
                break;
            case 1700:
                this.props.setTreeFunctionId(1701);
                break;
            default:
                this.props.setTreeFunctionId(value);
        }
    }
}
MainNav.propTypes = {
    isShow: PropTypes.bool,
    getIsShow: PropTypes.func,
}

const mapStateToProps = (state, ownProps) => {
    return {
        token: state.LoginReducer.token,
        account: state.LoginReducer.account,
        username: state.LoginReducer.username,
        role: state.LoginReducer.role,					//權限
        company: state.LoginReducer.company,			//公司別
        timeZone: state.LoginReducer.timeZone,			//時區
        language: state.LoginReducer.language,			//語系
        curLanguage: state.LoginReducer.curLanguage,    //目前語系
        functionList: state.LoginReducer.functionList,	//功能選單
        data: state.LoginReducer.data,
        alertCount: state.AlertCountReducer.alertCount, //告警數
        alertCountErrorMsg: state.AlertCountReducer.alertCountErrorMsg, //告警數錯誤訊息
        treeFunctionId: state.MainNavReducer.treeFunctionId, //指定目前頁面 
        updLogo: state.MainNavReducer.updateLogo, //更新目前LOGO 
    }
}
const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        fetchAlertCountLoad: (data) => dispatch(fetchAlertCountLoad(data)),
        setTreeFunctionId: (functionId) => dispatch(setTreeFunctionId(functionId)),
        updateLogo: (boolean) => dispatch(updateLogo(boolean)),
    }
}
export default connect(mapStateToProps, mapDispatchToProps)(MainNav);