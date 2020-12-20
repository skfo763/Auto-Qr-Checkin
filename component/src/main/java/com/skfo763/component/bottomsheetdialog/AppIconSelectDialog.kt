package com.skfo763.component.bottomsheetdialog

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skfo763.component.R
import com.skfo763.component.databinding.DialogAppIconSelectBinding

class AppIconSelectDialog : BottomSheetDialogFragment() {

    data class Icon(
        val type: String,
        val iconDrawable: Drawable,
        @ColorInt val backgroundColor: Int,
        @ColorInt val nameTextColor: Int,
        @ColorInt val descriptionTextColor: Int,
        val name: String,
        val description: String,
        inline val onItemClicked: (Icon) -> Unit
    ) {
        var recyclerItemClicked: ((Icon) -> Unit)? = null
    }

    private lateinit var binding: DialogAppIconSelectBinding

    val itemList = MutableLiveData<List<Icon>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_app_icon_select, container, false)
        binding.lifecycleOwner = this
        binding.dialogItem.layoutManager = GridLayoutManager(context, 2)
        binding.itemList = this.itemList
        return binding.root
    }

    fun setData(itemList: List<Icon>) {
        itemList.forEach {
            it.recyclerItemClicked = { icon ->
                dismiss()
                it.onItemClicked(icon)
            }
        }
        this.itemList.value = itemList
    }
}