package com.koushik.expansetracker.controller.security;

import com.koushik.expansetracker.dto.CreateScreenRequest;
import com.koushik.expansetracker.entity.security.Screen;
import com.koushik.expansetracker.service.security.ScreenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/screens")
public class ScreenController {

    private final ScreenService screenService;

    public ScreenController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @PostMapping
    public ResponseEntity<Screen> createScreen(@RequestBody CreateScreenRequest request) {
        Screen screen = screenService.createScreen(request);
        return ResponseEntity.ok(screen);
    }
}
