// /backend/models/songModel.js

import mongoose from 'mongoose';

const songSchema = new mongoose.Schema({
    title: {
        type: String,
        required: [true, 'Tên bài hát là bắt buộc.'],
        trim: true,
        maxlength: 100,
    },
    artist: {
        type: String,
        required: [true, 'Tên nghệ sĩ là bắt buộc.'],
        trim: true,
        maxlength: 100,
    },
    // Tham chiếu đến Genre Model (Many-to-One)
    genre: { 
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Genre', // Tên model tham chiếu
        required: [true, 'Thể loại là bắt buộc.'],
    },
    duration: { // Thời lượng bài hát (tính bằng giây)
        type: Number,
        required: [true, 'Thời lượng là bắt buộc.'],
    },
    imageUrl: { // URL ảnh bìa (sẽ dùng Multer để upload sau)
        type: String,
        default: '/images/default.jpg',
    },
    songUrl: { // URL file nhạc MP3 (sẽ dùng Multer để upload sau)
        type: String,
        required: [true, 'Đường dẫn file nhạc là bắt buộc.'],
    },
    isPublic: { // Có công khai cho mọi người không
        type: Boolean,
        default: true,
    },
    playCount: { // Số lượt nghe
        type: Number,
        default: 0,
    }
}, {
    timestamps: true
});

const Song = mongoose.model('Song', songSchema);
export default Song;