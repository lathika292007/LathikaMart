package com.lathika.lathikamart.service;

import com.lathika.lathikamart.ai.ChatProvider;
import com.lathika.lathikamart.ai.GeminiChatProvider;
import com.lathika.lathikamart.ai.MockChatProvider;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.exception.ValidationException;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of ChatService enforcing rate limits, input caps, and provider selection.
 */
public class ChatServiceImpl implements ChatService {

    private static final int MAX_INPUT_LENGTH = 500;
    private static final int RATE_LIMIT_WINDOW_SECONDS = 60;
    private static final int MAX_MESSAGES_PER_WINDOW = 10;

    private final ChatProvider chatProvider;

    public ChatServiceImpl() {
        Properties props = new Properties();
        try (InputStream in = ChatServiceImpl.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception ignored) {
        }

        String providerType = System.getenv("AI_CHATBOT_PROVIDER") != null ? 
                System.getenv("AI_CHATBOT_PROVIDER") : props.getProperty("ai.chatbot.provider", "mock");
        String apiKey = System.getenv("GEMINI_API_KEY") != null ? 
                System.getenv("GEMINI_API_KEY") : props.getProperty("gemini.api.key", "");

        if ("gemini".equalsIgnoreCase(providerType)) {
            this.chatProvider = new GeminiChatProvider(apiKey);
        } else {
            this.chatProvider = new MockChatProvider();
        }
    }

    public ChatServiceImpl(ChatProvider chatProvider) {
        this.chatProvider = chatProvider;
    }

    @Override
    public String processChatMessage(String userMessage, HttpSession session) throws AppException {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            throw new ValidationException("Message cannot be empty.");
        }

        if (userMessage.length() > MAX_INPUT_LENGTH) {
            throw new ValidationException("Message exceeds maximum length of " + MAX_INPUT_LENGTH + " characters.");
        }

        // Session rate limiting check
        enforceRateLimit(session);

        return chatProvider.getReply(userMessage.trim(), "LathikaMart Marketplace Assistant Context");
    }

    @SuppressWarnings("unchecked")
    private void enforceRateLimit(HttpSession session) throws ValidationException {
        long currentTime = System.currentTimeMillis();
        Map<String, Object> rateLimitData = (Map<String, Object>) session.getAttribute("chat_rate_limit");

        if (rateLimitData == null) {
            rateLimitData = new ConcurrentHashMap<>();
            rateLimitData.put("startTime", currentTime);
            rateLimitData.put("count", 1);
            session.setAttribute("chat_rate_limit", rateLimitData);
            return;
        }

        long startTime = (long) rateLimitData.get("startTime");
        int count = (int) rateLimitData.get("count");

        if (currentTime - startTime > RATE_LIMIT_WINDOW_SECONDS * 1000L) {
            // Reset window
            rateLimitData.put("startTime", currentTime);
            rateLimitData.put("count", 1);
        } else {
            if (count >= MAX_MESSAGES_PER_WINDOW) {
                throw new ValidationException("Rate limit exceeded. Maximum 10 messages per minute allowed.");
            }
            rateLimitData.put("count", count + 1);
        }
    }
}
