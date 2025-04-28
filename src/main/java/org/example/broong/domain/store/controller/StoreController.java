package org.example.broong.domain.store.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.dto.StoreRequestDto;
import org.example.broong.domain.store.dto.StoreResponseDto;
import org.example.broong.domain.store.dto.StoreResponseDto.Get;
import org.example.broong.domain.store.service.StoreService;
import org.example.broong.security.auth.CustomUserDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        service.addStore(dto, userDetails.getUserId());
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
        @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        return ResponseEntity.status(HttpStatus.OK).body(service.getStoreListByUserId(userDetails.getUserId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateStore(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Valid @RequestBody StoreRequestDto.Update dto,
        @PathVariable Long id
    ){
        service.updateStore(id, userDetails.getUserId(), dto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long id
    ){
        service.deleteStore(id, userDetails.getUserId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
