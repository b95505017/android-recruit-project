package `in`.hahow.recruit.ui.classes

import `in`.hahow.android_recruit_project.R
import `in`.hahow.recruit.data.local.database.ClassEntity
import `in`.hahow.recruit.data.remote.ClassStatus
import `in`.hahow.recruit.ui.theme.Purple40
import `in`.hahow.recruit.ui.theme.brandIncubating
import `in`.hahow.recruit.ui.theme.brandPublished
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.math.min

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HahowClassesScreen(
    modifier: Modifier = Modifier,
    viewModel: HahowClassesViewModel = hiltViewModel(),
) {
    val classes = viewModel.classes.collectAsLazyPagingItems()
    val now = remember { Clock.System.now() }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(classes, key = { it.uid }) {
            it?.apply {
                ClassItem(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .animateItemPlacement(),
                    item = this,
                    now = now
                )
            }
        }
    }
}

@Composable
fun ClassItem(
    modifier: Modifier = Modifier,
    item: ClassEntity,
    now: Instant
) = Row(
    modifier = modifier
        .background(MaterialTheme.colorScheme.surface)
        .padding(16.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.35f)
            .aspectRatio(1.6f),
        contentAlignment = Alignment.BottomEnd,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.coverImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
                .clip(MaterialTheme.shapes.small)

        )
        item.status?.let {
            StatusLabel(status = it)
        }
    }
    Column {
        Text(
            text = item.title.orEmpty(),
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(14.dp))
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.outline,
            LocalTextStyle provides MaterialTheme.typography.labelMedium,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (item.numSoldTickets != null
                    && item.successCriteriaNumSoldTickets != null
                ) {
                    Column {
                        val percentage =
                            remember(item.numSoldTickets, item.successCriteriaNumSoldTickets) {
                                min(
                                    item.numSoldTickets.toFloat() / item.successCriteriaNumSoldTickets,
                                    1f
                                )
                            }
                        if (item.status == ClassStatus.Published) {
                            val pInt = remember(percentage) {
                                (percentage * 100).toInt()
                            }
                            Text("${pInt}%")
                        } else {
                            Text(
                                stringResource(
                                    R.string.non_published_label,
                                    item.numSoldTickets,
                                    item.successCriteriaNumSoldTickets
                                )
                            )
                        }
                        item.status?.apply {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(0.3f),
                                progress = percentage,
                                color = remember(this) {
                                    when (this) {
                                        ClassStatus.Incubating -> brandIncubating
                                        ClassStatus.Published -> brandPublished
                                        ClassStatus.Success -> Purple40
                                    }
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                item.proposalDueTime?.also { proposalDueTime ->
                    val days = remember(proposalDueTime) { proposalDueTime.minus(now).inWholeDays }
                    Icon(
                        imageVector = Icons.Default.AvTimer,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(stringResource(R.string.proposalDueTime, days))
                }
            }
        }
    }
}

@Composable
private fun StatusLabel(
    modifier: Modifier = Modifier,
    status: ClassStatus,
) {
    val (color, stringRes) = remember(status) {
        when (status) {
            ClassStatus.Incubating -> brandIncubating to R.string.status_incubating
            ClassStatus.Published -> brandPublished to R.string.status_published
            ClassStatus.Success -> Purple40 to R.string.status_success
        }
    }
    val shape = MaterialTheme.shapes.small.copy(
        topEnd = CornerSize(0.dp),
        bottomStart = CornerSize(0.dp),
    )
    Text(
        text = stringResource(stringRes),
        modifier = modifier
            .background(color, shape)
            .padding(4.dp)
            .clip(shape),
        color = MaterialTheme.colorScheme.onPrimary,
        style = MaterialTheme.typography.labelMedium,
    )
}