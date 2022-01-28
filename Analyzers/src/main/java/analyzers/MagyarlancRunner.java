package analyzers;

import static java.nio.file.StandardOpenOption.*;

import hu.u_szeged.magyarlanc.*;
import hu.u_szeged.magyarlanc.util.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public final class MagyarlancRunner {

    public static void main(String[] args) throws IOException {
        var outputDirPath = Path.of("../outputs/analyze/magyarlanc");
        if(!Files.exists(outputDirPath)) {
            Files.createDirectory(outputDirPath);
        }

        Magyarlanc.morphInit();

        var filesToAnalyze = Files.list(Path.of("../outputs/text/paraphrised")).toArray(Path[]::new);
        var totalStartTime = System.currentTimeMillis();

        System.out.println("Analyzing " + filesToAnalyze.length + " files\n");

        Arrays.stream(filesToAnalyze)
              .forEach(k -> analyzeWithMagyarlanc(k, outputDirPath));

        var totalEndTime = System.currentTimeMillis();
        System.out.println("\nAll done in " + (totalEndTime - totalStartTime) + "ms!");
    }

    private static void analyzeWithMagyarlanc(Path inputFile, Path outputDirPath) {
        System.out.println("Analyzing file: " + inputFile);

        try(var output = Files.newBufferedWriter(Path.of(outputDirPath.toString() + '/' + inputFile.getFileName()), StandardCharsets.UTF_8, TRUNCATE_EXISTING, CREATE)) {
            var input = SafeReader.read(inputFile.toString(), StandardCharsets.UTF_8.name());

            Magyarlanc.morphParse(input, output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done with file: " + inputFile);
    }
}