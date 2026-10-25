package com.example.ui.viewmodel

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.SocialLinkItem
import com.example.data.model.UserProfile
import com.example.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ActiveDialog {
    data object None : ActiveDialog()
    data object EditProfile : ActiveDialog()
    data class EditLink(val item: SocialLinkItem?) : ActiveDialog()
    data object QrCode : ActiveDialog()
    data object Analytics : ActiveDialog()
    data object ThemeSelector : ActiveDialog()
}

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProfileRepository(application.applicationContext)

    val profileState: StateFlow<UserProfile> = repository.profileState

    private val _isBangla = MutableStateFlow(true) // Default to Bengali as user prompt is in Bengali
    val isBangla: StateFlow<Boolean> = _isBangla.asStateFlow()

    private val _activeDialog = MutableStateFlow<ActiveDialog>(ActiveDialog.None)
    val activeDialog: StateFlow<ActiveDialog> = _activeDialog.asStateFlow()

    private val _snackbarMessages = MutableSharedFlow<String>()
    val snackbarMessages: SharedFlow<String> = _snackbarMessages.asSharedFlow()

    fun toggleLanguage() {
        _isBangla.value = !_isBangla.value
    }

    fun openDialog(dialog: ActiveDialog) {
        _activeDialog.value = dialog
    }

    fun closeDialog() {
        _activeDialog.value = ActiveDialog.None
    }

    fun onLinkClick(context: Context, item: SocialLinkItem) {
        viewModelScope.launch {
            repository.incrementClick(item.id)
            var opened = false

            // Try opening in native application if appScheme is present
            if (!item.appScheme.isNullOrBlank()) {
                try {
                    val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.appScheme)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(appIntent)
                    opened = true
                } catch (e: Exception) {
                    opened = false
                }
            }

            // Fallback to web browser
            if (!opened) {
                try {
                    val webUrl = if (!item.url.startsWith("http://") && !item.url.startsWith("https://")) {
                        "https://${item.url}"
                    } else {
                        item.url
                    }
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(browserIntent)
                } catch (e: Exception) {
                    val msg = if (_isBangla.value) "লিঙ্কটি খোলা সম্ভব হয়নি" else "Could not open link"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun copyToClipboard(context: Context, text: String, label: String = "Link") {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(label, text)
            clipboard.setPrimaryClip(clip)
            val msg = if (_isBangla.value) "লিঙ্ক ক্লিপবোর্ডে কপি হয়েছে!" else "Copied to clipboard!"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // Ignore clipboard errors
        }
    }

    fun shareProfile(context: Context, url: String) {
        try {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "${profileState.value.handle} Official Links: $url")
                type = "text/plain"
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            val shareIntent = Intent.createChooser(sendIntent, "Share Links via")
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            // Ignore share errors
        }
    }

    fun updateProfile(handle: String, displayName: String, bio: String, bioBn: String) {
        viewModelScope.launch {
            repository.updateProfile(handle, displayName, bio, bioBn)
            val msg = if (_isBangla.value) "প্রোফাইল আপডেট হয়েছে" else "Profile updated successfully"
            _snackbarMessages.emit(msg)
        }
    }

    fun saveLink(linkItem: SocialLinkItem) {
        viewModelScope.launch {
            val exists = profileState.value.links.any { it.id == linkItem.id }
            if (exists) {
                repository.updateLink(linkItem)
            } else {
                repository.addLink(linkItem)
            }
            val msg = if (_isBangla.value) "লিঙ্ক সংরক্ষণ করা হয়েছে" else "Link saved"
            _snackbarMessages.emit(msg)
        }
    }

    fun deleteLink(linkId: String) {
        viewModelScope.launch {
            repository.deleteLink(linkId)
            val msg = if (_isBangla.value) "লিঙ্ক মুছে ফেলা হয়েছে" else "Link deleted"
            _snackbarMessages.emit(msg)
        }
    }

    fun selectTheme(themeKey: String) {
        viewModelScope.launch {
            repository.setTheme(themeKey)
        }
    }

    fun resetDefaults() {
        viewModelScope.launch {
            repository.resetToDefaults()
            val msg = if (_isBangla.value) "ডিফল্ট সেটিংস রিসেট হয়েছে" else "Defaults restored"
            _snackbarMessages.emit(msg)
        }
    }
}
