package com.goldmen.redisdomain.dto.request;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KaKaoKeywordAPIRequest {
    private String keyword;

    public String makeQuery() {
        return keyword;
    }
}
