package ru.rusguardian.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ncs_bot.telegram_data")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TelegramDataDto {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "text_message")
    private String textMessage;

    @Column(name = "photo_id")
    private String photoId;

    @Column(name = "sticker_id")
    private String stickerId;

    @Column(name = "video_id")
    private String videoId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TelegramDataDto)) return false;
        TelegramDataDto that = (TelegramDataDto) o;
        return getId() == that.getId() && getName().equals(that.getName()) && Objects.equals(getTextMessage(), that.getTextMessage()) && Objects.equals(getPhotoId(), that.getPhotoId()) && Objects.equals(getStickerId(), that.getStickerId()) && Objects.equals(getVideoId(), that.getVideoId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getTextMessage(), getPhotoId(), getStickerId(), getVideoId());
    }
}
