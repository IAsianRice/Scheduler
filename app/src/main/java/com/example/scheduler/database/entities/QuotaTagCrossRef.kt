package com.example.scheduler.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.Relation

@Entity(tableName = "quota_tag_cross_ref",
    primaryKeys = ["quotaId", "tagId"],
    foreignKeys = [
        ForeignKey(entity = QuotaItem::class,
            parentColumns = ["quotaId"],
            childColumns = ["quotaId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = TagItem::class,
            parentColumns = ["tagId"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE)
    ])
data class QuotaTagCrossRef (
    val quotaId: Long,
    val tagId: Long
)

data class QuotasWithTags(
    @Embedded val quotaItem: QuotaItem,
    @Relation(
        parentColumn = "quotaId",
        entityColumn = "tagId",
        associateBy = Junction(QuotaTagCrossRef::class)
    )
    val tags: List<TagItem>
)

data class TagsWithQuotas(
    @Embedded val tagItem: TagItem,
    @Relation(
        parentColumn = "tagId",
        entityColumn = "quotaId",
        associateBy = Junction(QuotaTagCrossRef::class)
    )
    val quotaItem: List<QuotaItem>
)