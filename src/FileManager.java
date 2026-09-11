import java.io.*;

public final class FileManager {
    public static String readFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null){
                sb.append(line).append("\n");
            }
        }
        catch (IOException error){
            throw new RuntimeException("Could not read file: " + new File(fileName).getAbsolutePath(), error);
        }

        return sb.toString();
    }
    public static void writeFile(String fileName, String content) {

    }
}
