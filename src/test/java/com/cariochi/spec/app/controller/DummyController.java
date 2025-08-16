package com.cariochi.spec.app.controller;

import com.cariochi.spec.Spec;
import com.cariochi.spec.Spec.AccessControl;
import com.cariochi.spec.app.model.DummyEntity;
import com.cariochi.spec.app.security.AllowedRegions;
import com.cariochi.spec.app.service.DummyService;
import com.cariochi.spec.operator.ContainsIgnoreCase;
import com.cariochi.spec.operator.In;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DummyController {

    private final DummyService testService;

    @GetMapping({"/dummy", "/organizations/{organizationId}/dummy"})
    public List<DummyEntity> findAll(
            @Spec.PathVariable(name = "organizationId", path = "organization.id")
            @Spec.RequestParam(name = "id")
            @Spec.RequestParam(name = "status", operator = In.class)
            @Spec.RequestParam(name = "name", path = "name", operator = ContainsIgnoreCase.class)
            @Spec.RequestParam(name = "orgId", path = "organization.id")
            @Spec.RequestParam(name = "labels", operator = In.class)
            @Spec.RequestParam(name = "propertyKey", path = "properties.key", operator = In.class)
            @Spec.RequestParam(name = "propertyValue", path = "properties.value", operator = In.class, joinType = JoinType.INNER, distinct = true)
            @Spec.RequestHeader(name = "region", path = "organization.region")
            @AccessControl(path = "organization.region", valueSupplier = AllowedRegions.class, operator = In.class)
            Specification<DummyEntity> spec
    ) {
        return testService.findAll(spec);
    }

}
