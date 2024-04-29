package io.github.matheusfy.screanmatch.service;

public interface IConverteDados {

    <T> T obterDados(String json, Class<T> classe);
}
