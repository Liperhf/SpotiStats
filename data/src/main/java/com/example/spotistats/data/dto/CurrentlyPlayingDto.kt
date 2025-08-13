import android.content.Context
import android.media.Image
import com.example.spotistats.data.dto.common.ActionsDto
import com.example.spotistats.data.dto.common.ContextDto
import com.example.spotistats.data.dto.common.CurrentlyItemDto
import com.example.spotistats.data.dto.common.DeviceDto
import com.example.spotistats.data.dto.common.ExternalUrlsDto

data class CurrentlyPlayingDto(
    val actions: ActionsDto,
    val context: ContextDto,
    val currently_playing_type: String,
    val device: DeviceDto,
    val is_playing: Boolean,
    val item: CurrentlyItemDto,
    val progress_ms: Int,
    val repeat_state: String,
    val shuffle_state: Boolean,
    val timestamp: Long
)