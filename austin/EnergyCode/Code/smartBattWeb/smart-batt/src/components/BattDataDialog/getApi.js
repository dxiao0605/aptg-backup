import { ajax } from "../../utils/ajax";

// fetch站台設定清單
export const fetchGroupSetup = ({query}) => {
    const { token,company,timeZone,language} = query;
    const url = `getGroupSetup?companyCode=${company}`;
    return ajax(url,'GET',token,language,timeZone,company)
}
// fetch 站台編輯 -電池數據-接續通訊序號清單
export const fetchNBList = ({query,GroupInternalId,nbid}) => {
    const { token,company,timeZone,language} = query;
    const url = `getGroupNBList?groupInternalId=${GroupInternalId}&nbid=${nbid}`;
    return ajax(url,"GET",token,language,timeZone,company);
}