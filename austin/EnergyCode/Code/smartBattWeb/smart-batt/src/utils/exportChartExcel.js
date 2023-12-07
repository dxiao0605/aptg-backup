/* Export File */
export const exportChartExcel = async(token,language,timezone,company,exportURL,excelName) => {
    await fetch(exportURL,{
        method: 'POST',
        headers: new Headers({
            'Accept': '*/*',
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8',
            'token': token,
            'language':language,
            'timezone': timezone,
            'company': company,
        })
    })
    .then(response => response.blob())
    .then(response => {
        const url = window.URL.createObjectURL(response);
        var a = document.createElement("a");
        a.href = url;    
        a.setAttribute('download', excelName);
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(response);
    })
}
exportChartExcel.defaultProps = {
    company: ''
}