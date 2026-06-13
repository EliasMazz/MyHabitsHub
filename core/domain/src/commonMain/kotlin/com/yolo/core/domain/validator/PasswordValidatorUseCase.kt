package com.yolo.core.domain.validator

import com.yolo.core.domain.usecase.UseCase

private const val MIN_PASSWORD_LENGTH = 9

/** The individual password rules, exposed so UI checklists can never drift from the validator. */
enum class PasswordRule {
    MIN_LENGTH,
    HAS_DIGIT,
    HAS_UPPERCASE;

    companion object {
        const val MIN_PASSWORD_LENGTH_VALUE = MIN_PASSWORD_LENGTH
    }
}

class PasswordValidatorUseCase : UseCase<String, Boolean>() {
    override suspend fun execute(password: String): Boolean {
        return rulesSatisfied(password).values.all { it }
    }

    companion object {
        /** Per-rule satisfaction for live checklist UIs (spec §2.3 PasswordRuleChecklist). */
        fun rulesSatisfied(password: String): Map<PasswordRule, Boolean> = mapOf(
            PasswordRule.MIN_LENGTH to (password.length >= MIN_PASSWORD_LENGTH),
            PasswordRule.HAS_DIGIT to password.any { it.isDigit() },
            PasswordRule.HAS_UPPERCASE to password.any { it.isUpperCase() },
        )
    }
}