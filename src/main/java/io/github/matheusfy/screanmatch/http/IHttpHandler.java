package io.github.matheusfy.screanmatch.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public interface IHttpHandler {

  public HttpRequest buildRequest(String uri);
  public HttpResponse<String> sendRequest(HttpRequest request);
  public String buildAndSendRequest(String uri);
}
