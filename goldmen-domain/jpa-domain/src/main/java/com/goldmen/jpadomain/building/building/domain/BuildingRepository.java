package com.goldmen.jpadomain.building.building.domain;

import com.goldmen.jpadomain.building.building.data.cond.FindBuildingOptionCond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Integer> {

    @Query(value = "select b from Building b " +
            "where b.legal.id = :#{#optionCond.legal.id} " +
            "and b.lat = :#{#optionCond.lat} " +
            "and b.lng = :#{#optionCond.lng} " +
            "and b.mainNumber = :#{#optionCond.mainNumber} " +
            "and b.subNumber = :#{#optionCond.subNumber} " +
            "and b.type = :#{#optionCond.buildingType} " +
            "and b.name = :#{#optionCond.buildingName}")
    Optional<Building> findFirstByOption(@Param("optionCond") FindBuildingOptionCond optionCond);

    //위치 기준으로 dist내에 있는 모든 건물 구하기
    @Query(value = "select b.* " +
            "from building b " +
            "where ST_Distance_Sphere(Point(:lng, :lat), POINT(b.lng, b.lat)) <= :dist " +
            "order by ST_Distance_Sphere(Point(:lng, :lat), POINT(b.lng, b.lat))", nativeQuery = true)
    List<Building> findAllByLocationAndDist(@Param("lat") double lat, @Param("lng") double lng, @Param("dist") int dist);

    @Query(value = "select b.* " +
            "from building b " +
            "where ST_Distance_Sphere(Point(:lng, :lat), POINT(b.lng, b.lat)) <= :dist " +
            "and b.building_type = :building_type " +
            "order by ST_Distance_Sphere(Point(:lng, :lat), POINT(b.lng, b.lat))", nativeQuery = true)
    List<Building> findAllByLocationAndDistAndBuildingType(@Param("lat") double lat, @Param("lng") double lng, @Param("dist") int dist,@Param("building_type") String buildingType);
}
