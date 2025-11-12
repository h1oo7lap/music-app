// /backend/routes/playlistRoute.js

import express from 'express';
const router = express.Router();

import { 
    createPlaylistController,
    getUserPlaylistsController,
    deletePlaylistController,
    addSongController,
    removeSongController
} from '../controllers/playlistController.js';

import { protect } from '../middlewares/authMiddleware.js'; 

// Tất cả các route Playlist đều yêu cầu xác thực (protect) vì đây là dữ liệu cá nhân.

// @route   POST /api/playlists
// @desc    Tạo Playlist mới
// @access  Private
router.post('/', protect, createPlaylistController);

// @route   GET /api/playlists/my
// @desc    Lấy tất cả Playlists của User hiện tại
// @access  Private
router.get('/my', protect, getUserPlaylistsController);

// @route   DELETE /api/playlists/:id
// @desc    Xóa Playlist theo ID
// @access  Private
router.delete('/:id', protect, deletePlaylistController); 

// @route   PUT /api/playlists/add
// @desc    Thêm Bài hát vào Playlist
// @access  Private
router.put('/add', protect, addSongController); 

// @route   PUT /api/playlists/remove
// @desc    Xóa Bài hát khỏi Playlist
// @access  Private
router.put('/remove', protect, removeSongController); 

export default router;