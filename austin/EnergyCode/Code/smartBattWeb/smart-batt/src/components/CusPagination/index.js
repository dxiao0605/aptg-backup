import React from "react";
import PropTypes from 'prop-types';
import Pagination from "react-js-pagination";
/* i18n Functional Components */
import { useTranslation } from "react-i18next";

const CusPagination = ({active,row,data,setAcitve}) => {
    const { t } = useTranslation();
	return (
		<div className='col-12 text-center p-0'>
			<Pagination
				prevPageText={t("1044")} //上一頁
				nextPageText={t("1045")} //下一頁
				firstPageText={t("1046")} //首頁
				lastPageText={t("1047")} //末頁
				activePage={active}
				itemsCountPerPage={row}
				totalItemsCount={data.length}
				onChange={setAcitve}
			/>
		</div>
	);
};
CusPagination.propTypes = {
    data: PropTypes.array,
    setAcitve: PropTypes.func,
}
export default CusPagination;
