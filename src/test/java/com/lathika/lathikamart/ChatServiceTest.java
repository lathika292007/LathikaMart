package com.lathika.lathikamart;

import com.lathika.lathikamart.ai.MockChatProvider;
import com.lathika.lathikamart.exception.ValidationException;
import com.lathika.lathikamart.service.ChatService;
import com.lathika.lathikamart.service.ChatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ChatServiceTest {

    private ChatService chatService;
    private HttpSession mockSession;

    @BeforeEach
    void setUp() {
        chatService = new ChatServiceImpl(new MockChatProvider());
        mockSession = Mockito.mock(HttpSession.class);
    }

    @Test
    void testProcessChatMessage_Success() throws Exception {
        String reply = chatService.processChatMessage("What is your return policy?", mockSession);
        assertNotNull(reply);
        assertTrue(reply.contains("30-day hassle-free return policy"));
    }

    @Test
    void testProcessChatMessage_EmptyMessageThrowsValidationException() {
        assertThrows(ValidationException.class, () -> chatService.processChatMessage("   ", mockSession));
    }

    @Test
    void testProcessChatMessage_ExceedsMaxLengthThrowsValidationException() {
        String longMessage = "a".repeat(501);
        assertThrows(ValidationException.class, () -> chatService.processChatMessage(longMessage, mockSession));
    }

    @Test
    void testProcessChatMessage_RateLimitingEnforced() throws Exception {
        ConcurrentHashMap<String, Object> rateData = new ConcurrentHashMap<>();
        rateData.put("startTime", System.currentTimeMillis());
        rateData.put("count", 10);
        when(mockSession.getAttribute(eq("chat_rate_limit"))).thenReturn(rateData);

        assertThrows(ValidationException.class, () -> chatService.processChatMessage("Hello", mockSession));
    }

    @Test
    void testMockChatProvider_FaqReplies() throws Exception {
        String shipping = chatService.processChatMessage("Tell me about shipping options", mockSession);
        assertTrue(shipping.contains("Standard shipping on LathikaMart takes 2-4 business days"));

        String payment = chatService.processChatMessage("How do I pay?", mockSession);
        assertTrue(payment.contains("We accept all major credit cards"));

        String products = chatService.processChatMessage("Do you sell headphones?", mockSession);
        assertTrue(products.contains("Electronics"));
    }
}
