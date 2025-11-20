// /backend/models/userModel.js
import mongoose from 'mongoose';
// import bcrypt from 'bcrypt'; 

const userSchema = new mongoose.Schema({
    username: {
        type: String,
        required: [true, 'Tên đăng nhập là bắt buộc.'],
        unique: true,
        trim: true,
    },
    displayName: {
        type: String,
        required: [true, 'Tên hiển thị là bắt buộc.'],
        trim: true,
    },
    password: {
        type: String,
        required: [true, 'Mật khẩu là bắt buộc.'],
    },
    role: {
        type: String,
        enum: ['user', 'admin'],
        default: 'user',
    },
    favoriteSongs: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Song',
    }],
}, {
    timestamps: true
});

// Hàm kiểm tra mật khẩu (chưa hash)
userSchema.methods.comparePassword = async function (candidatePassword) {
    return candidatePassword === this.password;
};

const User = mongoose.model('User', userSchema);
export default User;