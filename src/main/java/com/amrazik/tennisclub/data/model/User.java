package com.amrazik.tennisclub.data.model;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLRestriction;


/**
 * Represents a user in the system.
 *
 * The `User` class maps to a database entity that holds information about a user, including their name, phone number,
 * and deletion status. The class implements the `BaseEntity` interface to ensure it has an ID and a deletion flag.
 *
 * The `phoneNumber` field is unique, ensuring that no two users can have the same phone number. The class is annotated
 * with `@SQLRestriction` to ensure that only non-deleted users are retrieved from the database.
 */
@Entity
@Table(name = "\"user\"")
@SQLRestriction("deleted = false")
public class User implements BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String phoneNumber;
    private boolean deleted = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
