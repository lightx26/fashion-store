package com.pbl.fashionstore.controllers;

import com.pbl.fashionstore.services.StyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/styles")
@RequiredArgsConstructor
public class StyleController {
    private final StyleService styleService;

    @GetMapping
    public ResponseEntity<?> getStyles() {
        return ResponseEntity.ok(styleService.getAllStyles());
    }
}
