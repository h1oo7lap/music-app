// /backend/controllers/authController.js

import asyncHandler from 'express-async-handler';
import { registerUser, loginUser } from '../services/authService.js';
import generateToken from '../utils/generateToken.js';

// @desc    Đăng ký người dùng mới
// @route   POST /api/auth/register
// @access  Public
const registerUserController = asyncHandler(async (req, res, next) => {
    const { username, displayName, password } = req.body;

    // 1. Xử lý lỗi validation 400
    if (!username || !displayName || !password) {
        res.status(400);
        throw new Error('Vui lòng điền đầy đủ các trường bắt buộc.');
    }

    // Lỗi "Tên đăng nhập đã tồn tại" sẽ được Service throw ra và errorMiddleware bắt
    const newUser = await registerUser({ username, displayName, password });

    res.status(201).json({
        message: 'Đăng ký thành công.',
        user: newUser,
    });
});

// @desc    Đăng nhập người dùng
// @route   POST /api/auth/login
// @access  Public
const loginUserController = asyncHandler(async (req, res, next) => {
    const { username, password } = req.body;

    // 1. Xử lý lỗi validation 400
    if (!username || !password) {
        res.status(400);
        throw new Error('Vui lòng cung cấp tên đăng nhập và mật khẩu.');
    }

    // Lỗi 401 (Thông tin không đúng) sẽ được Service throw ra và errorMiddleware bắt
    const user = await loginUser(username, password);

    const token = generateToken(user._id, user.role);

    res.json({
        _id: user._id,
        username: user.username,
        displayName: user.displayName,
        role: user.role,
        token,
    });
});

export { registerUserController, loginUserController };