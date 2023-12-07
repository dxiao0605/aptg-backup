import React from "react";
import Header from '../Header';
import { Link } from 'react-router-dom'
/* i18n Functional Components */
import { useTranslation } from 'react-i18next'


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
export default PageTabHeader;
