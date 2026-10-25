package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.DefaultData
import com.example.data.model.SocialPlatform
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Shariful Islam", appName)
  }

  @Test
  fun `verify all requested social platforms exist in default data`() {
    val links = DefaultData.getDefaultLinks()
    val platforms = links.map { it.platform }
    assertTrue(platforms.contains(SocialPlatform.YOUTUBE))
    assertTrue(platforms.contains(SocialPlatform.TIKTOK))
    assertTrue(platforms.contains(SocialPlatform.FACEBOOK))
    assertTrue(platforms.contains(SocialPlatform.INSTAGRAM))
    assertTrue(platforms.contains(SocialPlatform.TELEGRAM))
  }

  @Test
  fun `verify social link urls match user specified links`() {
    val links = DefaultData.getDefaultLinks()
    val instagram = links.first { it.platform == SocialPlatform.INSTAGRAM }
    assertEquals("https://www.instagram.com/sharifulislam.p.s?xtok=MW80azY1NTRmdmdqMQ==", instagram.url)

    val tiktok = links.first { it.platform == SocialPlatform.TIKTOK }
    assertEquals("https://www.tiktok.com/@sharifvai019", tiktok.url)

    val facebook = links.first { it.platform == SocialPlatform.FACEBOOK }
    assertEquals("https://www.facebook.com/share/19YQpPG3DL/", facebook.url)

    val telegram = links.first { it.platform == SocialPlatform.TELEGRAM }
    assertEquals("https://t.me/sharifvai0196", telegram.url)
    assertEquals("t.me/sharifvai0196", telegram.subtitle)
  }
}
