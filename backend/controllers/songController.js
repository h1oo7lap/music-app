// /backend/controllers/songController.js

import { createSongService, getAllSongsService, deleteSongService, updateSongService, getSongByIdService, incrementPlayCountService, getTopSongsService } from '../services/songService.js'; // Sẽ tạo service này sau

// @desc    Tạo bài hát mới (bao gồm upload file)
// @route   POST /api/songs
// @access  Private/Admin
const createSongController = async (req, res, next) => {
    try {
        // Lấy dữ liệu văn bản từ req.body
        const { title, artist, genre, duration } = req.body;

        // 💡 Lấy đường dẫn file từ req.files (Multer gán vào)
        // Multer sẽ lưu trữ các thông tin này vào req.files:
        const songFile = req.files['songFile'] ? req.files['songFile'][0] : null;
        const albumImage = req.files['albumImage'] ? req.files['albumImage'][0] : null;

        // 1. Kiểm tra dữ liệu bắt buộc
        if (!title || !artist || !genre || !songFile) {
            // 🚨 Chúng ta cần xóa file đã upload nếu kiểm tra thất bại ở đây (logic sẽ thêm sau)
            return res.status(400).json({ message: 'Tiêu đề, nghệ sĩ, thể loại và file nhạc là bắt buộc.' });
        }

        // 2. Chuẩn bị dữ liệu cho Service
        const songData = {
            title,
            artist,
            genre,
            duration: Number(duration),
            songUrl: songFile.path,         // Đường dẫn file MP3 cục bộ
            imageUrl: albumImage ? albumImage.path : null, // Đường dẫn ảnh bìa cục bộ
            // userId: req.user._id, // Nếu bạn muốn lưu ID người tạo
        };

        // 3. Gọi Service để lưu metadata vào DB
        const newSong = await createSongService(songData);

        res.status(201).json({
            message: 'Bài hát được thêm thành công.',
            song: newSong,
        });

    } catch (error) {
        // ... (Xử lý lỗi)
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

// @desc    Lấy tất cả bài hát
// @route   GET /api/songs
// @access  Public

// const getAllSongsController = async (req, res, next) => {
//     try {
//         const songs = await getAllSongsService();
//         res.status(200).json(songs);
//     } catch (error) {
//         res.status(500).json({ message: 'Server error: ' + error.message });
//     }
// };

const getAllSongsController = async (req, res, next) => {
    try {
        // Lấy tham số truy vấn (Query Params) từ req.query
        const keyword = req.query.keyword || '';
        // parseInt để đảm bảo page và limit là số nguyên.
        const page = parseInt(req.query.page) || 1;
        const limit = parseInt(req.query.limit) || 10;

        // Gọi service với các tham số mới
        const data = await getAllSongsService(keyword, page, limit);

        res.status(200).json(data); // Trả về đối tượng data đã có thông tin phân trang

    } catch (error) {
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

// @desc    Xóa bài hát theo ID
// @route   DELETE /api/songs/:id
// @access  Private/Admin
const deleteSongController = async (req, res, next) => {
    try {
        const deletedSong = await deleteSongService(req.params.id);

        res.status(200).json({
            message: 'Bài hát đã được xóa thành công và các file đã được gỡ bỏ.',
            song: deletedSong
        });

    } catch (error) {
        if (error.message.includes('Không tìm thấy')) {
            return res.status(404).json({ message: error.message });
        }
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

// @desc    Cập nhật bài hát theo ID (hỗ trợ upload file)
// @route   PUT /api/songs/:id
// @access  Private/Admin
const updateSongController = async (req, res, next) => {
    try {
        const { title, artist, genre, duration } = req.body;

        // Dữ liệu mới được gửi
        const updatedData = {
            title,
            artist,
            genre,
            duration: duration ? Number(duration) : undefined,
        };

        // 💡 Kiểm tra nếu có file mới, cập nhật đường dẫn vào updatedData
        if (req.files && req.files['songFile']) {
            updatedData.songUrl = req.files['songFile'][0].path;
        }
        if (req.files && req.files['albumImage']) {
            updatedData.imageUrl = req.files['albumImage'][0].path;
        }

        const updatedSong = await updateSongService(req.params.id, updatedData);

        res.status(200).json({
            message: 'Bài hát đã được cập nhật thành công.',
            song: updatedSong
        });

    } catch (error) {
        // ... (Xử lý lỗi)
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

// @desc    Lấy một bài hát theo ID
// @route   GET /api/songs/:id
// @access  Public
const getSongByIdController = async (req, res, next) => {
    try {
        const song = await getSongByIdService(req.params.id);
        res.status(200).json(song);
    } catch (error) {
        if (error.message.includes('Không tìm thấy')) {
            return res.status(404).json({ message: error.message });
        }
        // Xử lý lỗi định dạng ID không hợp lệ (CastError)
        if (error.name === 'CastError') {
            return res.status(400).json({ message: 'Định dạng ID bài hát không hợp lệ.' });
        }
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

// @desc    Tăng lượt nghe của bài hát
// @route   POST /api/songs/:id/listen
// @access  Public (Tùy chọn: có thể để Public hoặc Private)
const incrementPlayCountController = async (req, res, next) => {
    try {
        const songId = req.params.id;

        const newPlayCount = await incrementPlayCountService(songId);

        res.status(200).json({
            message: 'Lượt nghe đã được cập nhật thành công.',
            playCount: newPlayCount
        });

    } catch (error) {
        if (error.message.includes('Không tìm thấy')) {
            return res.status(404).json({ message: error.message });
        }
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

// @desc    Lấy top N bài hát được nghe nhiều nhất
// @route   GET /api/songs/top?limit=N
// @access  Public
const getTopSongsController = async (req, res, next) => {
    try {
        // Lấy tham số limit từ query (mặc định là 10)
        const limit = parseInt(req.query.limit) || 10;

        const topSongs = await getTopSongsService(limit);

        res.status(200).json(topSongs);

    } catch (error) {
        res.status(500).json({ message: 'Server error: ' + error.message });
    }
};

export { createSongController, getAllSongsController, deleteSongController, updateSongController, getSongByIdController, incrementPlayCountController, getTopSongsController };