package com.texturas.ui.moodjournal

import com.texturas.data.model.MoodEntry
import com.texturas.data.model.TextureEntry

sealed class JournalItem {
    data class MoodEntry(val entry: MoodEntry) : JournalItem()
    data class TextureEntry(val entry: TextureEntry) : JournalItem()
}
