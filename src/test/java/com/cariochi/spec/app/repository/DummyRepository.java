package com.cariochi.spec.app.repository;

import com.cariochi.spec.app.model.DummyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyRepository extends JpaRepository<DummyEntity, Long>, JpaSpecificationExecutor<DummyEntity> {

}

