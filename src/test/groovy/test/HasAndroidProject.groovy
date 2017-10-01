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
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before

trait HasAndroidProject {

    Project mcpProject;


    @Before
    void createMCPProject() {

        mcpProject = ProjectBuilder.builder().withProjectDir(
                File.createTempDir()).build()
        mcpProject.plugins.apply('com.android.application')
        Eval.me("project", mcpProject,"\n" +
                "project.android {\n" +
                "    compileSdkVersion 26\n" +
                "    buildToolsVersion \"26.0.1\"\n" +
                "    defaultConfig {\n" +
                "        applicationId \"org.feedhenry.mcp.mcp_demo\"\n" +
                "        minSdkVersion 19\n" +
                "        targetSdkVersion 26\n" +
                "        versionCode 1\n" +
                "        versionName \"1.0\"\n" +
                "        testInstrumentationRunner \"android.support.test.runner.AndroidJUnitRunner\"\n" +
                "    }\n" +
                "\n" +
                "    buildTypes {\n" +
                "        release {\n" +
                "            minifyEnabled false\n" +
                "            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'\n" +
                "        }\n" +
                "    }\n" +
                "    compileOptions {\n" +
                "        targetCompatibility 1.8\n" +
                "        sourceCompatibility 1.8\n" +
                "    }\n" +
                "            sourceSets {\n" +
                "                main {\n" +
                "                    manifest.srcFile  'src/test/resources/AndroidManifest-complete.xml'\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "        }")

        new File((mcpProject.plugins.withType(AppPlugin).extension.sourceSets[0][2]).assets.getSrcDirs()[0].absolutePath).mkdirs();
        def mcpConfigFile = new File((mcpProject.plugins.withType(AppPlugin).extension.sourceSets[0][2]).assets.getSrcDirs()[0].absolutePath + '/mcp-config.json');
        mcpConfigFile.createNewFile()
        mcpConfigFile << new File('src/test/resources/mcp-config-complete.json').text

        new File([mcpProject.projectDir.absolutePath, "src", "test", "resources"].join(File.separator)).mkdirs();
        def manifestFile = new File([mcpProject.projectDir.absolutePath, "src", "test", "resources", "AndroidManifest-complete.xml"].join(File.separator));
        manifestFile.createNewFile()
        manifestFile << new File('src/test/resources/AndroidManifest-complete.xml').text

    }


}
