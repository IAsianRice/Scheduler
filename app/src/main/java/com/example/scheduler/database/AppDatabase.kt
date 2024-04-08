package com.example.scheduler.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scheduler.database.dao.ActiveScheduleItemDao
import com.example.scheduler.database.dao.ActiveScheduleQuotaCrossRefDao
import com.example.scheduler.database.dao.QuotaItemDao
import com.example.scheduler.database.dao.QuotaTagCrossRefDao
import com.example.scheduler.database.dao.ScheduleItemDao
import com.example.scheduler.database.dao.ScheduleTagCrossRefDao
import com.example.scheduler.database.dao.TagItemDao
import com.example.scheduler.database.entities.ActiveScheduleItem
import com.example.scheduler.database.entities.ActiveScheduleQuotaCrossRef
import com.example.scheduler.database.entities.QuotaItem
import com.example.scheduler.database.entities.QuotaTagCrossRef
import com.example.scheduler.database.entities.ScheduleItem
import com.example.scheduler.database.entities.ScheduleTagCrossRef
import com.example.scheduler.database.entities.TagItem

@Database(entities = [
    ActiveScheduleItem::class,
    ScheduleItem::class,
    QuotaItem::class,
    TagItem::class,
    ScheduleTagCrossRef::class,
    QuotaTagCrossRef::class,
    ActiveScheduleQuotaCrossRef::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scheduleItemDao(): ScheduleItemDao
    abstract fun quotaItemDao(): QuotaItemDao
    abstract fun activeScheduleItemDao(): ActiveScheduleItemDao
    abstract fun tagItemDao(): TagItemDao
    abstract fun scheduleTagCrossRefDao(): ScheduleTagCrossRefDao
    abstract fun quotaTagCrossRefDao(): QuotaTagCrossRefDao
    abstract fun activeScheduleQuotaCrossRefDao(): ActiveScheduleQuotaCrossRefDao
}