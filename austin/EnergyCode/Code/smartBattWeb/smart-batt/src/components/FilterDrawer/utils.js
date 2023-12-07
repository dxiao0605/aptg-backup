import { ajax } from '../../utils/ajax'

//整理checkbox狀態
export const checkObject = (object) => {
    const { isDataList, isChecked } = object;
    return {
        ...object,
        isChecked: isChecked === false && isDataList.length > 0 ? false : true
    }
}

// 連動清單
export const getSelectObj = (selectList) => {
    let isSelectObject = {};
    selectList.forEach((item) => {
        isSelectObject[item.Value] = item;
    });
    return isSelectObject;
};


//儲存篩選API(POST):
export const ajaxSaveFilter = ({query,postData}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `saveFilter`;
    return ajax(url, 'POST', token, curLanguage, timeZone, company, postData)
    //儲存篩選API(POST):
    //https://www.gtething.tw/battery/saveFilter
}

//取得篩選儲存清單API(GET)
export const ajaxGetFilter = ({query}) => {
	const { token, curLanguage, timeZone, company, account, functionId  } = query;
	const url = `getFilter?account=${account}&functionId=${functionId}`;
	return ajax(url, "GET", token, curLanguage, timeZone, company);
	//取得篩選儲存清單API(GET)
	//https://www.gtething.tw/battery/getFilter?account=admin&functionId=1400_1
};

//刪除篩選儲存清單API(POST):
export const ajaxDelFilter = ({query,postData}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `delFilter`;
	return ajax(url, "POST", token, curLanguage, timeZone, company, postData);
	//刪除篩選儲存清單API(POST):
	//https://www.gtething.tw/battery/delFilter
};



// 已解決告警
//取得已解決告警選清單API(GET):
export const ajaxGetAlertFilter = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getAlertFilter?eventStatus=6`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //取得參數設定篩選清單API(GET):
    //https://www.gtething.tw/battery/getAlertFilter
}
// 未解決告警
//取得電池數據篩選清單API(GET):
export const ajaxGetUnSolvedAlertFilter = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getAlertFilter?eventStatus=5`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //取得參數設定篩選清單API(GET):
    //https://www.gtething.tw/battery/getAlertFilter
}

// 電池數據(1)(2)
//取得電池數據篩選清單API(GET):
export const ajaxGetBatteryFilter = ({query}) => {
	const { token, curLanguage, timeZone, company } = query;
	const url = `getBatteryFilter`;
	return ajax(url, "GET", token, curLanguage, timeZone, company);
	//取得參數設定篩選清單API(GET):
	//https://www.gtething.tw/battery/getBatteryFilter
};
// 電池歷史(1)篩選清單API(GET):
//取得電池歷史(1)篩選清單API(GET):
export const ajaxGetBatteryHistoryFilter = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getHistoryFilter`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //取得參數設定篩選清單API(GET):
    //https://www.gtething.tw/battery/getBatteryFilter
}

//取得電池歷史(2)篩選清單API(GET):
export const ajaxGetBatteryDetailFilter = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getHistoryFilter`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //取得參數設定篩選清單API(GET):
    //https://www.gtething.tw/battery/getBatteryFilter
}

//電池組管理篩選清單API:
export const ajaxGetBattManageFilter = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getBattManageFilter`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //電池組管理篩選清單API:
    //https://www.gtething.tw/battery/getBattManageFilter
}

//電池型號管理篩選清單API:
export const ajaxGetBattTypeFilter = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getBattTypeFilter`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //電池型號管理篩選清單API:
    //https://www.gtething.tw/battery/getBattTypeFilter
}

//取得參數設定篩選清單API(GET):
export const ajaxGetCommandFilter = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getCommandFilter`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //取得參數設定篩選清單API(GET):
    //https://www.gtething.tw/battery/getCommandFilter
}

//站台管理篩選清單API(GET):
export const ajaxGetGroupManageFilter = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getGroupManageFilter`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //取得站台管理篩選清單API(GET):
    //https://www.gtething.tw/battery/getGroupManageFilter
}

 //取得電池數據篩選清單API(GET):
export const ajaxGetHomeFilter = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getBatteryFilter`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //取得參數設定篩選清單API(GET):
    //https://www.gtething.tw/battery/getBatteryFilter
}

//取得通訊模組異動紀錄篩選(GET):
export const ajaxGetNBHistoryFilter = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getNBHistoryFilter`;
    return ajax(url, 'GET', token, curLanguage, timeZone, company)
    //取得通訊模組異動紀錄篩選(GET):
    //https://www.gtething.tw/battery/getNBHistoryFilter
}

//取得通訊模組異動紀錄篩選(GET):
export const ajaxGetNBGroupHisFilter = ({query}) => {
    const { token, curLanguage, timeZone, company } = query;
    const url = `getNBGroupHisFilter`;
    return ajax(url, "GET", token, curLanguage, timeZone, company);
    //取得通訊模組異動紀錄篩選(GET):
    //https://www.gtething.tw/battery/getNBGroupHisFilter
};