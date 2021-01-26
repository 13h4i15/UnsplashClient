package com.l3h4i15.unsplashclient.navigation

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass

interface Navigation {
    fun add(fragment: Fragment, args: Bundle? = null) = change(fragment, true, args)
    fun <T : Fragment> add(kClass: KClass<T>, args: Bundle? = null) = change(kClass, true, args)

    fun replace(fragment: Fragment, args: Bundle? = null) = change(fragment, false, args)
    fun <T : Fragment> replace(kClass: KClass<T>, args: Bundle? = null) =
        change(kClass, false, args)

    fun change(fragment: Fragment, backStack: Boolean = false, args: Bundle? = null)
    fun <T : Fragment> change(kClass: KClass<T>, backStack: Boolean = false, args: Bundle? = null)

    fun newRootFragment(fragment: Fragment, args: Bundle? = null)
    fun <T : Fragment> newRootFragment(kClass: KClass<T>, args: Bundle? = null)

    fun back(): Boolean

    fun setTitle(@StringRes title: Int)
    fun setTitle(title: String)
}

inline fun <reified T : Fragment> Navigation.add(args: Bundle? = null) = add(T::class, args)

inline fun <reified T : Fragment> Navigation.replace(args: Bundle? = null) = replace(T::class, args)

inline fun <reified T : Fragment> Navigation.newRootFragment(args: Bundle? = null) =
    newRootFragment(T::class, args)

inline fun <reified T : Fragment> Navigation.change(
    backStack: Boolean = false, args: Bundle? = null
) = change(T::class, backStack, args)