import React from 'react';
import { useTranslation } from 'react-i18next';

const ContentDel = () => {
    const { t } = useTranslation();
    return (
        <>
            {/* 確認要刪除所選擇的電池型號? */}
            <div className="d-inline-block col-xl-6 col-12 p-0 my-2">
                {t("1512")}
            </div>
        </>
    )
}
export default ContentDel;


