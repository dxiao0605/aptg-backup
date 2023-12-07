export const checkObject = (object) => {
    const { isDataList, isChecked } = object;
    return {
        ...object,
        isChecked: isChecked === false && isDataList.length > 0 ? false : true
    }

}