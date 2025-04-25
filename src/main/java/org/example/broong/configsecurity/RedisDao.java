package org.example.broong.configsecurity;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisDao {

    private final RedisTemplate<String, String> redisTemplate;

    public void setRefreshToken(String key, String refreshToken, long refreshTokenTime) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(refreshToken.getClass())); // 리프레쉬 토큰을 직렬화 하는 코드 ( 데이터 압축효과도 있음 )
        redisTemplate.opsForValue().set(key, refreshToken, refreshTokenTime, TimeUnit.MINUTES);
    }

    public String getRefreshToken(String key) {
        return  redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(String key) {
        redisTemplate.delete(key);
    }

}




