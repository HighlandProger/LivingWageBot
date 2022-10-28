package ru.rusguardian.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ncs_bot.chats")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Chat {

    @Id
    @Column
    private Long id;

    @Column(name = "username")
    private String username;

    @Convert(converter = StatusConverter.class)
    private Status status;

    @Column(name = "employee_count")
    private int employeeCount;

    @Column(name = "child_count")
    private int childCount;

    @Column(name = "retiree_count")
    private int retireeCount;

    @ManyToOne
    @JoinColumn(name = "region_living_wage_id")
    private RegionLivingWage regionLivingWage;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> salaries = new ArrayList<>();

    @Column(name = "is_admin")
    private boolean isAdmin;

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
