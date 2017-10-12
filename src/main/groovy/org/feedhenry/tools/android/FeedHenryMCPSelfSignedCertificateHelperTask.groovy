package org.feedhenry.tools.android

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectories
import org.gradle.api.tasks.OutputDirectory

/**
 * This class will download the self-signed certificate and save it in debug/res/raw
 */
class FeedHenryMCPSelfSignedCertificateHelperTask extends DefaultTask {

    @Input
    String certificateNamePattern = 'mcp_'
    @Input
    String[] hosts = []
    @Input
    String networkSecurityFileName = 'network_security_config.xml'

    @OutputDirectory
    File resXMlDir;



}
