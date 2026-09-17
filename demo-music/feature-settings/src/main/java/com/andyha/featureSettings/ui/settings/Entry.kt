package com.andyha.featureSettings.ui.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class Entry<T>(val key: String) {
    private val mutableFlow by lazy {
        MutableStateFlow(getValueInternal())
    }

    val flow get() = mutableFlow.asStateFlow()
    val value get() = getValueInternal()

    fun setValue(value: T) {
        setValueInternal(value)
        mutableFlow.update { getValueInternal() }
    }

    protected abstract fun getValueInternal(): T
    protected abstract fun setValueInternal(value: T)
}