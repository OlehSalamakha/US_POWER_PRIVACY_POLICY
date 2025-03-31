package com.example.uspower.data

interface Cache<KEY, VALUE> {

    fun set(newValues: Map<KEY, VALUE>): Map<KEY, VALUE>

    fun getByKey(key: KEY): VALUE?

    fun addOrUpdate(newValues: Map<KEY, VALUE>): Map<KEY, VALUE>

    fun update(newValues: Map<KEY, VALUE>)

    fun deleteFromCache(values: Map<KEY, VALUE>)
}