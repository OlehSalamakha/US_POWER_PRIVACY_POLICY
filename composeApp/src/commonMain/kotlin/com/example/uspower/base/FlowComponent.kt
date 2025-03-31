package com.example.uspower.base

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack

import com.example.uspower.toStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.KSerializer

interface FlowComponent<T: Any>: ComponentContext {
    val childStack: StateFlow<ChildStack<*, T>>
}

abstract class FlowComponentImpl<Child: Any, Config: Any>(): FlowComponent<Child> {

    abstract val initialConfiguration: Config
    abstract val configSerializer: KSerializer<Config>

    abstract val handleBack: Boolean

    protected val nav = StackNavigation<Config>()

    override val childStack: StateFlow<ChildStack<*, Child>> by lazy {
        childStack(
            source = nav,
            serializer = configSerializer,
            initialConfiguration = initialConfiguration,
            childFactory = ::child,
            handleBackButton = handleBack
        ).toStateFlow(lifecycle)
    }



    protected abstract fun child(
        config: Config,
        componentContext: ComponentContext
    ): Child
}