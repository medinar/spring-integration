package com.medinar.spring.integration;

import com.jcraft.jsch.ChannelSftp;
import java.io.File;
import org.aopalliance.aop.Advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.GenericEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.remote.handler.FileTransferringMessageHandler;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.handler.advice.ExpressionEvaluatingRequestHandlerAdvice;
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

    @Value("${local.source.directory:${user.home}/local/outbox}")
    private String localSourceDirectory;

    @Value("${local.destination.directory:${java.io.tmpdir}/inbox}")
    private String localDestinationDirectory;

    @Bean
    public IntegrationFlow integrationFlow() {
        return IntegrationFlows.from(fileReader(), spec -> spec.poller(Pollers.fixedDelay(1000)))
                .transform(transformer, "transform")
                .handle(
                        Sftp.outboundAdapter(sftpSessionFactory, FileExistsMode.REPLACE).remoteDirectory(sftpRemoteDirectory), 
                        c -> c.advice(expressionAdvice(c))
                )
                .get();
    }

    @Bean
    public Advice expressionAdvice(GenericEndpointSpec<FileTransferringMessageHandler<ChannelSftp.LsEntry>> c) {
        ExpressionEvaluatingRequestHandlerAdvice advice = new ExpressionEvaluatingRequestHandlerAdvice();
        // https://stackoverflow.com/questions/60767867/unable-to-delete-payload-after-pushing-it-to-remote-server-using-expressionevalu
//        advice.setOnSuccessExpressionString("payload.delete()");
        advice.setOnSuccessExpressionString("headers[file_originalFile].delete()");
        advice.setOnFailureExpressionString("payload + ' failed to upload'");
        advice.setTrapException(true);
        return advice;
    }

    @Bean
    public FileWritingMessageHandler fileWriter() {
        FileWritingMessageHandler handler
                = new FileWritingMessageHandler(new File(localDestinationDirectory));
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
