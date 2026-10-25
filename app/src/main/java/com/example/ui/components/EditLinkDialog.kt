package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.model.SocialLinkItem
import com.example.data.model.SocialPlatform
import com.example.ui.theme.OrbitronFontFamily
import com.example.ui.theme.PlusJakartaFontFamily

@Composable
fun EditLinkDialog(
    itemToEdit: SocialLinkItem?,
    isBangla: Boolean,
    primaryColor: Color,
    onDismiss: () -> Unit,
    onSaveLink: (SocialLinkItem) -> Unit,
    onDeleteLink: ((String) -> Unit)? = null
) {
    val isNew = itemToEdit == null
    var selectedPlatform by remember {
        mutableStateOf(itemToEdit?.platform ?: SocialPlatform.YOUTUBE)
    }
    var title by remember {
        mutableStateOf(itemToEdit?.title ?: "YouTube")
    }
    var subtitle by remember {
        mutableStateOf(itemToEdit?.subtitle ?: "@lyuna")
    }
    var subtitleBn by remember {
        mutableStateOf(itemToEdit?.subtitleBn ?: "")
    }
    var url by remember {
        mutableStateOf(itemToEdit?.url ?: "https://youtube.com/@lyuna")
    }

    val platforms = listOf(
        SocialPlatform.YOUTUBE to ("YouTube" to R.drawable.ic_social_youtube),
        SocialPlatform.TIKTOK to ("TikTok" to R.drawable.ic_social_tiktok),
        SocialPlatform.FACEBOOK to ("Facebook" to R.drawable.ic_social_facebook),
        SocialPlatform.INSTAGRAM to ("Instagram" to R.drawable.ic_social_instagram),
        SocialPlatform.TELEGRAM to ("Telegram" to R.drawable.ic_social_telegram),
        SocialPlatform.WEBSITE to ("Website" to R.drawable.ic_social_generic)
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF13101C)),
            border = BorderStroke(1.5.dp, primaryColor.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("edit_link_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isNew) Icons.Default.AddLink else Icons.Default.Edit,
                            contentDescription = null,
                            tint = primaryColor,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isNew) {
                                if (isBangla) "নতুন প্ল্যাটফর্ম লিঙ্ক যোগ করুন" else "Add New Link"
                            } else {
                                if (isBangla) "লিঙ্ক সম্পাদনা করুন" else "Edit Link"
                            },
                            fontFamily = OrbitronFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Platform selector pills
                Text(
                    text = if (isBangla) "প্ল্যাটফর্ম নির্বাচন করুন" else "Select Platform",
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFA09EB0)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(platforms) { (platform, data) ->
                        val (pName, iconRes) = data
                        val isSelected = selectedPlatform == platform
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) primaryColor.copy(alpha = 0.25f) else Color(0xFF1B1728),
                            border = BorderStroke(
                                if (isSelected) 1.5.dp else 1.dp,
                                if (isSelected) primaryColor else Color(0xFF2E2740)
                            ),
                            modifier = Modifier.clickable {
                                selectedPlatform = platform
                                if (isNew) {
                                    title = pName
                                    url = when (platform) {
                                        SocialPlatform.YOUTUBE -> "https://youtube.com/@"
                                        SocialPlatform.TIKTOK -> "https://www.tiktok.com/@"
                                        SocialPlatform.FACEBOOK -> "https://facebook.com/"
                                        SocialPlatform.INSTAGRAM -> "https://instagram.com/"
                                        SocialPlatform.TELEGRAM -> "https://t.me/"
                                        else -> "https://"
                                    }
                                }
                            }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = pName,
                                    tint = Color.Unspecified,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = pName,
                                    fontFamily = PlusJakartaFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else Color(0xFFA09EB0)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Title
                Text(
                    text = if (isBangla) "শিরোনাম" else "Title",
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFA09EB0)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("link_title_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color(0xFF2C273C),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Subtitle
                Text(
                    text = if (isBangla) "উপশিরোনাম / বর্ণনা" else "Subtitle / Handle",
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFA09EB0)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("link_subtitle_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color(0xFF2C273C),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // URL
                Text(
                    text = if (isBangla) "লিঙ্ক ইউআরএল (URL)" else "Link URL",
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFA09EB0)
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("link_url_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color(0xFF2C273C),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Delete button if editing
                if (!isNew && onDeleteLink != null && itemToEdit != null) {
                    OutlinedButton(
                        onClick = {
                            onDeleteLink(itemToEdit.id)
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("delete_link_button"),
                        border = BorderStroke(1.dp, Color(0xFF55222E)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color(0xFFFF5252),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBangla) "লিঙ্ক মুছে ফেলুন" else "Delete Link",
                            fontFamily = PlusJakartaFontFamily,
                            fontSize = 12.sp,
                            color = Color(0xFFFF5252)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Save button
                Button(
                    onClick = {
                        val accentColor = when (selectedPlatform) {
                            SocialPlatform.YOUTUBE -> 0xFFFF0000
                            SocialPlatform.TIKTOK -> 0xFF25F4EE
                            SocialPlatform.FACEBOOK -> 0xFF1877F2
                            SocialPlatform.INSTAGRAM -> 0xFFE1306C
                            SocialPlatform.TELEGRAM -> 0xFF229ED9
                            else -> 0xFF6C5CE7
                        }
                        val iconRes = when (selectedPlatform) {
                            SocialPlatform.YOUTUBE -> R.drawable.ic_social_youtube
                            SocialPlatform.TIKTOK -> R.drawable.ic_social_tiktok
                            SocialPlatform.FACEBOOK -> R.drawable.ic_social_facebook
                            SocialPlatform.INSTAGRAM -> R.drawable.ic_social_instagram
                            SocialPlatform.TELEGRAM -> R.drawable.ic_social_telegram
                            else -> R.drawable.ic_social_generic
                        }
                        val finalItem = (itemToEdit ?: SocialLinkItem(
                            id = "link_${System.currentTimeMillis()}",
                            platform = selectedPlatform,
                            title = title,
                            subtitle = subtitle,
                            subtitleBn = subtitleBn,
                            url = url,
                            iconRes = iconRes,
                            accentColor = accentColor,
                            clicks = 0
                        )).copy(
                            platform = selectedPlatform,
                            title = title,
                            subtitle = subtitle,
                            subtitleBn = subtitleBn,
                            url = url,
                            iconRes = iconRes,
                            accentColor = accentColor
                        )

                        onSaveLink(finalItem)
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_link_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isBangla) "সংরক্ষণ করুন" else "Save Link",
                        fontFamily = PlusJakartaFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
