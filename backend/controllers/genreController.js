// /backend/controllers/genreController.js

import asyncHandler from 'express-async-handler';
import { createGenreService, getAllGenresService, deleteGenreService, updateGenreService, getGenreByIdService } from '../services/genreService.js';

// @desc    Tạo thể loại mới
// @route   POST /api/genres
// @access  Private/Admin
const createGenreController = asyncHandler(async (req, res, next) => {
    const { name } = req.body;

    if (!name) {
        res.status(400);
        throw new Error('Tên thể loại là bắt buộc.');
    }

    // Lỗi trùng tên sẽ được Service throw ra và errorMiddleware bắt
    const newGenre = await createGenreService({ name });

    res.status(201).json({
        message: 'Thể loại được tạo thành công.',
        genre: newGenre,
    });
});

// @desc    Lấy tất cả thể loại (Không cần bảo mật)
// @route   GET /api/genres
// @access  Public
const getAllGenresController = asyncHandler(async (req, res, next) => {
    // Lỗi 500 sẽ được asyncHandler bắt
    const genres = await getAllGenresService();
    res.status(200).json(genres);
});

// @desc    Xóa thể loại theo ID
// @route   DELETE /api/genres/:id
// @access  Private/Admin
const deleteGenreController = asyncHandler(async (req, res, next) => {
    // Logic 404 (Không tìm thấy) và CastError (ID không hợp lệ) được chuyển sang Middleware
    const deletedGenre = await deleteGenreService(req.params.id);

    res.status(200).json({
        message: 'Thể loại đã được xóa thành công.',
        genre: deletedGenre
    });
});

// @desc    Cập nhật thể loại theo ID
// @route   PUT /api/genres/:id
// @access  Private/Admin
const updateGenreController = asyncHandler(async (req, res, next) => {
    const { name } = req.body;

    if (!name) {
        res.status(400);
        throw new Error('Tên thể loại mới là bắt buộc.');
    }

    // Logic 404 (Không tìm thấy) và lỗi trùng lặp tên được chuyển sang Middleware/Service
    const updatedGenre = await updateGenreService(req.params.id, name);

    res.status(200).json({
        message: 'Thể loại đã được cập nhật thành công.',
        genre: updatedGenre
    });
});

// @desc    Lấy một thể loại theo ID
// @route   GET /api/genres/:id
// @access  Public
const getGenreByIdController = asyncHandler(async (req, res, next) => {
    // Logic 404 và CastError được chuyển sang Middleware
    const genre = await getGenreByIdService(req.params.id);
    res.status(200).json(genre);
});

export { createGenreController, getAllGenresController, deleteGenreController, updateGenreController, getGenreByIdController };