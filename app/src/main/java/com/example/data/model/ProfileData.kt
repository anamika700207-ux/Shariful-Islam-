package com.example.data.model

import com.example.R

enum class SocialPlatform {
    YOUTUBE,
    TIKTOK,
    FACEBOOK,
    INSTAGRAM,
    TELEGRAM,
    WEBSITE,
    OTHER
}

data class SocialLinkItem(
    val id: String,
    val platform: SocialPlatform,
    val title: String,
    val subtitle: String,
    val subtitleBn: String,
    val url: String,
    val appScheme: String? = null,
    val iconRes: Int,
    val accentColor: Long,
    val clicks: Int = 0,
    val isEnabled: Boolean = true
)

data class AppThemeOption(
    val key: String,
    val nameEn: String,
    val nameBn: String,
    val primaryColor: Long,
    val glowColor: Long,
    val surfaceColor: Long = 0xFF141310,
    val strokeColor: Long = 0xFF3D3318
)

data class UserProfile(
    val handle: String = "Shariful Islam",
    val displayName: String = "Shariful Islam",
    val bio: String = "Welcome to my official links",
    val bioBn: String = "আমার সকল অফিসিয়াল লিঙ্কে আপনাকে স্বাগতম",
    val avatarUri: String? = null,
    val isVerified: Boolean = true,
    val totalViews: Int = 2450,
    val selectedThemeKey: String = "ROYAL_GOLD",
    val links: List<SocialLinkItem> = emptyList()
)

object DefaultData {
    val themes = listOf(
        AppThemeOption(
            key = "ROYAL_GOLD",
            nameEn = "Royal Gold",
            nameBn = "রয়্যাল গোল্ড",
            primaryColor = 0xFFE5B842,
            glowColor = 0xFFFFD700,
            surfaceColor = 0xFF141310,
            strokeColor = 0xFF3E331A
        ),
        AppThemeOption(
            key = "CYBER_RED",
            nameEn = "Cyber Crimson",
            nameBn = "সাইবার লাল",
            primaryColor = 0xFFFF2A55,
            glowColor = 0xFFFF1744,
            surfaceColor = 0xFF131118,
            strokeColor = 0xFF351C26
        ),
        AppThemeOption(
            key = "NEON_CYAN",
            nameEn = "Electric Cyan",
            nameBn = "ইলেকট্রিক নীল",
            primaryColor = 0xFF00F2FE,
            glowColor = 0xFF4FACFE,
            surfaceColor = 0xFF0F151E,
            strokeColor = 0xFF173045
        ),
        AppThemeOption(
            key = "EMERALD_MATRIX",
            nameEn = "Matrix Emerald",
            nameBn = "ম্যাট্রিক্স সবুজ",
            primaryColor = 0xFF00E676,
            glowColor = 0xFF00C853,
            surfaceColor = 0xFF0E1712,
            strokeColor = 0xFF163723
        ),
        AppThemeOption(
            key = "ROYAL_VIOLET",
            nameEn = "Royal Violet",
            nameBn = "রয়েল বেগুনি",
            primaryColor = 0xFFBD00FF,
            glowColor = 0xFF9D00E0,
            surfaceColor = 0xFF14101E,
            strokeColor = 0xFF2C1945
        )
    )

    fun getDefaultLinks(): List<SocialLinkItem> {
        return listOf(
            SocialLinkItem(
                id = "youtube_1",
                platform = SocialPlatform.YOUTUBE,
                title = "YouTube",
                subtitle = "Shariful Islam • Official Channel",
                subtitleBn = "শরিফুল ইসলাম • অফিসিয়াল চ্যানেল",
                url = "https://youtube.com/@sharifulislam",
                appScheme = "vnd.youtube:https://youtube.com/@sharifulislam",
                iconRes = R.drawable.ic_social_youtube,
                accentColor = 0xFFFF0000,
                clicks = 980
            ),
            SocialLinkItem(
                id = "tiktok_1",
                platform = SocialPlatform.TIKTOK,
                title = "TikTok",
                subtitle = "@sharifvai019 • Short Videos",
                subtitleBn = "@sharifvai019 • শর্ট ভিডিও ও রিলস",
                url = "https://www.tiktok.com/@sharifvai019",
                appScheme = "snssdk1128://user/profile/sharifvai019",
                iconRes = R.drawable.ic_social_tiktok,
                accentColor = 0xFF25F4EE,
                clicks = 1420
            ),
            SocialLinkItem(
                id = "facebook_1",
                platform = SocialPlatform.FACEBOOK,
                title = "Facebook",
                subtitle = "Shariful Islam • Facebook Profile",
                subtitleBn = "শরিফুল ইসলাম • ফেসবুক প্রোফাইল",
                url = "https://www.facebook.com/share/19YQpPG3DL/",
                appScheme = "fb://facewebmodal/f?href=https://www.facebook.com/share/19YQpPG3DL/",
                iconRes = R.drawable.ic_social_facebook,
                accentColor = 0xFF1877F2,
                clicks = 750
            ),
            SocialLinkItem(
                id = "instagram_1",
                platform = SocialPlatform.INSTAGRAM,
                title = "Instagram",
                subtitle = "@sharifulislam.p.s • Photos & Stories",
                subtitleBn = "@sharifulislam.p.s • ফটো ও এক্সক্লুসিভ স্টোরিজ",
                url = "https://www.instagram.com/sharifulislam.p.s?xtok=MW80azY1NTRmdmdqMQ==",
                appScheme = "instagram://user?username=sharifulislam.p.s",
                iconRes = R.drawable.ic_social_instagram,
                accentColor = 0xFFE1306C,
                clicks = 890
            ),
            SocialLinkItem(
                id = "telegram_1",
                platform = SocialPlatform.TELEGRAM,
                title = "Telegram",
                subtitle = "t.me/sharifvai0196",
                subtitleBn = "t.me/sharifvai0196 • টেলিগ্রাম একাউন্ট",
                url = "https://t.me/sharifvai0196",
                appScheme = "tg://resolve?domain=sharifvai0196",
                iconRes = R.drawable.ic_social_telegram,
                accentColor = 0xFF229ED9,
                clicks = 520
            )
        )
    }
}
