package org.example.broong.domain.store.dto;

import static org.apache.logging.log4j.util.Strings.isEmpty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.example.broong.domain.store.Category;

public class StoreRequestDto {

    @Getter
    public static class Add {

        @NotBlank(message = "name은 필수 입력 값 입니다.")
        @Size(min = 4, max = 30, message = "4자 ~ 30자 사이의 길이로 입력해주세요.")
        private String name;
        @NotBlank(message = "category는 필수 입력 값 입니다.")
        @Size(min = 4, max = 50, message = "4자 ~ 50자 사이의 길이로 입력해주세요.")
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

        public Add(String name, Category category, String openingTime, String closingTime,
            String minOrderPrice) {
            this.name = name;
            this.category = category;
            this.openingTime = openingTime;
            this.closingTime = closingTime;
            this.minOrderPrice = isEmpty(minOrderPrice) ? 0 : Integer.parseInt(minOrderPrice) ;
        }
    }
}
