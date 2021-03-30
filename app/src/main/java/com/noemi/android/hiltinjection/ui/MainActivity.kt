package com.noemi.android.hiltinjection.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.noemi.android.hiltinjection.R
import com.noemi.android.hiltinjection.favorite.FavoriteFragment
import com.noemi.android.hiltinjection.latest.LatestFragment
import com.noemi.android.hiltinjection.popular.PopularFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var popularFragment: PopularFragment

    @Inject
    lateinit var latestFragment: LatestFragment

    @Inject
    lateinit var favoriteFragment: FavoriteFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.latest -> {
                    setCurrentFragment(latestFragment)
                    true
                }
                R.id.popular -> {
                    setCurrentFragment(popularFragment)
                    true
                }
                R.id.favorite -> {
                    setCurrentFragment(favoriteFragment)
                    true
                }
                else -> false
            }
        }

        bottom_navigation.selectedItemId = R.id.latest
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame, fragment).commit()
    }
}