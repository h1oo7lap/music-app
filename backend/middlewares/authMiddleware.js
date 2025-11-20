// /backend/middlewares/authMiddleware.js
import jwt from 'jsonwebtoken';
import User from '../models/userModel.js';

const protect = async (req, res, next) => {
    let token;

    // 1. Kiểm tra Token trong Header
    // Token thường ở dạng: "Bearer <token_string>"
    if (
        req.headers.authorization &&
        req.headers.authorization.startsWith('Bearer')
    ) {
        try {
            // Lấy Token (bỏ chữ 'Bearer ')
            token = req.headers.authorization.split(' ')[1];

            // 2. Giải mã Token
            const decoded = jwt.verify(token, process.env.JWT_SECRET);

            // 3. Tìm User và gán vào request
            // Lấy User (trừ mật khẩu) và lưu vào req.user
            req.user = await User.findById(decoded.id).select('-password');

            if (!req.user) {
                return res.status(401).json({ message: 'Không tìm thấy người dùng.' });
            }

            next(); // Chuyển sang Controller tiếp theo

        } catch (error) {
            console.error(error);
            return res.status(401).json({ message: 'Token không hợp lệ hoặc đã hết hạn.' });
        }
    }

    if (!token) {
        return res.status(401).json({ message: 'Không có Token, vui lòng đăng nhập.' });
    }
};

export { protect };