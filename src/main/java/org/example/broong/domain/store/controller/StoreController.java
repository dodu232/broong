package org.example.broong.domain.store.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.common.Auth;
import org.example.broong.domain.common.AuthUser;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.dto.StoreRequestDto;
import org.example.broong.domain.store.dto.StoreResponseDto;
import org.example.broong.domain.store.dto.StoreResponseDto.Get;
import org.example.broong.domain.store.service.StoreService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService service;

    @PostMapping
    public ResponseEntity<Void> addStore(
        @Valid @RequestBody StoreRequestDto.Add dto,
        @Auth AuthUser user
    ) {
        service.addStore(dto, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Slice<StoreResponseDto.Get>> getStoreList(
        @RequestParam(required = false) Category category,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getStoreList(category, pageable));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<Get>> getStore(
        @Auth AuthUser user
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.getStoreListByUserId(user.getId()));
    }
}
