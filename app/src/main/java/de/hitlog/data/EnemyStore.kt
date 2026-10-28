package de.hitlog.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/** Speichert die Gegnerliste lokal als JSON in den SharedPreferences. */
class EnemyStore(context: Context) {

    private val prefs = context.getSharedPreferences("fight_state", Context.MODE_PRIVATE)

    fun load(): List<Enemy> {
        val raw = prefs.getString(KEY_ENEMIES, null) ?: return emptyList()
        return try {
            val array = JSONArray(raw)
            (0 until array.length()).map { i ->
                val obj = array.getJSONObject(i)
                Enemy(
                    id = obj.getLong("id"),
                    name = obj.optString("name", ""),
                    toHit = obj.optString("toHit", ""),
                    damage = obj.optInt("damage", 0),
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun save(enemies: List<Enemy>) {
        val array = JSONArray()
        enemies.forEach { enemy ->
            array.put(
                JSONObject()
                    .put("id", enemy.id)
                    .put("name", enemy.name)
                    .put("toHit", enemy.toHit)
                    .put("damage", enemy.damage)
            )
        }
        prefs.edit().putString(KEY_ENEMIES, array.toString()).apply()
    }

    private companion object {
        const val KEY_ENEMIES = "enemies"
    }
}
