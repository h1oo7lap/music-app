// /backend/controllers/playlistController.js

import {
    createPlaylistService,
    getUserPlaylistsService,
    deletePlaylistService, // 🆕 Hàm xóa Playlist
    addSongToPlaylistService, // 🆕 Hàm thêm Bài hát
    removeSongFromPlaylistService // 🆕 Hàm xóa Bài hát khỏi Playlist
} from '../services/playlistService.js';

// @desc    Tạo Playlist mới
// @route   POST /api/playlists
// @access  Private
const createPlaylistController = async (req, res) => {
    try {
        const { name, isPublic } = req.body;

        if (!name) {
            return res.status(400).json({ message: 'Tên playlist là bắt buộc.' });
        }

        const userId = req.user._id;
        const newPlaylist = await createPlaylistService(userId, name, isPublic);

        res.status(201).json({
            message: 'Playlist được tạo thành công.',
            playlist: newPlaylist,
        });

    } catch (error) {
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

// @desc    Lấy tất cả Playlists của User hiện tại
// @route   GET /api/playlists/my
// @access  Private
const getUserPlaylistsController = async (req, res) => {
    try {
        const userId = req.user._id;
        const playlists = await getUserPlaylistsService(userId);
        res.status(200).json(playlists);
    } catch (error) {
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

// ------------------- CRUD THAO TÁC --------------------

// @desc    Xóa Playlist theo ID
// @route   DELETE /api/playlists/:id
// @access  Private
const deletePlaylistController = async (req, res) => {
    try {
        const deletedPlaylist = await deletePlaylistService(req.params.id, req.user._id);

        res.status(200).json({
            message: 'Playlist đã được xóa thành công.',
            playlist: deletedPlaylist
        });

    } catch (error) {
        if (error.message.includes('Không tìm thấy')) {
            return res.status(404).json({ message: error.message });
        }
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

// ------------------- QUẢN LÝ BÀI HÁT TRONG PLAYLIST --------------------

// @desc    Thêm Bài hát vào Playlist
// @route   PUT /api/playlists/add
// @access  Private
const addSongController = async (req, res) => {
    try {
        const { playlistId, songId } = req.body;

        if (!playlistId || !songId) {
            return res.status(400).json({ message: 'Cần cung cấp Playlist ID và Song ID.' });
        }

        const updatedPlaylist = await addSongToPlaylistService(playlistId, songId, req.user._id);

        res.status(200).json({
            message: 'Bài hát đã được thêm vào Playlist.',
            playlist: updatedPlaylist
        });

    } catch (error) {
        // Lỗi 400 cho các lỗi nghiệp vụ như "Bài hát đã có trong Playlist"
        res.status(400).json({ message: error.message });
    }
};

// @desc    Xóa Bài hát khỏi Playlist
// @route   PUT /api/playlists/remove
// @access  Private
const removeSongController = async (req, res) => {
    try {
        const { playlistId, songId } = req.body;

        if (!playlistId || !songId) {
            return res.status(400).json({ message: 'Cần cung cấp Playlist ID và Song ID.' });
        }

        const updatedPlaylist = await removeSongFromPlaylistService(playlistId, songId, req.user._id);

        res.status(200).json({
            message: 'Bài hát đã được xóa khỏi Playlist.',
            playlist: updatedPlaylist
        });

    } catch (error) {
        // Lỗi 400 cho các lỗi nghiệp vụ như "Playlist không tồn tại"
        res.status(400).json({ message: error.message });
    }
};


export {
    createPlaylistController,
    getUserPlaylistsController,
    deletePlaylistController,
    addSongController,
    removeSongController
};