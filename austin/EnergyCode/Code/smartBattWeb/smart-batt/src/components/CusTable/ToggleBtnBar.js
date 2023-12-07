import React, { Fragment } from "react";
import PropTypes from 'prop-types';
import { useTranslation } from "react-i18next";

const ToggleBtnBar = ({ company, list, onClickEvent, IMPType }) => {
	const { t } = useTranslation();
	return (
		<div className='toggleBtnBar'>
			<label>{t('1072')}：</label>
			{!list
				? ""
				: list.map((item) => {
					return (
						<Fragment key={item.id}>
							{
								(
									() => {
										if (IMPType > 0 && (item.id === '20' || item.id === '21' || item.id === '22' || item.id === '1402' || item.id === '1403' || item.id === '1408' || item.id === '1409' || item.id === '1410' || item.id === '1411')) {
											if (IMPType.toString() === item.id) {
												return (
													<div
														key={item.id}
														className={`toggleBtnBar_item ${item.active ? "active" : ""} ${item.fixed ? "fixed" : ""
															}`}
														onClick={() => { onClickEvent(item.id) }}>
														{t(item.id)}
													</div>
												)
											} else if (IMPType === 20 && (item.id === '1402' || item.id === '1403')) {
												return (
													<div
														key={item.id}
														className={`toggleBtnBar_item ${item.active ? "active" : ""} ${item.fixed ? "fixed" : ""
															}`}
														onClick={() => { onClickEvent(item.id) }}>
														{t(item.id)}
													</div>
												)
											}
											else if (IMPType === 21 && (item.id === '1408' || item.id === '1409')) {
												return (
													<div
														key={item.id}
														className={`toggleBtnBar_item ${item.active ? "active" : ""} ${item.fixed ? "fixed" : ""
															}`}
														onClick={() => { onClickEvent(item.id) }}>
														{t(item.id)}
													</div>
												)
											}
											else if (IMPType === 22 && (item.id === '1410' || item.id === '1411')) {
												return (
													<div
														key={item.id}
														className={`toggleBtnBar_item ${item.active ? "active" : ""} ${item.fixed ? "fixed" : ""
															}`}
														onClick={() => { onClickEvent(item.id) }}>
														{t(item.id)}
													</div>
												)
											}
										}
										else {
											if (company !== "1" && item.id === '1064') { return; }
											return (
												<div
													key={item.id}
													className={`toggleBtnBar_item ${item.active ? "active" : ""} ${item.fixed ? "fixed" : ""
														}`}
													onClick={() => { onClickEvent(item.id) }}>
													{t(item.id)}
												</div>
											)
										}
									}
								)()
							}
						</Fragment>
					);
				})}
		</div>
	);
};
ToggleBtnBar.defaultProps = {
	company: "0",	//判斷是否顯示公司欄位
	list: [],
	IMPType: 0,
	onClickEvent: () => { },
};
ToggleBtnBar.propTypes = {
	company: PropTypes.string,
	list: PropTypes.array,
	IMPType: PropTypes.number,
	onClickEvent: PropTypes.func,
};
export default ToggleBtnBar;
