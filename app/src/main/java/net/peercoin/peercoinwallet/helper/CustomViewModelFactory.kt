package net.peercoin.peercoinwallet.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import net.peercoin.peercoinwallet.ui.login.paper_key.stepOne.PaperKeyStepOneViewModel

class CustomViewModelFactory(private val neededWords: Int) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PaperKeyStepOneViewModel(neededWords) as T
    }

}