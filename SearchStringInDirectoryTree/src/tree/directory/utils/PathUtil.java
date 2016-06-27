package tree.directory.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by Damyan Manev on 20-Jan-16.
 */
public class PathUtil {

    public interface PathLoadListener {
        void onPathLoad(Path path);
    }

    /**
     * This method will start generate all file paths in directoryPath.
     */
    public static void loadFilesPaths(Path directoryPath, PathLoadListener pathLoadListener) {
        Queue<Path> queue = new ArrayDeque<>();
        queue.add(directoryPath);
        while (!queue.isEmpty()) {
            Path path = queue.poll();

            if (Files.isDirectory(path)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                    for (Path fileOrDirectory : stream) {
                        queue.add(fileOrDirectory);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                pathLoadListener.onPathLoad(path);
            }
        }
    }
}
