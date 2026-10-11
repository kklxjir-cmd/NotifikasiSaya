const express = require('express')
const TelegramBot = require('node-telegram-bot-api')

// ⚙️ Ganti dengan punya kamu
const BOT_TOKEN = '8873087534:AAHsqTtBUJpOQcLXSa1aJNhc7JwIq0qiwNk'
const ADMIN_ID = 8341977261  // ID Telegram kamu
const PORT = 2093

const app = express()
app.use(express.json())
const bot = new TelegramBot(BOT_TOKEN, { polling: true })

// Kirim notifikasi ke Telegram
function kirimKeTelegram(judul, pesan) {
  const waktu = new Date().toLocaleString('id-ID', { timeZone: 'Asia/Jakarta' })
  return bot.sendMessage(ADMIN_ID,
`🔔 *${judul}*

${pesan}
⏰ ${waktu}`, { parse_mode: 'Markdown' })
}

// Terima data dari APK
app.post('/api/kirim-data', async (req, res) => {
  const { namaPerangkat, info } = req.body
  
  if (!namaPerangkat || !info) {
    return res.status(400).json({ sukses: false, pesan: 'Data tidak lengkap' })
  }

  try {
    await kirimKeTelegram('📱 Notifikasi dari Aplikasi',
      `📦 Perangkat: *${namaPerangkat}*\n\n${info}`
    )
    res.json({ sukses: true, pesan: 'Notifikasi terkirim!' })
  } catch (e) {
    res.status(500).json({ sukses: false, pesan: 'Gagal mengirim' })
  }
})

// Bot Command
bot.onText(/^\/start/, (msg) => {
  bot.sendMessage(msg.chat.id,
`🤖 *Notifikasi Bot Aktif!*

Selamat datang! Gunakan aplikasi pendamping di HP kamu,
dan info akan dikirim ke sini.

🆔 ID Kamu: \`${msg.chat.id}\`
`, { parse_mode: 'Markdown' })
})

// Jalankan server
kirimKeTelegram('✅ Bot Online', 'Server dan Bot Telegram siap menerima notifikasi.')
app.listen(PORT, () => console.log(`🚀 Server berjalan di port ${PORT}`))
  
