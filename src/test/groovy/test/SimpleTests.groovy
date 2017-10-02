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
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testkit.runner.GradleRunner
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore;
import org.junit.Test

import java.util.logging.Level;

@Log
class SimpleTests {

    List<File> pluginClasspath

    @Before
    void setUp() {

        def pluginClasspathResource = getClass().classLoader.findResource("plugin-classpath.txt")
        if (pluginClasspathResource == null) {
            throw new IllegalStateException("Did not find plugin classpath resource, run `testClasses` build task.")
        }

        pluginClasspath = pluginClasspathResource.readLines().collect { new File(it) }
    }

    //@Test @Ignore
    public void testGenerateResource() {

        def result =GradleRunner.create()
                .withProjectDir(getProjectDirectory())
                .withArguments('tasks')
                .withPluginClasspath(pluginClasspath)
                .build()

        Assert.assertTrue(    result.output.contains('app:processMCPDebug'));
    }



    File getProjectDirectory() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("").getFile()+ "../../../resources/test/default-project");
        return file
    }


}
