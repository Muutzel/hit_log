package de.hitlog

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import de.hitlog.data.Enemy
import de.hitlog.data.EnemyStore

class FightViewModel(app: Application) : AndroidViewModel(app) {

    private val store = EnemyStore(app)

    /** Compose-State statt Flow, damit Texteingaben synchron bleiben (kein Cursor-Springen). */
    var enemies: List<Enemy> by mutableStateOf(store.load())
        private set

    private var lastRemoved: Pair<Int, Enemy>? = null

    private fun mutate(transform: (List<Enemy>) -> List<Enemy>) {
        enemies = transform(enemies)
        store.save(enemies)
    }

    private fun nextId(): Long = (enemies.maxOfOrNull { it.id } ?: 0L) + 1

    private fun updateEnemy(id: Long, change: (Enemy) -> Enemy) =
        mutate { list -> list.map { if (it.id == id) change(it) else it } }

    fun addEnemy() {
        val id = nextId()
        mutate { it + Enemy(id = id) }
    }

    fun rename(id: Long, name: String) = updateEnemy(id) { it.copy(name = name) }

    fun setToHit(id: Long, toHit: String) = updateEnemy(id) { it.copy(toHit = toHit) }

    /** Positiver Wert = Schaden, negativer Wert = Heilung. Schaden fällt nie unter 0. */
    fun changeDamage(id: Long, delta: Int) =
        updateEnemy(id) { it.copy(damage = (it.damage + delta).coerceIn(0, MAX_DAMAGE)) }

    fun duplicate(id: Long) {
        val list = enemies
        val index = list.indexOfFirst { it.id == id }
        if (index < 0) return
        val source = list[index]
        val copy = source.copy(id = nextId(), name = nextName(source.name, list), damage = 0)
        mutate { it.toMutableList().apply { add(index + 1, copy) } }
    }

    fun remove(id: Long) {
        val index = enemies.indexOfFirst { it.id == id }
        if (index < 0) return
        lastRemoved = index to enemies[index]
        mutate { list -> list.filterNot { it.id == id } }
    }

    fun undoRemove() {
        val (index, enemy) = lastRemoved ?: return
        lastRemoved = null
        mutate { list -> list.toMutableList().apply { add(index.coerceAtMost(size), enemy) } }
    }

    fun clearAll() {
        lastRemoved = null
        mutate { emptyList() }
    }

    /** "Goblin" -> "Goblin 2", "Goblin 2" -> "Goblin 3" (bzw. nächste freie Nummer). */
    private fun nextName(name: String, list: List<Enemy>): String {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return ""
        val base = NUMBERED.matchEntire(trimmed)?.groupValues?.get(1)?.takeIf { it.isNotBlank() } ?: trimmed
        val highest = list.maxOf { enemy ->
            val other = enemy.name.trim()
            val match = NUMBERED.matchEntire(other)
            when {
                other == base -> 1
                match != null && match.groupValues[1] == base -> match.groupValues[2].toIntOrNull() ?: 1
                else -> 0
            }
        }
        return "$base ${highest + 1}"
    }

    private companion object {
        const val MAX_DAMAGE = 99_999
        val NUMBERED = Regex("""^(.*?)\s*(\d+)$""")
    }
}
