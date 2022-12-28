package `in`.hahow.recruit.data.local.database

import `in`.hahow.recruit.data.remote.ClassStatus
import androidx.compose.runtime.Immutable
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.datetime.Instant

@Immutable
@Entity
data class ClassEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val status: ClassStatus?,
    val coverImageUrl: String?,
    val title: String?,
    val numSoldTickets: Int?,
    val successCriteriaNumSoldTickets: Int?,
    val proposalDueTime: Instant?,
)


@Dao
interface ClassDao {
    @Query("SELECT * FROM ClassEntity ORDER BY uid ASC")
    fun getClasses(): PagingSource<Int, ClassEntity>

    @Insert
    suspend fun insertClasses(items: List<ClassEntity>)

    @Query("DELETE FROM ClassEntity")
    suspend fun deleteClasses()
}