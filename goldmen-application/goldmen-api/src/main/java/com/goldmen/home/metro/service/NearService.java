package com.goldmen.home.metro.service;

import com.goldmen.home.building.service.BuildingEnum;
import com.goldmen.home.building.service.HouseService;
import com.goldmen.home.metro.dto.NearMetroRequest;
import com.goldmen.home.metro.dto.NearMetroResponse;
import com.goldmen.home.metro.duration.domain.Duration;
import com.goldmen.home.metro.duration.service.DurationService;
import com.goldmen.home.metro.station.domain.Station;
import com.goldmen.home.metro.station.service.StationServiceImpl;
import com.goldmen.home.type.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.goldmen.home.global.Mapper.stationToNearMetroResponse;

@Service
@RequiredArgsConstructor
public class NearService {
    private final DurationService durationService;
    private final HouseService houseService;
    private final StationServiceImpl stationService;

    public ApiResponse<List<NearMetroResponse>> getNearMetroList(NearMetroRequest request) {
        int standId = stationService.findFirstStationByName(request.name().substring(0, request.name().length() - 1)).getId();
        List<Duration> durationList = durationService.getNearDurationByTime(standId, request.time());
        List<NearMetroResponse> responseList = durationList.stream().map(duration -> {
            Station station = getDiffStation(duration, standId);
            String buildingTypeKorean = BuildingEnum.valueOf(request.buildingType()).strKorean;
            int middlePrice = houseService.getBuildingMiddlePrice(station, buildingTypeKorean, request.priceType());
            return stationToNearMetroResponse(station, middlePrice, duration.getTime());
        }).toList();
        return ApiResponse.valueOf(responseList);
    }

    //start와 end 중 다른 역의 아이디 추출
    Station getDiffStation(Duration duration, int stationId) {
        if (duration.getEndStation().getId() == stationId) {
            return duration.getStartStation();
        } else {
            return duration.getEndStation();
        }
    }
}