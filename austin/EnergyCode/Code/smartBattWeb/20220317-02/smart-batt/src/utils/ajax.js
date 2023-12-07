// location path
export const locpath = () => {
    const loc = window.location.href;
    return loc
}

// api path
export const apipath = () => {
    const loc = window.location.origin;

    if(loc.indexOf('localhost') >= 0) {
        // 開發用localhost :3000
        return 'https://www.gtething.tw/battery/'
    }
    else {
        // 正式 'https://www.rceibox.com/battery/'
        return `${loc}/battery/`
    }
}


// AJAX
export const ajax = async(url,protocol,token,language,timezone,company,postData) => {
    const controller = new AbortController();
    setTimeout(() => {
        controller.abort();
    }, 5000)
    try{
        let data = await fetch(url,{
            method: protocol,
            headers: new Headers({
                'Accept': '*/*',
                'Content-Type': 'application/json',
                'token': token,
                'language':language,
                'timezone':timezone,
                'company': company,
            }),
            body: JSON.stringify(postData),
            signal:controller.singal
        })
        .catch((error) => {
            console.error(error)
            return {}
        })
        .then(response => {
            if(response.status === 200) {
                return response.json();
            } else if (response.status < 400 && response.status >= 300) {
                console.error(response.status,'重新定向 (轉址)')
                return {}
            } else if (response.status < 500 && response.status >= 400) {
                console.error(response.status,'客戶端 錯誤 (無法完成請求)')
                return {}
            } else if (response.status < 600 && response.status >= 500) {
                console.error(response.status,'伺服器端 錯誤')
                return {}
            }  
            else {
                return {}
            }
        })
        return data;
    } catch (error) {
        console.error('error:',error);
        return {}
    }
}
ajax.defaultProps = {
    company: '',
    postData: ''
}
