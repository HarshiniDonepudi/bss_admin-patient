package com.example.lion_nav_barhomepage

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
//import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
lateinit var context_activity : Context
lateinit var fm: FragmentManager
var count: Int =0
lateinit  var view_Pager: ViewPager
 lateinit var sharedPreferences: SharedPreferences

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstancesState: Bundle?) {
        super.onCreate(savedInstancesState)
        setContentView(R.layout.activity_login)

        sharedPreferences=getSharedPreferences("login", MODE_PRIVATE)
        val v=0.0f
        val tabLayout: TabLayout = findViewById(R.id.tab)

        tabLayout.addTab(tabLayout.newTab().setText("Register"));
        tabLayout.addTab(tabLayout.newTab().setText("Patient Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Admin Login"));
        val viewPager: ViewPager = findViewById(R.id.viewpage)
        view_Pager= viewPager
//        fb.setOnClickListener {
////            tabLayout.getTabAt(0)
//            viewPager.setCurrentItem(1)
//        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        val adapter = LoginAdapter(
            this, supportFragmentManager,
            tabLayout.tabCount)
        context_activity=this
        fm=supportFragmentManager
        count=tabLayout.tabCount
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })



        tabLayout.setTranslationY(300F)

        tabLayout.setAlpha(v)

        tabLayout.animate().translationY(0F).alpha(1F).setDuration(1000).setStartDelay(400).start()

    }

//     fun calltab() {
//         val viewPager: ViewPager = findViewById(R.id.viewpage)
//         viewPager.setCurrentItem(1)
//
//    }
}
