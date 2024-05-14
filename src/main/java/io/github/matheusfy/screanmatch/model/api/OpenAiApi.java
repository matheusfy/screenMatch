package io.github.matheusfy.screanmatch.model.api;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class OpenAiApi {

    public OpenAiApi() {
    }

    public static String obterTraducao(String texto) {
        String OPENAI_TOKEN = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(OPENAI_TOKEN);

        CompletionRequest requisicao = CompletionRequest.builder()
            .model("gpt-3.5-turbo-instruct")
            .prompt("Traduza para o português o texto: " + texto)
            .maxTokens(1000)
            .temperature(0.7)
            .build();
        try{
            var resposta = service.createCompletion(requisicao);
            return resposta.getChoices().get(0).getText().trim();
        } catch (Exception error) {
            System.out.println("Error: Não foi possivel realizar a tradução da sinopse. Erro: " + error.getMessage());
            return texto;
        }
    }
}
