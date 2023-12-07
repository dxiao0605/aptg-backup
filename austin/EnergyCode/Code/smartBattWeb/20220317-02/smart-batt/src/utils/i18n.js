import i18n from 'i18next';
import { initReactI18next  } from 'react-i18next';

import Backend from 'i18next-http-backend';
import LanguageDetector from 'i18next-browser-languagedetector';
// don't want to use this?
// have a look at the Quick start guide 
// for passing in lng and translations on init

// i18n - 引入locale配置文件，具體路徑根據實際情況填寫
import zh from '../i18n/zh-TW.json';
import en from '../i18n/en.json';
import ja from '../i18n/ja.json';

// 語系文字檔
const resources ={
    zh: {
        translation: zh,
    },
    en: {
        translation: en,
    },
    ja: {
        translation: ja
    }
}
// 瀏覽器預設語系
// const locale = navigator.language;
const locale = navigator.language.split(/[-_]/)[0];

i18n
// load translation using http -> see /public/locales (i.e. https://github.com/i18next/react-i18next/tree/master/example/react/public/locales)
// learn more: https://github.com/i18next/i18next-http-backend
.use(Backend)
// detect user language
// learn more: https://github.com/i18next/i18next-browser-languageDetector
.use(LanguageDetector)
// pass the i18n instance to react-i18next.
.use(initReactI18next)
// init i18next
// for all options read: https://www.i18next.com/overview/configuration-options
    .init({
        resources,
        lng: locale,  //預設語言
        fallbackLng: 'en',   //如果當前切換的語言沒有對應的翻譯則使用這個語言，
        react: {
            useSuspense: false
        },
        debug: true,
        keySeparator: false, // we do not use keys in form messages.welcome
        interpolation: {
            /* 如果語系檔中有巢狀的結構，
            * 則 escapeValue 要設為 false，
            * 這樣就可以透過物件的方式來取得巢狀內的翻譯
            */
            escapeValue: false, // react already safes from xss

        },
    },(err, t) => {
        if (err) return console.log('something went wrong loading', err);
        t('key'); // -> same as i18next.t
    })

export default i18n;
