// /backend/services/authService.js
import User from '../models/userModel.js'; 

const registerUser = async (userData) => {
    const { username, displayName, password, role } = userData;

    const userExists = await User.findOne({ username });

    if (userExists) {
        throw new Error('Tên đăng nhập đã tồn tại.'); 
    }

    const newUser = await User.create({
        username, displayName, password,
        role: role || 'user',
    });

    if (newUser) {
        return {
            _id: newUser._id,
            username: newUser.username,
            displayName: newUser.displayName,
            role: newUser.role,
        };
    } else {
        throw new Error('Dữ liệu người dùng không hợp lệ.');
    }
};

const loginUser = async (username, password) => {
    const user = await User.findOne({ username });

    if (user && (await user.comparePassword(password))) { 
        return {
            _id: user._id,
            username: user.username,
            displayName: user.displayName,
            role: user.role,
        };
    } else {
        throw new Error('Tên đăng nhập hoặc mật khẩu không đúng.');
    }
};

export { registerUser, loginUser };