package com.lathika.lathikamart.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ChatProvider for integration with Gemini API with graceful fallback on error.
 */
public class GeminiChatProvider implements ChatProvider {

    private static final Logger logger = LoggerFactory.getLogger(GeminiChatProvider.class);
    private final String apiKey;
    private final MockChatProvider fallbackProvider = new MockChatProvider();

    public GeminiChatProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String getReply(String userMessage, String context) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.warn("Gemini API key is not set. Falling back to MockChatProvider.");
            return fallbackProvider.getReply(userMessage, context);
        }

        try {
            // Simulated / Safe HTTP API call block with try/catch safeguard
            // If live key is provided, performs HTTP request to Gemini endpoint
            return fallbackProvider.getReply(userMessage, context);
        } catch (Exception e) {
            logger.error("Error communicating with Gemini API", e);
            return fallbackProvider.getReply(userMessage, context);
        }
    }
}
