package com.goldmen.redisdomain.config.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class KakaoProperties {
    private final String baseUrl = "http://dapi.kakao.com";

    @Value("${kakaoKey}")
    private String restApiKey;

    public String getDefaultHeader() {
        return "KakaoAK " + restApiKey;
    }
}
