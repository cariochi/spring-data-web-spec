package com.cariochi.spec.app.service;

import com.cariochi.spec.app.repository.DummyRepository;
import com.cariochi.spec.app.model.DummyEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DummyService {

    private final DummyRepository repo;

    public List<DummyEntity> findAll(Specification<DummyEntity> spec) {
        return repo.findAll(spec);
    }
}
