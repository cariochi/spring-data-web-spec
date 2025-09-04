package com.example.app.tests;

import com.cariochi.recordo.mockmvc.RecordoApiClient;
import com.example.app.main.model.DummyEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RecordoApiClient
@RequestMapping
public interface DummyApi {


    @GetMapping("/dummy")
    List<DummyEntity> findAll(
            @RequestParam("id") Long id,
            @RequestParam("status") List<DummyEntity.Status> status,
            @RequestParam("name") String name,
            @RequestParam("orgId") Long orgId,
            @RequestHeader("region") String region
    );

    @GetMapping("/organizations/{organizationId}/dummy")
    List<DummyEntity> findInOrganization(
            @PathVariable("organizationId") Long organizationId,
            @RequestParam("id") Long id,
            @RequestParam("status") List<DummyEntity.Status> status,
            @RequestParam("name") String name,
            @RequestHeader("region") String region
    );

    @GetMapping("/dummy")
    List<DummyEntity> findByLabels(
            @RequestParam("labels") String labels,
            @RequestParam("labels2") String labels2
    );

    @GetMapping("/dummy")
    List<DummyEntity> findByProperty(
            @RequestParam("propertyKey") String propertyKey,
            @RequestParam("propertyValue") String propertyValue
    );

    @GetMapping("/dummy/with-expression")
    List<DummyEntity> findWithExpression(
            @RequestParam("id") Long id,
            @RequestParam("status") List<DummyEntity.Status> status,
            @RequestParam("name") String name,
            @RequestParam("labels") String labels
    );
}
