import React,{Fragment} from 'react';
/* i18n Functional Components */
import { useTranslation } from 'react-i18next'

const CusTHeader = ({tableHeader,IMPType,requestSort}) => {
    const { t } = useTranslation();
    return(
        <>
        {
            (
                tableHeader.map( (item) => {
                    return (
                        <Fragment key={item.id}>
                            {
                                (
                                    ()=>{
                                        // 判斷IMPType名稱(20內阻、21豪電阻、22電壓)
                                        if(item.id === '20' || item.id === '21' || item.id === '22' || item.id === '1402' || item.id === '1403' || item.id === '1408' || item.id === '1409' || item.id === '1410' || item.id === '1411'){
                                            if(item.id === IMPType.toString()) {
                                                return (
                                                    <th key={item.id} onClick={()=>{requestSort(`${item.sortName}`)}} className={item.active ? '': 'hide'}>
                                                        {t(item.id)}
                                                        {item.sortName === '' ? '' : <i className='fas fa-sort'/>}
                                                    </th>
                                                )
                                            }
                                            else if(IMPType === 20 && (item.id === '1402' || item.id === '1403')) {
                                                return (
                                                    <th key={item.id} onClick={()=>{requestSort(`${item.sortName}`)}} className={item.active ? '': 'hide'}>
                                                        {t(item.id)}
                                                        {item.sortName === '' ? '' : <i className='fas fa-sort'/>}
                                                    </th>
                                                )
                                            }
                                            else if(IMPType === 21 && (item.id === '1408' || item.id === '1409')) {
                                                return (
                                                    <th key={item.id} onClick={()=>{requestSort(`${item.sortName}`)}} className={item.active ? '': 'hide'}>
                                                        {t(item.id)}
                                                        {item.sortName === '' ? '' : <i className='fas fa-sort'/>}
                                                    </th>
                                                )
                                            }
                                            else if(IMPType === 22 && (item.id === '1410' || item.id === '1411')) {
                                                return (
                                                    <th key={item.id} onClick={()=>{requestSort(`${item.sortName}`)}} className={item.active ? '': 'hide'}>
                                                        {t(item.id)}
                                                        {item.sortName === '' ? '' : <i className='fas fa-sort'/>}
                                                    </th>
                                                )
                                            }



                                        }else{
                                            return (
                                                <th key={item.id} onClick={()=>{requestSort(`${item.sortName}`)}} className={item.active ? '': 'hide'}>
                                                    {t(item.id)}
                                                    {item.sortName === '' ? '' : <i className='fas fa-sort'/>}
                                                </th>
                                            )
                                        }
                                    }
                                )()
                            }
                        </Fragment>
                    )
                })
            )
        }
        </>
    )

}
CusTHeader.defaultProps ={
    tableHeader: [],
    IMPType: 20,
    requestSort: ()=>{},
}
export default CusTHeader;