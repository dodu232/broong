package org.example.broong.domain.store.controller;

import lombok.RequiredArgsConstructor;
import org.example.broong.domain.store.dto.StoreRequestDto;
import org.example.broong.domain.store.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {
    private final StoreService service;

    /**
     * security 사용하면 authentic 객체에서 유저 id 받아야 함~
     */
    @PostMapping
    public ResponseEntity<Void> addStore(
        @RequestBody StoreRequestDto.Add dto
    ){
        service.addStore(dto, 1);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
