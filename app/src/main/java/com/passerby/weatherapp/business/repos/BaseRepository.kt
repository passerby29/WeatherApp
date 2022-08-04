package com.passerby.weatherapp.business.repos

import com.passerby.weatherapp.business.ApiProvider
import io.reactivex.rxjava3.subjects.BehaviorSubject

abstract class BaseRepository<T>(val api: ApiProvider) {

    val dataEmitter: BehaviorSubject<T> = BehaviorSubject.create()
}