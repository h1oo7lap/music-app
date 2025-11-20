// /backend/services/songService.js

import Song from '../models/songModel.js';
import Genre from '../models/genreModel.js';
import fs from 'fs';
import { ApiError } from '../utils/errorUtils.js'; // 🆕 Import ApiError

/**
 * Lưu metadata bài hát vào MongoDB
 */
const createSongService = async (songData) => {

    // Tùy chọn: Kiểm tra xem ID Genre có tồn tại không
    const genreExists = await Genre.findById(songData.genre);
    if (!genreExists) {
        // 🚨 Dùng ApiError 404
        throw new ApiError('Thể loại không tồn tại.', 404);
    }

    const song = await Song.create(songData);

    return song;
};



const getAllSongsService = async (keyword = '', page = 1, limit = 10) => {

    // Xây dựng điều kiện tìm kiếm (Case-insensitive search)
    const searchCondition = keyword ? {
        // Tìm kiếm trong cả 'title' và 'artist' (sử dụng $or)
        $or: [
            { title: { $regex: keyword, $options: 'i' } },
            { artist: { $regex: keyword, $options: 'i' } },
        ]
    } : {};

    // Tính toán phân trang
    const skip = (page - 1) * limit;

    // 1. Lấy tổng số lượng bản ghi (phục vụ cho tính toán tổng số trang)
    const totalCount = await Song.countDocuments(searchCondition);

    // 2. Lấy dữ liệu bài hát đã phân trang
    const songs = await Song.find(searchCondition)
        .populate('genre', 'name')
        .select('-__v')
        .limit(limit)
        .skip(skip);

    // 3. Trả về đối tượng chứa dữ liệu và thông tin phân trang
    return {
        songs,
        page: Number(page),
        limit: Number(limit),
        totalSongs: totalCount,
        totalPages: Math.ceil(totalCount / limit),
    };
};

/**
 * Xóa bài hát theo ID và xóa các file liên quan
 */
const deleteSongService = async (id) => {
    // 1. Tìm bài hát
    const song = await Song.findById(id);

    if (!song) {
        // 🚨 Dùng ApiError 404
        throw new ApiError('Không tìm thấy bài hát để xóa.', 404);
    }

    // 2. Xóa các file liên quan trên server (Local Storage)
    try {
        fs.unlinkSync(song.songUrl); // Xóa file MP3
        if (song.imageUrl) {
            fs.unlinkSync(song.imageUrl); // Xóa file ảnh bìa (nếu có)
        }
    } catch (error) {
        // Log lỗi nhưng không dừng tiến trình
        console.error(`Không thể xóa file: ${error.message}`);
    }

    // 3. Xóa bản ghi trong MongoDB
    await Song.deleteOne({ _id: id });

    return song;
};

/**
 * Cập nhật bài hát theo ID và xử lý xóa file cũ nếu có file mới
 */
const updateSongService = async (id, updatedData) => {

    const song = await Song.findById(id);

    if (!song) {
        // 🚨 Dùng ApiError 404
        throw new ApiError('Không tìm thấy bài hát để cập nhật.', 404);
    }

    // 1. Xử lý xóa file cũ nếu có file mới được upload
    if (updatedData.songUrl && song.songUrl) {
        try {
            fs.unlinkSync(song.songUrl); // Xóa file MP3 cũ
        } catch (error) { console.error(`Lỗi xóa file nhạc cũ: ${error.message}`); }
    }
    if (updatedData.imageUrl && song.imageUrl) {
        try {
            fs.unlinkSync(song.imageUrl); // Xóa file ảnh cũ
        } catch (error) { console.error(`Lỗi xóa file ảnh cũ: ${error.message}`); }
    }

    // Tùy chọn: Kiểm tra lại ID Genre nếu có cập nhật
    if (updatedData.genre) {
        const genreExists = await Genre.findById(updatedData.genre);
        if (!genreExists) {
            // 🚨 Dùng ApiError 404
            throw new ApiError('Thể loại không tồn tại.', 404);
        }
    }


    // 2. Cập nhật bản ghi trong MongoDB
    const updatedSong = await Song.findByIdAndUpdate(
        id,
        { $set: updatedData },
        { new: true, runValidators: true }
    ).populate('genre', 'name');

    return updatedSong;
};

/**
 * Lấy một bài hát theo ID
 */
const getSongByIdService = async (id) => {
    // 💡 Sử dụng populate để lấy tên thể loại và select('-__v')
    const song = await Song.findById(id)
        .populate('genre', 'name')
        .select('-__v');

    if (!song) {
        // 🚨 Dùng ApiError 404
        throw new ApiError('Không tìm thấy bài hát.', 404);
    }
    return song;
};

/**
 * Tăng lượt nghe của bài hát lên 1
 */
const incrementPlayCountService = async (songId) => {
    // Sử dụng findByIdAndUpdate với toán tử $inc (increment) để tăng playCount lên 1
    const updatedSong = await Song.findByIdAndUpdate(
        songId,
        { $inc: { playCount: 1 } }, // Tăng playCount thêm 1
        { new: true } // Trả về bản ghi đã cập nhật
    );

    if (!updatedSong) {
        // 🚨 Dùng ApiError 404
        throw new ApiError('Không tìm thấy bài hát để cập nhật lượt nghe.', 404);
    }

    // Chỉ trả về lượt nghe mới
    return updatedSong.playCount;
};

/**
 * Lấy top N bài hát dựa trên playCount
 */
const getTopSongsService = async (limit = 10) => {

    const topSongs = await Song.find({})
        // 1. Sắp xếp: Giảm dần theo playCount (lớn nhất lên đầu)
        .sort({ playCount: -1 })
        // 2. Giới hạn số lượng
        .limit(limit)
        // 3. Populate và select như thường lệ
        .populate('genre', 'name')
        .select('-__v');

    return topSongs;
};

export { createSongService, getAllSongsService, deleteSongService, updateSongService, getSongByIdService, incrementPlayCountService, getTopSongsService };