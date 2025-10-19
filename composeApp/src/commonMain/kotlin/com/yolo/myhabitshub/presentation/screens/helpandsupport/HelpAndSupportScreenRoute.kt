package com.yolo.myhabitshub.presentation.screens.helpandsupport

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.yolo.myhabitshub.core.presentation.ScreenContract
import com.yolo.myhabitshub.core.presentation.ScreenRoute
import com.yolo.myhabitshub.util.AppUtil
import com.yolo.myhabitshub.util.Constants
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@Serializable
class HelpAndSupportScreenRoute() :
    ScreenRoute<HelpAndSupportViewModel, HelpAndSupportViewIntent, HelpAndSupportState, HelpAndSupportEvent> {

    @Composable
    override fun provideScreenContract(viewModel: HelpAndSupportViewModel): ScreenContract<HelpAndSupportState, HelpAndSupportEvent> {
        val localUriHandler = LocalUriHandler.current
        val appUtil = koinInject<AppUtil>()

       return object : ScreenContract<HelpAndSupportState, HelpAndSupportEvent> {

           @Composable
           override fun Screen(viewState: HelpAndSupportState) {
               HelpAndSupportScreen(
                   modifier = Modifier.fillMaxSize(),
                   itemList = viewState.settingsItemUiState,
                   onContactSupportClicked = { viewModel.handleIntent(HelpAndSupportViewIntent.OnContactSupportClicked) },
                   onTermsAndConditionsClicked = { viewModel.handleIntent(HelpAndSupportViewIntent.OnTermsAndConditionsClicked) },
                   onPrivacyPolicyClicked = { viewModel.handleIntent(HelpAndSupportViewIntent.OnPrivacyPolicyClicked) }
               )
           }

           override fun handleEvent(event: HelpAndSupportEvent) {
               when (event) {
                   HelpAndSupportEvent.OpenFeedbackMail -> appUtil.openFeedbackMail()
                   HelpAndSupportEvent.OpenPrivacyPoliceUri -> localUriHandler.openUri(Constants.URL_PRIVACY_POLICY)
                   HelpAndSupportEvent.OpenTermsAndConditionsUri -> localUriHandler.openUri(
                       Constants.URL_TERMS_CONDITIONS
                   )
               }
           }
       }
    }
}


