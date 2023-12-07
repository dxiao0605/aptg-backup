import React from 'react';


const Footer = () => {
    const Footerstyle = {
        position: 'fixed',
        left: 0,
        bottom: 0,
        width: '100%',
        height: '34px',
        background: '#dfdfdf',
        color: '#6d6d6d',
        fontSize: '12px',
        textAlign: 'right'
    }
    return (
        <div className="p-2" style={Footerstyle}>
            Copyright © 2020 FREESTYLE (1.0.0)
        </div>
    )
}
export default Footer;