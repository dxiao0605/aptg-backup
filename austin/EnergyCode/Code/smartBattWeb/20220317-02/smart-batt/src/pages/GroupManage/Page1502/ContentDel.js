import React from 'react';
import { useTranslation } from 'react-i18next';

const ContentDel = () => {
    const { t } = useTranslation();
    return (
        <>
           <div className="d-inline-block col-xl-10 col-12 p-0 my-2">
                {t("1543")}{/* 確認要刪除所選擇的站台? */}
                <br />
                {t("1544")}{/* 提醒:....(省略不打) */}
            </div>
        </>
    )
}
export default ContentDel;


