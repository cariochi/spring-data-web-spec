package com.example.app.main.service;

import com.example.app.main.model.DummyEntity;
import com.example.app.main.repository.DummyRepository;
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
