package com.epam.esm.gym.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_sequence")
    @SequenceGenerator(name = "role_sequence",
            sequenceName = "role_id_seq",
            allocationSize = 1, initialValue = 5)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    @Fetch(FetchMode.JOIN)
    private RoleType permission;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "role",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;
    @ElementCollection(targetClass = Permission.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "role_authorities", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "authority", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Permission> authorities;
}
