# Quick Setup for Real Device Testing

## 🎯 Single File to Change

**File**: `app/src/main/java/com/example/musique/utils/Constants.kt`

**Line 10**: Change this IP address:
```kotlin
private const val BASE_URL_HOST = "http://192.168.0.103:5000"
```

Replace `192.168.0.103` with **YOUR computer's IP address**.

---

## 📋 3-Step Setup

### 1️⃣ Get Your IP
```bash
ipconfig
```
Look for **IPv4 Address** (e.g., 192.168.0.105)

### 2️⃣ Update Constants.kt
```kotlin
// Line 10 in Constants.kt
private const val BASE_URL_HOST = "http://YOUR_IP_HERE:5000"
```

### 3️⃣ Rebuild App
- **Build** → **Clean Project**
- **Build** → **Rebuild Project**
- **Uninstall** old app from device
- **Run** app

---

## ✅ Pre-Flight Checklist

Before running the app:

- [ ] Backend is running: `npm run server`
- [ ] Computer and phone on **same WiFi**
- [ ] Firewall allows port 5000 (see below)
- [ ] Can access `http://YOUR_IP:5000/` in phone browser
- [ ] Updated `Constants.kt` with correct IP
- [ ] Rebuilt app after changing IP

---

## 🔥 Windows Firewall (One-Time Setup)

**PowerShell (Run as Administrator):**
```powershell
New-NetFirewallRule -DisplayName "Music Backend" -Direction Inbound -LocalPort 5000 -Protocol TCP -Action Allow
```

---

## 🧪 Test Backend Access

**From phone browser**, visit:
```
http://YOUR_IP:5000/
```

✅ **Should see**: "Music App API is running..."

❌ **If fails**: Network/firewall issue (not Android issue)

---

## 🚀 For Emulator

In `Constants.kt`, use:
```kotlin
private const val BASE_URL_HOST = "http://10.0.2.2:5000"
```

---

## 🐛 Troubleshooting

**"CLEARTEXT not permitted"**
→ Uninstall app completely and reinstall

**"Connection timeout"**
→ Check firewall, verify backend is running

**"Unable to resolve host"**
→ Wrong IP address, check `ipconfig` again

---

## 📱 Alternative: Use ngrok

Don't want to deal with IP/firewall?

```bash
# Install: https://ngrok.com/download
ngrok http 5000
```

Copy the HTTPS URL (e.g., `https://abc123.ngrok.io`)

In `Constants.kt`:
```kotlin
private const val BASE_URL_HOST = "https://abc123.ngrok.io"
```

✅ Works everywhere, no firewall config needed!

---

## 💡 Pro Tip

Create build variants for different environments:

**Constants.kt**
```kotlin
object Constants {
    // Automatically switch based on build variant
    private const val BASE_URL_HOST = when {
        BuildConfig.DEBUG -> "http://10.0.2.2:5000"      // Emulator
        else -> "https://yourproduction.com"              // Production
    }
    
    // Or use BuildConfig fields for easy switching
}
```
