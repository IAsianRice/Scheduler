package com.example.scheduler.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.Relation

@Entity(tableName = "schedule_tag_cross_ref",
    primaryKeys = ["scheduleId", "tagId"],
    foreignKeys = [
        ForeignKey(entity = ScheduleItem::class,
            parentColumns = ["scheduleId"],
            childColumns = ["scheduleId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = TagItem::class,
            parentColumns = ["tagId"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE)
    ])
data class ScheduleTagCrossRef (
    val scheduleId: Long,
    val tagId: Long
)

data class SchedulesWithTags(
    @Embedded val scheduleItem: ScheduleItem,
    @Relation(
        parentColumn = "scheduleId",
        entityColumn = "tagId",
        associateBy = Junction(ScheduleTagCrossRef::class)
    )
    val tags: List<TagItem>
)

data class TagsWithSchedules(
    @Embedded val tagItem: TagItem,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "scheduleId",
        associateBy = Junction(ScheduleTagCrossRef::class)
    )
    val scheduleItems: List<ScheduleItem>
)