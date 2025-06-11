package com.training.classicalragapi.controller;

import com.training.classicalragapi.embadding.service.DataLoaderService;
import com.training.classicalragapi.embadding.service.PdfFileReaderService;
import com.training.classicalragapi.rag.model.AIGenerativeResponse;
import com.training.classicalragapi.rag.service.RagService;
import lombok.RequiredArgsConstructor;

import org.springframework.ai.document.Document;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@ResponseBody
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final DataLoaderService dataLoaderService;
    private final PdfFileReaderService pdfFileReaderService;
    private final RagService ragService;

    @GetMapping("/{uuid}/assistant")
    ResponseEntity<AIGenerativeResponse> inquire(@PathVariable String uuid, @RequestParam String question) {
        return ResponseEntity.ok(ragService.generate(uuid, question));
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadKnowledge(
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(dataLoaderService.uploadKnowledge(file));
    }

    @GetMapping("/docs")
    ResponseEntity<List<Document>> query(@RequestParam String query) {

        List<Document> docs = pdfFileReaderService.getResources(query);
        return ResponseEntity.ok(docs);
    }
}
