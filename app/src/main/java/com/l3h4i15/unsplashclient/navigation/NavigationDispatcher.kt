package com.l3h4i15.unsplashclient.navigation

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.reflect.KClass

class NavigationDispatcher constructor(
    private val activity: AppCompatActivity,
    @IdRes private val containerId: Int
) : Navigation {

    init {
        activity.run {
            lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_CREATE) {
                        setBackActionVisibility()
                        lifecycle.removeObserver(this)
                    }
                }
            })
        }
    }

    override fun <T : Fragment> newRootFragment(kClass: KClass<T>, args: Bundle?) {
        val fragment = activity.supportFragmentManager.loadFragment(kClass) ?: return
        newRootFragment(fragment, args)
    }

    override fun newRootFragment(fragment: Fragment, args: Bundle?) = activity.run {
        supportFragmentManager.clearBackStack()
        change(fragment, args = args)
    }

    override fun <T : Fragment> change(kClass: KClass<T>, backStack: Boolean, args: Bundle?) {
        val fragment = activity.supportFragmentManager.loadFragment(kClass) ?: return
        change(fragment, backStack, args)
    }

    override fun change(fragment: Fragment, backStack: Boolean, args: Bundle?) {
        activity.run {
            supportFragmentManager.executeTransaction {
                if (backStack) addToBackStack(null)
                replace(containerId, fragment.apply { arguments = args })
            }
            setBackActionVisibility()
        }
    }

    override fun back(): Boolean = activity.run {
        val isBacked = supportFragmentManager.popBackStackImmediate()
        setBackActionVisibility()
        isBacked
    }

    override fun setTitle(title: Int) {
        activity.supportActionBar?.setTitle(title)
    }

    override fun setTitle(title: String) {
        activity.supportActionBar?.title = title
    }

    private fun AppCompatActivity.setBackActionVisibility() {
        supportActionBar?.setDisplayHomeAsUpEnabled(!supportFragmentManager.isBackStackEmpty())
    }

    private fun FragmentManager.clearBackStack() {
        if (!isBackStackEmpty()) {
            popBackStackImmediate(
                getBackStackEntryAt(0).id, FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
    }

    private fun <T : Fragment> FragmentManager.loadFragment(kClass: KClass<T>): Fragment? =
        kClass.java.run {
            val loader = classLoader ?: return null
            fragmentFactory.instantiate(loader, name)
        }

    private fun FragmentManager.isBackStackEmpty() = backStackEntryCount == 0

    private fun FragmentManager.executeTransaction(block: FragmentTransaction.() -> Unit) {
        beginTransaction().apply(block).commit()
        executePendingTransactions()
    }
}