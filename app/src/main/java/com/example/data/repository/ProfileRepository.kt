package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.R
import com.example.data.model.DefaultData
import com.example.data.model.SocialLinkItem
import com.example.data.model.SocialPlatform
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject

class ProfileRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("link_hub_profile_prefs", Context.MODE_PRIVATE)

    private val _profileState = MutableStateFlow(loadProfile())
    val profileState: StateFlow<UserProfile> = _profileState.asStateFlow()

    init {
        // Increment profile view count on launch
        val current = _profileState.value
        val updated = current.copy(totalViews = current.totalViews + 1)
        saveProfileInternal(updated)
    }

    private fun loadProfile(): UserProfile {
        var handle = prefs.getString("handle", "Shariful Islam") ?: "Shariful Islam"
        var displayName = prefs.getString("displayName", "Shariful Islam") ?: "Shariful Islam"
        val bio = prefs.getString("bio", "Welcome to my official links") ?: "Welcome to my official links"
        val bioBn = prefs.getString("bioBn", "আমার সকল অফিসিয়াল লিঙ্কে আপনাকে স্বাগতম") ?: "আমার সকল অফিসিয়াল লিঙ্কে আপনাকে স্বাগতম"
        val avatarUri = prefs.getString("avatarUri", null)
        val isVerified = prefs.getBoolean("isVerified", true)
        val totalViews = prefs.getInt("totalViews", 2450)
        var themeKey = prefs.getString("selectedThemeKey", "ROYAL_GOLD") ?: "ROYAL_GOLD"

        // Upgrade legacy profile if old placeholder was stored
        if (handle == "@lyuna" || displayName == "Lyuna") {
            handle = "Shariful Islam"
            displayName = "Shariful Islam"
            themeKey = "ROYAL_GOLD"
        }

        val linksJson = prefs.getString("links_json", null)
        val rawLinks = if (linksJson != null) {
            parseLinksJson(linksJson)
        } else {
            DefaultData.getDefaultLinks()
        }
        val links = rawLinks.map { link ->
            when (link.platform) {
                SocialPlatform.TIKTOK -> link.copy(
                    subtitle = "@sharifvai019 • Short Videos",
                    subtitleBn = "@sharifvai019 • শর্ট ভিডিও ও রিলস",
                    url = "https://www.tiktok.com/@sharifvai019",
                    appScheme = "snssdk1128://user/profile/sharifvai019"
                )
                SocialPlatform.FACEBOOK -> link.copy(
                    subtitle = "Shariful Islam • Facebook Profile",
                    subtitleBn = "শরিফুল ইসলাম • ফেসবুক প্রোফাইল",
                    url = "https://www.facebook.com/share/19YQpPG3DL/",
                    appScheme = "fb://facewebmodal/f?href=https://www.facebook.com/share/19YQpPG3DL/"
                )
                SocialPlatform.INSTAGRAM -> link.copy(
                    subtitle = "@sharifulislam.p.s • Photos & Stories",
                    subtitleBn = "@sharifulislam.p.s • ফটো ও এক্সক্লুসিভ স্টোরিজ",
                    url = "https://www.instagram.com/sharifulislam.p.s?xtok=MW80azY1NTRmdmdqMQ==",
                    appScheme = "instagram://user?username=sharifulislam.p.s"
                )
                SocialPlatform.TELEGRAM -> link.copy(
                    subtitle = "t.me/sharifvai0196",
                    subtitleBn = "t.me/sharifvai0196 • টেলিগ্রাম একাউন্ট",
                    url = "https://t.me/sharifvai0196",
                    appScheme = "tg://resolve?domain=sharifvai0196"
                )
                else -> link
            }
        }

        return UserProfile(
            handle = handle,
            displayName = displayName,
            bio = bio,
            bioBn = bioBn,
            avatarUri = avatarUri,
            isVerified = isVerified,
            totalViews = totalViews,
            selectedThemeKey = themeKey,
            links = links
        )
    }

    private fun parseLinksJson(jsonStr: String): List<SocialLinkItem> {
        val list = mutableListOf<SocialLinkItem>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val platformName = obj.optString("platform", SocialPlatform.OTHER.name)
                val platform = try {
                    SocialPlatform.valueOf(platformName)
                } catch (e: Exception) {
                    SocialPlatform.OTHER
                }

                val iconRes = when (platform) {
                    SocialPlatform.YOUTUBE -> R.drawable.ic_social_youtube
                    SocialPlatform.TIKTOK -> R.drawable.ic_social_tiktok
                    SocialPlatform.FACEBOOK -> R.drawable.ic_social_facebook
                    SocialPlatform.INSTAGRAM -> R.drawable.ic_social_instagram
                    SocialPlatform.TELEGRAM -> R.drawable.ic_social_telegram
                    else -> R.drawable.ic_social_generic
                }

                list.add(
                    SocialLinkItem(
                        id = obj.optString("id", "link_$i"),
                        platform = platform,
                        title = obj.optString("title", "Link"),
                        subtitle = obj.optString("subtitle", ""),
                        subtitleBn = obj.optString("subtitleBn", ""),
                        url = obj.optString("url", "https://"),
                        appScheme = if (obj.has("appScheme") && !obj.isNull("appScheme")) {
                            obj.getString("appScheme").takeIf { it.isNotBlank() }
                        } else null,
                        iconRes = iconRes,
                        accentColor = obj.optLong("accentColor", 0xFFE5B842),
                        clicks = obj.optInt("clicks", 0),
                        isEnabled = obj.optBoolean("isEnabled", true)
                    )
                )
            }
        } catch (e: Exception) {
            return DefaultData.getDefaultLinks()
        }
        return if (list.isEmpty()) DefaultData.getDefaultLinks() else list
    }

    private fun serializeLinksJson(links: List<SocialLinkItem>): String {
        val array = JSONArray()
        for (item in links) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("platform", item.platform.name)
            obj.put("title", item.title)
            obj.put("subtitle", item.subtitle)
            obj.put("subtitleBn", item.subtitleBn)
            obj.put("url", item.url)
            obj.put("appScheme", item.appScheme ?: "")
            obj.put("accentColor", item.accentColor)
            obj.put("clicks", item.clicks)
            obj.put("isEnabled", item.isEnabled)
            array.put(obj)
        }
        return array.toString()
    }

    private fun saveProfileInternal(profile: UserProfile) {
        prefs.edit()
            .putString("handle", profile.handle)
            .putString("displayName", profile.displayName)
            .putString("bio", profile.bio)
            .putString("bioBn", profile.bioBn)
            .putString("avatarUri", profile.avatarUri)
            .putBoolean("isVerified", profile.isVerified)
            .putInt("totalViews", profile.totalViews)
            .putString("selectedThemeKey", profile.selectedThemeKey)
            .putString("links_json", serializeLinksJson(profile.links))
            .apply()
        _profileState.value = profile
    }

    fun updateProfile(
        handle: String,
        displayName: String,
        bio: String,
        bioBn: String,
        avatarUri: String? = null
    ) {
        val current = _profileState.value
        val updated = current.copy(
            handle = handle,
            displayName = displayName,
            bio = bio,
            bioBn = bioBn,
            avatarUri = avatarUri ?: current.avatarUri
        )
        saveProfileInternal(updated)
    }

    fun setTheme(themeKey: String) {
        val current = _profileState.value
        saveProfileInternal(current.copy(selectedThemeKey = themeKey))
    }

    fun incrementClick(linkId: String) {
        val current = _profileState.value
        val updatedLinks = current.links.map {
            if (it.id == linkId) it.copy(clicks = it.clicks + 1) else it
        }
        saveProfileInternal(current.copy(links = updatedLinks))
    }

    fun updateLink(updatedItem: SocialLinkItem) {
        val current = _profileState.value
        val updatedLinks = current.links.map {
            if (it.id == updatedItem.id) updatedItem else it
        }
        saveProfileInternal(current.copy(links = updatedLinks))
    }

    fun addLink(newItem: SocialLinkItem) {
        val current = _profileState.value
        val updatedLinks = current.links + newItem
        saveProfileInternal(current.copy(links = updatedLinks))
    }

    fun deleteLink(linkId: String) {
        val current = _profileState.value
        val updatedLinks = current.links.filterNot { it.id == linkId }
        saveProfileInternal(current.copy(links = updatedLinks))
    }

    fun resetToDefaults() {
        val defaults = UserProfile(
            handle = "Shariful Islam",
            displayName = "Shariful Islam",
            bio = "Welcome to my official links",
            bioBn = "আমার সকল অফিসিয়াল লিঙ্কে আপনাকে স্বাগতম",
            avatarUri = null,
            isVerified = true,
            totalViews = 2450,
            selectedThemeKey = "ROYAL_GOLD",
            links = DefaultData.getDefaultLinks()
        )
        saveProfileInternal(defaults)
    }
}
