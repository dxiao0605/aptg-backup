import { apipath } from "./ajax";
/* Export File */
export const exportExcel = async (token, language, timezone, company, exportURL, excelName, postData, resolveFunc, method) => {
    const apiURL = apipath();
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
    await fetch(`${apiURL}${exportURL}`, header)
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
            resolveFunc();
        })
        .catch((e) => {
            //handle error and timeout error
            console.error(e)
        })

}
exportExcel.defaultProps = {
    company: '',
    method: 'POST',
    resolveFunc: () => {},
}