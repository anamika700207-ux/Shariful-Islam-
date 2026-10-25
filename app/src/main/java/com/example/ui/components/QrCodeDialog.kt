package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.UserProfile
import com.example.ui.theme.OrbitronFontFamily
import com.example.ui.theme.PlusJakartaFontFamily

@Composable
fun QrCodeDialog(
    profile: UserProfile,
    isBangla: Boolean,
    primaryColor: Color,
    onDismiss: () -> Unit,
    onCopyUrl: (String) -> Unit,
    onShareUrl: (String) -> Unit
) {
    val slug = profile.handle.lowercase().replace("@", "").replace(" ", ".")
    val profileUrl = "https://links.io/$slug"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF13101B)),
            border = BorderStroke(1.5.dp, primaryColor.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("qr_code_dialog")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Header row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = null,
                            tint = primaryColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBangla) "প্রোফাইল কিউআর কোড" else "Profile QR Code",
                            fontFamily = OrbitronFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
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

                // QR Code Display Card
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(14.dp)
                ) {
                    Canvas(modifier = Modifier.size(190.dp)) {
                        val gridSize = 21
                        val cellSize = size.width / gridSize
                        val darkColor = Color(0xFF0C0B12)

                        // Generate deterministic QR pattern matching handle hash
                        val hash = (profile.handle + profile.displayName).hashCode()

                        for (r in 0 until gridSize) {
                            for (c in 0 until gridSize) {
                                val isFinderCorner =
                                    (r < 7 && c < 7) || (r < 7 && c >= gridSize - 7) || (r >= gridSize - 7 && c < 7)
                                if (!isFinderCorner) {
                                    val isCenterLogo = r in 9..11 && c in 9..11
                                    if (!isCenterLogo) {
                                        val bit = ((r * 31 + c * 17 + hash) xor (r * c)) % 3 == 0
                                        if (bit) {
                                            drawRoundRect(
                                                color = darkColor,
                                                topLeft = Offset(c * cellSize, r * cellSize),
                                                size = Size(cellSize * 0.92f, cellSize * 0.92f),
                                                cornerRadius = CornerRadius(2f, 2f)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Draw the 3 standard finder patterns
                        fun drawFinder(leftCol: Int, topRow: Int) {
                            // Outer box
                            drawRoundRect(
                                color = darkColor,
                                topLeft = Offset(leftCol * cellSize, topRow * cellSize),
                                size = Size(7 * cellSize, 7 * cellSize),
                                cornerRadius = CornerRadius(8f, 8f)
                            )
                            // White gap
                            drawRoundRect(
                                color = Color.White,
                                topLeft = Offset((leftCol + 1) * cellSize, (topRow + 1) * cellSize),
                                size = Size(5 * cellSize, 5 * cellSize),
                                cornerRadius = CornerRadius(5f, 5f)
                            )
                            // Inner dot
                            drawRoundRect(
                                color = primaryColor,
                                topLeft = Offset((leftCol + 2) * cellSize, (topRow + 2) * cellSize),
                                size = Size(3 * cellSize, 3 * cellSize),
                                cornerRadius = CornerRadius(3f, 3f)
                            )
                        }

                        drawFinder(0, 0)
                        drawFinder(gridSize - 7, 0)
                        drawFinder(0, gridSize - 7)
                    }

                    // Center emblem
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0C0B12))
                            .border(2.dp, primaryColor, CircleShape)
                    ) {
                        Text(
                            text = if (profile.displayName.contains("Shariful")) "SI" else profile.displayName.take(1),
                            fontFamily = OrbitronFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = primaryColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = profile.handle,
                    fontFamily = OrbitronFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = primaryColor
                )

                Text(
                    text = profileUrl,
                    fontFamily = PlusJakartaFontFamily,
                    fontSize = 12.sp,
                    color = Color(0xFF9E9DB0)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onCopyUrl(profileUrl) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("qr_dialog_copy_button"),
                        border = BorderStroke(1.dp, Color(0xFF353048)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBangla) "কপি লিঙ্ক" else "Copy Link",
                            fontFamily = PlusJakartaFontFamily,
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }

                    Button(
                        onClick = { onShareUrl(profileUrl) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("qr_dialog_share_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBangla) "শেয়ার করুন" else "Share",
                            fontFamily = PlusJakartaFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
