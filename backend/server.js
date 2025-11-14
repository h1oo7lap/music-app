// /backend/server.js
import express from 'express';
import dotenv from 'dotenv';
import cors from 'cors';
import connectDB from './config/db.js';
import authRoute from './routes/authRoute.js';
import { protect } from './middlewares/authMiddleware.js';
import { isAdmin } from './middlewares/roleMiddleware.js';
import genreRoute from './routes/genreRoute.js';
import songRoute from './routes/songRoute.js';
import playlistRoute from './routes/playlistRoute.js';
import userRoute from './routes/userRoute.js';
import { notFound, errorHandler } from './middlewares/errorMiddleware.js';

// Load các biến môi trường
dotenv.config();

// Gọi hàm kết nối DB
connectDB();

// Khởi tạo Express App
const app = express();

// Middleware cơ bản
app.use(cors());
app.use(express.json());

// Định nghĩa Route đầu tiên (test server)
app.get('/', (req, res) => {
    res.send('Music App API is running...');
});

// Định nghĩa đường dẫn cho Auth Route
app.use('/api/auth', authRoute);

// Định nghĩa đường dẫn cho Genre Route
app.use('/api/genres', genreRoute);

// Định nghĩa đường dẫn cho Song Route
app.use('/api/songs', songRoute);

// Định nghĩa đường dẫn cho Playlist Route
app.use('/api/playlists', playlistRoute);

// Định nghĩa đường dẫn cho User Route
app.use('/api/users', userRoute);

// Định nghĩa Port và Lắng nghe
const PORT = process.env.PORT || 5000;

// Route test cho Admin
app.get('/api/admin-data', protect, isAdmin, (req, res) => {
    res.json({
        message: `Chào mừng Admin: ${req.user.displayName}`,
        role: req.user.role,
        userId: req.user._id
    });
});

// --- XỬ LÝ LỖI TOÀN CỤC (ĐẶT CUỐI CÙNG) ---

// 1. Middleware bắt 404 cho các route không tồn tại
app.use(notFound);

// 2. Middleware xử lý lỗi chung (sử dụng 4 tham số: err, req, res, next)
app.use(errorHandler);

// app.listen() - Sửa lỗi "clean exit"
app.listen(PORT, () => {         // "0.0.0.0" là IP address cơ bản (có nghiệm)
    console.log(`🚀 Server running on port ${PORT}`);
});