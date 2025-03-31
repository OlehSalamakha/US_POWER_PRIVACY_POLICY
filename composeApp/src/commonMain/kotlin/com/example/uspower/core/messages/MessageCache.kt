package com.example.uspower.core.messages

import com.example.uspower.Log
import com.example.uspower.data.Cache
import com.example.uspower.models.Message

class MessageCache(
    private val cache: MutableMap<String, Map<String, Message>> = mutableMapOf()
): Cache<String, Map<String, Message>> {
    override fun set(newValues: Map<String, Map<String, Message>>): Map<String, Map<String, Message>> {
        cache.clear()
        cache.putAll(newValues)
        return cache
    }

    override fun getByKey(key: String): Map<String, Message>? {
        return cache[key]
    }

    override fun addOrUpdate(newValues: Map<String, Map<String, Message>>): Map<String, Map<String, Message>> {
        Log("MessageCache", "Add or update newvalues: ${newValues}")
        newValues.forEach { (outerKey, newInnerMap) ->
            val existingInnerMap = cache[outerKey] ?: emptyMap()
            val mergedInnerMap = existingInnerMap + newInnerMap
            cache[outerKey] = mergedInnerMap
        }
        return cache
    }



    override fun deleteFromCache(values: Map<String, Map<String, Message>>) {

        values.keys.forEach { key ->
            val cachedMessages = cache[key]?.toMutableMap()

            val messagesToRemove = values[key]?.values

            messagesToRemove?.forEach {
                cachedMessages?.remove(it.messageId)
            }

            cachedMessages?.let {
                cache[key] = cachedMessages.toMap()
            }
        }
    }

    override fun update(newValues: Map<String, Map<String, Message>>) {

        newValues.keys.forEach { key ->
            val cachedMessages = cache[key]?.toMutableMap()

            val messagesToUpdate = newValues[key]?.values

            messagesToUpdate?.forEach {
                if (cachedMessages?.get(it.messageId) != null) {
                    cachedMessages[it.messageId] = it
                }
            }

            cachedMessages?.let {
                cache[key] = cachedMessages.toMap()
            }
        }
    }


}