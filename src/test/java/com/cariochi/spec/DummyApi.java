package com.cariochi.spec;

import com.cariochi.recordo.mockmvc.RecordoApiClient;
import com.cariochi.spec.app.model.DummyEntity;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
