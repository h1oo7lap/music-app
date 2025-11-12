// /backend/middlewares/errorMiddleware.js

// Middleware xử lý lỗi 404 (Không tìm thấy Route)
const notFound = (req, res, next) => {
    const error = new Error(`Không tìm thấy - ${req.originalUrl}`);
    res.status(404);
    next(error); // Chuyển lỗi này đến middleware xử lý lỗi chung
};

// Middleware xử lý lỗi chung
const errorHandler = (err, req, res, next) => {
    // Nếu status code vẫn là 200, đặt thành 500 (Internal Server Error)
    const statusCode = res.statusCode === 200 ? 500 : res.statusCode;
    res.status(statusCode);

    let errorMessage = err.message;

    // Xử lý lỗi Mongoose CastError (ví dụ: ID không hợp lệ)
    if (err.name === 'CastError' && err.kind === 'ObjectId') {
        statusCode = 400; // Đặt lỗi thành Bad Request
        errorMessage = 'Định dạng ID không hợp lệ.';
    }

    res.json({
        message: errorMessage,
        // Chỉ trả về stack trace khi ở môi trường phát triển (development)
        stack: process.env.NODE_ENV === 'production' ? null : err.stack,
    });
};

export { notFound, errorHandler };