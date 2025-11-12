// /backend/controllers/genreController.js

import { createGenreService, getAllGenresService, deleteGenreService, updateGenreService, getGenreByIdService } from '../services/genreService.js';
// @desc    Tạo thể loại mới
// @route   POST /api/genres
// @access  Private/Admin
const createGenreController = async (req, res, next) => {
    try {
        const { name } = req.body;

        if (!name) {
            return res.status(400).json({ message: 'Tên thể loại là bắt buộc.' });
        }

        // Gọi Service để tạo và lưu vào DB
        const newGenre = await createGenreService({ name });

        res.status(201).json({
            message: 'Thể loại được tạo thành công.',
            genre: newGenre,
        });

    } catch (error) {
        // Xử lý lỗi trùng tên (unique: true)
        if (error.message.includes('đã tồn tại')) {
            return res.status(400).json({ message: error.message });
        }
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

// @desc    Lấy tất cả thể loại (Không cần bảo mật)
// @route   GET /api/genres
// @access  Public
const getAllGenresController = async (req, res, next) => {
    try {
        const genres = await getAllGenresService();
        res.status(200).json(genres);
    } catch (error) {
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

// @desc    Xóa thể loại theo ID
// @route   DELETE /api/genres/:id
// @access  Private/Admin
const deleteGenreController = async (req, res, next) => {
    try {
        const deletedGenre = await deleteGenreService(req.params.id);
        
        res.status(200).json({
            message: 'Thể loại đã được xóa thành công.',
            genre: deletedGenre
        });

    } catch (error) {
        if (error.message.includes('Không tìm thấy')) {
            return res.status(404).json({ message: error.message });
        }
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

// @desc    Cập nhật thể loại theo ID
// @route   PUT /api/genres/:id
// @access  Private/Admin
const updateGenreController = async (req, res, next) => {
    try {
        const { name } = req.body;
        
        if (!name) {
            return res.status(400).json({ message: 'Tên thể loại mới là bắt buộc.' });
        }

        const updatedGenre = await updateGenreService(req.params.id, name);

        res.status(200).json({
            message: 'Thể loại đã được cập nhật thành công.',
            genre: updatedGenre
        });

    } catch (error) {
        if (error.message.includes('Không tìm thấy')) {
            return res.status(404).json({ message: error.message });
        }
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

// @desc    Lấy một thể loại theo ID
// @route   GET /api/genres/:id
// @access  Public
const getGenreByIdController = async (req, res, next) => {
    try {
        const genre = await getGenreByIdService(req.params.id);
        res.status(200).json(genre);
    } catch (error) {
        if (error.message.includes('Không tìm thấy')) {
            return res.status(404).json({ message: error.message });
        }
        // Xử lý lỗi định dạng ID không hợp lệ (CastError)
        if (error.name === 'CastError') {
             return res.status(400).json({ message: 'Định dạng ID thể loại không hợp lệ.' });
        }
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

export { createGenreController, getAllGenresController, deleteGenreController, updateGenreController, getGenreByIdController };