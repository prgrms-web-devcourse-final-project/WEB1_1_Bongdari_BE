package com.somemore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "interest_center")
public class InterestCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

}