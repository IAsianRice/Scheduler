package com.example.scheduler.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.Relation

@Entity(tableName = "active_schedule_quota_cross_ref",
    primaryKeys = ["activeScheduleId", "quotaId"],
    foreignKeys = [
        ForeignKey(entity = ActiveScheduleItem::class,
            parentColumns = ["activeScheduleId"],
            childColumns = ["activeScheduleId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = QuotaItem::class,
            parentColumns = ["quotaId"],
            childColumns = ["quotaId"],
            onDelete = ForeignKey.CASCADE)
    ])
data class ActiveScheduleQuotaCrossRef (
    val activeScheduleId: Long,
    val quotaId: Long,
    val quotaNeeded: Int,
    val quotaFinished: Int
)

data class ActiveSchedulesWithQuota(
    @Embedded val activeScheduleItem: ActiveScheduleItem,
    @Relation(
        parentColumn = "activeScheduleId",
        entityColumn = "quotaId",
        associateBy = Junction(ActiveScheduleQuotaCrossRef::class)
    )
    val quotas: List<QuotaItem>
)

data class QuotaWithActiveSchedules(
    @Embedded val quotaItem: QuotaItem,
    @Relation(
        parentColumn = "quotaId",
        entityColumn = "activeScheduleId",
        associateBy = Junction(ActiveScheduleQuotaCrossRef::class)
    )
    val activeScheduleItems: List<ActiveScheduleItem>
)