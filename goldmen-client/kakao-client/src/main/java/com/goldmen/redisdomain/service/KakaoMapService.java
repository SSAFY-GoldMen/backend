package com.goldmen.redisdomain.service;

import com.goldmen.redisdomain.config.property.KakaoAddressProperties;
import com.goldmen.redisdomain.dto.request.KaKaoKeywordAPIRequest;
import com.goldmen.redisdomain.dto.request.KakaoAddressAPIRequest;
import com.goldmen.redisdomain.dto.response.KakaoAPIResponse;
import com.goldmen.redisdomain.vo.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KakaoMapService {
    private final WebClient kakaoWebClient;
    private final KakaoAddressProperties kakaoAddressProperties;

    /**
     * 파라미터에 맞는 주소의 좌표(위도, 경도)를 추출한다.
     *
     * @param request {@link KakaoAddressAPIRequest}
     * @return x(경도), y(위도)를 return
     * @throws IndexOutOfBoundsException 해당 주소의 좌표를 찾을 수 없으면 던지는 Exception
     */
    public Position getPosition(KakaoAddressAPIRequest request)
            throws IndexOutOfBoundsException {
        /* roadAddress : 지번 ( 구 / 동 / 본번-부번 ) */
        KakaoAPIResponse kakaoAPIResponse = fetchAPI(request);
        validateToAPIResponse(kakaoAPIResponse);
        return kakaoAPIResponse.getPositionList().get(0);
    }

    private KakaoAPIResponse fetchAPI(KakaoAddressAPIRequest request) {
        return kakaoWebClient
                .method(kakaoAddressProperties.getMethod())
                .uri(builder -> builder
                        .path(kakaoAddressProperties.getAddressPath())
                        .queryParam("query", request.makeRoadAddress())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(KakaoAPIResponse.class)
                .block();
    }

    private void validateToAPIResponse(KakaoAPIResponse kakaoAPIResponse) {
        if (kakaoAPIResponse.getPositionList().isEmpty()) {
            throw new IndexOutOfBoundsException();
        }
    }

    public Position getPosition(KaKaoKeywordAPIRequest request) throws IndexOutOfBoundsException {
        KakaoAPIResponse kakaoAPIResponse = fetchAPI(request);
        validateToAPIResponse(kakaoAPIResponse);
        return kakaoAPIResponse.getPositionList().get(0);
    }

    private KakaoAPIResponse fetchAPI(KaKaoKeywordAPIRequest request) {
        return kakaoWebClient
                .method(kakaoAddressProperties.getMethod())
                .uri(builder -> builder
                        .path(kakaoAddressProperties.getKeywordPath())
                        .queryParam("query", request.makeQuery())
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(KakaoAPIResponse.class)
                .block();
    }
}
