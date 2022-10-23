package ru.rusguardian.domain;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegionLivingWage {

    private String regionName;
    private int humanLivingWage;
    private int employeeLivingWage;
    private int retireeLivingWage;
    private int childLivingWage;
}
