package com.fges.todoapp.e2e;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class GhostTests {

    /**
     * Version of the TP to use with the API, refer to the TP name on the slides!!
     */
    private static final String TP_NAME = "tp3";

    /**
     * DO NOT CHANGE FROM THIS LINE TO THE END OF THE FILE
     */


    private static final String API_ENDPOINT = "https://testcase-api.jho.ovh";
    private static final String EXEC_CLASS = "com.fges.todoapp.App";
    private static final String EXEC_METHOD_NAME = "exec";


    /**
     * Represents a sequence of command execution and their results.
     * Stdout is used to compare results, that's why the
     *
     * @param sequence    List of arguments of the commands to execute. Example:<br>
     *                    {"insert", "-s", "source.json", "Hello World"},<br>
     *                    {"insert", "-s", "source.json", "Bye"},<br>
     *                    {"list", "-s", "source.json"},
     * @param stdoutLines List of lines in the stdout
     * @param exitCode    Exit code of the program
     * @param name        Details about what is tested
     */
    public record ExecOutput(List<List<String>> sequence, List<String> stdoutLines, int exitCode, String name) {

        @Override
        public String toString() {
            if (name != null) {
                return name;
            }
            return "Format=" + sequence.get(0).stream().filter(arg -> arg.startsWith("tmp-")).findFirst().orElse("file.unknown").split("\\.")[1] +
                    ", Todo count=" + (sequence.size() - 1) +
                    ", exitCode=" + exitCode;
        }
    }

    /**
     * Fetch the data used for the tests
     * @return a list of ExecOutput that will be taken as test cases.
     * @throws Exception if the API call is missing or the exec() method does not exist
     */
    @Parameterized.Parameters(name = "{index}: {0}")
    public static List<Object[]> data() throws Exception {

        try {
            Class.forName(EXEC_CLASS).getMethod(EXEC_METHOD_NAME, String[].class);
        } catch (ReflectiveOperationException e) {
            throw new Exception("exec() method is not available");
        }

        var output = getApiExecOutput(TP_NAME);

        return output.stream().map(
                o -> new Object[]{o}
        ).toList();
    }


    // Execution output used for the test
    private final ExecOutput execOutput;

    public GhostTests(ExecOutput execOutput) {
        this.execOutput = execOutput;
    }

    /**
     * The test
     *
     * @throws Exception Any exception, it's ok to not handle them in tests because
     */
    @Test
    public void ghostTest() throws Exception {
        var testOutput = runMain(this.execOutput.sequence);
        Assert.assertEquals("Exit code should be the same", this.execOutput.exitCode, testOutput.exitCode);
        assertStdoutEquals(this.execOutput.stdoutLines, testOutput.stdoutLines);
    }

    private static void assertStdoutEquals(List<String> expected, List<String> actual) {
        var expectedAsArray = formatStdoutLines(expected).toArray();
        var actualAsArray = formatStdoutLines(actual).toArray();

        System.err.println(Arrays.toString(expectedAsArray));
        System.err.println(Arrays.toString(actualAsArray));

        Assert.assertEquals("Length should be the same", expectedAsArray.length, actualAsArray.length);

        for (int i = 0; i < expectedAsArray.length; i++) {
            Assert.assertEquals("Line should be the same", expectedAsArray[0], actualAsArray[0]);
        }

    }

    private static List<String> formatStdoutLines(List<String> baseLines) {
        return baseLines.stream()
                .map(line -> line.trim().replace("\"", ""))
                .filter(line -> !line.isEmpty())
                .toList();
    }

    private ExecOutput runMain(List<List<String>> sequence) throws Exception {
        var out = System.out;
        ByteArrayOutputStream sout = new ByteArrayOutputStream();
        System.setOut(new PrintStream(sout));

        int exitOutput = 0;

        for (var args : sequence) {
            var execMethod = Class.forName(EXEC_CLASS).getMethod(EXEC_METHOD_NAME, String[].class);

            exitOutput = (int) execMethod.invoke(null, (Object) args.toArray(new String[0]));
        }

        System.setOut(out);

        return new ExecOutput(
                sequence,
                Arrays.stream(sout.toString().split("\n")).map(String::trim).toList(),
                exitOutput,
                "unknown"
        );
    }

    private static List<ExecOutput> getApiExecOutput(String tpName) throws IOException {
        URL url = new URL(API_ENDPOINT + "/tp?tpName=" + tpName);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(response.toString(), new TypeReference<>() {
            });
        }
    }

    /**
     * Remove if you want to keep temporary test files
     */
    @After
    public void after() {
        deleteTmpFiles();
    }

    /**
     * The API gives sequences of code that uses the
     */
    private void deleteTmpFiles() {
        File directory = Paths.get(System.getProperty("user.dir")).toFile();
        var files = directory.listFiles();
        if (files == null) {
            System.err.println("Null directory");
            return;
        }
        for (File f : files) {
            if (f.getName().startsWith("tmp-")) {
                f.delete();
            }
        }
    }
}