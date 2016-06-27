package tree.directory.main;

import tree.directory.models.LineInfo;
import tree.directory.structures.BlockingStorage;
import tree.directory.utils.PathUtil;
import tree.directory.workers.LineConsumer;
import tree.directory.workers.LineProducer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static String DIRECTORY_NAME = "D:\\Games-Installation\\CS_1.6_WarZone";
    private static final int PRODUCERS_COUNTER = 4;
    private static final int CONSUMERS_COUNTER = 2;
    private static final int MAX_ELEMENTS_COUNT = 100;
    private static final String TARGET_WORD = "someting";

    public static void main(String[] args) throws InterruptedException {

        LineConsumer.MatchFoundListener matchFoundListener = lineInfo ->
                System.out.printf("File name: %s\nLine: %d\nContain: %s\n\n", lineInfo.getFileName(),
                        lineInfo.getLineNumber(), lineInfo.getLine());

        findStringInDirectory(PRODUCERS_COUNTER, CONSUMERS_COUNTER, MAX_ELEMENTS_COUNT, DIRECTORY_NAME, TARGET_WORD,
                matchFoundListener);
    }

    public static void findStringInDirectory(int producersCount, int consumersCount, int maxElementsInStorage,
                                             String rootDirectoryName, String targetWord,
                                             LineConsumer.MatchFoundListener matchFoundListener) throws InterruptedException {

        long start = System.currentTimeMillis();

        BlockingStorage<LineInfo> linesStorage = new BlockingStorage<>(maxElementsInStorage, producersCount);
        BlockingStorage<Path> pathStorage = new BlockingStorage<>(1);

        Path directoryPath = Paths.get(rootDirectoryName);

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < producersCount; ++i) {
            // names are for debugging purposes
            Thread producer = new LineProducer("LineProducer: " + i, pathStorage, linesStorage);
            threads.add(producer);
            producer.start();
        }

        for (int i = 0; i < consumersCount; ++i) {
            // names are for debugging purposes
            LineConsumer lineConsumer = new LineConsumer("LineConsumer: " + i, targetWord, linesStorage, matchFoundListener);
            threads.add(lineConsumer);
            lineConsumer.start();
        }

        PathUtil.loadFilesPaths(directoryPath, path -> {
            try {
                pathStorage.add(path);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        pathStorage.decrementProducers();

        for (Thread t : threads) {
            t.join();
        }

        System.out.printf("Executed for: %s ms\nProducers: %d\n Consumers: %s\n\n", System.currentTimeMillis() - start,
                producersCount, consumersCount);
    }

}
