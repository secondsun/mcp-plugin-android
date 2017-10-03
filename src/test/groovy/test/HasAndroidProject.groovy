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


import com.android.build.gradle.AppPlugin
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before

trait HasAndroidProject {

    Project createMCPProject() {

        def temp = File.createTempDir();

        FileUtils.copyDirectory(getProjectDirectory(), temp)

        def mcpProject = ProjectBuilder.builder().withProjectDir(temp).build()

        new File((mcpProject.plugins.withType(AppPlugin).extension.sourceSets[0][2]).assets.getSrcDirs()[0].absolutePath).mkdirs();
        def mcpConfigFile = new File((mcpProject.plugins.withType(AppPlugin).extension.sourceSets[0][2]).assets.getSrcDirs()[0].absolutePath + '/mcp-config.json');
        mcpConfigFile.createNewFile()
        mcpConfigFile << getMCPFile().text;

        return mcpProject

    }

    File getMCPFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("").getFile()+ "../../../resources/test/mcp-config-complete.json");
        return file

    }

    private File getProjectDirectory() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("").getFile()+ "../../../resources/test/default-project");
        return file
    }

}
