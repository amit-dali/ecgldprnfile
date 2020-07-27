package nl.ebay.creditlimittracker.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

public final class FileUtil {
    private FileUtil() {
    }

    public static File getFileFromClasspath(String filePath) throws IOException {
        Resource resource = new ClassPathResource(filePath);
        return resource.getFile();
    }
}
