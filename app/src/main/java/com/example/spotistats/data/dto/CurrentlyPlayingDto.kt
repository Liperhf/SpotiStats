import android.content.Context
import android.media.Image
import com.example.spotistats.data.dto.ItemDto

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

data class ActionsDto(
    val interrupting_playback: Boolean,
    val pausing: Boolean,
    val resuming: Boolean,
    val seeking: Boolean,
    val skipping_next: Boolean,
    val skipping_prev: Boolean,
    val toggling_repeat_context: Boolean,
    val toggling_repeat_track: Boolean,
    val toggling_shuffle: Boolean,
    val transferring_playback: Boolean
)

data class ContextDto(
    val external_urls: ExternalUrlsDto,
    val href: String,
    val type: String,
    val uri: String
)

data class DeviceDto(
    val id: String,
    val is_active: Boolean,
    val is_private_session: Boolean,
    val is_restricted: Boolean,
    val name: String,
    val supports_volume: Boolean,
    val type: String,
    val volume_percent: Int
)

data class CurrentlyItemDto(
    val album: AlbumDto,
    val artists: List<ArtistXDto>,
    val available_markets: List<String>,
    val disc_number: Int,
    val duration_ms: Int,
    val explicit: Boolean,
    val external_ids: ExternalIdsDto,
    val external_urls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val is_local: Boolean,
    val is_playable: Boolean,
    val linked_from: LinkedFromDto,
    val name: String,
    val popularity: Int,
    val preview_url: String,
    val restrictions: RestrictionsXDto,
    val track_number: Int,
    val type: String,
    val uri: String
)

data class ExternalUrlsDto(
    val spotify: String
)

data class AlbumDto(
    val album_type: String,
    val artists: List<ArtistXDto>,
    val available_markets: List<String>,
    val external_urls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val images: List<ImageDto>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val restrictions: RestrictionsXDto,
    val total_tracks: Int,
    val type: String,
    val uri: String
)

data class ArtistXDto(
    val external_urls: ExternalUrlsDto,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

data class ExternalIdsDto(
    val ean: String,
    val isrc: String,
    val upc: String
)

class LinkedFromDto

data class RestrictionsXDto(
    val reason: String
)

data class ImageDto(
    val height: Int,
    val url: String,
    val width: Int
)