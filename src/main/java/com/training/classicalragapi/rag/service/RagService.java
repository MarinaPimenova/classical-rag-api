package com.training.classicalragapi.rag.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @noinspection SpringJavaInjectionPointsAutowiringInspection
 */
@Service
public class RagService {
    private String template = """
            You're assisting with questions.
            Use the following context and chat history to answer the QUESTION but act as if you knew this information innately.
            If unsure, simply state that you don't know.
                        
            QUESTION
            {question}
                        
            """;
    @Value("classpath:/system-prompt-template.st")
    private Resource systemPrompt;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;
    private final ChatClient chatClient;

    public RagService(
            VectorStore vectorStore,
            ChatMemory chatMemory, ChatClient chatClient) {
        this.vectorStore = vectorStore;
        this.chatMemory = chatMemory;
        this.chatClient = chatClient;

    }

    public String generate(String sessionId, String question) {

        PromptTemplate pt = new PromptTemplate(template);
        Prompt p = pt.create(Map.of("question", question));
        return chatClient
                .prompt(p)
                .system(systemSpec -> systemSpec.text(systemPrompt)
                        .param("question", question))
                .advisors(
                        promptChatMemoryAdvisor(sessionId),
                        new QuestionAnswerAdvisor(vectorStore),
                        new SimpleLoggerAdvisor())
                .call()
                .content();
    }

    protected PromptChatMemoryAdvisor promptChatMemoryAdvisor(String conversationId) {
        return PromptChatMemoryAdvisor
                .builder(chatMemory)
                .conversationId(conversationId)
                .build();
    }
}

