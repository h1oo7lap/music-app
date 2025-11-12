// /backend/controllers/authController.js
import { registerUser, loginUser } from '../services/authService.js';
import generateToken from '../utils/generateToken.js';

const registerUserController = async (req, res, next) => { // Đổi tên hàm để tránh trùng tên với Service
    try {
        const { username, displayName, password } = req.body;
        
        if (!username || !displayName || !password) {
            res.status(400).json({ message: 'Vui lòng điền đầy đủ các trường bắt buộc.' });
            return;
        }

        // Gọi service
        const newUser = await registerUser({ username, displayName, password });

        res.status(201).json({
            message: 'Đăng ký thành công.',
            user: newUser,
        });

    } catch (error) {
        if (error.message.includes('Tên đăng nhập đã tồn tại')) {
            return res.status(400).json({ message: error.message });
        }
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

const loginUserController = async (req, res, next) => { // Đổi tên hàm để tránh trùng tên với Service
    try {
        const { username, password } = req.body;

        if (!username || !password) {
            return res.status(400).json({ message: 'Vui lòng cung cấp tên đăng nhập và mật khẩu.' });
        }

        // Gọi service
        const user = await loginUser(username, password);

        const token = generateToken(user._id, user.role);

        res.json({
            _id: user._id,
            username: user.username,
            displayName: user.displayName,
            role: user.role,
            token, 
        });

    } catch (error) {
        if (error.message.includes('không đúng')) {
            return res.status(401).json({ message: error.message }); 
        }
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

export { registerUserController, loginUserController };