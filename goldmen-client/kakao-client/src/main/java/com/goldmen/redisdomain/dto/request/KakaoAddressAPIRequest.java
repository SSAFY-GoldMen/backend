package com.goldmen.redisdomain.dto.request;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KakaoAddressAPIRequest {
    private String sggNm;
    private String bjDongNm;
    private String bobn;
    private String bubn;

    public String makeRoadAddress() {
        return sggNm + " " + bjDongNm + " " +
                bobn + "-" + bubn;
    }
}
