package ru.rusguardian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rusguardian.domain.RegionLivingWage;

import java.util.Optional;

@Repository
public interface RegionLivingWageRepository extends JpaRepository<RegionLivingWage, Long> {

    @Query(value = "SELECT * FROM ncs_bot.region_living_wage WHERE region_name = :regionName", nativeQuery = true)
    Optional<RegionLivingWage> findByName(@Param("regionName") String regionName);

}
