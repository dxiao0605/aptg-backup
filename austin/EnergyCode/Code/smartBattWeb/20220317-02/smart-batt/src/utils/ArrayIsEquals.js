

export const ArrayIsEquals = (a, b) => {
    if (!(a instanceof Array)) { return false; }
    if (!(b instanceof Array)) { return false; }
    if (a.length !== b.length) {
        return true;
    } else {
        let result = true;
        for (var i = 0; i < a.length; i++) {
            if (result && (a[i] !== b[i])) {
                result = result * false;
            }
        }
        return !result;
    }
}

