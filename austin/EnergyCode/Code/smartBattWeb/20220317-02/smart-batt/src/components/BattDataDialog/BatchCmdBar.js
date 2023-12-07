import React from "react";
import { makeStyles, withStyles } from '@material-ui/core/styles';
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import InputBase from '@material-ui/core/InputBase';
import { useTranslation } from 'react-i18next'


// 自訂義select list
const CusSelectInput = withStyles((theme) => ({
	root: {
	  'label + &': {
		marginTop: theme.spacing(3),
	  },
	},
	input: {
	  borderRadius: 4,
	  position: 'relative',
	  paddingLeft: '0.5rem',
	  backgroundColor: theme.palette.background.paper,
	  transition: theme.transitions.create(['border-color', 'box-shadow']),
	  '&:focus': {
		  color: '#ffffff',
	  },
	},
  }))(InputBase);

const useStyles = makeStyles((theme) => ({
	formControl: {
	  margin: theme.spacing(0),
	  marginLeft: theme.spacing(1),
	  minWidth: 120,
	  color: '#ffffff',
	},
	selectEmpty: {
	  marginTop: theme.spacing(0),
	},
}));



const BatchCmdBar = ({layer,selected,list,getSelectOpen,checkedItem}) => {
	const classes = useStyles();
    const { t } = useTranslation();

	
	// 判斷selectOpen是否變更(變更從父層傳回值)
	const handleChange = (event) => {
		getSelectOpen(event.target.value)
	};




	return (
		<div className={`page_multiSelectOperateBar ${selected !== '' ? 'open' : ''}`}>
			<div className="d-inline-block" style={{verticalAlign: 'sub'}}>
				({t('1022')}{list.length}{t('1023')})
				{t('1504')}
			</div>
		
			{//當電池數據第二層,已選清單為多選,已選清單內無為零BatteryId時,顯示錯誤訊息
				layer === 2 && list.length > 1 && checkedItem === 0 ? <div className="d-inline-block ml-2 p-0" style={{verticalAlign: 'sub'}}>{t('1418')}</div>
				: (
					<FormControl className={classes.formControl}>
						<Select
							value={selected}
							displayEmpty
							onChange={handleChange}
							input={<CusSelectInput />}>
							<MenuItem value=''>{t('1413')}</MenuItem>
							{/* 電池數據(1) */}
							{layer === 1 && <MenuItem value='186'>{t('186')}</MenuItem>}
							{layer === 1 && <MenuItem value='187'>{t('187')}</MenuItem>}
							{/* 電池數據(2)校正內阻,僅單選時顯示,checkedItem為已選擇清單中為零的總數 */}
							{layer === 2 && list.length === 1 && <MenuItem value='179'>{t('179')}</MenuItem>}
							{/* 電池數據(2)校正電壓,僅單選時顯示,checkedItem為已選擇清單中為零的總數 */}
							{layer === 2 && list.length === 1 && <MenuItem value='181'>{t('181')}</MenuItem>}
							{/* 電池數據(2)BA,checkedItem為已選擇清單中為零的總數  */}
							{layer === 2 && checkedItem > 0 && <MenuItem value='186'>{t('186')}</MenuItem>}
							{/* 電池數據(2)BB,checkedItem為已選擇清單中不為零的總數  */}
							{layer === 2 && checkedItem > 0 && <MenuItem value='187'>{t('187')}</MenuItem>}
						</Select>
					</FormControl>
				)
			}
		</div>
	);
};
BatchCmdBar.defaultProps = {
	selected: '',
	list: [],					//己選擇清單
	getSelectOpen: () => {},
	checkedItem: 0,
}
export default BatchCmdBar;
