import { apipath } from '../../utils/ajax';

export const getForgetPw = ({list}) => {
    const url = `${apipath()}forgetPassword`;
    try{
        const data = fetch(url,{
            method: "POST",
            headers: new Headers({
                'Accept': '*/*',
                'Content-Type': 'application/json',
            }),
            body: JSON.stringify(list)
        }).then(response => {
            if(response.status === 200) {
                return response.json();
            }else {
                return {}
            }
        })
        return data;
    } catch (error) {
        console.error(error);
        return {}
    }
}