# Test Device Connection - Quick Guide

## Step-by-Step Fix for "CLEARTEXT communication not permitted"

### ✅ What I've Already Fixed

1. ✅ Updated `network_security_config.xml` to allow cleartext traffic
2. ✅ Added `<base-config cleartextTrafficPermitted="true">` for all HTTP
3. ✅ Added your IP (192.168.0.103) to allowed domains
4. ✅ Updated backend to listen on `0.0.0.0` (all interfaces)
5. ✅ AndroidManifest has all required permissions

### 🚀 What You Need to Do Now

#### Step 1: Verify Your IP Address
```bash
# On Windows
ipconfig

# Look for "IPv4 Address" under your WiFi adapter
# Example: 192.168.0.103
```

#### Step 2: Update Android App
Edit: `forntend/app/src/main/java/com/example/musique/data/network/RetrofitClient.kt`

Change line 13:
```kotlin
private const val BASE_URL = "http://192.168.0.103:5000/api/"
```
Replace `192.168.0.103` with YOUR actual IP.

#### Step 3: Restart Backend
```bash
cd backend
npm run server
```

You should see:
```
🚀 Server running on port 5000
📱 Access from device: http://192.168.0.103:5000
💻 Access locally: http://localhost:5000
```

#### Step 4: Test Backend from Phone

**IMPORTANT**: Before rebuilding the app, test if backend is reachable:

1. Open **Chrome** on your phone
2. Type: `http://192.168.0.103:5000/`
3. You should see: `"Music App API is running..."`

**If this doesn't work**, your issue is network-related:
- ✅ Ensure phone and computer are on **same WiFi**
- ✅ Disable VPN on both devices
- ✅ Check Windows Firewall (see below)

#### Step 5: Rebuild Android App

In Android Studio:
1. **Build** → **Clean Project**
2. **Build** → **Rebuild Project**
3. **Uninstall old app** from device (important!)
4. **Run** → Install fresh APK

### 🔥 Windows Firewall Fix

If backend is unreachable from phone, add firewall rule:

**Method 1: PowerShell (Run as Administrator)**
```powershell
New-NetFirewallRule -DisplayName "Node Music Backend" -Direction Inbound -LocalPort 5000 -Protocol TCP -Action Allow
```

**Method 2: GUI**
1. Windows Security → Firewall & network protection
2. Advanced settings → Inbound Rules → New Rule
3. Port → TCP → Specific local ports: 5000
4. Allow the connection → Apply to all profiles
5. Name: "Music App Backend"

### 🧪 Testing Checklist

Run through these tests:

- [ ] Backend starts without errors
- [ ] Can access `http://localhost:5000/` in PC browser
- [ ] Can access `http://192.168.0.103:5000/` in phone browser
- [ ] Phone and PC on same WiFi network
- [ ] No VPN active on either device
- [ ] Firewall rule added for port 5000
- [ ] Android app uninstalled before reinstall
- [ ] App rebuilt after changing BASE_URL

### 🐛 Still Not Working?

#### Error: "CLEARTEXT communication not permitted"

**Solution**: The app was cached. Do this:

```bash
# Uninstall via ADB
adb uninstall com.example.musique

# Clear Android Studio cache
# File → Invalidate Caches → Invalidate and Restart

# Rebuild and reinstall
```

#### Error: "Unable to resolve host"

**Solution**: Wrong IP or network issue

1. Verify IP: `ipconfig` (Windows) or `ifconfig` (Mac/Linux)
2. Ping from phone to PC (use Network Analyzer app)
3. Check if phone can access other services on PC

#### Error: "Connection timeout"

**Solution**: Firewall or backend not listening

1. Check backend console for "Server running" message
2. Test: `curl http://localhost:5000` on PC
3. Add firewall rule (see above)
4. Temporarily disable Windows Firewall to test

#### Error: "Failed to connect to /192.168.0.103:5000"

**Solution**: Backend might not be listening on correct interface

Already fixed in `server.js` with:
```javascript
app.listen(PORT, '0.0.0.0', ...)
```

Restart backend to apply.

### 📱 Alternative: Use ngrok (Easiest)

If network issues persist, use ngrok to expose backend:

```bash
# Install ngrok: https://ngrok.com/download

# In backend directory
ngrok http 5000
```

You'll get a URL like: `https://abc123.ngrok.io`

Update RetrofitClient.kt:
```kotlin
private const val BASE_URL = "https://abc123.ngrok.io/api/"
```

**Advantages:**
- ✅ Works with HTTPS (no cleartext issues)
- ✅ No firewall configuration needed
- ✅ Works from anywhere, not just local network

**Disadvantages:**
- ❌ URL changes each time (free plan)
- ❌ Requires internet connection
- ❌ Slightly slower

### ✨ Final Verification

Once everything works, test these in the app:

1. **Login Screen**
   - Register new account
   - Login with credentials
   - Check token is stored

2. **Home Screen**
   - See top songs list
   - Album images load
   - Click play button

3. **Search Screen**
   - Type to search songs
   - Results update in real-time

4. **Profile Screen**
   - See your username
   - Logout works

5. **Music Playback**
   - Song plays audio
   - Notification appears
   - Can pause/resume

### 📞 Need Help?

Check these logs:

**Backend logs**:
```bash
# In backend terminal - watch for errors
```

**Android logs**:
```bash
# In Android Studio Logcat, filter by "Musique"
# Look for network errors or JSON parsing issues
```

**Network test**:
```bash
# From PC, test if backend responds
curl http://localhost:5000/api/songs

# Should return JSON with songs
```

---

**Current Status:**
- ✅ Network security config allows 192.168.0.103
- ✅ Backend listens on 0.0.0.0:5000
- ✅ All permissions in manifest
- ⚠️ You need to: Update RetrofitClient BASE_URL and rebuild
