package com.medinar.spring.integration;

import com.medinar.spring.integration.sftp.SftpIntegrationConfig;
import com.medinar.spring.integration.sftp.SftpIntegrationConfig.UploadGateway;
import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;

/**
 *
 * @author rommelmedina
 */
@Configuration
public class IntegrationConfig {

    @Autowired
    Transformer transformer;

    @Autowired
    UploadGateway uploadGateway;

    @Bean
    public IntegrationFlow integrationFlow() {
        return IntegrationFlows.from(fileReader(), spec -> spec.poller(Pollers.fixedDelay(1000)))
                .transform(transformer, "transform")
                .handle(fileWriter())
//                .handle(uploadGateway.upload(new File("/Users/rommelmedina/git/spring-integration-sftp/src/main/resources/sftp/inbox/sftp-file-1.txt")))
                .get();
    }

    @Bean
    public FileWritingMessageHandler fileWriter() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("src/main/resources/file/inbox"));
        handler.setExpectReply(false);
        return handler;
    }

    @Bean
    public FileReadingMessageSource fileReader() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File("src/main/resources/file/outbox"));
        return source;
    } 
}
