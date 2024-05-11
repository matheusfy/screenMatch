package io.github.matheusfy.screanmatch.http;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpHandler implements IHttpHandler{

  private final HttpClient client;
  public HttpHandler(){
    this.client = HttpClient.newHttpClient();
  }

  @Override
  public HttpRequest buildRequest(String uri) {
    try {
      return HttpRequest.newBuilder(new URI(uri)).GET().build();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public HttpResponse<String> sendRequest(HttpRequest request) {
    try {
      return client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      // TODO: apenas logar que houve erro no envio
      return null;
    }
  }

  @Override
  public String buildAndSendRequest(String uri) {
    HttpResponse<String> response = sendRequest(buildRequest(uri));

    if (response != null) {
      return response.body();
    } else {
      return "";
//      TODO: Deveria retornar um tipo de erro?
    }
  }
}
