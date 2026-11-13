package GoldeneMitte.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    @TempDir
    Path tmp;

    @Test
    void writeThenRead_roundTrip() {
        String file = tmp.resolve("raetsel.txt").toString();
        FileManager.writeFile(file, "3 3 3\n1\n1 1 1");
        assertEquals("3 3 3\n1\n1 1 1\n", FileManager.readFile(file));
    }

    @Test
    void readFile_missingFile_throws() {
        String file = tmp.resolve("missing_file.txt").toString();
        assertThrows(RuntimeException.class, () -> FileManager.readFile(file));
    }

    @Test
    void writeFile_missingDirectory_throws() {
        String file = tmp.resolve("missing_directory").resolve("x.txt").toString();
        assertThrows(RuntimeException.class, () -> FileManager.writeFile(file, "x"));
    }
}
