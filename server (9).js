const express = require('express')
const TelegramBot = require('node-telegram-bot-api')

// ⚙️ Ganti dengan punya kamu
const BOT_TOKEN = 'ISI_TOKEN_BOT_TELEGRAM_KAMU'
const ADMIN_ID = 123456789  // ID Telegram kamu
const PORT = 2093

const app = express()
app.use(express.json())
const bot = new TelegramBot(BOT_TOKEN, { polling: true })

// ✅ KIRIM NOTIFIKASI KE TELEGRAM
function kirimKeTelegram(judul, pesan) {
  const waktu = new Date().toLocaleString('id-ID', { timeZone: 'Asia/Jakarta' })
  return bot.sendMessage(ADMIN_ID,
`🔔 *${judul}*

${pesan}
⏰ ${waktu}`, { parse_mode: 'Markdown' })
}

// ✅ TERIMA DATA DARI APK
app.post('/api/kirim-data', async (req, res) => {
  const { namaPerangkat, info, tipe } = req.body
  
  if (!namaPerangkat || !info) {
    return res.status(400).json({ sukses: false, pesan: 'Data tidak lengkap' })
  }

  try {
    await kirimKeTelegram(
      tipe === 'notifikasi' ? '📱 Notifikasi dari Aplikasi' : '📡 Info Perangkat',
      `📦 Perangkat: *${namaPerangkat}*\n\n${info}`
    )
    res.json({ sukses: true, pesan: 'Notifikasi terkirim!' })
  } catch (e) {
    res.status(500).json({ sukses: false, pesan: 'Gagal mengirim' })
  }
})

// ✅ BOT COMMAND
bot.onText(/^\/start/, (msg) => {
  bot.sendMessage(msg.chat.id,
`🤖 *Notifikasi Bot Aktif!*

Selamat datang! Gunakan aplikasi pendamping di HP kamu, 
dan info akan dikirim ke sini secara otomatis.

🆔 ID Kamu: \`${msg.chat.id}\`
`, { parse_mode: 'Markdown' })
})

bot.onText(/^\/status/, (msg) => {
  bot.sendMessage(msg.chat.id, '✅ Server berjalan normal. Siap menerima notifikasi dari aplikasi!')
})

// ✅ JALANKAN
kirimKeTelegram('✅ Bot Online', 'Server dan Bot Telegram sudah siap menerima notifikasi dari aplikasi.')
app.listen(PORT, () => console.log(`🚀 Server berjalan di port ${PORT}`))
