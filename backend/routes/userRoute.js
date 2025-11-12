// /backend/routes/userRoute.js

import express from 'express';
const router = express.Router();

import { toggleFavoriteController, getFavoritesController } from '../controllers/userController.js';
import { protect } from '../middlewares/authMiddleware.js';

// @route   PUT /api/users/favorites
// Thao tác: Thêm hoặc Xóa Bài hát Yêu thích (Toggle)
router.put('/favorites', protect, toggleFavoriteController);

// @route   GET /api/users/favorites
// Lấy danh sách Bài hát Yêu thích
router.get('/favorites', protect, getFavoritesController);

export default router;