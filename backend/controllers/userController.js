// /backend/controllers/userController.js

import asyncHandler from 'express-async-handler';
import { toggleFavoriteService, getFavoritesService } from '../services/userService.js';

// @desc    Thêm/Xóa Bài hát Yêu thích (Toggle)
// @route   PUT /api/users/favorites
// @access  Private
const toggleFavoriteController = asyncHandler(async (req, res) => {
    const { songId } = req.body;

    if (!songId) {
        res.status(400); // Đặt status code
        throw new Error('Cần cung cấp Song ID.'); // Ném lỗi để Middleware bắt
    }

    // Lỗi 404 (Không tìm thấy User/Song) và CastError (ID không hợp lệ) được Service throw ra
    const favorites = await toggleFavoriteService(req.user._id, songId);

    // Xác định hành động để trả về thông báo phù hợp
    const isCurrentlyFavorite = favorites.some(song => song._id.equals(songId));
    const action = isCurrentlyFavorite ? 'thêm vào' : 'gỡ bỏ khỏi';

    res.status(200).json({
        message: `Bài hát đã được ${action} danh sách yêu thích.`,
        favorites: favorites
    });
});

// @desc    Lấy danh sách Bài hát Yêu thích
// @route   GET /api/users/favorites
// @access  Private
const getFavoritesController = asyncHandler(async (req, res) => {
    // Lỗi 500 (Server Error) sẽ được asyncHandler bắt
    const favorites = await getFavoritesService(req.user._id);
    res.status(200).json(favorites);
});

export { toggleFavoriteController, getFavoritesController };