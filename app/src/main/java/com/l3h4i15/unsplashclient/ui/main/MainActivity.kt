package com.l3h4i15.unsplashclient.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.databinding.ActivityMainBinding
import com.l3h4i15.unsplashclient.navigation.Navigation
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigation: Navigation

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        binding.onNavigationClickListener = View.OnClickListener { onBackPressed() }

        val viewModel by viewModels<MainViewModel> { viewModelFactory }
        if (savedInstanceState == null) viewModel.setupRoot(navigation)
    }

    override fun onBackPressed() {
        if (!navigation.back()) super.onBackPressed()
    }
}