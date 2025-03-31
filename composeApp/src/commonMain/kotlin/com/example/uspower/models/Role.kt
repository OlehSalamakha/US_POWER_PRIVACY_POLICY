package com.example.uspower.models

import kotlinx.serialization.Serializable

@Serializable
enum class Role(val displayName: String) {
    ENERGY_CONSULT("Energy Consultant"),
    LEAD_GENERATOR("Lead Generator"),
    OTHER("Other"); // Placeholder, actual value will be dynamic

    companion object {
        fun fromString(value: String): Role {
            return values().find { it.displayName.equals(value, ignoreCase = true) }
                ?: OTHER.apply { customValue = value }
        }
    }

    var customValue: String = displayName
        private set

    override fun toString(): String {
        return if (this == OTHER) customValue else displayName
    }
}