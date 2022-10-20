package ru.rusguardian.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "ncs_bot.telegram_data")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TelegramData extends GenericEntity<Long> {

    @Column(name = "name")
    private String name;

    @Column(name = "text_message")
    private String textMessage;

    @Column(name = "photo_id")
    private String photoId;

    @Column(name = "sticker_id")
    private String stickerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TelegramData that)) return false;
        if (!super.equals(o)) return false;
        return getName().equals(that.getName()) && Objects.equals(getTextMessage(), that.getTextMessage()) && Objects.equals(getPhotoId(), that.getPhotoId()) && Objects.equals(getStickerId(), that.getStickerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getName(), getTextMessage(), getPhotoId(), getStickerId());
    }
}
