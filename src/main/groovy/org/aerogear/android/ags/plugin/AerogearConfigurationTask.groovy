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
package org.aerogear.android.ags.plugin

import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class AerogearConfigurationTask extends DefaultTask {

    @InputFile @Optional
    public File mcpConfigFile;


    @OutputDirectory
    public File generatedPropsDir;

    @TaskAction
    void action(){
        if (!mcpConfigFile.isFile()) {
            throw new GradleException("File $mcpConfigFile.name is missing. ")
        }

        project.logger.debug("Parsing $mcpConfigFile.path")



        def slurper = new JsonSlurper();
        MCPConfig mcpConfig = new MCPConfig(slurper.parse(mcpConfigFile)).verify()

        File configXmlDir = new File(generatedPropsDir, "values");
        if (!configXmlDir.exists() && !configXmlDir.mkdirs()) {
            throw new GradleException("Failed to create folder: " + configXmlDir);
        }

        writeValues(configXmlDir, mcpConfig);

    }

    void writeValues(File dir, MCPConfig mcpConfig) {
        if (!dir.exists() &&!dir.mkdirs()) {
            throw new GradleException("Could not create $dir.path");
        }

        def outputFile = new File(dir, "values.xml");
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.resources {
            string name:'mcp_host', translatable:'false', mcpConfig.host
            string name:'mcp_apiKey', translatable:'false', mcpConfig.apiKey
            string name:'mcp_appId', translatable:'false', mcpConfig.appId
        }

        outputFile << writer.toString()

    }

    private static class MCPConfig {

        final String host, appId, apiKey

        MCPConfig(Map json) {
            host = json.host
            appId = json.appID
            apiKey = json.apiKey
        }

        MCPConfig verify() {
            if (!host) {
                throw new GradleException("config file is missing host property")
            }

            if (!apiKey) {
                throw new GradleException("config file is missing apiKey property")
            }

            if (!appId) {
                throw new GradleException("config file is missing appId property")
            }
            return this
        }

    }

}
