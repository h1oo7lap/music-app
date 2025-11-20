// backend/utils/getSongDuration.js

import * as mm from 'music-metadata';

const getSongDuration = async (filePath) => {
    try {
        const metadata = await mm.parseFile(filePath);
        const duration = metadata.format.duration;

        if (typeof duration === 'number' && duration > 0) {
            return Math.floor(duration);
        }
        // Trả về 0 nếu không đọc được hoặc giá trị không hợp lệ
        return 0;
    } catch (error) {
        console.error(`Không thể trích xuất metadata từ file ${filePath}: ${error.message}`);
        return 0;
    }
};

export { getSongDuration };