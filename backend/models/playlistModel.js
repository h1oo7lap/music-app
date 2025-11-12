// /backend/models/playlistModel.js

import mongoose from 'mongoose';

const playlistSchema = new mongoose.Schema({
    name: {
        type: String,
        required: [true, 'Tên playlist là bắt buộc.'],
        trim: true,
        maxlength: 100,
    },
    // Tham chiếu đến User tạo playlist (One-to-One)
    user: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true,
    },
    // Tham chiếu đến mảng các Bài hát (One-to-Many)
    songs: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Song',
    }],
    isPublic: { // Có công khai cho mọi người không (mặc định là riêng tư)
        type: Boolean,
        default: false,
    },
}, {
    timestamps: true
});

const Playlist = mongoose.model('Playlist', playlistSchema);
export default Playlist;