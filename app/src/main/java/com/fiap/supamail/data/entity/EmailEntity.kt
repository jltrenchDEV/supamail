package com.fiap.supamail.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = tableName)
data class EmailEntity(
    @PrimaryKey(autoGenerate = true)
    @SerialName("id")
    val id: Int = 0,

    @SerialName("sender")
    val sender: String = "",

    @SerialName("receiver")
    val receiver: String = "",

    @SerialName("subject")
    val subject: String = "",

    @SerialName("body")
    val body: String = "",

    @SerialName("alreadyOpened")
    val alreadyOpened: Boolean = false,

    @SerialName("favorite")
    val favorite: Boolean = true,

    @SerialName("sentDate")
    val sentDate: String = ""
)

const val tableName = "emails"