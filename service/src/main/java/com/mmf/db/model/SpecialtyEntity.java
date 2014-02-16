package com.mmf.db.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Svetlana.Voyteh
 * Date: 13.10.12
 */
@Entity
@Table(name = "specialty")
public class SpecialtyEntity implements EntityClass<Long> {
    private static final long serialVersionUID = -4074979088297122885L;

    private Long id;
    private String name;
    private String description;
    private Set<GroupEntity> groups = new HashSet<GroupEntity>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "specialty", fetch = FetchType.LAZY)
    public Set<GroupEntity> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupEntity> groups) {
        this.groups = groups;
    }
}
