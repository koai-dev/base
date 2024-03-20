package com.koai.base.main.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.base.main.screens.BaseScreen
import org.koin.androidx.viewmodel.ext.android.getActivityViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.parameter.ParametersHolder
import org.koin.core.qualifier.Qualifier

inline fun <reified T : ViewModel> Module.screenViewModel(
    qualifier: Qualifier? = null,
    noinline definition: Definition<T>,
): KoinDefinition<T> = factory(qualifier, definition)

inline fun <reified T : ViewModel> Module.journeyViewModel(
    qualifier: Qualifier? = null,
    noinline definition: Definition<T>,
): KoinDefinition<T> = factory(qualifier, definition)

inline fun <reified T : ViewModel> Module.navigatorViewModel(
    qualifier: Qualifier? = null,
    noinline definition: Definition<T>,
): KoinDefinition<T> = factory(qualifier, definition)

inline fun <reified T : ViewModel> BaseScreen<*,*,*>.screenViewModel(
    qualifier: Qualifier? = null,
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline parameters: (() -> ParametersHolder)? = null,
): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) {
        getViewModel(qualifier, ownerProducer, extrasProducer, parameters)
    }

inline fun <reified T : ViewModel> Fragment.journeyViewModel(
    qualifier: Qualifier? = null,
    noinline ownerProducer: () -> ViewModelStoreOwner = { requireActivity() },
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline parameters: (() -> ParametersHolder)? = null,
): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) {
        getActivityViewModel(qualifier, ownerProducer, extrasProducer, parameters)
    }

inline fun <reified T : ViewModel> Fragment.navigatorViewModel(
    qualifier: Qualifier? = null,
    noinline ownerProducer: () -> ViewModelStoreOwner = { requireActivity() },
    noinline extrasProducer: (() -> CreationExtras)? = null,
    noinline parameters: (() -> ParametersHolder)? = null,
): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) {
        getActivityViewModel(qualifier, ownerProducer, extrasProducer, parameters)
    }
