import React,{useState} from 'react';
import classNames from 'classnames';

const CusAccordion = ({title,hasExportIcon,handleEcxecl,loading,children,open}) => {
    const [isOpen,setOpen] = useState(open);

    // style
    const classChecked = classNames({
        'unUse': loading === true,
    })
    
    return (
        <dl>
            <dt className={`accordion--title ${isOpen ? '': 'open'}`}>
                <div className="accordion--btn" onClick={()=>{setOpen(!isOpen)}}>
                    <span className="d-inline-block col-6">{title} </span>
                    <span className="d-inline-block col-6 text-right">
                        <i className={`pl-2 fas ${isOpen ? 'fa-chevron-down':'fa-chevron-up'}`} />
                    </span>
                </div>
                
                {
                    hasExportIcon === true &&
                    <div className="exportTools_group">
                        <div className={`icon EcxeclIcon ${classChecked}`} onClick={handleEcxecl}></div>
                    </div>
                }
            </dt>
            <dd className={`accordion--item ${isOpen ? '': 'collapsed'}`}>
                <div className="accordion--content">
                    {
                        isOpen === true ? children : ''
                    }
                </div>
            </dd>
        </dl>
    )
}
CusAccordion.defaultProps = {
    open: true,
    hasExportIcon: false,
    handleRefresh: () =>{},
    handlePrint: () =>{},
    handlePDF: () =>{},
    handleEcxecl: () =>{},
}
export default CusAccordion;