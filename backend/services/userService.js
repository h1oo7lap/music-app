// /backend/services/userService.js

import User from '../models/userModel.js';
// Bạn cần đảm bảo bạn đã import User model.

/**
 * Thêm hoặc xóa bài hát khỏi danh sách yêu thích
 * @param {string} userId - ID người dùng
 * @param {string} songId - ID bài hát
 */
const toggleFavoriteService = async (userId, songId) => {
    // 1. Tìm kiếm User
    const user = await User.findById(userId);

    if (!user) {
        throw new Error('Không tìm thấy người dùng.');
    }

    // 2. Kiểm tra xem bài hát đã có trong danh sách yêu thích chưa
    const isFavorite = user.favoriteSongs.includes(songId);

    if (isFavorite) {
        // Nếu đã thích -> Xóa khỏi danh sách (Pull)
        user.favoriteSongs.pull(songId);
    } else {
        // Nếu chưa thích -> Thêm vào danh sách (Push)
        user.favoriteSongs.push(songId);
    }

    await user.save();

    // 3. Lấy lại danh sách yêu thích đã được populate (điền dữ liệu bài hát)
    const updatedUser = await User.findById(userId)
        .populate('favoriteSongs', 'title artist duration imageUrl')
        .select('favoriteSongs');

    return updatedUser.favoriteSongs;
};

/**
 * Lấy danh sách bài hát yêu thích của User
 */
const getFavoritesService = async (userId) => {
    const user = await User.findById(userId)
        .populate('favoriteSongs', 'title artist duration imageUrl')
        .select('favoriteSongs');

    if (!user) {
        throw new Error('Không tìm thấy người dùng.');
    }
    return user.favoriteSongs;
};

export { toggleFavoriteService, getFavoritesService };