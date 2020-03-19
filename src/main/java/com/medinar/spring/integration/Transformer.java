package com.medinar.spring.integration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;

/**
 *
 * @author rommelmedina
 */
@Component
public class Transformer {

    public String transform(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return "Transformed content: " + content;
    }

}
