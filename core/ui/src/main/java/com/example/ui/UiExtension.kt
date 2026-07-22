package com.example.ui

import com.example.core.ui.R
import com.example.moviesexample.domain.entity.AppError
import com.example.ui.model.UiText

fun AppError.toUiText(): UiText = when (this) {
    AppError.Network.NoInternet -> UiText.Resource(R.string.error_no_internet)
    AppError.Network.Timeout -> UiText.Resource(R.string.error_timeout)
    AppError.Network.ServerError -> UiText.Resource(R.string.error_server)
    is AppError.Network.HttpError -> UiText.Resource(R.string.error_server)
    AppError.Location.PermissionDenied -> UiText.Resource(R.string.error_location_permission)
    AppError.Location.Unavailable -> UiText.Resource(R.string.error_location_unavailable)
    AppError.Location.GeocoderFailed -> UiText.Resource(R.string.error_location_unavailable)
    AppError.Unknown -> UiText.Resource(R.string.error_unknown)
}
