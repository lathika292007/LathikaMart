package com.lathika.lathikamart.service;

import com.lathika.lathikamart.exception.AppException;

import javax.servlet.http.HttpSession;

/**
 * Service interface for Chatbot queries.
 */
public interface ChatService {
    String processChatMessage(String userMessage, HttpSession session) throws AppException;
}
