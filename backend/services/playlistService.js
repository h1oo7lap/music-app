// /backend/services/playlistService.js

import Playlist from '../models/playlistModel.js';
import { ApiError } from '../utils/errorUtils.js'; // 🆕 Import ApiError
import Song from '../models/songModel.js'; // 🆕 IMPORT Song model

/**
 * Tạo Playlist mới
 */
const createPlaylistService = async (userId, name, isPublic = false) => {

    const newPlaylist = await Playlist.create({
        user: userId,
        name: name,
        isPublic: isPublic
    });

    return newPlaylist;
};

/**
 * Lấy tất cả Playlists của một User (của tôi)
 */
const getUserPlaylistsService = async (userId) => {
    // Chỉ lấy những playlist có trường user khớp với userId
    const playlists = await Playlist.find({ user: userId })
        .populate('songs', 'title artist duration imageUrl') // Populate thông tin bài hát
        .select('-__v');
    return playlists;
};

/**
 * Xóa Playlist theo ID và chỉ cho phép người tạo xóa
 */
const deletePlaylistService = async (playlistId, userId) => {
    // Tìm Playlist theo ID và User ID
    const playlist = await Playlist.findOneAndDelete({
        _id: playlistId,
        user: userId
    });

    if (!playlist) {
        // 🚨 Dùng ApiError 404
        throw new ApiError('Không tìm thấy Playlist hoặc bạn không có quyền xóa.', 404);
    }
    return playlist;
};

/**
 * Thêm bài hát vào Playlist
 */
const addSongToPlaylistService = async (playlistId, songId, userId) => {

    // 1.  KIỂM TRA BÀI HÁT TỒN TẠI
    const songExists = await Song.findById(songId);
    if (!songExists) {
        throw new ApiError('Bài hát không tồn tại.', 404); 
    }

    //  Tìm Playlist và đảm bảo thuộc về User
    const playlist = await Playlist.findOne({
        _id: playlistId,
        user: userId
    });

    if (!playlist) {
        // 🚨 Dùng ApiError 404
        throw new ApiError('Playlist không tồn tại hoặc bạn không có quyền chỉnh sửa.', 404);
    }

    // 2. Ngăn chặn trùng lặp
    if (playlist.songs.includes(songId)) {
        // 🚨 Dùng ApiError 400 cho lỗi Validation/Nghiệp vụ
        throw new ApiError('Bài hát đã có trong Playlist.', 400);
    }

    // 3. Thêm bài hát (sử dụng $push trong Mongoose)
    playlist.songs.push(songId);
    await playlist.save();

    // Lấy lại Playlist đã populate để phản hồi
    const updatedPlaylist = await Playlist.findById(playlistId)
        .populate('songs', 'title artist duration imageUrl');
    return updatedPlaylist;
};

/**
 * Xóa bài hát khỏi Playlist
 */
const removeSongFromPlaylistService = async (playlistId, songId, userId) => {

    // 1. 🆕 KIỂM TRA BÀI HÁT TỒN TẠI (Đảm bảo ID hợp lệ và tồn tại)
    const songExists = await Song.findById(songId);
    if (!songExists) {
        // Có thể chọn trả về 404 hoặc 400 tùy ý. 404 là hợp lý hơn.
        throw new ApiError('Bài hát không tồn tại.', 404); 
    }

    const playlist = await Playlist.findOne({
        _id: playlistId,
        user: userId
    });

    if (!playlist) {
        // 🚨 Dùng ApiError 404
        throw new ApiError('Playlist không tồn tại hoặc bạn không có quyền chỉnh sửa.', 404);
    }

    // Xóa bài hát (sử dụng $pull trong Mongoose)
    // Lưu ý: Nếu songId không tồn tại trong mảng, $pull vẫn sẽ chạy và không gây lỗi.
    playlist.songs.pull(songId);
    await playlist.save();

    const updatedPlaylist = await Playlist.findById(playlistId)
        .populate('songs', 'title artist duration imageUrl');
    return updatedPlaylist;
};


export {
    createPlaylistService,
    getUserPlaylistsService,
    deletePlaylistService,
    addSongToPlaylistService,
    removeSongFromPlaylistService,
};