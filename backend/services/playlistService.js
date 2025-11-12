// /backend/services/playlistService.js

import Playlist from '../models/playlistModel.js';

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
        // Có thể là không tìm thấy ID, hoặc tìm thấy nhưng không phải của User này
        throw new Error('Không tìm thấy Playlist hoặc bạn không có quyền xóa.');
    }
    return playlist;
};

/**
 * Thêm bài hát vào Playlist
 */
const addSongToPlaylistService = async (playlistId, songId, userId) => {
    // 1. Tìm Playlist và đảm bảo thuộc về User
    const playlist = await Playlist.findOne({
        _id: playlistId,
        user: userId
    });

    if (!playlist) {
        throw new Error('Playlist không tồn tại hoặc bạn không có quyền chỉnh sửa.');
    }

    // 2. Ngăn chặn trùng lặp
    if (playlist.songs.includes(songId)) {
        throw new Error('Bài hát đã có trong Playlist.');
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
    const playlist = await Playlist.findOne({
        _id: playlistId,
        user: userId
    });

    if (!playlist) {
        throw new Error('Playlist không tồn tại hoặc bạn không có quyền chỉnh sửa.');
    }

    // Xóa bài hát (sử dụng $pull trong Mongoose)
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