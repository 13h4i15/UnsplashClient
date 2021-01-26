package com.l3h4i15.unsplashclient.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentFactory
import com.l3h4i15.unsplashclient.R
import com.l3h4i15.unsplashclient.databinding.ActivityMainBinding
import com.l3h4i15.unsplashclient.navigation.Navigation
import com.l3h4i15.unsplashclient.navigation.newRootFragment
import com.l3h4i15.unsplashclient.ui.random.RandomPictureFragment
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var factory: FragmentFactory

    @Inject
    lateinit var navigation: Navigation

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = factory
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbar)
        binding.onNavigationClickListener = View.OnClickListener { onBackPressed() }

        if (savedInstanceState == null) navigation.newRootFragment<RandomPictureFragment>()
    }

    override fun onBackPressed() {
        if (!navigation.back()) super.onBackPressed()
    }
}