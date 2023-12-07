const fetchWithTimeout = (url, options, timeout) => {
    return Promise.race([
        fetch(url, options),
        new Promise((_, reject) =>
            setTimeout(() => reject(new Error('timeout')), timeout)
        )
    ]);
}
fetchWithTimeout.defaultProps = {
    timeout: 300000, // 5 minute
}
export default fetchWithTimeout;
