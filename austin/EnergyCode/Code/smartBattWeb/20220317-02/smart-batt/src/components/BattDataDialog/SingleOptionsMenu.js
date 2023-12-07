import React, { Fragment } from 'react';
import { Link } from 'react-router-dom';
import Divider from '@material-ui/core/Divider';
import Menu from '@material-ui/core/Menu';
import MenuItem from '@material-ui/core/MenuItem';
import ListItemText from '@material-ui/core/ListItemText';

import { useTranslation } from 'react-i18next';
import OptionsMenuItem from './OptionsMenuItem';        // 判斷頁面權限 1可看可編輯 2可看不可編輯  3不可看不可編輯 


const ITEM_HEIGHT = 48;


const SingleOptionsMenu = ({
  layer,                      // 電池數據第幾層(判斷顯示隱藏欄位)
  BatteryID,                  // 電池ID 為0時顯示BA,BB
  buttonControlList,          // 電池數據使用者按鈕權限
  data,                       // 資料
  setOpenDialogId,            // 判斷彈跳視窗內容[BA(186),BB(187),B3(179),B5(181),Group(1416),Battery(1551)]
  redirectURLAlert            // 跳轉至未解決頁面,代入篩選條件(站台編號,站台名稱,電池組ID)
}) => {
  const { t } = useTranslation();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);


    // 開啟功能列表
    const handleClick = (event) => {
      setAnchorEl(event.currentTarget);
    };
    // 關閉功能列表
    const handleClose = () => {
      setAnchorEl(null);
    };
    // 開啟彈跳窗
    const handleItemClick = (id) => {
      /* 選擇令命 
       * id:判斷BA(186),BB(187),B3(179),B5(181),Group(1416),Battery(1551)
       * data:資料
       */
      setOpenDialogId({id:id,data:data})
    }
  
    return (
      <div>
        <div className="btn_icon" onClick={handleClick}>
            <i className='fas fa-ellipsis-v' />
        </div>
        <Menu
          id="long-menu"
          anchorEl={anchorEl}
          keepMounted
          open={open}
          onClose={handleClose}
          PaperProps={{
            style: {
              maxHeight: ITEM_HEIGHT * 4.5,
              width: '24ch',
            },
          }}
        >
          <div>
          { //電池組編輯,站台設定,查看告警
            <Fragment>
              {// 電池組編輯  Enabled 1可看可編輯 2可看不可編輯  3不可看不可編輯 
                layer === 2 && (<OptionsMenuItem name='1551' enabled={buttonControlList['GroupEdit']} handleItemClick={handleItemClick} /> )
              }

              {// 站台設定  Enabled 1可看可編輯 2可看不可編輯  3不可看不可編輯 
                layer === 2 && (<OptionsMenuItem name='1416' enabled={buttonControlList['BattEdit']} handleItemClick={handleItemClick} />)
              }

              {// 查看告警 QueryEvent Enabled 1可看可編輯 2可看不可編輯  3不可看不可編輯 
                buttonControlList['QueryEvent'] ? (
                  (()=>{
                      switch(buttonControlList['QueryEvent']){
                        case 1:
                          return (
                            <Link to="/AlertUnsolved" style={{textDecoration:'none',color:'#333'}} onClick={redirectURLAlert}>
                              <MenuItem><ListItemText primary={t('1415')} /></MenuItem>
                            </Link>
                          )
                        default:
                          return ''
                      }
                  })()
                ): ''
              }
            </Fragment>
          }

          
          {/* 電池參數設定 */}
          {/* <div style={{padding:'0.5rem 0 0 0'}}>{t('1504')}</div> */}
          {
            buttonControlList['BatchCmd'] === 1  && (
                <Fragment>
                   <Divider/>
                   <div style={{padding:'0.5rem 0 0 0'}}>{t('1504')}</div>
                </Fragment>
            )
          }
          <Fragment >
            {// 內阻設定測試值(BB) Enabled 1可看可編輯 2可看不可編輯  3不可看不可編輯 
              (BatteryID === '0' ) ? (<OptionsMenuItem name='187' enabled={buttonControlList['BB']} handleItemClick={handleItemClick} />) : ''
            }
            {// 時間週期設定(BA) Enabled 1可看可編輯 2可看不可編輯  3不可看不可編輯
              (BatteryID === '0' ) ? (<OptionsMenuItem name='186' enabled={buttonControlList['BA']} handleItemClick={handleItemClick} />) : ''
            }
            {// 校正電壓(B5) Enabled 1可看可編輯 2可看不可編輯  3不可看不可編輯 
              (layer === 2 ) && (<OptionsMenuItem name='181' enabled={buttonControlList['B5']} handleItemClick={handleItemClick} />)
            }
            {// 校正內阻(B3) Enabled 1可看可編輯 2可看不可編輯  3不可看不可編輯 
              (layer === 2 ) && (<OptionsMenuItem name='179' enabled={buttonControlList['B3']} handleItemClick={handleItemClick} />)
            }
          </Fragment>
          </div>
        </Menu>
      </div>
    );
}
SingleOptionsMenu.defaultProps = {
  layer:1,                    // 電池數據第幾層
  BatteryID: '0',             // 電池組ID
  buttonControlList: [],      // 電池數據按鈕權限清單
  setOpenDialogId: ()=>{},    // 變更彈跳視窗(id視窗號碼,data單筆資料,role限權)
  redirectURLAlert:()=>{},    // 跳轉至未解決頁面,代入篩選條件(站台編號,電池組ID)
}
export default SingleOptionsMenu;


