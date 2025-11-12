// /backend/routes/authRoute.js
import express from 'express';
const router = express.Router();
import { registerUserController, loginUserController } from '../controllers/authController.js';

router.post('/register', registerUserController);
router.post('/login', loginUserController);

export default router;