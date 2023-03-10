package com.cbsindia.cbs.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cbsindia.cbs.data.repository.base.PrefRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WelcomeViewModel : ViewModel(), KoinComponent {
    private val prefRepository: PrefRepository by inject()

    fun isIntroScreenShown() =
        prefRepository.getIsIntroScreenShown()

    fun setIsIntoScreenShown(isShown: Boolean) {
        prefRepository.setIsIntroScreenShown(isShown)
    }
}
