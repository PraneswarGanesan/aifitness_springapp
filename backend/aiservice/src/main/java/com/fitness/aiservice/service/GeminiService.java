package com.fitness.aiservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;
@Service
public class GeminiService {
    private final WebClient webClient; // Inject WebClient instance
    public GeminiService(WebClient.Builder webClientBuilder){
        this.webClient = WebClient.builder().build();
    }
    @Value("${Gemini_API-url}")
    private String geminiApiurl;

    @Value("${Gemini_API-key}")
    private String geminiApikey;

    public String getAnswer(String question) {
        // Create the request body as required by your API
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of("text", question)
                                }
                        )
                }
        );

        String response = webClient.post()
                .uri(geminiApiurl + geminiApikey) // Using the URL and key
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Make it synchronous for blocking the response (not ideal for production, use async instead)
        return response;
    }
}
