// /backend/services/authService.js
import User from '../models/userModel.js';
import { ApiError } from '../utils/errorUtils.js';

// const registerUser = async (userData) => {
//     const { username, displayName, password, role } = userData;

//     const userExists = await User.findOne({ username });

//     if (userExists) {
//         throw new Error('Tên đăng nhập đã tồn tại.'); 
//     }

//     const newUser = await User.create({
//         username, displayName, password,
//         role: role || 'user',
//     });

//     if (newUser) {
//         return {
//             _id: newUser._id,
//             username: newUser.username,
//             displayName: newUser.displayName,
//             role: newUser.role,
//         };
//     } else {
//         throw new Error('Dữ liệu người dùng không hợp lệ.');
//     }
// };

const registerUser = async (userData) => {
    const { username, displayName, password, role } = userData;

    const userExists = await User.findOne({ username });

    if (userExists) {
        // 🚨 Thay thế bằng ApiError(message, 400)
        throw new ApiError('Tên đăng nhập đã tồn tại.', 400);
    }

    const newUser = await User.create({
        username, displayName, password,
        role: role || 'user',
    });

    if (newUser) {
        // ... (trả về user thành công)
        return {
            _id: newUser._id,
            username: newUser.username,
            displayName: newUser.displayName,
            role: newUser.role,
        };
    } else {
        // 🚨 Thay thế bằng ApiError(message, 400)
        throw new ApiError('Dữ liệu người dùng không hợp lệ.', 400);
    }
};

// const loginUser = async (username, password) => {
//     const user = await User.findOne({ username });

//     if (user && (await user.comparePassword(password))) {
//         return {
//             _id: user._id,
//             username: user.username,
//             displayName: user.displayName,
//             role: user.role,
//         };
//     } else {
//         throw new Error('Tên đăng nhập hoặc mật khẩu không đúng.');
//     }
// };

const loginUser = async (username, password) => {
    const user = await User.findOne({ username });

    // 💡 Logic này kiểm tra cả tên đăng nhập và mật khẩu không đúng
    if (user && (await user.comparePassword(password))) {
        return {
            _id: user._id,
            username: user.username,
            displayName: user.displayName,
            role: user.role,
        };
    } else {
        // 🚨 Thay thế bằng ApiError(message, 401)
        // Lỗi xác thực (Authentication failure) nên trả về 401 Unauthorized
        throw new ApiError('Tên đăng nhập hoặc mật khẩu không đúng.', 401);
    }
};

export { registerUser, loginUser };