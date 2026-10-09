package com.lathika.lathikamart.ai;

import java.util.Locale;

/**
 * Fallback / Offline rule-based ChatProvider handling e-commerce FAQ queries.
 */
public class MockChatProvider implements ChatProvider {

    @Override
    public String getReply(String userMessage, String context) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Hello! I am your LathikaMart AI assistant. How can I help you with products or orders today?";
        }

        String msg = userMessage.trim().toLowerCase(Locale.ROOT);

        if (msg.contains("shipping") || msg.contains("delivery") || msg.contains("deliver")) {
            return "Standard shipping on LathikaMart takes 2-4 business days. Express shipping options are available at checkout.";
        } else if (msg.contains("return") || msg.contains("refund")) {
            return "LathikaMart offers a 30-day hassle-free return policy on most items. Go to your Orders page to initiate a return.";
        } else if (msg.contains("payment") || msg.contains("pay") || msg.contains("card")) {
            return "We accept all major credit cards, debit cards, net banking, and mock payment options for testing.";
        } else if (msg.contains("headphone") || msg.contains("electronics") || msg.contains("watch") || msg.contains("shoe") || msg.contains("jacket")) {
            return "Check out our Electronics and Fashion sections for top-rated Noise-Canceling Headphones, Smart Fitness Watches, and Leather Jackets!";
        } else if (msg.contains("seller") || msg.contains("vendor")) {
            return "To become a seller on LathikaMart, register an account as a 'Seller' and list your products from your Seller Dashboard!";
        } else if (msg.contains("track") || msg.contains("order")) {
            return "You can view your current order status by navigating to 'My Orders' in your buyer account menu.";
        } else if (msg.contains("discount") || msg.contains("coupon") || msg.contains("offer")) {
            return "Enjoy competitive seller pricing and daily marketplace deals across all categories on LathikaMart.";
        } else {
            return "Thank you for asking! LathikaMart is a multi-seller marketplace where you can browse products, manage orders, and leave reviews. For specific item availability, please search our catalog!";
        }
    }
}
