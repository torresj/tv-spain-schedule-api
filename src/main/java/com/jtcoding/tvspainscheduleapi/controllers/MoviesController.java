package com.jtcoding.tvspainscheduleapi.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/movies")
@Slf4j
@RequiredArgsConstructor
public class MoviesController {
    @GetMapping("/live")
    public ResponseEntity<String> getLiveMovies(){
        return ResponseEntity.ok("test");
    }
}
