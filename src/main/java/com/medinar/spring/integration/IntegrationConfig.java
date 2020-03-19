package com.medinar.spring.integration;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.dsl.Sftp;

/**
 *
 * @author rommelmedina
 */
@Configuration
public class IntegrationConfig {

    @Autowired
    Transformer transformer;

    @Autowired
    SessionFactory sftpSessionFactory;

    @Value("${sftp.remote.directory:${user.home}/sftp/inbox}")
    private String sftpRemoteDirectory;

    @Value("${local.source.directory:${java.io.tmpdir}/outbox}")
    private String localSourceDirectory;

    @Value("${local.destination.directory:${java.io.tmpdir}/inbox}")
    private String localDestinationDirectory;

    @Bean
    public IntegrationFlow integrationFlow() {
        return IntegrationFlows.from(fileReader(), spec -> spec.poller(Pollers.fixedDelay(1000)))
                .transform(transformer, "transform")
                .handle(Sftp.outboundAdapter(sftpSessionFactory)
                        .remoteDirectory(sftpRemoteDirectory)
                ) // Automatically uploads file to remote directory.
                // TODO: Need to delete the file that was uploaded.
                .get();
    }

    @Bean
    public FileWritingMessageHandler fileWriter() {
        FileWritingMessageHandler handler = 
                new FileWritingMessageHandler(new File(localDestinationDirectory));
        handler.setExpectReply(false);
        return handler;
    }

    @Bean
    public FileReadingMessageSource fileReader() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File(localSourceDirectory));
        return source;
    }

}
