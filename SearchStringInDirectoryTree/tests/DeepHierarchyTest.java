/**
 * Created by Damyan Manev on 21-Jan-16.
 */

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
public class DeepHierarchyTest {

    private static final String TARGET_STRING = "Java";
    private static final String PATH_NAME = "SecondTestFolder";
    private static final Path ROOT_DIRECTORY_PATH = Paths.get(PATH_NAME);
    private static final Path FOLDER_PATH_A = ROOT_DIRECTORY_PATH.resolve("A");
    private static final Path FOLDER_PATH_AA = FOLDER_PATH_A.resolve("AA");
    private static final Path FOLDER_PATH_AAA = FOLDER_PATH_AA.resolve("AAA");
    private static final Path FOLDER_PATH_AAAA = FOLDER_PATH_AAA.resolve("AAAA");
    private static final Path FOLDER_PATH_AAAAA = FOLDER_PATH_AAAA.resolve("AAAAA");

    private static final Path pathToWrite = FOLDER_PATH_AAAAA.resolve("DeepInTheHierarchyFile.txt");

    private static final String lineToWrite = "Something dump text here" + TARGET_STRING + "end of story again";

    @Before
    public void setup() throws IOException, InterruptedException {
        Files.createDirectories(FOLDER_PATH_A);
        Files.createDirectories(FOLDER_PATH_AA);
        Files.createDirectories(FOLDER_PATH_AAA);
        Files.createDirectories(FOLDER_PATH_AAAA);
        Files.createDirectories(FOLDER_PATH_AAAAA);

        String lineSeparator = System.getProperty("line.separator");

        Files.createFile(pathToWrite);
        Files.write(pathToWrite, ("dummyyyy " + pathToWrite.toString() + lineSeparator).getBytes()); // dummy text

        Files.write(pathToWrite, lineToWrite.getBytes(), StandardOpenOption.APPEND); // this will be
        // on the second line of this file
    }

    @Test
    public void pathSearchTest() {
        List<Path> expectedResultList = new ArrayList<>();
        expectedResultList.add(pathToWrite);

        List<Path> result = new ArrayList<>();
        PathUtil.loadFilesPaths(ROOT_DIRECTORY_PATH, path -> result.add(path));
        Collections.sort(result);

        //TO DO fix it
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
        expectedResult.add(new LineInfo(2, lineToWrite, pathToWrite.toAbsolutePath().toString()));
        Collections.sort(expectedResult);

        Assert.assertEquals(result.size(), expectedResult.size());
        for (int i = 0; i < expectedResult.size(); ++i) {
            Assert.assertEquals(expectedResult.get(i).equals(result.get(i)), true);
        }
    }

    @After
    public void finish() throws IOException {
        TestUtils.deleteDirectoryRecursively(ROOT_DIRECTORY_PATH);
    }

}

