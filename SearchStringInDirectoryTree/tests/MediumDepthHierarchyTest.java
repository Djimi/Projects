import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tree.directory.main.Main;
import tree.directory.models.LineInfo;
import tree.directory.utils.PathUtil;
import tree.directory.workers.LineConsumer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Damyan Manev on 20-Jan-16.
 */
public class MediumDepthHierarchyTest {

    private static final String TARGET_STRING = "O my god";
    private static final String PATH_NAME = "FirstTestFolder";
    private static final Path ROOT_DIRECTORY_PATH = Paths.get(PATH_NAME);
    private static final Path FOLDER_PATH_A = ROOT_DIRECTORY_PATH.resolve("A");
    private static final Path FOLDER_PATH_B = ROOT_DIRECTORY_PATH.resolve("B");
    private static final Path FOLDER_PATH_C = ROOT_DIRECTORY_PATH.resolve("C");

    private static final Path FOLDER_PATH_AA = FOLDER_PATH_A.resolve("AA");
    private static final Path FOLDER_PATH_BA = FOLDER_PATH_B.resolve("BA");
    private static final Path FOLDER_PATH_CA = FOLDER_PATH_C.resolve("CA");

    private static final Path FOLDER_PATH_CAA = FOLDER_PATH_CA.resolve("CAA");

    private static final Path[] FILES_PATHS = new Path[]{
            ROOT_DIRECTORY_PATH.resolve("some.txt"),
            FOLDER_PATH_A.resolve("testFile.txt"),
            FOLDER_PATH_B.resolve("think.txt"),
            FOLDER_PATH_C.resolve("Paste.txt"),
            FOLDER_PATH_AA.resolve("test.txt"),
            FOLDER_PATH_BA.resolve("try.txt"),
            FOLDER_PATH_CA.resolve("hash.txt"),
            FOLDER_PATH_CAA.resolve("last.txt")};

    private static final String firstLineToWrite = "abccdefg enought letters" + TARGET_STRING + "end of story";
    private static final Path firstPathToWrite = FILES_PATHS[4];

    private static final String secondLineToWrite = TARGET_STRING + " aloo";
    private static final Path secondPathToWrite = FILES_PATHS[6];

    @Before
    public void setup() throws IOException, InterruptedException {
        Files.createDirectories(FOLDER_PATH_AA);
        Files.createDirectories(FOLDER_PATH_BA);
        Files.createDirectories(FOLDER_PATH_CAA);

        String lineSeparator = System.getProperty("line.separator");

        for (Path filePath : FILES_PATHS) {
            Files.createFile(filePath);
            Files.write(filePath, ("fake text " + filePath.toString() + lineSeparator).getBytes()); // dummy text
        }

        Files.write(firstPathToWrite, firstLineToWrite.getBytes(), StandardOpenOption.APPEND); // this will be
        // on the second line of this file

        Files.write(secondPathToWrite, ("another line" + lineSeparator).getBytes(),
                StandardOpenOption.APPEND); // some text + new line
        Files.write(secondPathToWrite, ("one more another line" + lineSeparator).getBytes(), StandardOpenOption.APPEND);
        Files.write(secondPathToWrite, secondLineToWrite.getBytes(), StandardOpenOption.APPEND); // on 4th line
    }

    @Test
    public void pathSearchTest() {
        List<Path> expectedResultList = new ArrayList<>();
        Collections.addAll(expectedResultList, FILES_PATHS);
        Collections.sort(expectedResultList); // firstPathToWrite implements comparable

        List<Path> result = new ArrayList<>();
        PathUtil.loadFilesPaths(ROOT_DIRECTORY_PATH, path -> result.add(path));
        Collections.sort(result);

        Assert.assertEquals(expectedResultList.size(), result.size());

        for (int index = 0; index < expectedResultList.size(); ++index) {
            Assert.assertEquals(expectedResultList.get(index), result.get(index));
        }
    }

    @Test
    public void findStringInDirectoryTest() throws InterruptedException {
        List<LineInfo> result = new ArrayList<>();
        LineConsumer.MatchFoundListener matchFoundListener = result::add;
        Main.findStringInDirectory(4, 2, 1000, ROOT_DIRECTORY_PATH.toAbsolutePath().toString(), TARGET_STRING,
                matchFoundListener);
        Collections.sort(result);

        List<LineInfo> expectedResult = new ArrayList<>();
        expectedResult.add(new LineInfo(2, firstLineToWrite, firstPathToWrite.toAbsolutePath().toString()));
        expectedResult.add(new LineInfo(4, secondLineToWrite, secondPathToWrite.toAbsolutePath().toString()));
        Collections.sort(expectedResult);

        Assert.assertEquals(result.size(), expectedResult.size());
        for (int i = 0; i < expectedResult.size(); ++i) {
            Assert.assertEquals(expectedResult.get(i).equals(result.get(i)), true);
        }
    }

    @After
    public void finish() throws IOException {
        TestUtils.deleteDirectoryRecursively(MediumDepthHierarchyTest.ROOT_DIRECTORY_PATH);
    }

}
