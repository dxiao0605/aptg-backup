
// 變更表格內(電壓、內阻...)滑桿展開/隱藏
export const setNewSliderList = (data,keyword) => {
    const newData = [...data];
    // 先找出修改項目位置。刪除資料後加入變更後資料。
    data.forEach((element,idx) => {
        if(element.EventSeq){
            if((element.EventSeq).toString() === keyword){
                element.slider = !element.slider;
                newData[idx] = element;
            }
        }

        if(element.Seq){
            if((element.Seq).toString() === keyword){
                element.slider = !element.slider;
                newData[idx] = element;
            }
        }
    });
    return newData
} 
// 變更各別checkbox狀態
export const setNewCheckboxList = (data,e) => {
    const newData = [...data];
    // 先找出修改項目位置。刪除資料後加入變更後資料。
    data.forEach((element,idx) => {
        if(element.EventSeq){
            if((element.EventSeq).toString() === e.target.name){
                element.checked = e.target.checked;
                newData[idx] = element;
            }
        }else if(element.Seq){
            if((element.Seq).toString() === e.target.name){
                element.checked = e.target.checked;
                newData[idx] = element;
            }
        }
        
    });
    return newData
}
// 變更已選則清單(selectedList)
export const setSelectedList = (data) => {
    return data.filter((filterItem)=> filterItem.checked === true)
}

// 變更表格顯示隱藏欄位
export const setNewTableHeader = (data,id) => {
    const newData = [...data];
    // 先找出修改項目位置。刪除資料後加入變更後資料。
    data.forEach((element,idx) => {
        if((element.id).toString() === id && element.fixed === false){
            element.active = !element.active;
            newData[idx] = element;
        }
    })
    return newData
}

// 變更checkbox全選(data有含有控制slider功能)
export const setSelectAllSliderData = (data,checked,active,perPage) => {
    const newData = [];
    const range_end = active*perPage; //最後一筆
    const range_start = range_end - perPage;    //起始筆數
    // 全選(目前頁面第active頁,每頁共有perPage筆)
    data.forEach(
        (element,idx) => {
            if(idx >= range_start && idx < range_end){
                return newData.push({...element,checked: checked,slider:true})
            }
            else{
                return newData.push({...element,checked: false,slider:true})
            }
        }
    )

    return newData
}
// 變更checkbox全選
export const setSelectAllData = (data,checked,active,perPage) => {
    const newData = [];
    const range_end = active*perPage; //最後一筆
    const range_start = range_end - perPage;    //起始筆數
    // 全選(目前頁面第active頁,每頁共有perPage筆)
    data.forEach(
        (element,idx) => {
            if(idx >= range_start && idx < range_end){
                return newData.push({...element,checked: checked})
            }
            else{
                return newData.push({...element,checked: false})
            }
        }
    )

    return newData
}
