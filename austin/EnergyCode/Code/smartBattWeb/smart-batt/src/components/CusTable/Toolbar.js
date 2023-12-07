import React from "react";
import PropTypes from 'prop-types';
import { useTranslation } from 'react-i18next'
// images
import AddIcon from '../../image/plus.png';
import DeleteIcon from '../../image/minus.png';
import ImportIcon from '../../image/inbox incoming.png';
import ExportIcon from '../../image/inbox outgoing.png';

const ToolBar = ({
	isAddItem,
	isDeleteItem,
	isInportExcel,
	isExportExcel,
	isRefresh,
	addEvent,
	deleteEvent,
	importExcel,
	exportExcel,
	refreshEvent
}) => {
	const { t } = useTranslation();
	return (
		<div className="toolbar">
			{isAddItem ? (
				<div className='toolbar_item' onClick={addEvent}>
					<img src={AddIcon} alt='add' />
					{t('1051')} {/* 新增 */}
				</div>
			) : (
					""
				)}
			{isDeleteItem ? (
				<div className='toolbar_item' onClick={deleteEvent}>
					<img src={DeleteIcon} alt='delete' />
					{t('1052')} {/* 刪除 */}
				</div>
			) : (
					""
				)}
			{isInportExcel ? (
				<div className='toolbar_item' onClick={importExcel}>
					<img src={ImportIcon} alt='inport' />
					{t('1054')} {/* 匯入 */}
				</div>
			) : (
					""
				)}
			{isExportExcel ? (
				<div className='toolbar_item' onClick={exportExcel}>
					<img src={ExportIcon} alt='export' />
					{t('1053')} {/* 匯出 */}
				</div>
			) : (
					""
				)}
			{isRefresh ? (
				<div className='toolbar_item' onClick={refreshEvent}>
					<i className="fas fa-redo-alt mr-1" style={{ color: 'rgba(51, 51, 51,0.8)' }} />
					{t('1056')} {/* 更新顯示 */}
				</div>
			) : (
					""
				)}
		</div>
	);
};
ToolBar.defaultProps = {
	isAddItem: false, //是否顯示新增欄位
	isDeleteItem: false, //是否顯示刪除欄位
	isInportExcel: false, //是否顯示匯入Excel
	isExportExcel: false, //是否顯示輸出Excel
	isRefresh: false,//是否顯示更新顯示欄位
	addEvent: () => { }, //新增事件
	deleteEvent: () => { }, //刪除事件
	inportExcel: () => { }, //匯入Excel事件
	exportExcel: () => { }, //輸出Excel事件
	refreshEvent: () => { },//更新顯示事件
};
ToolBar.propTypes = {
	isAddItem: PropTypes.bool,
	isDeleteItem: PropTypes.bool,
	isInportExcel: PropTypes.bool,
	isExportExcel: PropTypes.bool,
	isRefresh: PropTypes.bool,
	addEvent: PropTypes.func,
	deleteEvent: PropTypes.func,
	inportExcel: PropTypes.func,
	exportExcel: PropTypes.func,
	refreshEvent: PropTypes.func,
};
export default ToolBar;
