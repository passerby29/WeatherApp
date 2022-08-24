package com.passerby.weatherapp.business

import com.passerby.weatherapp.business.model.GeoCodeModel
import com.passerby.weatherapp.business.room.GeoCodeEntity

fun GeoCodeModel.mapToEntity() = GeoCodeEntity(
    name = this.name,
    local_names = this.local_names,
    lat = this.lat,
    lon = this.lon,
    country = this.country,
    state = this.state ?: "",
    isFavorite = this.isFavorite
)

fun GeoCodeEntity.mapToModel() = GeoCodeModel(
    name = this.name,
    local_names = this.local_names,
    lat = this.lat,
    lon = this.lon,
    country = this.country,
    state = this.state,
    isFavorite = this.isFavorite

)
