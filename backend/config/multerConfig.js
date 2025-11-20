// /backend/config/multerConfig.js

import multer from 'multer';
import path from 'path';

// Định nghĩa nơi lưu trữ file
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        // Kiểm tra loại file để đặt vào thư mục tương ứng
        if (file.fieldname === 'songFile') {
            cb(null, 'uploads/songs/'); // Lưu file nhạc
        } else if (file.fieldname === 'albumImage') {
            cb(null, 'uploads/images/'); // Lưu ảnh bìa
        } else {
            cb(new Error('Loại file không được hỗ trợ.'));
        }
    },
    filename: (req, file, cb) => {
        // Tạo tên file duy nhất: fieldname-timestamp.ext
        const ext = path.extname(file.originalname);
        cb(null, file.fieldname + '-' + Date.now() + ext);
    }
});

// Định nghĩa hàm lọc file (chỉ cho phép MP3 và JPEG/PNG)
const fileFilter = (req, file, cb) => {
    if (file.fieldname === 'songFile') {
        if (file.mimetype === 'audio/mpeg') {
            cb(null, true);
        } else {
            cb(null, false); // Từ chối các file nhạc không phải MP3
        }
    } else if (file.fieldname === 'albumImage') {
        if (file.mimetype === 'image/jpeg' || file.mimetype === 'image/png') {
            cb(null, true);
        } else {
            cb(null, false); // Từ chối các file ảnh không phải JPEG/PNG
        }
    } else {
        cb(null, false);
    }
};


// Cấu hình Multer cuối cùng
const upload = multer({
    storage: storage,
    fileFilter: fileFilter,
    limits: {
        fileSize: 1024 * 1024 * 50 // Giới hạn kích thước file, ví dụ: 50MB
    }
});

export default upload;