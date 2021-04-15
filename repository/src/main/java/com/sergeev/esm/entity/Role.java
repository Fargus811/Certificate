package com.sergeev.esm.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.MERGE})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> users;
}
