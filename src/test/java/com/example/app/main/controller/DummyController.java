package com.example.app.main.controller;

import com.cariochi.spec.Spec;
import com.cariochi.spec.operator.ContainsIgnoreCase;
import com.cariochi.spec.operator.In;
import com.example.app.main.model.DummyEntity;
import com.example.app.main.security.UserAllowedRegions;
import com.example.app.main.service.DummyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static jakarta.persistence.criteria.JoinType.INNER;

@RestController
@RequiredArgsConstructor
public class DummyController {

    private final DummyService testService;

    @GetMapping({"/dummy", "/organizations/{organizationId}/dummy"})
    public List<DummyEntity> findAll(
            @Spec.Path(name = "organizationId", attribute = "organization.id")
            @Spec.Param(name = "id")
            @Spec.Param(name = "status", operator = In.class)
            @Spec.Param(name = "name", operator = ContainsIgnoreCase.class)
            @Spec.Param(name = "orgId", attribute = "organization.id")
            @Spec.Param(name = "labels", operator = In.class)
            @Spec.Param(name = "labels2", attribute = "labels", operator = In.class)
            @Spec.Param(name = "propertyKey", attribute = "properties.key", operator = In.class)
            @Spec.Param(name = "propertyValue", attribute = "properties.value", operator = In.class, joinType = INNER, distinct = true)
            @Spec.Header(name = "region", attribute = "organization.region")
            @Spec.Condition(attribute = "organization.region", valueResolver = UserAllowedRegions.class, operator = In.class)
            Specification<DummyEntity> spec
    ) {
        return testService.findAll(spec);
    }

    @GetMapping("/dummy/with-expression")
    public List<DummyEntity> findWithExpression(
            @Spec.Param(name = "id")
            @Spec.Param(name = "name", operator = ContainsIgnoreCase.class)
            @Spec.Param(name = "status", operator = In.class)
            @Spec.Param(name = "labels", operator = In.class)
            @Spec.Expression(value = "(id || status) && (name || !labels)", strict = false)
            Specification<DummyEntity> spec
    ) {
        return testService.findAll(spec);
    }

}
