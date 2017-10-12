package org.feedhenry.tools.android

import groovy.util.logging.Log
import groovy.xml.MarkupBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.net.URL;
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.cert.X509Certificate
import java.util.regex.Matcher
import java.util.regex.Pattern

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


    FeedHenryMCPSelfSignedCertificateHelperTask() {
        outputs.upToDateWhen { false }
    }

    @TaskAction
    void action() {
        File configXmlDir = new File(resXMlDir, "xml");
        File configRawDir = new File(resXMlDir, "raw");
        if (!configXmlDir.exists() && !configXmlDir.mkdirs()) {
            throw new GradleException("Failed to create folder: " + configXmlDir);
        }

        if (!configRawDir.exists() && !configRawDir.mkdirs()) {
            throw new GradleException("Failed to create folder: " + configRawDir);
        }


        X509Certificate[] certChain
        def trustManager = [
                checkClientTrusted: { chain, authType -> },
                checkServerTrusted: { chain, authType -> certChain = chain },
                getAcceptedIssuers: { null }
        ] as X509TrustManager

        def context = SSLContext.getInstance("TLS")
        context.init(null, [trustManager] as TrustManager[], null)
        Map<String, String> certificateMap = new HashMap<>();
        hosts.each { String host ->

            URL aURL = new URL(host);
            project.logger.info("Loading host : " + host);
            context.socketFactory.createSocket(aURL.host, aURL.port).with {
                startHandshake()
                close()
            }

            certChain.each { X509Certificate cert ->

                certificateMap[cert.getSubjectDN().toString()] =
                        "-----BEGIN CERTIFICATE-----\n" +
                                cert.encoded.encodeBase64(true).toString() +
                                "-----END CERTIFICATE-----\n"
            }

        }

        writeValues(configXmlDir, configRawDir, certificateMap);

    }

    private void writeValues(File configXmlDir, File configRawDir, Map<String, String> certificateMap) {

        def networkSecurityFile = new File(configXmlDir, networkSecurityFileName);
        def writer = new StringWriter()
        MarkupBuilder xml = new MarkupBuilder(writer)




            xml."network-security-config" {
                    "debug-overrides" {
                        "trust-anchors" {
                            certificateMap.keySet().each { String key ->

                                certificates src: "@raw/" + certificateNamePattern + FeedHenryMCPSelfSignedCertificateHelperTask.escape(key);
                            }
                        }
                    }
                }
            

        certificateMap.keySet().each { String key ->
            def certFile = new File(configRawDir, certificateNamePattern + FeedHenryMCPSelfSignedCertificateHelperTask.escape(key));
            certFile << certificateMap[key];
        }

        networkSecurityFile << writer.toString()

    }

    private static String escape(def key) {

        Pattern pt = Pattern.compile("[^a-zA-Z0-9]");
        Matcher match= pt.matcher(key);
        while(match.find())
        {
            String s= match.group();
            key=key.replaceAll("\\"+s, "");
        }
        return key.toLowerCase()
    }

}

