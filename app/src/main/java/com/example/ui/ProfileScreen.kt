package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DefaultData
import com.example.ui.components.AnalyticsDialog
import com.example.ui.components.EditLinkDialog
import com.example.ui.components.EditProfileDialog
import com.example.ui.components.GlowLinkCard
import com.example.ui.components.NeonAvatar
import com.example.ui.components.QrCodeDialog
import com.example.ui.components.ThemeSelectorDialog
import com.example.ui.theme.OrbitronFontFamily
import com.example.ui.theme.PlusJakartaFontFamily
import com.example.ui.viewmodel.ActiveDialog
import com.example.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val profile by viewModel.profileState.collectAsState()
    val isBangla by viewModel.isBangla.collectAsState()
    val activeDialog by viewModel.activeDialog.collectAsState()

    val currentTheme = DefaultData.themes.find { it.key == profile.selectedThemeKey } ?: DefaultData.themes.first()
    val primaryColor = Color(currentTheme.primaryColor)
    val glowColor = Color(currentTheme.glowColor)

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessages.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFF09080E),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openDialog(ActiveDialog.EditLink(null)) },
                containerColor = primaryColor,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .padding(bottom = navBarPadding)
                    .testTag("fab_add_link")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Link",
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF130F1F),
                            Color(0xFF09080E),
                            Color(0xFF06050A)
                        )
                    )
                )
        ) {
            // Ambient red/theme glow behind upper section
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .height(380.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                glowColor.copy(alpha = 0.22f),
                                glowColor.copy(alpha = 0.08f),
                                Color.Transparent
                            ),
                            radius = 650f
                        )
                    )
            )

            // Center container for large screens (responsive tablet support)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = statusBarPadding)
                    .widthIn(max = 600.dp)
                    .align(Alignment.TopCenter)
            ) {
                // Top Action Toolbar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Language Switcher Pill
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFF191526),
                        border = BorderStroke(1.dp, Color(0xFF2F2742)),
                        modifier = Modifier
                            .testTag("language_toggle_button")
                            .clickable { viewModel.toggleLanguage() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = primaryColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBangla) "বাংলা" else "ENG",
                                fontFamily = OrbitronFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }

                    // Right utility icons (Stats, Theme, Share)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Analytics
                        IconButton(
                            onClick = { viewModel.openDialog(ActiveDialog.Analytics) },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF191526))
                                .border(1.dp, Color(0xFF2F2742), CircleShape)
                                .testTag("toolbar_analytics_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "Analytics",
                                tint = Color(0xFFD6D4E5),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Theme Preset
                        IconButton(
                            onClick = { viewModel.openDialog(ActiveDialog.ThemeSelector) },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF191526))
                                .border(1.dp, Color(0xFF2F2742), CircleShape)
                                .testTag("toolbar_theme_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = "Theme",
                                tint = primaryColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // QR Code / Share
                        IconButton(
                            onClick = { viewModel.openDialog(ActiveDialog.QrCode) },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF191526))
                                .border(1.dp, Color(0xFF2F2742), CircleShape)
                                .testTag("toolbar_qr_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode,
                                contentDescription = "QR Code",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                // Scrollable Content
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        top = 10.dp,
                        bottom = navBarPadding + 90.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("profile_links_list")
                ) {
                    // Profile Header Item
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.height(6.dp))

                            // Neon Avatar
                            NeonAvatar(
                                avatarUri = profile.avatarUri,
                                glowColor = glowColor,
                                isVerified = profile.isVerified,
                                onClick = { viewModel.openDialog(ActiveDialog.EditProfile) }
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Handle (e.g. @lyuna) in cyber red neon font
                            Text(
                                text = profile.handle,
                                fontFamily = OrbitronFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 28.sp,
                                letterSpacing = 1.2.sp,
                                color = primaryColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.testTag("profile_handle_text")
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Bio / Tagline
                            val bioText = if (isBangla && profile.bioBn.isNotBlank()) {
                                profile.bioBn
                            } else {
                                profile.bio
                            }

                            Text(
                                text = bioText,
                                fontFamily = PlusJakartaFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp,
                                color = Color(0xFFA5A3B8),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(horizontal = 24.dp)
                                    .testTag("profile_bio_text")
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Action Pills Row: Share Profile, Edit Profile
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF181526),
                                    border = BorderStroke(1.dp, Color(0xFF2F2742)),
                                    modifier = Modifier
                                        .testTag("share_profile_pill")
                                        .clickable {
                                            val slug = profile.handle.lowercase().replace("@", "").replace(" ", ".")
                                            viewModel.shareProfile(
                                                context,
                                                "https://links.io/$slug"
                                            )
                                        }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = null,
                                            tint = primaryColor,
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isBangla) "শেয়ার" else "Share",
                                            fontFamily = PlusJakartaFontFamily,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF181526),
                                    border = BorderStroke(1.dp, Color(0xFF2F2742)),
                                    modifier = Modifier
                                        .testTag("edit_profile_pill")
                                        .clickable { viewModel.openDialog(ActiveDialog.EditProfile) }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = null,
                                            tint = Color(0xFFD6D4E5),
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isBangla) "প্রোফাইল সাজান" else "Edit Profile",
                                            fontFamily = PlusJakartaFontFamily,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(26.dp))
                        }
                    }

                    // Links list
                    items(
                        items = profile.links,
                        key = { it.id }
                    ) { item ->
                        GlowLinkCard(
                            item = item,
                            isBangla = isBangla,
                            primaryGlowColor = glowColor,
                            onClick = { viewModel.onLinkClick(context, item) },
                            onLongClick = { viewModel.openDialog(ActiveDialog.EditLink(item)) },
                            onCopyClick = { viewModel.copyToClipboard(context, item.url, item.title) },
                            onEditClick = { viewModel.openDialog(ActiveDialog.EditLink(item)) },
                            modifier = Modifier.padding(vertical = 7.dp)
                        )
                    }

                    // Empty state if all deleted
                    if (profile.links.isEmpty()) {
                        item {
                            Card(
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF13101C)),
                                border = BorderStroke(1.dp, Color(0xFF292438)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 20.dp)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp)
                                ) {
                                    Text(
                                        text = if (isBangla) "কোনো লিঙ্ক যুক্ত করা হয়নি" else "No Links Added Yet",
                                        fontFamily = OrbitronFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = if (isBangla) "নিচের + বাটনে চাপ দিয়ে আপনার ফেসবুক, ইউটিউব, টিকটক, ইনস্টাগ্রাম বা টেলিগ্রাম যোগ করুন" else "Tap the + button below to add your links",
                                        fontFamily = PlusJakartaFontFamily,
                                        fontSize = 12.sp,
                                        color = Color(0xFFA09EB0),
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Button(
                                        onClick = { viewModel.resetDefaults() },
                                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Text(
                                            text = if (isBangla) "ডিফল্ট লিঙ্ক ফিরিয়ে আনুন" else "Restore Default Links",
                                            fontFamily = PlusJakartaFontFamily,
                                            fontSize = 12.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Footer branding
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "SHARIFUL ISLAM • LINK HUB",
                                fontFamily = OrbitronFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 2.sp,
                                color = Color(0xFF5D5A6E)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isBangla) "আধুনিক ও দ্রুতগতির সামাজিক লিঙ্ক হাব" else "Modern & Responsive Social Link Profile",
                                fontFamily = PlusJakartaFontFamily,
                                fontSize = 10.5.sp,
                                color = Color(0xFF434052)
                            )
                        }
                    }
                }
            }
        }
    }

    // Active Dialogs Overlay
    when (val dialog = activeDialog) {
        ActiveDialog.None -> {}
        ActiveDialog.EditProfile -> {
            EditProfileDialog(
                profile = profile,
                isBangla = isBangla,
                primaryColor = primaryColor,
                onDismiss = { viewModel.closeDialog() },
                onSave = { h, d, b, bn -> viewModel.updateProfile(h, d, b, bn) },
                onResetDefaults = { viewModel.resetDefaults() }
            )
        }
        is ActiveDialog.EditLink -> {
            EditLinkDialog(
                itemToEdit = dialog.item,
                isBangla = isBangla,
                primaryColor = primaryColor,
                onDismiss = { viewModel.closeDialog() },
                onSaveLink = { link -> viewModel.saveLink(link) },
                onDeleteLink = { id -> viewModel.deleteLink(id) }
            )
        }
        ActiveDialog.QrCode -> {
            QrCodeDialog(
                profile = profile,
                isBangla = isBangla,
                primaryColor = primaryColor,
                onDismiss = { viewModel.closeDialog() },
                onCopyUrl = { url -> viewModel.copyToClipboard(context, url, "Profile URL") },
                onShareUrl = { url -> viewModel.shareProfile(context, url) }
            )
        }
        ActiveDialog.Analytics -> {
            AnalyticsDialog(
                profile = profile,
                isBangla = isBangla,
                primaryColor = primaryColor,
                onDismiss = { viewModel.closeDialog() }
            )
        }
        ActiveDialog.ThemeSelector -> {
            ThemeSelectorDialog(
                selectedThemeKey = profile.selectedThemeKey,
                isBangla = isBangla,
                primaryColor = primaryColor,
                onDismiss = { viewModel.closeDialog() },
                onSelectTheme = { themeKey -> viewModel.selectTheme(themeKey) }
            )
        }
    }
}
