import React from "react";
import PropTypes from 'prop-types';
import Header from '../Header';


const PageHeader = ({title}) => {
	return (
		<div className='page_headerTab'>
			<div className='page_headerTab-group singleTitle'>
				<div className="page_headerTab-title">{title}</div>
			</div>

			<div className='page_headerTab-group singleTitle' style={{textAlign:'right', justifyContent: 'flex-end'}}>
				{/* 編輯 */}
				{/* 變更語系、使用者設定、登出 */}
				<Header />
			</div>
		</div>
	);
};
PageHeader.defaultProps = {
    title: '',
    refreshData: () => {}
}
PageHeader.propTypes = {
	refreshData: PropTypes.func,
}
export default PageHeader;
