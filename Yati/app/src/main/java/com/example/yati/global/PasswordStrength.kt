package com.example.yati.global

import android.graphics.Color
import com.example.yati.R

enum class PasswordStrength constructor (internal var resId:Int,color: Int){
    WEAK(R.string.pass_stre_weak,Color.RED),
    MEDIUM(R.string.pass_stre_medium,Color.YELLOW),
    STRONG(R.string.pass_stre_strong,Color.GREEN);

    var color:Int = 0
    internal set

    init {
        this.color = color
    }

    fun getText(ctx: android.content.Context): CharSequence {
        return ctx.getText(resId)
    }

    companion object {
        internal var REQUIRED_LENGHT = 8

        internal var MAXIMUM_LENGHT = 15

        internal var REQUIRE_SPECIAL_CHARACTERS = true

        internal var REQUIRE_DIGITS = true

        internal var REQUIRE_LOWER_CASE = true

        internal var REQUIRE_UPPER_CASE = false

        fun calculateStrength(password:String):PasswordStrength{
            var currentScore = 0
            var sawUpper = false
            var sawLower = false
            var sawDigit = false
            var sawSpecial = false

            for (i in 0 until password.length) {
                val c = password[i]

                if (!sawSpecial && !Character.isLetterOrDigit(c)) {
                    currentScore += 1
                    sawSpecial = true
                } else {
                    if (!sawDigit && Character.isDigit(c)) {
                        currentScore += 1
                        sawDigit = true
                    } else {
                        if (!sawUpper || !sawLower) {
                            if (Character.isUpperCase(c))
                                sawUpper = true
                            else
                                sawLower = true
                            if (sawUpper && sawLower)
                                currentScore += 1
                        }
                    }
                }

            }

            if (password.length > REQUIRED_LENGHT) {
                if (REQUIRE_SPECIAL_CHARACTERS && !sawSpecial
                    || REQUIRE_UPPER_CASE && !sawUpper
                    || REQUIRE_LOWER_CASE && !sawLower
                    || REQUIRE_DIGITS && !sawDigit) {
                    currentScore = 1
                } else {
                    currentScore = 2
                    if (password.length > MAXIMUM_LENGHT) {
                        currentScore = 3
                    }
                }
            } else {
                currentScore = 0
            }

            when (currentScore) {
                0 -> return WEAK
                1 -> return MEDIUM
                2 -> return STRONG

            }
            return STRONG
        }

    }
}