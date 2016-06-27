package tree.directory.workers;

import tree.directory.models.LineInfo;
import tree.directory.structures.BlockingStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Damyan Manev on 17-Jan-16.
 */
public class LineProducer extends Thread {

    private BlockingStorage<Path> pathsBlockingStorage;
    private BlockingStorage<LineInfo> modelBlockingStorage;

    public LineProducer(String name, BlockingStorage<Path> pathsBlockingStorage, BlockingStorage<LineInfo> modelBlockingStorage) {
        super(name);
        this.pathsBlockingStorage = pathsBlockingStorage;
        this.modelBlockingStorage = modelBlockingStorage;
    }

    @Override
    public void run() {
        Path path;
        try {
            while ((path = pathsBlockingStorage.get()) != null) {

                // no idea why UTF-8 not working (an exception is thrown)
                try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1)) {
                    int counter = 0; // we start counting from 1 !

                    String line;
                    while ((line = reader.readLine()) != null) {
                        ++counter;
                        modelBlockingStorage.add(new LineInfo(counter, line, path.toAbsolutePath().toString()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // this producer ends its work, so notify model storage about it
        modelBlockingStorage.decrementProducers();
    }
}
