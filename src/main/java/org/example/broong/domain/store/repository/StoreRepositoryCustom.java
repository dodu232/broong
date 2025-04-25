package org.example.broong.domain.store.repository;

import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.dto.StoreResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface StoreRepositoryCustom {
    Slice<StoreResponseDto.Get> findAllByCategory(Category category, Pageable pageable);
}
