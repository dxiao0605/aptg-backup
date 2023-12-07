import React, { useEffect, useState } from "react";
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';
import Header from '../Header';



const PageNavHeader = ({ list, activeNum, onClick, extraStyle }) => {
	const { t } = useTranslation();
	const [active, setActive] = useState(activeNum);
	const changeTab = (idx, item) => {
		setActive(idx);
		onClick(idx, item);
	}
	useEffect(() => {
		if (activeNum !== active) {
			setActive(activeNum);
		}
	}, [activeNum, active])
	return (
		<div className='page_headerTab'>
			<div className={`page_headerTab-group ${extraStyle}`}>
				{
					list ? (
						list.map((item, idx) => {
							return (
								item.isShow &&
								(
									item.openLink ?
										<div
											key={idx}
											className={`page_headerTab-title ${idx === active ? 'active' : ''}`}
											onClick={() => { changeTab(idx, item) }}
										><Link to={item.to}>{t(item.Name)}</Link></div>
										:
										<div
											key={idx}
											className={`page_headerTab-title ${idx === active ? 'active' : ''}`}
											onClick={() => { changeTab(idx, item) }}
										>{t(item.Name)}</div>
								)
							)
						})
					) : ''
				}
			</div>


			<div className={`page_headerTab-group ${extraStyle}`} style={{textAlign:'right', justifyContent: 'flex-end'}}>
				<Header />{/* 變更語系、使用者設定、登出 */}
			</div>
		</div>
	);
};
PageNavHeader.defaultProps = {
	extraStyle: '',
	list: [{
		Name: '',
		isShow: false,
		Active: true,
	}],
	activeNum: 0,
}
PageNavHeader.propTypes = {
	extraStyle: PropTypes.string,
	list: PropTypes.array,
	activeName: PropTypes.number,
}
export default PageNavHeader;
