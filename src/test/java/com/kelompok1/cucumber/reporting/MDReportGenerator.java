package com.kelompok1.cucumber.reporting;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MDReportGenerator {

    private static final Pattern QUOTED_STRING = Pattern.compile("\"([^\"]*)\"");
    private static final String OUTPUT_DIR = "target/sit-reports";

    public static void generate(String platform) {
        String expectedPath = "target/cucumber-reports/" + platform + "/cucumber.json";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss"));
        String fileName = "SIT_Report_" + platform.toUpperCase() + "_" + timestamp + ".md";

        System.out.println("[SIT MD] ============================================================");
        System.out.println("[SIT MD] Generating SIT MD report for platform: " + platform);

        try {
            Files.createDirectories(Paths.get(OUTPUT_DIR));

            Path jsonFile = Paths.get(expectedPath);
            System.out.println("[SIT MD] Reading JSON: " + jsonFile.toAbsolutePath());

            String content = readJson(jsonFile);
            if (content.isEmpty()) {
                System.err.println("[SIT MD] FAILED: Could not read JSON. No MD report written.");
                return;
            }

            if (content.charAt(0) == '\uFEFF') {
                content = content.substring(1);
            }

            String trimmed = content.trim();
            if (!trimmed.startsWith("[")) {
                System.err.println("[SIT MD] FAILED: JSON does not start with '['. First 200 chars:");
                System.err.println(trimmed.substring(0, Math.min(200, trimmed.length())));
                return;
            }

            JSONArray features = new JSONArray(trimmed);
            System.out.println("[SIT MD] Parsed " + features.length() + " feature(s)");

            // Load platform test data for inference
            Properties testDataProps = loadTestDataProperties(platform);

            StringBuilder md = new StringBuilder();
            md.append("| Test Case ID | Module | Test Scenario | Type | Platform | Preconditions | Test Data | Test Step | Expected Result | Actual Result | Status | Note | Evidence |\n");
            md.append("|---|---|---|---|---|---|---|---|---|---|---|---|---|\n");

            // Track module counters for TC ID numbering
            Map<String, Integer> moduleCounters = new LinkedHashMap<>();

            for (int f = 0; f < features.length(); f++) {
                JSONObject feature = features.getJSONObject(f);
                JSONArray elements = feature.optJSONArray("elements");
                if (elements == null) continue;

                String preconditions = extractBackground(elements);

                for (int e = 0; e < elements.length(); e++) {
                    JSONObject element = elements.getJSONObject(e);
                    if (!"scenario".equals(element.optString("type"))) continue;

                    String scenarioName = element.optString("name", "");
                    List<String> tags = extractTags(element.optJSONArray("tags"));

                    // Module from tag (e.g., @login → "Login")
                    String module = resolveModule(tags);
                    if (module.isEmpty()) {
                        // Fallback: first word of feature name
                        module = feature.optString("name", "Unknown").split("\\s+")[0];
                    }

                    // TC ID: TC_MODULE_001
                    int counter = moduleCounters.getOrDefault(module, 0) + 1;
                    moduleCounters.put(module, counter);
                    String tcId = "TC_" + module.toUpperCase() + "_" + String.format("%03d", counter);

                    String type = resolveType(tags);
                    String plat = resolvePlatform(tags, platform);

                    JSONArray steps = element.optJSONArray("steps");
                    if (steps == null) steps = new JSONArray();

                    // Remove keywords from steps
                    String testSteps = extractTestStepsNoKeywords(steps);
                    String testData = extractTestData(steps, testDataProps, scenarioName);
                    String expectedResult = extractExpectedResultNoKeywords(steps);
                    String status = determineStatus(steps);
                    String errorMsg = extractErrorMessage(steps);

                    String actualResult = "passed".equalsIgnoreCase(status) ? "As expected" : errorMsg;
                    String note = "passed".equalsIgnoreCase(status) ? "" : errorMsg;
                    String evidence = "failed".equalsIgnoreCase(status) ? "Screenshot attached" : "";

                    md.append("| ").append(esc(tcId))
                      .append(" | ").append(esc(module))
                      .append(" | ").append(esc(scenarioName))
                      .append(" | ").append(esc(type))
                      .append(" | ").append(esc(plat))
                      .append(" | ").append(esc(preconditions))
                      .append(" | ").append(esc(testData))
                      .append(" | ").append(esc(testSteps))
                      .append(" | ").append(esc(expectedResult))
                      .append(" | ").append(esc(actualResult))
                      .append(" | ").append(esc(status.toUpperCase()))
                      .append(" | ").append(esc(note))
                      .append(" | ").append(esc(evidence))
                      .append(" |\n");
                }
            }

            Path out = Paths.get(OUTPUT_DIR, fileName);
            Files.writeString(out, md.toString());
            System.out.println("[SIT MD] SUCCESS: Report written to " + out.toAbsolutePath());

        } catch (Exception ex) {
            System.err.println("[SIT MD] CRASH: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static Properties loadTestDataProperties(String platform) {
        String fileName = "test-data-" + platform.toLowerCase() + ".properties";
        Properties props = new Properties();
        try (InputStream in = MDReportGenerator.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in != null) {
                props.load(in);
                System.out.println("[SIT MD] Loaded test data: " + fileName);
            } else {
                System.err.println("[SIT MD] Test data file not found on classpath: " + fileName);
            }
        } catch (IOException e) {
            System.err.println("[SIT MD] Failed to load test data: " + e.getMessage());
        }
        return props;
    }

    private static String readJson(Path path) {
        try {
            if (!Files.exists(path)) {
                System.err.println("[SIT MD] JSON not found: " + path);
                return "";
            }
            byte[] bytes = Files.readAllBytes(path);
            String content = new String(bytes, StandardCharsets.UTF_8);
            System.out.println("[SIT MD] JSON size: " + content.length() + " chars");
            return content;
        } catch (IOException e) {
            System.err.println("[SIT MD] Failed to read JSON: " + e.getMessage());
            return "";
        }
    }

    private static String extractBackground(JSONArray elements) {
        for (int i = 0; i < elements.length(); i++) {
            JSONObject el = elements.getJSONObject(i);
            if ("background".equals(el.optString("type"))) {
                return extractStepsNoKeywords(el.optJSONArray("steps"));
            }
        }
        return "";
    }

    private static List<String> extractTags(JSONArray tags) {
        List<String> list = new ArrayList<>();
        if (tags == null) return list;
        for (int i = 0; i < tags.length(); i++) {
            list.add(tags.getJSONObject(i).optString("name", ""));
        }
        return list;
    }

    private static String resolveModule(List<String> tags) {
        for (String tag : tags) {
            // Skip meta tags
            if (tag.equals("@smoke") || tag.equals("@positive") || tag.equals("@negative")
                || tag.equals("@happy-path") || tag.equals("@edge-case")
                || tag.equals("@regression") || tag.equals("@wip")
                || tag.equals("@web") || tag.equals("@mobile")) {
                continue;
            }
            // First non-meta tag is the module: @login → "Login"
            if (tag.startsWith("@")) {
                return capitalize(tag.substring(1));
            }
        }
        return "";
    }

    private static String resolveType(List<String> tags) {
        if (tags.contains("@happy-path")) return "Happy Path";
        if (tags.contains("@positive")) return "Positive";
        if (tags.contains("@negative")) return "Negative";
        if (tags.contains("@edge-case")) return "Edge Case";
        return "";
    }

    private static String resolvePlatform(List<String> tags, String fallback) {
        if (tags.contains("@web")) return "Web";
        if (tags.contains("@mobile")) return "Mobile";
        return capitalize(fallback);
    }

    private static String extractStepsNoKeywords(JSONArray steps) {
        if (steps == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < steps.length(); i++) {
            JSONObject step = steps.getJSONObject(i);
            String name = step.optString("name", "");
            if (sb.length() > 0) sb.append("<br>");
            sb.append(name);
        }
        return sb.toString();
    }

    private static String extractTestStepsNoKeywords(JSONArray steps) {
        if (steps == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < steps.length(); i++) {
            JSONObject step = steps.getJSONObject(i);
            String keyword = step.optString("keyword", "").trim();
            String name = step.optString("name", "");
            if ("Then".equalsIgnoreCase(keyword)) break;
            if (sb.length() > 0) sb.append("<br>");
            sb.append(name);
        }
        return sb.toString();
    }

    private static String extractExpectedResultNoKeywords(JSONArray steps) {
        if (steps == null) return "";
        StringBuilder sb = new StringBuilder();
        boolean foundThen = false;
        for (int i = 0; i < steps.length(); i++) {
            JSONObject step = steps.getJSONObject(i);
            String keyword = step.optString("keyword", "").trim();
            String name = step.optString("name", "");
            if ("Then".equalsIgnoreCase(keyword)) foundThen = true;
            if (foundThen) {
                if (sb.length() > 0) sb.append("<br>");
                sb.append(name);
            }
        }
        return sb.toString();
    }

    private static String extractTestData(JSONArray steps, Properties testDataProps, String scenarioName) {
        if (steps == null) return "";
        List<String> items = new ArrayList<>();
        StringBuilder allStepText = new StringBuilder();
        String lastRealKeyword = "";

        for (int i = 0; i < steps.length(); i++) {
            JSONObject step = steps.getJSONObject(i);
            String keyword = step.optString("keyword", "").trim();
            String name = step.optString("name", "");
            allStepText.append(" ").append(keyword).append(" ").append(name);

            if (!"And".equalsIgnoreCase(keyword) && !"But".equalsIgnoreCase(keyword)) {
                lastRealKeyword = keyword;
            }

            if ("Given".equalsIgnoreCase(lastRealKeyword) || "When".equalsIgnoreCase(lastRealKeyword)) {
                Matcher m = QUOTED_STRING.matcher(name);
                while (m.find()) {
                    String val = m.group(1);
                    items.add(val.isEmpty() ? "(empty)" : val);
                }
            }
        }

        // If we found inline quoted values, use them
        if (!items.isEmpty()) {
            return String.join(", ", items);
        }

        // No inline data — infer from properties based on scenario context
        String lowerScenario = scenarioName.toLowerCase();
        String lowerSteps = allStepText.toString().toLowerCase();
        StringBuilder inferred = new StringBuilder();

        boolean isLogin = lowerSteps.contains("login") || lowerSteps.contains("email") || lowerSteps.contains("password");

        if (isLogin) {
            boolean isValid = lowerScenario.contains("valid") || lowerSteps.contains("valid credentials");
            boolean isEmpty = lowerScenario.contains("empty");

            if (isValid && !isEmpty) {
                appendProp(inferred, testDataProps, "user.valid.email", "Email");
                appendProp(inferred, testDataProps, "user.valid.password", "Password");
            } else if (isEmpty) {
                appendLiteral(inferred, "Email: (empty)");
                appendLiteral(inferred, "Password: (empty)");
            }
        }

        if (inferred.length() > 0) {
            return inferred.toString();
        }

        return "See test-data.properties";
    }

    private static void appendProp(StringBuilder sb, Properties props, String key, String label) {
        String value = props.getProperty(key);
        if (value != null) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(label).append(": ").append(value);
        }
    }

    private static void appendLiteral(StringBuilder sb, String text) {
        if (sb.length() > 0) sb.append(", ");
        sb.append(text);
    }

    private static String determineStatus(JSONArray steps) {
        if (steps == null) return "skipped";
        boolean hasFailed = false;
        boolean hasSkipped = false;
        for (int i = 0; i < steps.length(); i++) {
            JSONObject result = steps.getJSONObject(i).optJSONObject("result");
            if (result == null) continue;
            String status = result.optString("status", "");
            if ("failed".equals(status)) hasFailed = true;
            if ("skipped".equals(status)) hasSkipped = true;
        }
        if (hasFailed) return "failed";
        if (hasSkipped) return "skipped";
        return "passed";
    }

    private static String extractErrorMessage(JSONArray steps) {
        if (steps == null) return "";
        for (int i = 0; i < steps.length(); i++) {
            JSONObject result = steps.getJSONObject(i).optJSONObject("result");
            if (result == null) continue;
            if ("failed".equals(result.optString("status", ""))) {
                String msg = result.optString("error_message", "");
                int idx = msg.indexOf('\n');
                return idx > 0 ? msg.substring(0, idx).trim() : msg.trim();
            }
        }
        return "";
    }

    private static String esc(String text) {
        if (text == null) return "";
        return text.replace("|", "\\|").replace("\n", " ").replace("\r", "");
    }

    private static String capitalize(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}
