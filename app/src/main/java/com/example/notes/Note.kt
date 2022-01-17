package com.example.notes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Note(
    val first: ByteArray,
    val second: ByteArray
):Serializable{
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}