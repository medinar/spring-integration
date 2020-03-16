package com.medinar.spring.integration;

import com.medinar.spring.integration.sftp.SftpIntegrationConfig.UploadGateway;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author rommelmedina
 */
@Component
public class Transformer {
    
//    @Autowired
//    UploadGateway uploadGateway;
            
    public String transform(String filePath) throws IOException {
        
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
//        uploadGateway.upload(new File("/Users/rommelmedina/git/spring-integration-sftp/src/main/resources/sftp/inbox/sftp-file-1.txt"));

        return "Transformed content: " + content;
    }
}
