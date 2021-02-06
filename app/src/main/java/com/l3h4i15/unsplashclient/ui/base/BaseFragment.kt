package com.l3h4i15.unsplashclient.ui.base

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.l3h4i15.unsplashclient.navigation.Navigation
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseFragment : Fragment() {
    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    @Inject
    lateinit var inputMethodManager: InputMethodManager

    @Inject
    lateinit var navigation: Navigation

    protected abstract val viewModel: BaseViewModel

    @StringRes
    protected open val title: Int? = null

    protected open val hasOptionsMenu: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(hasOptionsMenu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title?.let { viewModel.setTitle(it, navigation) }
    }

    override fun onDestroyView() {
        compositeDisposable.clear()
        if (!requireActivity().isChangingConfigurations) clearFocus()
        super.onDestroyView()
    }

    private fun clearFocus() {
        (requireActivity().currentFocus ?: requireView()).apply {
            clearFocus()
            inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
        }
    }
}