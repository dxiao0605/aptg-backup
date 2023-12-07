// 重組資料
export const getNewArray = (data) => {
    const newData = [...data];      // 要搜查資料
    const resultData = [];          // 搜查結果
    newData.map((item) => {
        // 重組資料
        // 移除內部為array的資料，僅搜尋字串、數字
        const newArr = Object.values(item).filter(filterItem => filterItem instanceof Array === false);
        // 將array的資料展開
        const arrayItem = Object.values(item).filter(filterItem => filterItem instanceof Array === true);
        arrayItem.length > 0 && (
            arrayItem.map(item => {
                item.map(element => {
                    return newArr.push(element)
                })
                return ''
            })
        )
        return resultData.push(newArr)
    })
    return resultData
}
//for index 關鍵字搜尋
export const filterArray = ({ dataArray, searchText, allowArray }) => {
    if (searchText !== undefined && searchText !== "" && typeof searchText === "string") {
        const data = Array.isArray(dataArray) ? dataArray : [];
        const resultArray = [];
        data.forEach((item, idx) => {
            let newItem = {};
            //過濾陣列
            Array.isArray(allowArray) && allowArray.length > 0 && allowArray.forEach(name => {
                if (item[name] !== undefined) {
                    newItem[name] = item[name];
                }
            });
            const boolean = !chkArray({ data: newItem, searchText: searchText });
            if (boolean) {
                resultArray.push(data[idx]);
            }
        });
        return resultArray;
    } else {
        return dataArray;
    }
}

//for select 搜尋選單使用
export const filterSelectArray = ({ dataArray, searchText }) => {
    if (searchText !== undefined && searchText !== "" && typeof searchText === "string") {
        const data = Array.isArray(dataArray) ? dataArray : [];
        const resultArray = data.map((item) => {
            const filterItem = item?.Label ? item.Label : '';
            const boolean = !chkArray({ data: filterItem, searchText: searchText });
            return { ...item, selectShow: boolean };
        });
        return resultArray;
    } else {
        return dataArray.map(item => ({ ...item, selectShow: true }));
    }
}

const chkArray = ({ data, searchText }) => {
    if (data instanceof Array) {
        let result = true;
        data.length > 0 && data.forEach(item => {
            if (result) {
                const bool = chkArray({ data: item, searchText });
                result = result * bool;
            }
        });
        return result
    } else if (data instanceof Object) {
        let result = true;
        const newArr = Object.values(data);
        result = result * chkArray({ data: newArr, searchText });
        return result
    } else {
        if (typeof data === "string" && data.indexOf(searchText) >= 0) {
            return false
        } else if (typeof data === "number" && data.toString().indexOf(searchText) >= 0) {
            return false
        } else {
            return true
        }
    }
}


//for RoleId to Users page use 
export const filterArray_LinkToUsers = ({ dataArray, searchText, allowArray }) => {
    if (searchText !== undefined && searchText !== "" && typeof searchText === "string") {
        const data = Array.isArray(dataArray) ? dataArray : [];
        const resultArray = [];
        data.forEach((item, idx) => {
            let newItem = {};
            //過濾陣列
            Array.isArray(allowArray) && allowArray.length > 0 && allowArray.forEach(name => {
                if (item[name] !== undefined) {
                    newItem[name] = item[name];
                }
            });
            const boolean = !chkArray_LinkToUsers({ data: newItem, searchText: searchText });
            if (boolean) {
                resultArray.push(data[idx]);
            }
        });
        return resultArray;
    } else {
        return dataArray;
    }
}
//for RoleId to Users page use
const chkArray_LinkToUsers = ({ data, searchText }) => {
    if (data instanceof Array) {
        let result = true;
        data.length > 0 && data.forEach(item => {
            if (result) {
                const bool = chkArray_LinkToUsers({ data: item, searchText });
                result = result * bool;
            }
        });
        return result
    } else if (data instanceof Object) {
        let result = true;
        const newArr = Object.values(data);
        result = result * chkArray_LinkToUsers({ data: newArr, searchText });
        return result
    } else {
        if (typeof data === "string" && data === searchText.toString()) {
            return false
        } else if (typeof data === "number" && data.toString() === searchText.toString()) {
            return false
        } else {
            return true
        }
    }
}