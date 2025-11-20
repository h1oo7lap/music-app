// /backend/controllers/songController.js

import asyncHandler from 'express-async-handler';
import { createSongService, getAllSongsService, deleteSongService, updateSongService, getSongByIdService, incrementPlayCountService, getTopSongsService } from '../services/songService.js';
import { getSongDuration } from '../utils/getSongDuration.js';
import fs from 'fs';

// @desc    Tạo bài hát mới (bao gồm upload file)
// @route   POST /api/songs
// @access  Private/Admin

const createSongController = asyncHandler(async (req, res, next) => {

    // Lấy file từ req.files (Multer gán vào)
    const songFile = req.files['songFile'] ? req.files['songFile'][0] : null;
    const albumImage = req.files['albumImage'] ? req.files['albumImage'][0] : null;

    // Khai báo hàm dọn dẹp file (Cleanup Logic)
    const cleanupFiles = () => {
        if (songFile && fs.existsSync(songFile.path)) {
            fs.unlinkSync(songFile.path);
        }
        if (albumImage && fs.existsSync(albumImage.path)) {
            fs.unlinkSync(albumImage.path);
        }
    };

    try {
        // Lấy dữ liệu văn bản từ req.body (BỎ duration)
        const { title, artist, genre } = req.body;

        // 1. Kiểm tra dữ liệu bắt buộc (Lỗi 400 Validation)
        if (!title || !artist || !genre || !songFile) {
            res.status(400);
            throw new Error('Tiêu đề, nghệ sĩ, thể loại và file nhạc là bắt buộc.');
        }

        // 2. Tính toán Duration TỰ ĐỘNG
        // 🚨 CHÚ Ý: Đảm bảo hàm này trả về một giá trị số (ví dụ: 0 nếu lỗi)
        const durationInSeconds = await getSongDuration(songFile.path);

        // 3. Chuẩn bị dữ liệu cho Service
        const songData = {
            title,
            artist,
            genre,
            duration: durationInSeconds, // ⬅️ DURATION ĐƯỢC TÍNH TỰ ĐỘNG
            songUrl: songFile.path,
            imageUrl: albumImage ? albumImage.path : null,
        };

        // 4. Gọi Service để lưu metadata vào DB
        const newSong = await createSongService(songData);

        res.status(201).json({
            message: 'Bài hát được thêm thành công.',
            song: newSong,
        });

    } catch (error) {
        // 5. Bắt lỗi (từ validation, service 404, hoặc DB 500) và DỌN DẸP FILE
        cleanupFiles();

        // 6. Ném lỗi lại để Global Error Handler bắt và trả về response JSON đồng bộ
        throw error;
    }
});

// @desc    Lấy tất cả bài hát
// @route   GET /api/songs
// @access  Public
const getAllSongsController = asyncHandler(async (req, res, next) => {

    // Lấy tham số truy vấn (Query Params) từ req.query
    const keyword = req.query.keyword || '';
    const page = parseInt(req.query.page) || 1;
    const limit = parseInt(req.query.limit) || 10;

    // Gọi service với các tham số mới (Lỗi sẽ được tự động chuyển tiếp)
    const data = await getAllSongsService(keyword, page, limit);

    res.status(200).json(data);

});

// @desc    Xóa bài hát theo ID
// @route   DELETE /api/songs/:id
// @access  Private/Admin
const deleteSongController = asyncHandler(async (req, res, next) => {

    // Logic 404/CastError/500 sẽ được errorMiddleware xử lý
    const deletedSong = await deleteSongService(req.params.id);

    res.status(200).json({
        message: 'Bài hát đã được xóa thành công và các file đã được gỡ bỏ.',
        song: deletedSong
    });

});

// @desc    Cập nhật bài hát theo ID (hỗ trợ upload file)
// @route   PUT /api/songs/:id
// @access  Private/Admin
const updateSongController = asyncHandler(async (req, res, next) => {

    const { title, artist, genre, duration } = req.body;

    // Dữ liệu mới được gửi
    const updatedData = {
        title,
        artist,
        genre,
        duration: duration ? Number(duration) : undefined,
    };

    // Kiểm tra nếu có file mới, cập nhật đường dẫn vào updatedData
    if (req.files && req.files['songFile']) {
        updatedData.songUrl = req.files['songFile'][0].path;
    }
    if (req.files && req.files['albumImage']) {
        updatedData.imageUrl = req.files['albumImage'][0].path;
    }

    const updatedSong = await updateSongService(req.params.id, updatedData);

    res.status(200).json({
        message: 'Bài hát đã được cập nhật thành công.',
        song: updatedSong
    });

});

// @desc    Lấy một bài hát theo ID
// @route   GET /api/songs/:id
// @access  Public
const getSongByIdController = asyncHandler(async (req, res, next) => {
    // Logic 404/CastError sẽ được errorMiddleware xử lý
    const song = await getSongByIdService(req.params.id);

    res.status(200).json(song);

});

// @desc    Tăng lượt nghe của bài hát
// @route   POST /api/songs/:id/listen
// @access  Public
const incrementPlayCountController = asyncHandler(async (req, res, next) => {

    const songId = req.params.id;

    const newPlayCount = await incrementPlayCountService(songId);

    res.status(200).json({
        message: 'Lượt nghe đã được cập nhật thành công.',
        playCount: newPlayCount
    });

});

// @desc    Lấy top N bài hát được nghe nhiều nhất
// @route   GET /api/songs/top?limit=N
// @access  Public
const getTopSongsController = asyncHandler(async (req, res, next) => {

    // Lấy tham số limit từ query (mặc định là 10)
    const limit = parseInt(req.query.limit) || 10;

    const topSongs = await getTopSongsService(limit);

    res.status(200).json(topSongs);

});

export { createSongController, getAllSongsController, deleteSongController, updateSongController, getSongByIdController, incrementPlayCountController, getTopSongsController };