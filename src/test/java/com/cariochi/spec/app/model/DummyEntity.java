package com.cariochi.spec.app.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import java.util.Map;
import java.util.Set;
import lombok.Data;

import static jakarta.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class DummyEntity {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String name;

    @ManyToOne
    private Organization organization;

    @ElementCollection
    @CollectionTable(name = "dummy_entity_labels", joinColumns = @JoinColumn(name = "dummy_entity_id"))
    @Column(name = "label")
    Set<String> labels;

    @ElementCollection
    @CollectionTable(
            name = "dummy_entity_properties",
            joinColumns = @JoinColumn(name = "dummy_entity_id")
    )
    @MapKeyColumn(name = "prop_key")
    @Column(name = "prop_value")
    private Map<String, String> properties;

    public enum Status {STOPPED, FAILED, ACTIVE}
}
