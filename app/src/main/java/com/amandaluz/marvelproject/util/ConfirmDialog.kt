package com.amandaluz.marvelproject.util

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.amandaluz.marvelproject.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ConfirmDialog(
    private val title: String,
    private val message: String
) : DialogFragment() {

    private var yesListener: (() -> Unit )? = null
    fun setListener(listener: () -> Unit){yesListener = listener}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Sim"){ _, _ ->
                yesListener?.let {
                    it()
                }
            }
            .setNegativeButton("Não"){ dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()

}