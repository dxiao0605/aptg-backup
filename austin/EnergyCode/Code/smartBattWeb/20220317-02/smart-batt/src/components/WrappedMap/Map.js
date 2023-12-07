import React, { Component, Fragment } from "react";
import { connect } from 'react-redux';
import { setGroupID } from '../../actions/BattDataAction';
import { resetCompany,updateCompany, updateGroupId } from '../../actions/BattFilterAction';
/* i18n Functional Components */
import { Translation } from 'react-i18next';
// 
import { checkedMarkerType, checkedMarkerVisible, groupIcon } from './utils';
import SetIMPType from './SetIMPType';
import StatusDesc from '../../components/CusTable/StatusDesc';
import { CusMainBtnStyle } from '../../components/CusMainBtnStyle';
import './Map.scss';
// We will use these things from the lib
// https://react-google-maps-api-docs.netlify.com/
import { Marker, InfoWindow } from "@react-google-maps/api";


class Map extends Component {
	constructor(props) {
		super(props)
		this.state = {
			keyCode: '',
		}
	}
	render() {
		const {
			data,
			IMPType,					//判斷IMP名稱(20,21,22)
			infoOpen,
			markerVisible, 				//判斷 zoom大小
			selectedPlace,
			markerMap,
			markerLoadHandler,
			markerClickHandler,
			setInfoOpen,
			// showBattDataHandler, 	//查看數據
		} = this.props;
		return (
			<>
				{data.map((place) => (
					<Fragment key={place.Seq}>
						{
							place.Type === 'Group' ? (
								<Marker
									position={{ lat: place.Lat, lng: place.Lng }}
									onLoad={(marker) => markerLoadHandler(marker, place)}
									onClick={(event) => markerClickHandler(event, place)}
									label={{
										text: place.Count ? (place.Count).toString() : '',
										color: '#ffffff'
									}}
									visible={checkedMarkerVisible(markerVisible, place.Type)}
									// Not required, but if you want a custom icon:
									icon={groupIcon}
								/>
							) : (
								<Marker
									position={{ lat: place.Lat, lng: place.Lng }}
									onLoad={(marker) => markerLoadHandler(marker, place)}
									onClick={(event) => markerClickHandler(event, place)}
									visible={checkedMarkerVisible(markerVisible, place.Type)}
									// Not required, but if you want a custom icon:
									icon={{
										url: checkedMarkerType(place.StatusCode).toString(),
										scaledSize: { width: 55, height: 65 },
										// scaledSize: { width: 35, height: 45 },
									}}
								/>
							)
						}
					</Fragment>
				))}

				{data && infoOpen && selectedPlace && (
					<InfoWindow
						anchor={markerMap[selectedPlace.Seq]}
						onCloseClick={() => setInfoOpen(false)}>
						<div className='map_infoWindow'>
							{/* <div className='col-12'> */}
							{/* <div className='map_infoWindow--title'> */}
							{/* {selectedPlace.Seq ? selectedPlace.Seq : ""}{" "} */}
							{/* {selectedPlace.GroupName ? selectedPlace.GroupName : ""} */}
							{/* </div> */}
							{/* </div> */}
							<div className='map_infoWindow--content'>
								<div className='col-12'>
									<div className='map_infoWindow--label col-6 d-inline-block'>
										{/* 國家/地域 */}
										<Translation>
											{(t) => <>{t('1011')}</>}
										</Translation>
									</div>
									<div className='col-6 d-inline-block'>
										{selectedPlace.Country}/{selectedPlace.Area}
									</div>
								</div>
								<div className='col-12'>
									<div className='map_infoWindow--label col-6 d-inline-block'>
										{/* 基地台號碼 */}
										<Translation>
											{(t) => <>{t('1012')}</>}
										</Translation>
									</div>
									<div className='col-6 d-inline-block'>
										{selectedPlace.GroupID ? selectedPlace.GroupID : ""}
									</div>
								</div>
								<div className='col-12'>
									<div className='map_infoWindow--label col-6 d-inline-block'>
										{/* 基地台名稱 */}
										<Translation>
											{(t) => <>{t('1013')}</>}
										</Translation>
									</div>
									<div className='col-6 d-inline-block'>
										{selectedPlace.GroupName ? selectedPlace.GroupName : ""}
									</div>
								</div>
								<div className='col-12'>
									<div className='map_infoWindow--label col-6 d-inline-block'>
										{/* 電池組數 */}
										<Translation>
											{(t) => <>{t('1014')}</>}
										</Translation>
									</div>
									<div className='col-6 d-inline-block'>
										{selectedPlace.BatteryCount ? selectedPlace.BatteryCount : ""}
									</div>
								</div>
								<div className='col-12'>
									<div className='map_infoWindow--label col-6 d-inline-block'>
										{/* 數據更新時間 */}
										<Translation>
											{(t) => <>{t('1015')}</>}
										</Translation>
									</div>
									<div className='col-6 d-inline-block'>
										{selectedPlace.RecTime ? selectedPlace.RecTime : ""}
									</div>
								</div>
								<div className='col-12'>
									<div className='map_infoWindow--label col-6 d-inline-block'>
										{/* 內阻[UΩ] */}
										<SetIMPType IMPType={IMPType} />
									</div>
									<div className='col-6 d-inline-block'>
										{/* 類型:{selectedPlace.IMPType ? checkedIMPType(selectedPlace.IMPType).toString() : "" }<br/> */}
										Max/Min {' '}
										{selectedPlace.MaxIMP ? selectedPlace.MaxIMP : "0"}/{selectedPlace.MinIMP ? selectedPlace.MinIMP : "0"}
									</div>
								</div>
								<div className='col-12'>
									<div className='map_infoWindow--label col-6 d-inline-block'>
										{/* 電壓[V] */}
										<Translation>
											{(t) => <>{t('1017')}</>}
										</Translation>
									</div>
									<div className='col-6 d-inline-block'>
										Max/Min {' '}
										{selectedPlace.MaxVol ? selectedPlace.MaxVol : "0"}/{selectedPlace.MinVol ? selectedPlace.MinVol : "0"}
									</div>
								</div>
								<div className='col-12'>
									<div className='map_infoWindow--label col-6 d-inline-block'>
										{/* 溫度[℃] */}
										<Translation>
											{(t) => <>{t('1018')}</>}
										</Translation>
									</div>
									<div className='col-6 d-inline-block'>
										Max/Min {' '}
										{selectedPlace.MaxTemperature ? selectedPlace.MaxTemperature : "0"}/{selectedPlace.MinTemperature ? selectedPlace.MinTemperature : "0"}
									</div>
								</div>
								<div className='col-12'>
									<div className='map_infoWindow--label col-6 d-inline-block'>
										{/* 電池狀態 */}
										<Translation>
											{(t) => <>{t('1021')}</>}
										</Translation>
									</div>
									<div className='col-6 d-inline-block' name={selectedPlace.StatusCode}>
										{selectedPlace.StatusCode ? <StatusDesc StatusCode={selectedPlace.StatusCode} /> : ''}
									</div>
								</div>

								<div className='col-12 text-right mt-3'>
										<CusMainBtnStyle name={
											<Translation>
												{(t) => <>{t('1202')}</>}
											</Translation>
										}
											icon=""
											clickEvent={()=>this.redirectURLBattData({ groupId: selectedPlace.GroupInternalID, groupLabel: selectedPlace.GroupLabel })}
										/>

								</div>
							</div>
						</div>
					</InfoWindow>
				)}
			</>

		);

	}
	//轉址電池數據第二層
	redirectURLBattData = async(data) => {
		const { groupId, groupLabel } = data;
		const GroupId = {
			isOpen: true,
			isChecked: false,
			isDataList: [groupId],
			isButtonList: [{ Value: groupId, Label: groupLabel }],
		}
		const linkUrl = window.location.href;

		// 僅按enter鍵有功能
		// console.log('%cwindow','background:yellow',this)
		await this.props.updateGroupId(GroupId);
		await this.props.resetCompany();
		await this.props.updateCompany(this.props.isFilterCompanyData);
		// console.log(linkUrl)
		window.location.href = `${linkUrl}BattData`;
	}
}

const mapStateToProps = (state, ownProps) => {
	return {
		token: state.LoginReducer.token,
		account: state.LoginReducer.account,
		username: state.LoginReducer.username,
		role: state.LoginReducer.role,
		company: state.LoginReducer.company,
		curLanguage: state.LoginReducer.curLanguage,                 //目前語系
		timeZone: state.LoginReducer.timeZone,
		functionList: state.LoginReducer.functionList,
		// perPage: state.LoginReducer.perPage,
		// data: state.LoginReducer.data,
		isFilterCompanyData: state.HomeFilterReducer.isFilterCompanyData,
	}
}
const mapDispatchToProps = (dispatch, ownProps) => {
	return {
		setGroupID: (value) => dispatch(setGroupID(value)),
		//篩選條件
		updateGroupId: (object) => dispatch(updateGroupId(object)),
		updateCompany: (object) => dispatch(updateCompany(object)),
		resetCompany: () => dispatch(resetCompany()),
	}
}
export default connect(mapStateToProps, mapDispatchToProps)(Map);
