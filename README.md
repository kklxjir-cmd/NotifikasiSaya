# 🔔 Notifikasi Saya

Aplikasi Android + Server untuk mengirim notifikasi dari perangkat Anda ke Telegram secara aman dan transparan.

## ✅ Fitur
- Kirim notifikasi ke Telegram bot Anda sendiri
- Minta izin notifikasi secara terbuka & jujur
- Ada tombol uji coba manual
- Server Node.js + Express
- Build otomatis via GitHub Actions

## 🚀 Cara Menjalankan Server

```bash
npm install
node server.js
```

Jangan lupa ganti `BOT_TOKEN` dan `ADMIN_ID` di `server.js`!

## 📱 Cara Build APK

1. Upload semua file ke GitHub
2. Buka tab **Actions** → **Build Notifikasi Saya APK** → **Run workflow**
3. Tunggu beberapa menit → APK siap diunduh!

## ⚙️ Konfigurasi

- Ganti `IP_KAMU` di `MainActivity.kt` dengan IP server Anda
- Ganti `BOT_TOKEN` dan `ADMIN_ID` di `server.js`

## 📂 Struktur Folder

```
NotifikasiSaya/
├── server.js              # Server Node.js + Bot Telegram
├── package.json           # Dependensi server
├── README.md              # Ini
├── .github/workflows/
│   └── build.yml          # Build otomatis APK
└── android/
    ├── app/
    │   ├── build.gradle
    │   ├── proguard-rules.pro
    │   └── src/main/
    │       ├── AndroidManifest.xml
    │       ├── java/com/notifikasisaya/app/
    │       │   ├── MainActivity.kt
    │       │   └── MyNotificationService.kt
    │       └── res/layout/
    │           └── activity_main.xml
    ├── build.gradle
    ├── settings.gradle
    ├── gradle.properties
    ├── gradlew
    └── gradle/wrapper/
        └── gradle-wrapper.properties
```

## ⚠️ Catatan Penting
Aplikasi ini dibuat untuk **kepentingan pribadi yang legal**. Jangan gunakan untuk menipu atau merugikan orang lain.
