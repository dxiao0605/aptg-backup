const code187 = {//BB
    IRTestTime: { min: 5, max: 200, length: 3 },//內組測試時間(5~200)
    BatteryCapacity: { min: 5, max: 250, length: 3 }, //電池容量(5~250)
    CorrectionValue: { min: 10, max: 10000, length: 5 },//補正值(10~10000)
    Resistance: { min: 1.5, max: 1000, length: 4 },//放電電阻值(1.5~1000)  
}

const code186 = {//BA
    UploadCycle: { min: 300, max: 60000, length: 5 },//通訊上傳週期時間(300~60000)
    IRCycle: { min: 60, max: 60000, length: 5 }, //內阻測試週期(60~60000)
    CommunicationCycle: { min: 5, max: 60000, length: 5 },//子板通信週期(5~60000)
}

const code179 = {
    Vol: { min: 1000, max: 60000, length: 5 },//CH1 電壓(1000~60000)
}
const code181 = {
    IR: { min: 1000, max: 60000, length: 5 },//CH1 電壓(1000~60000)
}

export const checkedInputValue = (code, value, name, onfocus) => {
    const OnlyInteger = value.replace(/\D/g, '');
    switch (code) {
        case '187':// 變更BB input內容並判斷是否為數字(判斷最大值,無判斷最小值)
            const { IRTestTime, BatteryCapacity, CorrectionValue, Resistance } = code187;
            if (name === 'IRTestTime') {                    //內組測試時間(0~200)
                if (value.toString().length <= IRTestTime.length && Number(value) <= IRTestTime.max && Number(value) >= (onfocus ? 0 : IRTestTime.min)) {
                    return onfocus ? OnlyInteger : Number(OnlyInteger)
                }
            } else if (name === 'BatteryCapacity') {        //電池容量(0~250)
                if (value.toString().length <= BatteryCapacity.length && Number(value) <= BatteryCapacity.max && Number(value) >= (onfocus ? 0 : BatteryCapacity.min)) {
                    return onfocus ? OnlyInteger : Number(OnlyInteger)
                }
            } else if (name === 'CorrectionValue') {        //補正值(0~10000)
                if (value.toString().length <= CorrectionValue.length && Number(value) <= CorrectionValue.max && Number(value) >= (onfocus ? 0 : CorrectionValue.min)) {
                    return onfocus ? OnlyInteger : Number(OnlyInteger)
                }
            } else if (name === 'Resistance') {             //放電電阻值(0~1000)
                const dotIdx = value.toString().indexOf('.');
                if (dotIdx >= 0) {//含有小數點
                    if (dotIdx !== value.toString().length - 1 && value.toString().length <= (Resistance.length + 1) && (Number(value) * 10) <= Resistance.max * 10 && (Number(value) * 10) >= (onfocus ? 0 : Resistance.min * 10)) {//小數點不在結尾
                        return onfocus ? value : Number(value)
                    } else if (dotIdx === value.toString().length - 1) {
                        return onfocus ? value : Number(value)
                    }
                } else { //沒有小數點
                    if (value.toString().length <= Resistance.length && Number(value) <= Resistance.max && Number(value) >= (onfocus ? 0 : Resistance.min)) {
                        return onfocus ? OnlyInteger : Number(OnlyInteger)
                    }
                }
            }
            break;
        case '186':// 變更BA input內容並判斷是否為數字(判斷最大值,無判斷最小值)
            const { UploadCycle, IRCycle, CommunicationCycle } = code186;
            if (name === 'UploadCycle') {                    //通訊上傳週期時間(300~60000)
                if (value.toString().length <= UploadCycle.length && Number(value) <= UploadCycle.max && Number(value) >= (onfocus ? 0 : UploadCycle.min)) {
                    return onfocus ? OnlyInteger : Number(OnlyInteger)
                }
            } else if (name === 'IRCycle') {                //內阻測試週期(60~60000)
                if (value.toString().length <= IRCycle.length && Number(value) <= IRCycle.max && Number(value) >= (onfocus ? 0 : IRCycle.min)) {
                    return onfocus ? OnlyInteger : Number(OnlyInteger)
                }
            } else if (name === 'CommunicationCycle') {      //子板通信週期(5~60000)
                if (value.toString().length <= CommunicationCycle.length && Number(value) <= CommunicationCycle.max && Number(value) >= (onfocus ? 0 : CommunicationCycle.min)) {
                    return onfocus ? OnlyInteger : Number(OnlyInteger)
                }
            }
            break;
        case '179':// 變更B3 input內容並判斷是否為數字(判斷最大值,無判斷最小值)
            const { Vol } = code179;
            if (value.toString().length <= Vol.length && Number(value) <= Vol.max && Number(value) >= (onfocus ? 0 : Vol.min)) {
                return onfocus ? OnlyInteger : Number(OnlyInteger)
            }
            break;
        case '181':// 變更B5 input內容並判斷是否為數字(判斷最大值,無判斷最小值)    
            const { IR } = code181;
            if (value.toString().length <= IR.length && Number(value) <= IR.max && Number(value) >= (onfocus ? 0 : IR.min)) {
                return onfocus ? OnlyInteger : Number(OnlyInteger)
            }
            break;
        default:// 變更Group站台設定(1416),Battery電池組編輯(1551) input內容
            return value
    }
}

checkedInputValue.defaultProps = {
    code: '',
    value: '',
    name: '',
    onfocus: true,
}