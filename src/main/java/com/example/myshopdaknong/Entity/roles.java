package com.example.myshopdaknong.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity(name = "roles")
public class roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45,nullable = false,unique = true)
    private String Name;

    @Column(name = "descripton")
    private String des;

    @ManyToMany(mappedBy = "listRoles",fetch = FetchType.EAGER)
    private Set<Users> list_User=new HashSet<>();

    public roles(String name, String des) {
        Name = name;
        this.des = des;
    }

    public roles() {
    }
}
