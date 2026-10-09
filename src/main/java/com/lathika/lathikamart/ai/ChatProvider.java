package com.lathika.lathikamart.ai;

/**
 * Interface for pluggable AI Chatbot providers per specification Section 17.
 */
public interface ChatProvider {
    String getReply(String userMessage, String context);
}
