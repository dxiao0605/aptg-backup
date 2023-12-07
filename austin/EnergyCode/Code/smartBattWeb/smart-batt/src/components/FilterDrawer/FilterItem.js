import React, { Fragment } from "react";
import { useTranslation } from "react-i18next";



// 篩選核取清單
const FilterItem = ({
	name,   //篩選名稱
	selectName,	//名稱
	list,   //篩選清單
	selectAll,  //全選(預設false)
	isOpen, //是否展開清單(預設不展開)
	promptNum,  //共選取幾項
	errorMsg,   //清單錯誤訊息
	openHandler, //變更展開清單
	changeSelectedListHandler, //變更已選擇清單
	changeSelectAllHandler,   //變更全選
	isKeySearch,    //是否有關鍵字搜尋
}) => {
	const { t } = useTranslation();
	const lists = list;
	// 變更checkbox狀態
	const onCheckboxChange = (checkboxes) => {
		const target = checkboxes.Seq;
		const newItem = {
			Seq: checkboxes.Seq,
			Value: checkboxes.Value,
			Label: checkboxes.Label,
			Checked: !checkboxes.Checked,
		};
		const newList = lists.filter((item) => item !== checkboxes);
		newList.splice(target, 0, newItem);	//將更新的項目放回原來的位置
		changeSelectedListHandler({ name: selectName, list: newList });
	};
	//變更selectAll
	const onClickSelectAll = (e) => {
		const newList = [];
		lists.map((item) => {
			if (e.target.checked === true) {
				return newList.push({
					Seq: item.Seq,
					Value: item.Value,
					Label: item.Label,
					Checked: true,
				});
			} else {
				return newList.push({
					Seq: item.Seq,
					Value: item.Value,
					Label: item.Label,
					Checked: false,
				});
			}
		});
		// 變更全選
		changeSelectAllHandler({ name: e.target.name, value: e.target.checked });
		// 更新核選(checkbox)清單
		changeSelectedListHandler({ name: selectName, list: newList });
		// console.log("selectAll onChange", newList, e.target.checked)
	};




	return (
		<Fragment>
			<div className='filterItem'>
				{t(name)}
				{/* 篩選名稱 */} <span className='filter_prompt'>{promptNum}</span>

				<input
					type='checkbox'
					className='ml-2'
					name={selectName}
					onChange={(e) => {
						onClickSelectAll(e);
					}}
					checked={selectAll}
				/>{" " + t("1076")}
				<span
					className='filter_toggle--btn'
					onClick={() => {
						openHandler(!isOpen);
					}}>
					<i className={`fas ${!isOpen ? "fa-plus" : "fa-minus"} ml-2`} />
				</span>
			</div>
			<ul className={!isOpen ? "d-none" : "d-block"}>
				{/* 關鍵字搜尋 */}
				{isKeySearch ? (
					<div className='align-middle mt-2'>
						<input
							type='text'
							className='align-middle d-inline-block'
							style={{ width: "80%" }}
						/>
						<button className='btn btn-success d-inline-block pr-1 pl-1 pt-0 pb-0 ml-1'>
							<i className='fas fa-search' />
						</button>
					</div>
				) : (
						""
					)}
				{/* 核選(checkbox)清單 */}
				{list
					? list.map((item, idx) => {
						return (
							<li key={idx}>
								<input
									type='checkbox'
									className='mr-2'
									name={selectName}
									value={item.Value}
									checked={item.Checked}
									onChange={() => { onCheckboxChange(item); }}
									id={selectName + idx}
								/>
								<label className='mb-0 w-100' htmlFor={selectName + idx}>{item.Label}</label>
							</li>
						)
					})
					: errorMsg}
			</ul>
		</Fragment>
	);
};
export default FilterItem;
