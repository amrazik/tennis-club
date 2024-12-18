package com.amrazik.tennisclub.data.model;



public interface BaseEntity {
    Long getId();
    void setId(Long id);
    boolean isDeleted();
    void setDeleted(boolean deleted);
}
