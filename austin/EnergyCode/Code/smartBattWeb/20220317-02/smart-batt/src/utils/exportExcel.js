// import fetchWithTimeout from './fetchWithTimeout'

/* Export File */
// 輸出Excel
export const exportExcel = async (token, language, timezone, company, exportURL, excelName, postData, resolveFunc, method , timeoutFunc) => {
    const header = method === "POST" ?
        {
            method: method,
            headers: new Headers({
                'Accept': '*/*',
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
                'token': token,
                'language': language,
                'timezone': timezone,
                'company': company,
            }),
            body: JSON.stringify(postData)
        } :
        {
            method: method,
            headers: new Headers({
                'Accept': '*/*',
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
                'token': token,
                'language': language,
                'timezone': timezone,
                'company': company,
            }),
        };
    // await fetchWithTimeout(exportURL, header , 300000)
    await fetch(exportURL, header)
        .then(response => response.blob())
        .then(response => {
            const url = window.URL.createObjectURL(response);
            // console.log('輸出Excel',response,url,excelName)
            // window.location.href = url;
            // window.URL.revokeObjectURL(response);
            var a = document.createElement("a");
            a.href = url;
            a.setAttribute('download', excelName);
            document.body.appendChild(a);
            a.click();
            document.body.removeChild(a);
            window.URL.revokeObjectURL(response);
            resolveFunc();
        })
        .catch((e) => {
            //handle error and timeout error
            console.error(e)
            timeoutFunc();
        })

}
exportExcel.defaultProps = {
    company: '',
    method: 'POST',
    resolveFunc: () => {},
    timeoutFunc: () => {},
}