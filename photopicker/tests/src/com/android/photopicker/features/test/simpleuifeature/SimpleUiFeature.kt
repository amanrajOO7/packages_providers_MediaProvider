/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.photopicker.features.simpleuifeature

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import com.android.photopicker.core.configuration.PhotopickerConfiguration
import com.android.photopicker.core.events.RegisteredEventClass
import com.android.photopicker.core.features.FeatureManager
import com.android.photopicker.core.features.FeatureRegistration
import com.android.photopicker.core.features.Location
import com.android.photopicker.core.features.LocationParams
import com.android.photopicker.core.features.PhotopickerUiFeature
import com.android.photopicker.core.features.Priority
import com.android.photopicker.core.navigation.Route

/** Test [PhotopickerUiFeature] that renders a simple string to [Location.COMPOSE_TOP] */
open class SimpleUiFeature : PhotopickerUiFeature {

    companion object Registration : FeatureRegistration {
        override val TAG: String = "SimpleUiFeature"

        override fun isEnabled(config: PhotopickerConfiguration) = true

        override fun build(featureManager: FeatureManager) = SimpleUiFeature()

        val UI_STRING = "I'm a simple string, from a SimpleUiFeature"
        val SIMPLE_ROUTE = "simple"
        val BUTTON_LABEL = "Simple"
    }

    override val token = TAG

    override val eventsConsumed = emptySet<RegisteredEventClass>()

    override val eventsProduced = emptySet<RegisteredEventClass>()

    /** Compose Location callback from feature framework */
    override fun registerLocations(): List<Pair<Location, Int>> {
        return listOf(
            Pair(Location.COMPOSE_TOP, Priority.REGISTRATION_ORDER.priority),
            Pair(Location.SELECTION_BAR_SECONDARY_ACTION, Priority.REGISTRATION_ORDER.priority),
        )
    }

    /** Navigation registration callback from feature framework */
    override fun registerNavigationRoutes(): Set<Route> {
        return setOf(
            object : Route {
                override val route = SIMPLE_ROUTE
                override val initialRoutePriority = Priority.MEDIUM.priority
                override val arguments = emptyList<NamedNavArgument>()
                override val deepLinks = emptyList<NavDeepLink>()
                override val isDialog = false
                override val dialogProperties = null
                override val enterTransition = null
                override val exitTransition = null
                override val popEnterTransition = null
                override val popExitTransition = null

                @Composable
                override fun composable(navBackStackEntry: NavBackStackEntry?) {
                    simpleRoute()
                }
            },
        )
    }

    /* Feature framework compose-at-location callback */
    @Composable
    override fun compose(
        location: Location,
        modifier: Modifier,
        params: LocationParams,
    ) {
        when (location) {
            Location.COMPOSE_TOP -> composeTop(params)
            Location.SELECTION_BAR_SECONDARY_ACTION -> selectionBarAction()
            else -> {}
        }
    }

    /* Private composable used for the [Location.COMPOSE_TOP] location */
    @Composable
    private fun composeTop(params: LocationParams) {
        TextButton(
            onClick = {
                val clickHandler = params as? LocationParams.WithClickAction
                clickHandler?.onClick()
            }
        ) {
            Text(UI_STRING)
        }
    }

    /* Composes the [SIMPLE_ROUTE] */
    @Composable
    private fun simpleRoute() {
        Text(UI_STRING)
    }

    /* Composes the action button for the selection bar */
    @Composable
    private fun selectionBarAction() {
        TextButton(onClick = {}) { Text(BUTTON_LABEL) }
    }
}