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
package test

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.google.common.collect.ImmutableMap
import groovy.util.logging.Log
import org.apache.log4j.Level;
import org.junit.Test;

@Log
class SimpleTests implements HasAndroidProject {

    @Test
    public void testGenerateResource() {
        mcpProject.apply(ImmutableMap.of("plugin", "com.android.application"))
        def plugin
        plugin = mcpProject.plugins.getPlugin(AppPlugin)

        def android = mcpProject.getExtensions().getByType(AppExtension);
        android.setCompileSdkVersion(26);
        android.setBuildToolsVersion("26.0.1");
        plugin.createAndroidTasks(false);

        mcpProject.apply(ImmutableMap.of("plugin", "mcp-android-plugin"))
        def tasks = mcpProject.getAllTasks(true)
        log.log(Level.DEBUG, tasks);
        //test that tasks contains processDebugMCP
    }

}
