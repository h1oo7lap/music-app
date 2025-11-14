// /backend/routes/songRoute.js

import express from 'express';
const router = express.Router();

import { createSongController, getAllSongsController, deleteSongController, updateSongController, getSongByIdController, incrementPlayCountController, getTopSongsController } from '../controllers/songController.js';
import { protect } from '../middlewares/authMiddleware.js';
import { isAdmin } from '../middlewares/roleMiddleware.js';
import upload from '../config/multerConfig.js';

// Cấu hình Multer để nhận 2 field: songFile (tối đa 1 file) và albumImage (tối đa 1 file)
const songUploadMiddleware = upload.fields([
    { name: 'songFile', maxCount: 1 },
    { name: 'albumImage', maxCount: 1 }
]);

// @route   POST /api/songs
// Bảo vệ: Admin mới được thêm bài hát
// 💡 Thứ tự Middleware: Xác thực -> Upload -> Phân quyền -> Controller
router.post(
    '/',
    protect,
    isAdmin,
    songUploadMiddleware,
    createSongController
);

// @route   DELETE /api/songs/:id
// Bảo vệ: Admin mới được xóa bài hát
router.delete('/:id', protect, isAdmin, deleteSongController);

// @route   PUT /api/songs/:id
// Bảo vệ: Admin newcom mới được cập nhật bài hát`
router.put(
    '/:id',
    protect,
    isAdmin,
    songUploadMiddleware,
    updateSongController
);

// @route   GET /api/songs/top
// Công khai: Mọi người đều có thể xem bài hát
router.get('/top', getTopSongsController);

// @route   GET /api/songs/:id
// Công khai: Mọi người đều có thể xem bài hát
router.get('/:id', getSongByIdController);

// @route   POST /api/songs/:id/listen
// Công khai: Mọi người đều có thể xem bài hát
router.post('/:id/listen', incrementPlayCountController);

// @route   GET /api/songs
// Công khai: Mọi người đều có thể xem bài hát
router.get('/', getAllSongsController);

export default router;