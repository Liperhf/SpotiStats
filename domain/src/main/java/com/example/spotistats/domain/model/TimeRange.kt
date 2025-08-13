package com.example.spotistats.domain.model

enum class TimeRange(val api: String) {
    SHORT("short_term"),
    MEDIUM("medium_term"),
    LONG("long_term")
}
