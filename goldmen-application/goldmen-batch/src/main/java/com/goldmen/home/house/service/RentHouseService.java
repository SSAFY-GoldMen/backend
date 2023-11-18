package com.goldmen.home.house.service;

import com.goldmen.home.building.Monthly.domain.Monthly;
import com.goldmen.home.building.Monthly.service.MonthlyService;
import com.goldmen.home.building.building.domain.Building;
import com.goldmen.home.building.building.service.BuildingService;
import com.goldmen.home.building.global.domain.HouseInfo;
import com.goldmen.home.building.jeonse.domain.Jeonse;
import com.goldmen.home.building.jeonse.service.JeonseService;
import com.goldmen.home.dto.request.KakaoAddressAPIRequest;
import com.goldmen.home.house.dto.request.SeoulOpenDataRentHouseAPIRequest;
import com.goldmen.home.house.mapper.RentHouseMapper;
import com.goldmen.home.house.vo.SeoulOpenDataRentHouse;
import com.goldmen.home.house.vo.SeoulOpenDataRentHouseData;
import com.goldmen.home.map.district.domain.District;
import com.goldmen.home.map.district.service.DistrictService;
import com.goldmen.home.map.legal.domain.Legal;
import com.goldmen.home.map.legal.service.LegalService;
import com.goldmen.home.service.KakaoMapService;
import com.goldmen.home.vo.Position;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RentHouseService {
    private final SeoulOpenDataRentHouseClient seoulOpenDataRentHouseClient;
    private final DistrictService districtService;
    private final LegalService legalService;
    private final BuildingService buildingService;
    private final KakaoMapService kakaoMapClient;
    private final JeonseService jeonseService;
    private final MonthlyService monthlyService;
    private final RentHouseMapper rentHouseMapper;

    /**
     * 전세집 데이터를 추출 및 DB에 저장한다.
     * @param devide HouseData를 어느 정도로 줄인건지에 대한 param
     */
    public void saveHouseDatas(int devide) {
        SeoulOpenDataRentHouseData healthCheckResponse = seoulOpenDataRentHouseClient.fetchAPI(SeoulOpenDataRentHouseAPIRequest.healthCheckRequest());
        int totalCount = healthCheckResponse.listTotalCount() / devide;

        for (int i = 1; i < totalCount; i += 999) {
            SeoulOpenDataRentHouseData seoulOpenDataRentHouseData = seoulOpenDataRentHouseClient.fetchAPI(SeoulOpenDataRentHouseAPIRequest.toRequest(i, 999));
            saveHouseData(seoulOpenDataRentHouseClient.filteringHouse(seoulOpenDataRentHouseData.seoulOpenDataRentHouseList()));
            log.info("data search {}%", (float) i / totalCount * 100);
        }
        log.info("fetchHouseData method end");
    }

    /**
     * 전세집 데이터를 DB에 저장한다. 저장 테이블은 region, houseGeo, houseDetail 이다.
     *
     * @param rentHouseList 공공데이터에서 뽑은 전세집 데이터들
     */
    private void saveHouseData(List<SeoulOpenDataRentHouse> rentHouseList) {
        for (SeoulOpenDataRentHouse rentHouse : rentHouseList) {
            try {
                District district = districtService.saveDistrict(rentHouseMapper.toDistrict(rentHouse));
                Legal legal = legalService.saveLegal(rentHouseMapper.toLegal(rentHouse,district));
                Position position = kakaoMapClient.getPosition(rentHouseMapper.toKakaoAddressAPIRequest(rentHouse));
                Building building = buildingService.save(rentHouseMapper.toBuilding(rentHouse,legal,position));

                if(rentHouse.rentGbn().equals("전세")){
                    jeonseService.save(rentHouseMapper.toJeonse(rentHouse,building));
                }else if(rentHouse.rentGbn().equals("월세")){
                    monthlyService.save(rentHouseMapper.toMonthly(rentHouse,building));
                }
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
    }
}