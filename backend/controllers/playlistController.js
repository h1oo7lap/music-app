// /backend/controllers/playlistController.js

import asyncHandler from 'express-async-handler';
import {
    createPlaylistService,
    getUserPlaylistsService,
    deletePlaylistService,
    addSongToPlaylistService,
    removeSongFromPlaylistService
} from '../services/playlistService.js';

// @desc    Tạo Playlist mới
// @route   POST /api/playlists
// @access  Private
const createPlaylistController = asyncHandler(async (req, res) => {
    const { name, isPublic } = req.body;

    if (!name) {
        res.status(400); // Đặt status code
        throw new Error('Tên playlist là bắt buộc.');
    }

    const userId = req.user._id;
    // Lỗi 500 sẽ được asyncHandler bắt
    const newPlaylist = await createPlaylistService(userId, name, isPublic);

    res.status(201).json({
        message: 'Playlist được tạo thành công.',
        playlist: newPlaylist,
    });
});

// @desc    Lấy tất cả Playlists của User hiện tại
// @route   GET /api/playlists/my
// @access  Private
const getUserPlaylistsController = asyncHandler(async (req, res) => {
    const userId = req.user._id;
    const playlists = await getUserPlaylistsService(userId);
    res.status(200).json(playlists);
});

// ------------------- CRUD THAO TÁC --------------------

// @desc    Xóa Playlist theo ID
// @route   DELETE /api/playlists/:id
// @access  Private
const deletePlaylistController = asyncHandler(async (req, res) => {
    // Logic 404 (Không tìm thấy) và CastError (ID không hợp lệ) được chuyển sang Middleware
    const deletedPlaylist = await deletePlaylistService(req.params.id, req.user._id);

    res.status(200).json({
        message: 'Playlist đã được xóa thành công.',
        playlist: deletedPlaylist
    });
});

// ------------------- QUẢN LÝ BÀI HÁT TRONG PLAYLIST --------------------

// @desc    Thêm Bài hát vào Playlist
// @route   PUT /api/playlists/add
// @access  Private
const addSongController = asyncHandler(async (req, res) => {
    const { playlistId, songId } = req.body;

    if (!playlistId || !songId) {
        res.status(400);
        throw new Error('Cần cung cấp Playlist ID và Song ID.');
    }

    // Lỗi nghiệp vụ (400) hoặc lỗi Mongoose sẽ được Middleware xử lý
    const updatedPlaylist = await addSongToPlaylistService(playlistId, songId, req.user._id);

    res.status(200).json({
        message: 'Bài hát đã được thêm vào Playlist.',
        playlist: updatedPlaylist
    });
});

// @desc    Xóa Bài hát khỏi Playlist
// @route   PUT /api/playlists/remove
// @access  Private
const removeSongController = asyncHandler(async (req, res) => {
    const { playlistId, songId } = req.body;

    if (!playlistId || !songId) {
        res.status(400);
        throw new Error('Cần cung cấp Playlist ID và Song ID.');
    }

    // Lỗi nghiệp vụ (400) hoặc lỗi Mongoose sẽ được Middleware xử lý
    const updatedPlaylist = await removeSongFromPlaylistService(playlistId, songId, req.user._id);

    res.status(200).json({
        message: 'Bài hát đã được xóa khỏi Playlist.',
        playlist: updatedPlaylist
    });
});


export {
    createPlaylistController,
    getUserPlaylistsController,
    deletePlaylistController,
    addSongController,
    removeSongController
};