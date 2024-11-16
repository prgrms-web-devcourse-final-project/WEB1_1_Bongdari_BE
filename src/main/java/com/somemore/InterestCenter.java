package com.somemore;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "interest_center")
public class InterestCenter {
    @EmbeddedId
    private InterestCenterId id;

}