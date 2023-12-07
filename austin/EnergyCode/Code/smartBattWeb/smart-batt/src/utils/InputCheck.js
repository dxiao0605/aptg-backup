export const InputCheck = (text) => {
    // for 電池型號、站台號碼、通訊序號 
    return text === "" ? "" : text.match(/^[a-zA-Z0-9_-]+$/g)
}