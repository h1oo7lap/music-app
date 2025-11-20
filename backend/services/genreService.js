// /backend/services/genreService.js

import Genre from '../models/genreModel.js';
import { ApiError } from '../utils/errorUtils.js'; // 🆕 Import ApiError

/**
 * Tạo thể loại mới
 */
const createGenreService = async ({ name }) => {
    // 1. Kiểm tra tồn tại (để tránh lỗi Mongoose Duplicate Key)
    const genreExists = await Genre.findOne({ name });
    if (genreExists) {
        // 🚨 Dùng ApiError 400 cho lỗi trùng lặp
        throw new ApiError(`Thể loại "${name}" đã tồn tại.`, 400);
    }

    // 2. Tạo và lưu Genre
    const genre = await Genre.create({ name });

    return genre;
};

/**
 * Lấy tất cả thể loại
 */
const getAllGenresService = async () => {
    const genres = await Genre.find({}).select('-__v');
    return genres;
};

/**
 * Xóa thể loại theo ID
 */
const deleteGenreService = async (id) => {
    const genre = await Genre.findByIdAndDelete(id);

    if (!genre) {
        // 🚨 Dùng ApiError 404 khi không tìm thấy
        throw new ApiError('Không tìm thấy thể loại để xóa.', 404);
    }
    return genre;
};

/**
 * Cập nhật thể loại theo ID
 */
const updateGenreService = async (id, name) => {

    // 💡 Tùy chọn: Kiểm tra trùng tên trước khi update
    const genreExists = await Genre.findOne({ name, _id: { $ne: id } });
    if (genreExists) {
        throw new ApiError(`Thể loại "${name}" đã tồn tại.`, 400);
    }

    // Tìm và cập nhật Genre
    const updatedGenre = await Genre.findByIdAndUpdate(
        id,
        { name },
        { new: true, runValidators: true }
    );

    if (!updatedGenre) {
        // 🚨 Dùng ApiError 404 khi không tìm thấy
        throw new ApiError('Không tìm thấy thể loại để cập nhật.', 404);
    }
    return updatedGenre;
};

/**
 * Lấy một thể loại theo ID
 */
const getGenreByIdService = async (id) => {
    const genre = await Genre.findById(id).select('-__v');

    if (!genre) {
        // 🚨 Dùng ApiError 404 khi không tìm thấy
        throw new ApiError('Không tìm thấy thể loại.', 404);
    }
    return genre;
};

export { createGenreService, getAllGenresService, deleteGenreService, updateGenreService, getGenreByIdService };