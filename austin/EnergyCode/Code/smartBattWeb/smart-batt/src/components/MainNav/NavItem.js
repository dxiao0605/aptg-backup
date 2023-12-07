import React, { Fragment } from "react";
import { Link } from 'react-router-dom';
import SubItem from './SubItem';
/* i18n Functional Components */
import { useTranslation } from 'react-i18next'


const NavItem = ({ item, hasSub, hasAlert, alertTotal, setCurrentIdx, setOpenMenu }) => {
	const { t } = useTranslation();
	return (
		<Fragment>
			{(() => {
				switch (item.Type) {
					case "text"://無子選單
						return (
							<dt className={`d-flex mainNav_item padding disabled`}>
								<div className={`icon${item.FunctionId}`} />
								<div>{t(item.FunctionId)}</div>
							</dt>
						);
					case "fold"://有子選單
						return (
							<>
								{/* 判斷是否有子選單欄 */}
								<dt
									className={`d-flex mainNav_item padding ${hasSub.length > 0 ? "sub" : ""} ${item.openMenu ? "open" : (item.active ? "active" : "")}`}
									onClick={() => { setOpenMenu(item.FunctionId); }}
								>
									<div className={`icon${item.FunctionId}`} />
									{/* 是否顯示告警提示&&有告警時顯示筆數  */}
									{item.FunctionId === 1300 && (
										<div className={hasAlert ? "prompt" : "d-none"}>
											{alertTotal}
										</div>
									)}
									<div>{t(item.FunctionId)}</div>
								</dt>

								{/* 子清單列表 */}
								{hasSub.length > 0 && (
									<dd
										className={
											item.openMenu ? "d-block" : "d-none"
										}>
										<SubItem data={hasSub} hasAlert={hasAlert} alertTotal={alertTotal} setCurrentSubIdx={setCurrentIdx} setOpenMenu={setOpenMenu} />
									</dd>
								)}
							</>
						);
					case "url":
						return (
							<>
								<dt
									className={`mainNav_item padding ${item.active ? "active" : ""
										}`}
									onClick={() => {
										setCurrentIdx(item.FunctionId);
									}}>
									<Link className="w-100" to={item.Url}>
										<div className={`icon${item.FunctionId}`} />
										{t(item.FunctionId)}
									</Link>
								</dt>
							</>
						);
					default:
						return (
							<>
								<dt
									className={`mainNav_item padding ${item.active ? "active" : ""
										}`}
									onClick={() => { setCurrentIdx(item.FunctionId); }}>
									<div className={item.IconPath} />
									{t(item.FunctionId)}
								</dt>
							</>
						);
				}
			})()}
		</Fragment>
	);
};
export default NavItem;
