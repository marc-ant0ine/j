import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

/**
 * Utility class responsible for loading a {@link Level} from a text file.
 *
 * <p>File format rules:
 * <ul>
 *   <li>Each line represents one row of the grid.</li>
 *   <li>Each character is either {@code '#'} (wall) or {@code ' '} (empty space).</li>
 *   <li>The file is assumed to be correctly formatted.</li>
 *   <li>If the file does not exist or cannot be read, an error message is printed
 *       to {@link System#err} and {@code null} is returned.</li>
 * </ul>
 */
public class LevelLoader {

    /**
     * Loads a {@link Level} from the given file path.
     *
     * @param filePath Path to the text file describing the level.
     * @return A {@link Level} built from the file content,
     *         or {@code null} if the file cannot be read.
     */
    public static Level load(String filePath) {
        Path path = Paths.get(filePath);

        // Check existence before attempting to read
        if (!Files.exists(path)) {
            System.err.println("Erreur : le fichier '" + filePath + "' n'existe pas.");
            return null;
        }

        try {
            List<String> lines = Files.readAllLines(path);

            if (lines.isEmpty()) {
                System.err.println("Erreur : le fichier '" + filePath + "' est vide.");
                return null;
            }

            int rows = lines.size();
            // Determine column count from the longest line so all rows are the same width
            int cols = 0;
            for (String line : lines) {
                if (line.length() > cols) {
                    cols = line.length();
                }
            }

            char[][] grid = new char[rows][cols];

            for (int r = 0; r < rows; r++) {
                String line = lines.get(r);
                for (int c = 0; c < cols; c++) {
                    if (c < line.length()) {
                        grid[r][c] = line.charAt(c);
                    } else {
                        // Pad with spaces if the line is shorter than the max width
                        grid[r][c] = Level.EMPTY;
                    }
                }
            }

            return new Level(grid);

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier '"
                    + filePath + "' : " + e.getMessage());
            return null;
        }
    }
}