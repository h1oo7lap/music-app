// /backend/models/genreModel.js

import mongoose from 'mongoose';
import slugify from 'slugify';

const genreSchema = new mongoose.Schema({
    name: {
        type: String,
        required: [true, 'Tên thể loại là bắt buộc.'],
        unique: true, // Thể loại không được trùng nhau
        trim: true,
        maxlength: 50,
    },
    slug: { // Để tạo URL thân thiện (ví dụ: /api/genres/pop-music)
        type: String,
        lowercase: true,
        unique: true,
    }
}, {
    timestamps: true
});

// 💡 Ghi chú: Chúng ta sẽ thêm logic tạo slug vào pre-save hook sau
// (Hiện tại chưa cần thiết, chỉ cần tạo Schema)

// 💡 PRE-SAVE HOOK: Tự động tạo slug trước khi lưu
genreSchema.pre('save', function (next) {
    if (!this.isModified('name')) { // Chỉ tạo/cập nhật slug nếu tên thay đổi
        next();
    }

    // Tự động tạo slug từ trường name
    this.slug = slugify(this.name, { lower: true, strict: true });
    next();
});

const Genre = mongoose.model('Genre', genreSchema);
export default Genre;