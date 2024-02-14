package com.example.scheduler

import android.app.PictureInPictureParams
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.example.scheduler.database.AppDatabase
import com.example.scheduler.fragments.AddScheduleFragment
import com.example.scheduler.fragments.LandingPageFragment
import com.example.scheduler.fragments.ScheduleDetailsFragment
import com.example.scheduler.fragments.ScheduleListFragment
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.viewmodels.DatabaseViewModel
import com.example.scheduler.viewmodels.FragmentState
import com.example.scheduler.viewmodels.FragmentViewModel

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var appDatabase: AppDatabase
        lateinit var fragmentViewModel: FragmentViewModel
        lateinit var databaseViewModel: DatabaseViewModel
    }

    private val _fragmentViewModel: FragmentViewModel by viewModels()
    private val _databaseViewModel: DatabaseViewModel by viewModels()

    private lateinit var addScheduleFragment: AddScheduleFragment
    private lateinit var landingPageFragment: LandingPageFragment
    private lateinit var scheduleDetailsFragment: ScheduleDetailsFragment
    private lateinit var scheduleListFragment: ScheduleListFragment
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onUserLeaveHint() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            var pictureInPictureParams = PictureInPictureParams.Builder()
            enterPictureInPictureMode(pictureInPictureParams.build())
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()

        fragmentViewModel = _fragmentViewModel
        databaseViewModel = _databaseViewModel

        addScheduleFragment = AddScheduleFragment()
        landingPageFragment = LandingPageFragment()
        scheduleDetailsFragment = ScheduleDetailsFragment()
        scheduleListFragment = ScheduleListFragment()



        // Back Button Functionality
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!fragmentViewModel.back())
                {
                    finish()
                }
            }
        })

        setContent {
            SchedulerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FragmentContainer()
                }
            }
        }
    }
    @Composable
    fun FragmentContainer() {
        // Observe FragmentStates and Change Dynamically
        val currentFragmentState by fragmentViewModel.fragmentStateFlow.collectAsState()
        when (currentFragmentState)
        {
            FragmentState.AddScheduleFragment -> addScheduleFragment.Content()
            FragmentState.LandingPageFragment -> landingPageFragment.Content()
            FragmentState.ScheduleDetailsFragment -> scheduleDetailsFragment.Content()
            FragmentState.ScheduleListFragment -> scheduleListFragment.Content()
        }
    }
}
