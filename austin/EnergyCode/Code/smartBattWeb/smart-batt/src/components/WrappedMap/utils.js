// customer marker style
import markerGroup from "../../image/lcation.png";
import markerGreen from "../../image/Location_normal.png";
import markerYellow from "../../image/Location_alarm.png";
import markerBlue from "../../image/Location_Offline.png";
import markerRed from "../../image/Location_Need to be replaced.png";


// 群組樣式
export const groupIcon = {
    url: markerGroup ,
    scaledSize: { width: 50, height: 50 },
}


// 判斷MarkerIcon樣式
export const checkedMarkerType = (status) => {
    switch(status){
        case 1:
            return markerGreen
        case 2:
            return markerYellow
        case 3:
            return markerRed
        case 4:
            return markerBlue
        default:
            return markerGreen
    }
}



// 檢查是否顯示Marker
export const checkedMarkerVisible = (isVisible,type) => {
    // console.log('isVisible,type',isVisible,type)
    if(isVisible === true){
        if(type === 'Battery') return true
        return false
    }else if ( isVisible === false){
        if(type === 'Group') return true
        return false
    }
    
}

// 檢查內阻類型
export const checkedIMPType = (value) => {
    let type = '';
    switch(value) {
            case "0":
                type = '內阻值';
                break;
            case "1":
                type = '電導值';
                break;
            case "2":
                type = '毫內阻';
                break;
            default:
                type = '';
    }
    return type
}