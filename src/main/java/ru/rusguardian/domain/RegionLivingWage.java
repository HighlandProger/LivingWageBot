package ru.rusguardian.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ncs_bot.region_living_wage")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegionLivingWage {

    @Id
    private long id;

    @Column(name = "region_name")
    private String regionName;

    @Column(name = "human_living_wage")
    private int humanLivingWage;

    @Column(name = "employee_living_wage")
    private int employeeLivingWage;

    @Column(name = "retiree_living_wage")
    private int retireeLivingWage;

    @Column(name = "child_living_wage")
    private int childLivingWage;

}
