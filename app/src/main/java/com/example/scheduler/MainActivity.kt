package com.example.scheduler

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Rational
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
import com.example.scheduler.fragments.ActiveScheduleFragment
import com.example.scheduler.fragments.AddScheduleFragment
import com.example.scheduler.fragments.CalendarFragment
import com.example.scheduler.fragments.LandingPageFragment
import com.example.scheduler.fragments.PictureInPictureFragment
import com.example.scheduler.fragments.ScheduleDetailsFragment
import com.example.scheduler.fragments.ScheduleListFragment
import com.example.scheduler.fragments.SetScheduleFragment
import com.example.scheduler.fragments.UnimplementedFragment
import com.example.scheduler.fragments.WeeklyScheduleFragment
import com.example.scheduler.services.SchedulerNotificationService
import com.example.scheduler.services.SchedulerTrackerService
import com.example.scheduler.ui.theme.SchedulerTheme
import com.example.scheduler.viewmodels.DatabaseViewModel
import com.example.scheduler.viewmodels.FragmentState
import com.example.scheduler.viewmodels.FragmentViewModel
import com.example.scheduler.viewmodels.ScheduleViewModel

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var appDatabase: AppDatabase
        lateinit var fragmentViewModel: FragmentViewModel
        lateinit var scheduleViewModel: ScheduleViewModel
        lateinit var databaseViewModel: DatabaseViewModel
        const val TRACK_SCHEDULE = "com.example.scheduler.TRACK_SCHEDULE"

    }

    private val _fragmentViewModel: FragmentViewModel by viewModels()
    private val _scheduleViewModel: ScheduleViewModel by viewModels()
    private val _databaseViewModel: DatabaseViewModel by viewModels()

    private lateinit var addScheduleFragment: AddScheduleFragment
    private lateinit var landingPageFragment: LandingPageFragment
    private lateinit var scheduleDetailsFragment: ScheduleDetailsFragment
    private lateinit var calendarFragment: CalendarFragment
    private lateinit var weeklyScheduleFragment: WeeklyScheduleFragment
    private lateinit var scheduleListFragment: ScheduleListFragment
    private lateinit var pictureInPictureFragment: PictureInPictureFragment
    private lateinit var setScheduleFragment: SetScheduleFragment
    private lateinit var tasksFragment: ActiveScheduleFragment
    private lateinit var unimplementedFragment: UnimplementedFragment
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onUserLeaveHint() {
        val aspectRatio = Rational(1, 1) // Aspect ratio for PiP window
        var pictureInPictureParams = PictureInPictureParams.Builder()
        enterPictureInPictureMode(pictureInPictureParams.setAspectRatio(aspectRatio)
            .build())
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            fragmentViewModel.setFragmentState(FragmentState.PictureInPictureFragment)
        } else {
            fragmentViewModel.skipBackOver(FragmentState.PictureInPictureFragment)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the intent that started this activity

        val schedulerTrackerServiceIntent = Intent(this, SchedulerTrackerService::class.java)
        startService(schedulerTrackerServiceIntent)

        val schedulerNotificationServiceIntent = Intent(this, SchedulerNotificationService::class.java)
        startService(schedulerNotificationServiceIntent)

        appDatabase = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()

        fragmentViewModel = _fragmentViewModel
        scheduleViewModel = _scheduleViewModel
        databaseViewModel = _databaseViewModel

        databaseViewModel.bindService(this)

        addScheduleFragment = AddScheduleFragment()
        landingPageFragment = LandingPageFragment()
        scheduleDetailsFragment = ScheduleDetailsFragment()
        calendarFragment = CalendarFragment()
        weeklyScheduleFragment = WeeklyScheduleFragment()
        scheduleListFragment = ScheduleListFragment()
        setScheduleFragment = SetScheduleFragment()
        pictureInPictureFragment = PictureInPictureFragment()
        tasksFragment = ActiveScheduleFragment()
        unimplementedFragment = UnimplementedFragment()

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

        handleIntent(intent)

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent != null) {
            when(intent.action) {
                TRACK_SCHEDULE -> {
                    val scheduleId = intent.getLongExtra("schedule_id", 0L)
                    fragmentViewModel.setFragmentState(FragmentState.TasksFragment)
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
            FragmentState.CalendarFragment -> calendarFragment.Content()
            FragmentState.WeeklyScheduleFragment -> weeklyScheduleFragment.Content()
            FragmentState.SetScheduleFragment -> setScheduleFragment.Content()
            FragmentState.PictureInPictureFragment -> pictureInPictureFragment.Content()
            FragmentState.TasksFragment -> tasksFragment.Content()
            FragmentState.UnimplementedFragment -> unimplementedFragment.Content()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseViewModel.unbindService(this)
    }
}
