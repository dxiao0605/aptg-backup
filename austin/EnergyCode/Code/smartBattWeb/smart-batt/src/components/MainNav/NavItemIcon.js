import React, { Fragment, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import classNames from 'classnames';

const NavItemIcon = ({ item, hasSub, hasAlert, alertTotal, setCurrentIdx }) => {
	const [loaded,setLoaded] = useState(false);
	const isLoading = classNames({
		'loading': loaded === false
	})

	// DidMount
	useEffect( ()=> {
		setLoaded(true);
	},[])



	return (
		<Fragment>
			{(() => {
				switch (item.Type) {
					case "text":
						return (
							<dt className={`mainNavIcon_item disabled`}>
								<div className={`icon${item.FunctionId} ${isLoading}`} />
							</dt>
						);
					case "fold":
						return (
							<>
								{/* 判斷是否有子選單欄 */}
								<dt
									className={`mainNavIcon_item ${hasSub.length > 0 ? "sub" : ""} ${item.active ? "open" : ""}`} onClick={() => {
										setCurrentIdx(item.FunctionId);
									}}>
									{/* {console.log('hasSub',hasSub[0].Url)} */}
									<Link to={`${hasSub[0].Url}`}>
										<div className={`icon${item.FunctionId} ${isLoading}`} />
										{/* <div className={item.IconPath} /> */}
										{/* 是否顯示告警提示&&有告警時顯示筆數  */}
										{item.FunctionId === 1300 && (
											<div className={hasAlert ? "prompt" : "d-none"}>
												{alertTotal}
											</div>
										)}
									</Link>
								</dt>
							</>
						);
					case "url":
						return (
							<>
								<dt
									className={`mainNavIcon_item ${item.active ? "active" : ""
										}`}
									onClick={() => {
										setCurrentIdx(item.FunctionId);
									}}>
									<Link to={item.Url}>										
										<div className={`icon${item.FunctionId} ${isLoading}`} />
									</Link>
								</dt>
							</>
						);
					default:
						return (
							<>
								<dt
									className={`mainNav_item ${item.active ? "active" : ""
										}`}
									onClick={() => { setCurrentIdx(item.FunctionId); }}>
									<Link to={item.Url}>
										<div className={item.IconPath} />
									</Link>
								</dt>
							</>
						);
				}
			})()}
		</Fragment>
	)
}

export default NavItemIcon;