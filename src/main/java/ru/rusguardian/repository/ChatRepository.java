package ru.rusguardian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.rusguardian.domain.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE ncs_bot.chats SET status = :status WHERE id = :id", nativeQuery = true)
    void updateUserStatus(@Param("id") Long id, @Param("status") String status);

}
