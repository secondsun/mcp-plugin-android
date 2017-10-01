/*
 * Copyright 2017 Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.feedhenry.tools.android


import org.gradle.api.Plugin
import org.gradle.api.Project

import static org.feedhenry.tools.android.util.AndroidPathUtils.handleVariant


class FeedHenryAndroidMCPPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        target.with  {
            if (!plugins.hasPlugin("com.android.application") ||
                    !plugins.hasPlugin("com.android.library")) {
                logger.warn("Did not detect Android Plugin, be sure the mcp plugin is declared after the android plugins.");
            }

            plugins.withId("com.android.application", {
                android.applicationVariants.all { variant ->
                    handleVariant(project, variant)
                }
            });

            plugins.withId("com.android.library", {
                android.libraryVariants.all { variant ->
                    handleVariant(project, variant)
                }
            });
        }


    }


}
