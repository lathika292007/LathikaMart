/* AI Chatbot Floating Widget Controller */
document.addEventListener('DOMContentLoaded', () => {
    const chatBtn = document.getElementById('chat-widget-btn');
    const chatPanel = document.getElementById('chat-panel');
    const closeChatBtn = document.getElementById('close-chat-btn');
    const chatForm = document.getElementById('chat-form');
    const chatInput = document.getElementById('chat-input');
    const chatMessages = document.getElementById('chat-messages');

    if (!chatBtn || !chatPanel) return;

    chatBtn.addEventListener('click', () => {
        chatPanel.classList.toggle('active');
        if (chatPanel.classList.contains('active')) {
            chatInput.focus();
        }
    });

    closeChatBtn.addEventListener('click', () => {
        chatPanel.classList.remove('active');
    });

    chatForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const msg = chatInput.value.trim();
        if (!msg) return;

        // Append user message
        appendMessage(msg, 'user');
        chatInput.value = '';

        // Temporary loading message
        const loadingId = appendMessage('Typing...', 'bot');

        try {
            const contextPath = window.contextPath || '';
            const res = await fetch(contextPath + '/api/v1/chat', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ message: msg })
            });

            const data = await res.json();
            removeMessage(loadingId);

            if (data.success && data.data && data.data.reply) {
                appendMessage(data.data.reply, 'bot');
            } else if (data.error && data.error.message) {
                appendMessage(data.error.message, 'bot');
            } else {
                appendMessage('Sorry, I am unable to answer at this moment.', 'bot');
            }
        } catch (err) {
            removeMessage(loadingId);
            appendMessage('Network error. Please check your connection.', 'bot');
        }
    });

    function appendMessage(text, sender) {
        const id = 'msg-' + Date.now() + '-' + Math.random().toString(36).substring(2, 5);
        const msgDiv = document.createElement('div');
        msgDiv.id = id;
        msgDiv.className = `chat-msg ${sender === 'user' ? 'chat-msg-user' : 'chat-msg-bot'}`;
        msgDiv.textContent = text;
        chatMessages.appendChild(msgDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
        return id;
    }

    function removeMessage(id) {
        const el = document.getElementById(id);
        if (el) el.remove();
    }
});
