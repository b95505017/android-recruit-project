package `in`.hahow.recruit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.datetime.Instant

@TypeConverters(
    InstantConverter::class,
)
@Database(
    entities = [ClassEntity::class],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun classDao(): ClassDao
}

object InstantConverter {
    @TypeConverter
    fun decode(databaseValue: Long) = Instant.fromEpochSeconds(databaseValue)

    @TypeConverter
    fun encode(value: Instant) = value.epochSeconds
}