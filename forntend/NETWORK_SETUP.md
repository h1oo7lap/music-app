# Network Setup Guide for Real Device Testing

## Problem: CLEARTEXT Communication Error

When testing on a real Android device, you may see:
```
CLEARTEXT communication to <your-ip> not permitted by network security policy
```

## Solutions

### Option 1: Allow All Cleartext (Development Only - Current Setup)

The current `network_security_config.xml` allows all cleartext HTTP traffic:

```xml
<base-config cleartextTrafficPermitted="true">
```

✅ **Pros**: Works with any IP address, no configuration needed
❌ **Cons**: Less secure, not recommended for production

### Option 2: Specific Domain Allowlist (Recommended)

Replace the content of `res/xml/network_security_config.xml` with:

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <!-- Emulator localhost -->
        <domain includeSubdomains="true">10.0.2.2</domain>
        
        <!-- Local machine -->
        <domain includeSubdomains="true">localhost</domain>
        <domain includeSubdomains="true">127.0.0.1</domain>
        
        <!-- Your computer's IP - CHANGE THIS -->
        <domain includeSubdomains="true">192.168.0.103</domain>
    </domain-config>
</network-security-config>
```

✅ **Pros**: More secure, explicit control
❌ **Cons**: Must update when IP changes

### Option 3: Use HTTPS (Production)

For production, your backend should use HTTPS:

1. Get an SSL certificate (Let's Encrypt, etc.)
2. Configure backend with HTTPS
3. Update BASE_URL to `https://yourdomain.com/api/`
4. Remove `cleartextTrafficPermitted` from config

## Steps to Test on Real Device

### 1. Find Your Computer's IP Address

**Windows:**
```bash
ipconfig
```
Look for "IPv4 Address" (e.g., 192.168.0.103)

**Mac/Linux:**
```bash
ifconfig
# or
ip addr show
```

### 2. Update Backend to Accept Connections

Edit `backend/server.js` to ensure it listens on all interfaces:

```javascript
app.listen(PORT, '0.0.0.0', () => {         
    console.log(`🚀 Server running on port ${PORT}`);
});
```

### 3. Update Android App

Edit `forntend/app/src/main/java/com/example/musique/data/network/RetrofitClient.kt`:

```kotlin
private const val BASE_URL = "http://192.168.0.103:5000/api/"
```

Replace `192.168.0.103` with your actual IP address.

### 4. Update Network Security Config

If using Option 2, add your IP to `res/xml/network_security_config.xml`:

```xml
<domain includeSubdomains="true">192.168.0.103</domain>
```

### 5. Rebuild and Install

```bash
# In Android Studio: Build > Clean Project
# Then: Build > Rebuild Project
# Then: Run the app
```

**Important**: Sometimes you need to uninstall the old APK first:
```bash
adb uninstall com.example.musique
```

## Firewall Configuration

Make sure your firewall allows incoming connections on port 5000:

**Windows Firewall:**
```powershell
netsh advfirewall firewall add rule name="Node Backend" dir=in action=allow protocol=TCP localport=5000
```

**Mac:**
System Preferences > Security & Privacy > Firewall > Firewall Options > Add port 5000

**Linux (ufw):**
```bash
sudo ufw allow 5000/tcp
```

## Testing Connection

### From Your Phone's Browser

Before testing the app, verify the backend is reachable:

1. Open Chrome on your phone
2. Visit: `http://192.168.0.103:5000/`
3. You should see: "Music App API is running..."

If this doesn't work, the issue is network-related, not Android-specific.

### Common Issues

**Problem**: "Cannot connect to server"
- ✅ Check both devices are on same WiFi network
- ✅ Verify IP address is correct
- ✅ Ensure backend is running
- ✅ Check firewall settings

**Problem**: "Connection timeout"
- ✅ Backend might not be listening on 0.0.0.0
- ✅ Firewall blocking port 5000
- ✅ Wrong IP address

**Problem**: "CLEARTEXT not permitted" (even after fix)
- ✅ Uninstall app completely and reinstall
- ✅ Verify network_security_config.xml is in res/xml/
- ✅ Check AndroidManifest.xml has networkSecurityConfig attribute
- ✅ Clean and rebuild project

## Current Configuration Status

✅ AndroidManifest.xml has:
- `android:usesCleartextTraffic="true"`
- `android:networkSecurityConfig="@xml/network_security_config"`

✅ network_security_config.xml has:
- `base-config` allowing all cleartext (development mode)
- Specific domains for 192.168.0.103

✅ Required permissions added:
- INTERNET
- ACCESS_NETWORK_STATE

## Production Checklist

Before releasing to production:

- [ ] Remove `cleartextTrafficPermitted="true"`
- [ ] Use HTTPS only
- [ ] Remove development IP addresses
- [ ] Add proper SSL certificate pinning
- [ ] Use ProGuard/R8 obfuscation
- [ ] Implement certificate transparency
