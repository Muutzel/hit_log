# Hit Log

Android app for D&D combat: track how much damage each enemy has taken and the minimum to-hit roll needed to hit it.

## Features
- **Add enemy** via the button at the bottom right
- Per enemy: **name**, **hit from** (to-hit, editable at any time), **damage** (starts at 0)
- Type in a value and press **+** (damage) or **−** (healing); damage never drops below 0
- Menu (⋮) per enemy: **Duplicate** (e.g. "Goblin" → "Goblin 2", damage 0) and **Delete** (with undo)
- **New fight** (↻ top right) removes all enemies after a confirmation
- Everything is stored locally and survives app restarts

## Build
- Open Android Studio → open the folder → Gradle sync → Run
- Requires JDK 17+ (the JDK bundled with Android Studio is sufficient)
- Tech: Kotlin, Jetpack Compose, Material 3, minSdk 26 (Android 8.0)

## Contributing / Commit Convention
Every commit that gets pushed must start with one of these prefixes:

| Prefix  | Use for                                                  |
|---------|----------------------------------------------------------|
| `feat:` | New feature                                              |
| `fix:`  | Bug fix                                                  |
| `docs:` | Documentation, releases, comment-only changes            |

Example: `feat: add duplicate option to enemy menu`

## AI Assistance
This project was developed with the help of Claude Sonnet 5.5 and Claude Opus 5.5 (Anthropic).
