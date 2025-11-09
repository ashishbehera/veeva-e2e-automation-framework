package com.veeva.automation.runner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class DynamicRunnerGenerator {

    private static final String TEMPLATE_PATH = "src/main/resources/templates/FeatureRunnerTemplate.java";
    private static final String GENERATED_PACKAGE = "com.veeva.automation.runner.generated";
    private static final String OUTPUT_DIR = "src/test/java/com/veeva/automation/runner/generated/";
    private static final String FEATURE_PATH = "src/test/resources/features";
    private static final String FEATURE_KEY = ".feature";
    private static final String RUNNER_KEY = "_Runner";
    private static final String JAVA_KEY = ".java";
    private static final String RUNNER_PACKAGE = "package com.veeva.automation.runner;";


    public static void generateRunners(String browser, String tags) throws IOException {
        // Ensure output directory exists
        Path outputDirPath = Paths.get(OUTPUT_DIR);
        if (!Files.exists(outputDirPath)) {
            Files.createDirectories(outputDirPath);
        }

        // Scan all feature files
        try (Stream<Path> featureFiles = Files.walk(Paths.get(FEATURE_PATH))) {
            featureFiles.filter(path -> path.toString().endsWith(FEATURE_KEY))
                        .forEach(featureFile -> {
                try {
                    // Skip features that don't contain the tag (if specified)
                    String featureContent = Files.readString(featureFile);
                    if (tags != null && !tags.isEmpty() && !featureContent.contains(tags)) {
                        return;
                    }

                    // Generate runner class name
                    String featureName = featureFile.getFileName().toString().replace(FEATURE_KEY, "");
                    String runnerName = featureName + "_" + browser + RUNNER_KEY;

                    // Path for the generated runner
                    Path runnerPath = Paths.get(OUTPUT_DIR + runnerName + JAVA_KEY);
                    if (Files.exists(runnerPath)) {
                        System.out.println("⚠️ Runner already exists, skipping: " + runnerName);
                        return;
                    }

                    // Read template
                    String template = Files.readString(Paths.get(TEMPLATE_PATH));

                    // Replace placeholders
                    String runnerClass = template
                            .replace("<FEATURE_PATH>", featureFile.toString().replace("\\", "/"))
                            .replace("<RUNNER_NAME>", runnerName)
                            .replace("<BROWSER>", browser)
                            .replace(RUNNER_PACKAGE, "package " + GENERATED_PACKAGE + ";");

                    // Write generated runner
                    Files.writeString(runnerPath, runnerClass, StandardOpenOption.CREATE_NEW);

                    System.out.println("✅ Generated runner: " + runnerName);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        System.out.println("🎉 Runner generation completed for browser: " + browser);
    }
}
