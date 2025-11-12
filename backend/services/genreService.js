// /backend/services/genreService.js

import Genre from '../models/genreModel.js';

/**
 * Tạo thể loại mới
 */
const createGenreService = async ({ name }) => {
    // 1. Kiểm tra tồn tại (để tránh lỗi Mongoose Duplicate Key)
    const genreExists = await Genre.findOne({ name });
    if (genreExists) {
        throw new Error(`Thể loại "${name}" đã tồn tại.`);
    }

    // 2. Tạo và lưu Genre (Logic tạo slug sẽ được thêm vào Model sau)
    const genre = await Genre.create({ name });

    return genre;
};

/**
 * Lấy tất cả thể loại
 */
const getAllGenresService = async () => {
    const genres = await Genre.find({}).select('-__v'); // Loại bỏ trường __v
    return genres;
};

/**
 * Xóa thể loại theo ID
 */
const deleteGenreService = async (id) => {
    const genre = await Genre.findByIdAndDelete(id);

    if (!genre) {
        throw new Error('Không tìm thấy thể loại để xóa.');
    }
    return genre;
};

/**
 * Cập nhật thể loại theo ID
 */
const updateGenreService = async (id, name) => {
    // Tìm và cập nhật Genre
    const updatedGenre = await Genre.findByIdAndUpdate(
        id,
        { name }, // Chỉ cập nhật trường name
        { new: true, runValidators: true } // Trả về bản ghi mới, chạy validation
    );

    if (!updatedGenre) {
        throw new Error('Không tìm thấy thể loại để cập nhật.');
    }
    return updatedGenre;
};

/**
 * Lấy một thể loại theo ID
 */
const getGenreByIdService = async (id) => {
    const genre = await Genre.findById(id).select('-__v'); 

    if (!genre) {
        throw new Error('Không tìm thấy thể loại.');
    }
    return genre;
};

export { createGenreService, getAllGenresService, deleteGenreService, updateGenreService, getGenreByIdService };