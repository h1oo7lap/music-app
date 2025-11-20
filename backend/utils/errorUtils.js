// /backend/utils/errorUtils.js

class ApiError extends Error {
    constructor(message, statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}

export { ApiError };