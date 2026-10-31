package de.hitlog.data

/**
 * Ein Gegner im aktuellen Kampf.
 *
 * @param toHit Wurf, ab dem der Gegner getroffen wird. Als String gespeichert,
 *              damit das Feld beim Bearbeiten auch leer sein darf.
 * @param defeated Gegner ist besiegt.
 */
data class Enemy(
    val id: Long,
    val name: String = "",
    val toHit: String = "",
    val damage: Int = 0,
    val defeated: Boolean = false,
)
