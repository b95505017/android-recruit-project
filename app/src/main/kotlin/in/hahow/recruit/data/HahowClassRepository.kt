package `in`.hahow.recruit.data

import `in`.hahow.recruit.data.local.database.AppDatabase
import `in`.hahow.recruit.data.local.database.ClassEntity
import `in`.hahow.recruit.data.remote.ClassesDataResponse
import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject

interface HahowClassRepository {
    val classes: Flow<PagingData<ClassEntity>>
}

@OptIn(ExperimentalPagingApi::class)
class DefaultClassRepository @Inject constructor(
    @ApplicationContext
    context: Context,
    database: AppDatabase,
    json: Json,
) : HahowClassRepository {
    override val classes = Pager(
        config = PagingConfig(pageSize = 10),
        remoteMediator = ClassRemoteMediator(
            context = context,
            database = database,
            json = json,
        ),
    ) {
        database.classDao().getClasses()
    }.flow


    @OptIn(ExperimentalSerializationApi::class)
    class ClassRemoteMediator(
        private val context: Context,
        private val database: AppDatabase,
        private val json: Json,
    ): RemoteMediator<Int, ClassEntity>() {
        override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, ClassEntity>
        ) = try {
            if (loadType == LoadType.REFRESH) {
                val data = withContext(Dispatchers.Default) {
                    json.decodeFromStream<ClassesDataResponse>(
                        context.assets.open("data.json")
                    ).data.orEmpty()
                        .map {
                            with(it) {
                                ClassEntity(
                                    status = status,
                                    coverImageUrl = coverImageUrl,
                                    title = title,
                                    numSoldTickets = numSoldTickets,
                                    successCriteriaNumSoldTickets = successCriteria?.numSoldTickets,
                                    proposalDueTime = proposalDueTime,
                                )
                            }
                        }
                }
                with(database) {
                    withTransaction {
                        classDao().deleteClasses()
                        classDao().insertClasses(data)
                    }
                }
            }
            MediatorResult.Success(endOfPaginationReached = true)
        } catch (t: Throwable) {
            MediatorResult.Error(t)
        }
    }
}