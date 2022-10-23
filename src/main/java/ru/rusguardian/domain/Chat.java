package ru.rusguardian.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Chat {

    private Long id;
    private String username;
    private Status status;
    private int employeeCount;
    private int childCount;
    private int retireeCount;
    private RegionLivingWage regionLivingWage;
    private List<Integer> salaries = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chat)) return false;
        Chat chat = (Chat) o;
        return getId().equals(chat.getId()) && Objects.equals(getUsername(), chat.getUsername()) && getStatus() == chat.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getStatus());
    }
}
