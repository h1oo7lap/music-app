// /backend/middlewares/roleMiddleware.js

// Hàm này kiểm tra nếu vai trò của người dùng có trong mảng allowedRoles không
const isAdmin = (req, res, next) => {
    // Chúng ta dựa vào req.user đã được gán bởi 'protect' middleware
    if (req.user && req.user.role === 'admin') {
        next(); // Cho phép Admin đi tiếp
    } else {
        res.status(403).json({ message: 'Truy cập bị từ chối. Chỉ Admin mới có quyền.' });
    }
};

export { isAdmin };