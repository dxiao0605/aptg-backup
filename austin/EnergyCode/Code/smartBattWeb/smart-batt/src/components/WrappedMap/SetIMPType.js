import React,{Fragment} from "react";
import PropTypes from 'prop-types';
import { useTranslation } from "react-i18next";

const SetIMPType = ({ IMPType }) => {
	const { t } = useTranslation();
	return (
		<Fragment>
		{
			(
				()=>{
					if(IMPType === 20){
						return <>{t('20')}</>
					}
					else if(IMPType === 21){
						return <>{t('21')}</>
					}
					else if(IMPType === 22) {
						return <>{t('22')}</>
					}
					else{
						return <>{t('20')}</>
					}
				}
			)()
		}
		</Fragment>
	)
};
SetIMPType.defaultProps = {
	IMPType: 0,
};
SetIMPType.propTypes = {
	IMPTypes: PropTypes.number,
}
export default SetIMPType;
