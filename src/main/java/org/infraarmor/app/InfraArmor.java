package org.infraarmor.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.cli.CommandLine;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class InfraArmor {
    private static final Logger logger = LoggerFactory.getLogger(InfraArmor.class);
    public static void main(String[] args) {
        try {
            CommandLine cmd = CommandLineArgParser.parse(args);
            String tfFilepath = cmd.getOptionValue("f");
            String tfCode = FileLoader.loadFileContent(tfFilepath);
            String shots = ShotsLoader.loadShotsContent();

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
            String openApiToken = System.getenv("OPEN_API_TOKEN");

            String responseText = makeApiRequest(prompt, openApiToken);
            logger.info("API Response: {}", responseText);
        } catch (IOException e) {
            logger.error("An error occurred: {}", e.getMessage(), e);
        }
    }

    private static String makeApiRequest(String prompt, String openApiToken) throws IOException {
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
                return apiResponseBody.path("choices").get(0).path("text").asText();
            } else {
                String errorMessage = "API Request failed. Response code: " + response.code();
                logger.error(errorMessage);
                return errorMessage;
            }
        } catch (IOException e) {
            logger.error("An error occurred during the API request: {}", e.getMessage(), e);
            throw e;
        }
    }
}
