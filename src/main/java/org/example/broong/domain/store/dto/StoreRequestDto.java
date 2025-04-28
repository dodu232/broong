package org.example.broong.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.broong.domain.store.Category;

public class StoreRequestDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Add {

        @NotBlank(message = "name은 필수 입력 값 입니다.")
        @Size(min = 4, max = 30, message = "4자 ~ 30자 사이의 길이로 입력해주세요.")
        private String name;
        @NotNull(message = "category는 필수 입력 값 입니다.")
        private Category category;
        @NotBlank(message = "시간은 필수 입력 항목입니다.")
        @Pattern(
                regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",
                message = "시간 형식은 HH:mm (00:00~23:59) 이어야 합니다."
        )
        private String openingTime;
        @NotBlank(message = "시간은 필수 입력 항목입니다.")
        @Pattern(
                regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",
                message = "시간 형식은 HH:mm 이어야 합니다."
        )
        private String closingTime;
        private int minOrderPrice;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update {
        @Size(min = 4, max = 30, message = "4자 ~ 30자 사이의 길이로 입력해주세요.")
        private String name;
        private Category category;
        @Pattern(
                regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",
                message = "시간 형식은 HH:mm (00:00~23:59) 이어야 합니다."
        )
        private String openingTime;
        @Pattern(
                regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",
                message = "시간 형식은 HH:mm 이어야 합니다."
        )
        private String closingTime;
        private int minOrderPrice;
    }
}
