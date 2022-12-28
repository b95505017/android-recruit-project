package `in`.hahow.recruit.data.remote

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassResponse(
    @SerialName("status")
    private val _status: String? = null,

    @SerialName("numSoldTickets")
    val numSoldTickets: Int? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("coverImageUrl")
    val coverImageUrl: String? = null,

    @SerialName("successCriteria")
    val successCriteria: SuccessCriteria? = null,

    @SerialName("proposalDueTime")
    private val _proposalDueTime: String? = null,
) {
    val status by lazy { ClassStatus.fromStatus(_status) }

    val proposalDueTime by lazy {
        runCatching {
            _proposalDueTime?.let {
                Instant.parse(it)
            }
        }.getOrNull()
    }
}

@Serializable
data class ClassesDataResponse(
    @SerialName("data")
    val data: List<ClassResponse>? = null,
)

@Serializable
data class SuccessCriteria(
    @SerialName("numSoldTickets")
    val numSoldTickets: Int? = null,
)

@Immutable
enum class ClassStatus(val status: String) {
    Incubating("INCUBATING"),
    Published("PUBLISHED"),
    Success("SUCCESS");

    companion object {
        fun fromStatus(value: String?) = values().find { it.status == value }
    }
}