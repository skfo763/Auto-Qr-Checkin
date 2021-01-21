package com.skfo763.component.cbalertdialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.skfo763.base.theme.ThemeType

class ThemeDialogBuilder(mContext: Context): AlertDialog.Builder(mContext) {

    data class Item(
        val itemName: String,
        val themeType: ThemeType
    )

    var dialogItems: List<Item> = listOf()

    fun setDialogTitle(title: String): ThemeDialogBuilder {
        setTitle(title)
        return this
    }

    fun setThemeItems(
        currentTheme: ThemeType?,
        items: List<Item>,
        onSelectedListener: (Item) -> Unit
    ): ThemeDialogBuilder {
        val choiceItems = items.map { it.itemName }
        val currentItem = items.indexOfFirst { it.themeType == (currentTheme ?: ThemeType.DEFAULT_MODE) }

        setSingleChoiceItems(choiceItems.toTypedArray(), currentItem) { dialog, i ->
            onSelectedListener.invoke(dialogItems[i])
            dialog.dismiss()
        }
        dialogItems = items
        return this
    }
}