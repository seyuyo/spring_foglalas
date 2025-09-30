package application.manager;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUploadManager {
    private static String RESOURCE_PATH = System.getProperty("user.dir") + "/src/main/resources/static/img/";

    private static String PROFILE_PATH  = "profilkepek";
    private static String FILM_COVER    = "filmboritok";
    private static String SERIES_COVER  = "sorozatboritok";

    public enum UploadType {
        Profilkep,
        Filmborito,
        Sorozatborito
    }

    private static String getFolderByType(UploadType type) {
        if(type == null)
            return "";

        switch(type) {
            case Profilkep:     return PROFILE_PATH;
            case Filmborito:    return FILM_COVER;
            case Sorozatborito: return SERIES_COVER;
            default:            return "";
        }
    }

    public static void deleteFile(String path, UploadType type) throws IOException {
        Files.delete(Paths.get(RESOURCE_PATH, getFolderByType(type), path));
    }

    public static void upload(MultipartFile file, String filename, UploadType type) throws IOException {
        Files.write(Paths.get(RESOURCE_PATH, getFolderByType(type), filename == null ? file.getOriginalFilename() : filename), file.getBytes());
    }
}
