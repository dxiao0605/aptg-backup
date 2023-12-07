export const checkFunctionList = (list, currentHash) => {
	let check = false;
    const hashList = list.filter(fitlerItem => fitlerItem.Url !== '').map(element => element.Url);
	
    if(hashList && hashList.length > 0) {

        if(hashList.indexOf(currentHash) > -1) {
            check = true
        }else if  (currentHash === '/') {
            check = true
        }else if  (currentHash === '/login') {
            check = true
        }else if  (currentHash === '/BattData') {
            check = true
        }else if (currentHash === '/BattHistory') {
            check = true
        }else if  (currentHash === '/PrivacyPolicy') {
            check = true
        }
    }
    
	return check;
};
