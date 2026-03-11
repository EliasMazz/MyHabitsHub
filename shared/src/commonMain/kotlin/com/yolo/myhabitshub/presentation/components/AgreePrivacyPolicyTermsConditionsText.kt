package com.yolo.myhabitshub.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import com.yolo.myhabitshub.generated.resources.Res
import com.yolo.myhabitshub.generated.resources.and
import com.yolo.myhabitshub.generated.resources.privacy_policy
import com.yolo.myhabitshub.generated.resources.terms_conditions
import com.yolo.myhabitshub.generated.resources.txt_agree_privacy_policy_and_terms
import com.yolo.myhabitshub.core.presentation.theme.AppTheme
import com.yolo.myhabitshub.util.Constants
import org.jetbrains.compose.resources.stringResource

@Composable
fun AgreePrivacyPolicyTermsConditionsText(modifier: Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        val privacyPolicy = stringResource(Res.string.privacy_policy)
        val termsConditions = stringResource(Res.string.terms_conditions)
        val customStyle = SpanStyle(
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.text.secondary,
            textDecoration = TextDecoration.Underline
        )
        val annotatedString = buildAnnotatedString {
            append(stringResource(Res.string.txt_agree_privacy_policy_and_terms))

            withStyle(style = customStyle) {
                withLink(LinkAnnotation.Url(url = Constants.URL_PRIVACY_POLICY)) {
                    append(privacyPolicy)
                }
            }
            append(" ${stringResource(Res.string.and)} ")
            withStyle(customStyle) {
                withLink(LinkAnnotation.Url(url = Constants.URL_TERMS_CONDITIONS)) {
                    append(termsConditions)
                }
            }

        }
        Text(
            text = annotatedString,
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colors.text.secondary
        )
    }
}