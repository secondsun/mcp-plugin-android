package org.feedhenry.tools.android

import groovy.json.JsonSlurper
import groovy.xml.MarkupBuilder
import org.codehaus.groovy.tools.shell.util.Logger
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

import java.util.logging.Level

class MCPExtension {
    /**
     * Enable downloading and saving certificates from MCP services
     */
    boolean enableCertificateHelper = false
    /**
     * A pattern to prepend to downloaded certificates
     */

    String certificateNamePattern = 'mcp_'

    /**
     * Hostnames to pull certificates for
     */
    String[] hosts = []

    /**
     * NetworkSecurity file
     */
    String networkSecurityFileName = 'network_security_config.xml'

    @TaskAction
    void action(){
        Logger.log(Level.ALL, "generate values");
        //writeValues(configXmlDir, mcpConfig);
    }

    void writeValues(File dir) {
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


}
