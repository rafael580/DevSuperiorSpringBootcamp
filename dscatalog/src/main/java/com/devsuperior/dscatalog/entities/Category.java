package com.devsuperior.dscatalog.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "tb_category")
public class Category implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String Name;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createAt;
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createUpdateAt;

    public Category(){}
    public Category(String name){
        this.Name = name;
    }

    public Long getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public Instant getCreateUpdateAt() {
        return createUpdateAt;
    }

    @PrePersist
    public void prePersist(){
        createAt = Instant.now();
    }
    @PreUpdate
    public void preUpdate(){
        createUpdateAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(Id, category.Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }
}
