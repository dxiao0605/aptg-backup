import React from "react";
import { Link } from 'react-router-dom'
import { useTranslation } from 'react-i18next';
import PropTypes from 'prop-types';
import Header from '../Header';


const PageTabHeader = ({list,extraStyle}) => {
    const { t } = useTranslation();
	return (
		<div className='page_headerTab'>
			<div className={`page_headerTab-group ${extraStyle}`}>
			{
				list ? (
					list.map( (item,idx) => {
						return (
							<div className={`page_headerTab-title ${item.Active ? "active" : ""}`} key={idx}>
								<Link to={item.Url}>
									{/* <Link to={item.Url} onClick={()=>{setGroupID(item.GroupInternalID)}}> */}
									<div>{t(item.Name)}</div>
								</Link>
							</div>
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
PageTabHeader.defaultProps = {
	extraStyle: '',
    list: [{
		Name:'',
		Url:'',
		Active:true,
	}],
}
PageTabHeader.propTypes = {
	extraStyle: PropTypes.string,
    list: PropTypes.array,
}
export default PageTabHeader;
