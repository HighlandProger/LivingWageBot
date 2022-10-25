package ru.rusguardian.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FileUtils {

    private static final String NEW_LINE = "\n";

    private FileUtils() {
    }

    public static File getFileFromResources(String resourcePath) {
        File file;
        try (InputStream inputStream = FileUtils.class.getResourceAsStream(resourcePath);
             OutputStream outputStream = new FileOutputStream(file = getTempFileByName(resourcePath))) {
            assert inputStream != null;
            IOUtils.copy(inputStream, outputStream);
            return file;
        } catch (IOException e) {
            throw new NoSuchElementException();
        }
    }

    private static File getTempFileByName(String resourcePath) throws IOException {
        String[] fileName = resourcePath.substring(resourcePath.lastIndexOf("/") + 1).split("\\.");
        return File.createTempFile(fileName[0], "." + fileName[1]);
    }

    public static String getTextFromFile(File captionFile) {
        String caption = "";
        try (Stream<String> data = Files.lines(captionFile.toPath())) {
            caption = data.collect(Collectors.joining(NEW_LINE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return caption;
    }

    public static String getTextFromResourcesFile(String resourcePath) {
        String caption = "";
        File resourceFile = getFileFromResources(resourcePath);
        try (Stream<String> data = Files.lines(resourceFile.toPath())) {
            caption = data.collect(Collectors.joining(NEW_LINE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return caption;
    }
}
