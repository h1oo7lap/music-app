// /backend/routes/genreRoute.js

import express from 'express';
const router = express.Router();

import { createGenreController, getAllGenresController, deleteGenreController, updateGenreController, getGenreByIdController } from '../controllers/genreController.js';
import { protect } from '../middlewares/authMiddleware.js';
import { isAdmin } from '../middlewares/roleMiddleware.js';

// @route   POST /api/genres
// Bảo vệ: Chỉ Admin mới được tạo thể loại
router.post('/', protect, isAdmin, createGenreController);

// @route   DELETE /api/genres/:id
// Bảo vệ: Chỉ Admin mới được xóa thể loại  
router.delete('/:id', protect, isAdmin, deleteGenreController);

// @route   PUT /api/genres/:id
// Bảo vệ: Chỉ Admin mới được cập nhật thể loại
router.put('/:id', protect, isAdmin, updateGenreController);

// @route   GET /api/genres/:id 
// Công khai: Mọi người đều có thể xem thể loại
router.get('/:id', getGenreByIdController);

// @route   GET /api/genres
// Công khai: Mọi người đều có thể xem thể loại
router.get('/', getAllGenresController);

export default router;