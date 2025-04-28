package org.example.broong.domain.store.service;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.broong.domain.store.Category;
import org.example.broong.domain.store.dto.StoreRequestDto;
import org.example.broong.domain.user.entity.User;
import org.example.broong.domain.user.service.UserService;
import org.example.broong.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class StoreIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    private String accessToken;
    private Long userId = 1L;
    private Long storeId = 1L;

    @BeforeEach
    void setUp() {
        User testUser = userService.getById(userId);

        accessToken = jwtService.generateAccessToken(
            testUser.getId(),
            testUser.getEmail(),
            testUser.getUserType()
        );

    }

    @Test
    @DisplayName("가게 생성")
    void addStore() throws Exception {
        // given
        StoreRequestDto.Add dto = new StoreRequestDto.Add(
            "함부르크버거",
            Category.FAST_FOOD,
            "09:00",
            "21:00",
            10000
        );
        // when
        ResultActions actions = mockMvc.perform(
            post("/api/stores")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto))
        );

        // then
        actions.andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    @DisplayName("보유 중인  가게 리스트 조회")
    void getStoreListByUserId() throws Exception {
        // given
        for (int i = 1; i <= 2; i++) {
            var dto = new StoreRequestDto.Add(
                "가게게" + i,
                Category.FAST_FOOD,
                "09:00",
                "21:00",
                10000 + i * 1000
            );
            storeService.addStore(dto, userId);
        }

        // when
        ResultActions actions = mockMvc.perform(get("/api/stores/owner")
            .header("Authorization", accessToken)
            .accept(MediaType.APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].name").value("맛있는 닭도리찬 1호점"))
            .andExpect(jsonPath("$[1].name").value("가게게1"))
            .andExpect(jsonPath("$[2].name").value("가게게2"))
            .andDo(print());
    }

    @Test
    @DisplayName("가게 업데이트")
    void updateStore() throws Exception {
        // given
        StoreRequestDto.Update dto = new StoreRequestDto.Update(
            "맛있는 닭도리탕",
            Category.KOREAN,
            "11:00",
            "21:00",
            12000
        );

        // when
        ResultActions actions = mockMvc.perform(
            patch("/api/stores/{id}", storeId)
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto))
        );

        // then
        actions.andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("가게 삭제")
    void deleteStore() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc.perform(
            delete("/api/stores/{id}", storeId)
                .header("Authorization", accessToken)
        );

        // then
        actions.andExpect(status().isNoContent())
            .andDo(print());

        mockMvc.perform(
            delete("/api/stores/{id}", storeId)
                .header("Authorization", accessToken)
        ).andExpect(status().isBadRequest());
    }
}
