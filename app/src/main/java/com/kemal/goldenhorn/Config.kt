package com.kemal.goldenhorn

// ==========================================================================
// AYARLAR — iOS uygulamasıyla AYNI backend. Değerler gerçek.
// ==========================================================================
object Config {
    const val SUPABASE_URL = "https://ppnhzgghpxauazyafpfd.supabase.co"
    const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBwbmh6Z2docHhhdWF6eWFmcGZkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzQwNzg4NTYsImV4cCI6MjA4OTY1NDg1Nn0.SU56CC-8SNH2e6BB8F7xIVtXYvlII6Svjt9CSOCDpKY"

    // Takım giriş şifresi (admin paneli)
    const val TEAM_PASSWORD = "goldenapp2027Biobuzz"

    // FTC Events API — ftc-events.firstinspires.org/services/API/register'dan al
    // (WhatsApp botunda kullandığın değerlerin aynısı)
    const val FTC_USERNAME = "kemalgmsoy"   // <-- DOLDUR
    const val FTC_TOKEN    = "99F4B76A-8192-4BD5-B9ED-7330E2C9F24C"             // <-- DOLDUR
    const val FTC_SEASON   = 2026

    const val DEFAULT_TEAM_NUMBER = 25039
    const val DEFAULT_TEAM_NAME = "Golden Horn Jr"
}
