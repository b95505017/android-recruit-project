package `in`.hahow.recruit.data.di

import `in`.hahow.recruit.data.DefaultClassRepository
import `in`.hahow.recruit.data.HahowClassRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindsHaHowClassRepository(
        repository: DefaultClassRepository,
    ): HahowClassRepository
}

@Module
@InstallIn(SingletonComponent::class)
object JsonModule {
    @get:Singleton
    @get:Provides
    val json
        get() = Json {
            ignoreUnknownKeys = true
            isLenient = true
            allowSpecialFloatingPointValues = true
            encodeDefaults = false
        }
}