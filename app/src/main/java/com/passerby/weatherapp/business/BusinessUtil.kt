package com.passerby.weatherapp.business

import com.passerby.weatherapp.business.model.GeoCodeModel
import com.passerby.weatherapp.business.room.GeoCodeEntity

fun GeoCodeModel.mapToEntity() = GeoCodeEntity(
    country = this.country,
    local_names = this.local_names,
    lat = this.lat,
    lon = this.lon,
    name = this.name,
    state = this.state ?: "",
    isFavorite = this.isFavorite
)

fun GeoCodeEntity.mapToModel() = GeoCodeModel(
    country = this.country,
    local_names = this.local_names,
    lat = this.lat,
    lon = this.lon,
    name = this.name,
    state = this.state,
    isFavorite = this.isFavorite

)
