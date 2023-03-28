package com.plznoanr.data.model.local.json

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Map")
data class MapEntity(
    val mapName: String,
    @PrimaryKey val mapId: String
)