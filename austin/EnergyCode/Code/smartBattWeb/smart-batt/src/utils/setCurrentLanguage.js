export const setCurrentLanguage = (language) => {
    // console.log('checkLanguage',language)
    let lang;
    switch(language) {
        case '1':
            lang = 'zh';
            break;
        case '2':
            lang = 'en';
            break;
        case '3':
            lang = 'ja';
            break;
        default:
            lang = 'en';
    }
    return lang
}