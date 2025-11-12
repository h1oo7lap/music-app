// /backend/controllers/userController.js

import { toggleFavoriteService, getFavoritesService } from '../services/userService.js';

// @desc    Thêm/Xóa Bài hát Yêu thích (Toggle)
// @route   PUT /api/users/favorites
// @access  Private
const toggleFavoriteController = async (req, res) => {
    try {
        const { songId } = req.body;

        if (!songId) {
            return res.status(400).json({ message: 'Cần cung cấp Song ID.' });
        }

        const favorites = await toggleFavoriteService(req.user._id, songId);

        // Xác định hành động để trả về thông báo phù hợp
        const isCurrentlyFavorite = favorites.some(song => song._id.equals(songId));
        const action = isCurrentlyFavorite ? 'thêm vào' : 'gỡ bỏ khỏi';

        res.status(200).json({
            message: `Bài hát đã được ${action} danh sách yêu thích.`,
            favorites: favorites
        });

    } catch (error) {
        res.status(400).json({ message: error.message });
    }
};

// @desc    Lấy danh sách Bài hát Yêu thích
// @route   GET /api/users/favorites
// @access  Private
const getFavoritesController = async (req, res) => {
    try {
        const favorites = await getFavoritesService(req.user._id);
        res.status(200).json(favorites);
    } catch (error) {
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

export { toggleFavoriteController, getFavoritesController };