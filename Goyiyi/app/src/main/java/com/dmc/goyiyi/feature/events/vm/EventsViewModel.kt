package com.dmc.goyiyi.feature.events.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmc.goyiyi.feature.events.data.model.Event
import com.dmc.goyiyi.feature.events.data.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.Normalizer
import javax.inject.Inject
import kotlin.math.max

data class EventFilters(
    val estados: Set<String> = emptySet(),
    val tipos: Set<String> = emptySet(),
    val organizadores: Set<String> = emptySet(),
    val metodosPago: Set<String> = emptySet(),
    val precioMin: Double? = null,
    val precioMax: Double? = null
)

enum class SortOrder { POPULARIDAD, PRECIO_ASC, PRECIO_DESC, CUPO_DESC, NOMBRE_ASC }

data class EventSearchUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val query: String = "",
    val filters: EventFilters = EventFilters(),
    val sort: SortOrder = SortOrder.POPULARIDAD,
    val all: List<Event> = emptyList(),
    val visible: List<Event> = emptyList()
)

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _allEvents = MutableStateFlow<List<Event>>(emptyList())
    private val _loading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    private val _query = MutableStateFlow("")
    private val _filters = MutableStateFlow(EventFilters())
    private val _sort = MutableStateFlow(SortOrder.POPULARIDAD)

    val uiState: StateFlow<EventSearchUiState>

    init {
        @OptIn(FlowPreview::class)
        val visibleFlow = combine(
            _allEvents,
            _query.debounce(250),
            _filters,
            _sort
        ) { all, q, f, s ->
            val normQ = normalize(q)
            val filtered = all.asSequence()
                .filter { e -> matchesQuery(e, normQ) }
                .filter { e -> filterEvent(e, f) }
                .toList()
                .let { sortEvents(it, s) }

            filtered
        }

        uiState = combine(
            _loading,
            _error,
            _query,
            _filters,
            _sort,
            _allEvents,
            visibleFlow
        ) { array: Array<Any?> ->

            val loading = array[0] as Boolean
            val error = array[1] as String?
            val q = array[2] as String
            val f = array[3] as EventFilters
            val s = array[4] as SortOrder
            val all = array[5] as List<Event>
            val vis = array[6] as List<Event>

            EventSearchUiState(
                isLoading = loading,
                error = error,
                query = q,
                filters = f,
                sort = s,
                all = all,
                visible = vis
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), EventSearchUiState())



        // carga inicial
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val data = repository.getEvents()
                _allEvents.value = data
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error desconocido"
            } finally {
                _loading.value = false
            }
        }
    }

    fun setQuery(q: String) { _query.value = q }

    fun setSort(sort: SortOrder) { _sort.value = sort }

    fun setFilters(update: EventFilters) { _filters.value = update }

    fun clearFilters() { _filters.value = EventFilters() }

    // ---------- helpers ----------

    private fun matchesQuery(e: Event, normQ: String): Boolean {
        if (normQ.isEmpty()) return true
        val fields = listOfNotNull(
            e.nombre, e.descripcion, e.organizador, e.tipo
        ).joinToString(" ")
        return normalize(fields).contains(normQ)
    }

    private fun filterEvent(e: Event, f: EventFilters): Boolean {
        fun inOrEmpty(value: String?, set: Set<String>): Boolean =
            set.isEmpty() || (value != null && set.contains(value))

        if (!inOrEmpty(e.estado, f.estados)) return false
        if (!inOrEmpty(e.tipo, f.tipos)) return false
        if (!inOrEmpty(e.organizador, f.organizadores)) return false
        if (!inOrEmpty(e.metodoPago, f.metodosPago)) return false

        val price = parsePrice(e)
        if (f.precioMin != null && (price == null || price < f.precioMin)) return false
        if (f.precioMax != null && (price == null || price > f.precioMax)) return false

        return true
    }

    private fun sortEvents(list: List<Event>, s: SortOrder): List<Event> = when (s) {
        SortOrder.POPULARIDAD -> list.sortedByDescending { popularity(it) }
        SortOrder.PRECIO_ASC -> list.sortedWith(compareBy(nullsLast()) { parsePrice(it) })
        SortOrder.PRECIO_DESC -> list.sortedWith(compareBy(nullsFirst<Double>()) { -1 * (parsePrice(it) ?: Double.NEGATIVE_INFINITY) })
        SortOrder.CUPO_DESC -> list.sortedByDescending { cupoDisponible(it) }
        SortOrder.NOMBRE_ASC -> list.sortedBy { normalize(it.nombre) }
    }

    private fun popularity(e: Event): Int =
        (e.contLikes ?: 0) - (e.contDislikes ?: 0)

    private fun cupoDisponible(e: Event): Int =
        max((e.cupoMax ?: 0) - (e.contEntradasVendidas ?: 0), 0)

    private fun parsePrice(e: Event): Double? {
        val raw = e.precio?.trim() ?: return null
        val cleaned = raw
            .replace(Regex("[^0-9,\\.]"), "")
            .let { s ->
                if (s.contains(',') && s.contains('.')) s.replace(".", "").replace(',', '.')
                else if (s.contains(',')) s.replace(',', '.')
                else s
            }
        return cleaned.toDoubleOrNull()
    }

    private fun normalize(s: String?): String =
        Normalizer.normalize(s.orEmpty().lowercase(), Normalizer.Form.NFD)
            .replace("\\p{Mn}+".toRegex(), "")
}
