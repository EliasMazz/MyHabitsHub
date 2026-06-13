package com.yolo.core.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.yolo.core.catalog.demos.componentEntries
import com.yolo.core.catalog.demos.foundationEntries
import com.yolo.core.catalog.demos.patternEntries

/**
 * Design System Catalog registry.
 *
 * HOW TO ADD A NEW DEMO (keep the catalog growing — this is the whole process):
 * 1. Write a small `@Composable` demo in `demos/FoundationDemos.kt` or `demos/ComponentDemos.kt`
 *    (or a new file in `demos/` for a new area). The demo must follow the design system itself:
 *    tokens only, no hardcoded colors/sizes — the catalog is the reference implementation.
 * 2. Append a [CatalogEntry] to that file's entry list with a title + one-line description.
 * 3. Done — the entry appears in [DesignSystemCatalogScreen] automatically, grouped by category.
 *
 * Rules of the catalog:
 * - Every NEW design-system component MUST ship with a catalog entry in the same PR.
 * - Demos show the component in its meaningful states (enabled/disabled/error/loading, both
 *   modes via the catalog's dark toggle, all section worlds via the world selector).
 * - See docs/design/design-system-catalog.md for the written counterpart of this catalog.
 */
@Immutable
data class CatalogEntry(
    val title: String,
    val description: String,
    val content: @Composable () -> Unit,
)

enum class CatalogCategory(val label: String) {
    Foundations("Foundations — tokens"),
    Components("Components"),
    Patterns("Patterns — screen blueprints & drafts"),
}

val catalogSections: Map<CatalogCategory, List<CatalogEntry>> = mapOf(
    CatalogCategory.Foundations to foundationEntries,
    CatalogCategory.Components to componentEntries,
    CatalogCategory.Patterns to patternEntries,
)
