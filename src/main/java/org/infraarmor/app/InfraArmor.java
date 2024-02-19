package org.infraarmor.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class InfraArmor {
    public static void main(String[] args) {
        String openApiToken = System.getenv("OPEN_API_TOKEN");

        Options options = new Options();
        options.addOption(Option.builder("f")
                .longOpt("filepath")
                .desc("Filepath of the terraform file to review")
                .hasArg()
                .required()
                .build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("CommandLineParserExample", options);
            System.exit(1);
            return;
        }

        String tfFilepath = cmd.getOptionValue("f");
        String tfCode = loadFileContent(tfFilepath);

        // Load the shots
        StringBuilder shots = new StringBuilder();
        String shotsPath = "src/main/resources/shots";
        File shotsDir = new File(shotsPath);
        File[] listOfShots = shotsDir.listFiles();
        if (listOfShots != null) {
            for (File shot : listOfShots) {
                if (shot.isFile()) {
                    shots.append(loadFileContent(shot.getPath())).append("\n");
                }
            }
        }

        String prompt = String.format(
                "See the example below of Terraform code and its JSON Report. As a DevOps Security Engineer, review a " +
                "Terraform code for potential OWASP vulnerabilities. Analyze the provided Terraform code and list any " +
                "security concerns related to the OWASP Top Ten, such as injection flaws, security misconfigurations, " +
                "or any other potential vulnerabilities. Provide specific details and recommendations on how to address " +
                "these issues with line numbers referring to the place in the code where you would make the change. " +
                "Present the required changes in a JSON list with each element containing line_numbers(string) where " +
                "the change should happen, associated OWASP_top_ten_concern, suggestion, and recommendation as " +
                "attributes.\n" +
                "%s" +
                "-------\n" +
                "%s", shots, tfCode
        );

        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            JSONObject jsonBody = new JSONObject()
                    .put("model", "gpt-3.5-turbo-instruct")
                    .put("prompt", prompt)
                    .put("temperature", 0)
                    .put("max_tokens", 2048)
                    .put("top_p", 0.1)
                    .put("frequency_penalty", 0)
                    .put("presence_penalty", 0);

            RequestBody body = RequestBody.create(jsonBody.toString(), mediaType);

            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/completions")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", String.format("Bearer %s", openApiToken))
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    JsonNode apiResponseBody = objectMapper.readTree(response.body().string());
                    String responseText = apiResponseBody.path("choices").get(0).path("text").asText();
                    System.out.println(responseText);
                } else {
                    System.out.println("API Request failed. Response code: " + response.code());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String loadFileContent(String filePath) {
        Path path = Paths.get(filePath);
        byte[] fileBytes = new byte[0];
        try {
            fileBytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(fileBytes);
    }
}
