package ru.rusguardian.domain;

import lombok.*;

import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TelegramData {

    private String name;
    private String textMessage;
    private String photoId;
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
