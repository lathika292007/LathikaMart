package com.lathika.lathikamart.controller;

import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.service.ChatService;
import com.lathika.lathikamart.service.ChatServiceImpl;
import com.lathika.lathikamart.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ChatServlet processes AI Chatbot query requests.
 */
@WebServlet(name = "ChatServlet", urlPatterns = {"/api/v1/chat"})
public class ChatServlet extends HttpServlet {

    private ChatService chatService;

    @Override
    public void init() throws ServletException {
        this.chatService = new ChatServiceImpl();
    }

    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ChatRequest body = parseRequestBody(req, ChatRequest.class);
            if (body == null || body.getMessage() == null) {
                JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", "Message field is required.");
                return;
            }

            String reply = chatService.processChatMessage(body.getMessage(), req.getSession(true));

            Map<String, Object> data = new HashMap<>();
            data.put("reply", reply);

            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, data);
        } catch (AppException e) {
            JsonUtil.sendError(resp, e.getStatusCode(), "CHAT_ERROR", e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SERVER_ERROR", "Chatbot service temporarily unavailable.");
        }
    }

    private <T> T parseRequestBody(HttpServletRequest req, Class<T> clazz) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return JsonUtil.fromJson(sb.toString(), clazz);
    }

    private static class ChatRequest {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
