package com.example.android.politicalpreparedness.utils

fun concatStateAndCountry(state: String, country: String): String {
    return when{
        state.isEmpty() -> country
        country.isEmpty() -> state
        else -> "$state $country"
    }
}