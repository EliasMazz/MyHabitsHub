package com.yolo.auth.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yolo.core.designsystem.theme.YoloTokens
import com.yolo.core.designsystem.theme.extended
import com.yolo.core.domain.validator.PasswordRule
import com.yolo.core.domain.validator.PasswordValidatorUseCase
import myhabitshub.feature.auth.presentation.generated.resources.Res
import myhabitshub.feature.auth.presentation.generated.resources.password_rule_digit
import myhabitshub.feature.auth.presentation.generated.resources.password_rule_length
import myhabitshub.feature.auth.presentation.generated.resources.password_rule_uppercase
import org.jetbrains.compose.resources.stringResource

/**
 * Live password requirement checklist (auth-screens-improvement-spec §2.3): each rule ticks
 * green per keystroke (positive feedback is exempt from the no-keystroke-errors rule, V3).
 * Rules come from PasswordValidatorUseCase.rulesSatisfied so copy can never drift from the
 * validator again (the old hint promised 6+/digit while the validator demanded 9+/digit/upper).
 */
@Composable
fun PasswordRuleChecklist(
    password: String,
    modifier: Modifier = Modifier,
) {
    val satisfied = PasswordValidatorUseCase.rulesSatisfied(password)
    Column(modifier = modifier.fillMaxWidth()) {
        PasswordRule.entries.forEach { rule ->
            val met = satisfied[rule] == true
            val label = when (rule) {
                PasswordRule.MIN_LENGTH -> stringResource(Res.string.password_rule_length)
                PasswordRule.HAS_DIGIT -> stringResource(Res.string.password_rule_digit)
                PasswordRule.HAS_UPPERCASE -> stringResource(Res.string.password_rule_uppercase)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = YoloTokens.spacing.stackGapTight / 2),
            ) {
                Text(
                    text = if (met) "✓" else "•",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (met) {
                        MaterialTheme.colorScheme.extended.success
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(YoloTokens.sizing.iconSmall),
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (met) {
                        MaterialTheme.colorScheme.extended.success
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.padding(start = YoloTokens.spacing.stackGapTight),
                )
            }
        }
    }
}
