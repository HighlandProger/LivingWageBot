package ru.rusguardian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.rusguardian.domain.TelegramData;

import java.util.Optional;

@Repository
public interface TelegramDataRepository extends JpaRepository<TelegramData, Long> {

    @Query(value = "SELECT * FROM ncs_bot.telegram_data WHERE name = :name", nativeQuery = true)
    Optional<TelegramData> findByName(@Param("name") String name);

}
