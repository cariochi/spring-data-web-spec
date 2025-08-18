package com.example.app.tests;

import com.cariochi.recordo.core.Recordo;
import com.cariochi.recordo.core.RecordoExtension;
import com.example.app.main.TestApp;
import com.example.app.main.model.DummyEntity;
import com.example.app.main.model.Organization;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static com.example.app.main.model.DummyEntity.Status.FAILED;
import static com.example.app.main.model.DummyEntity.Status.STOPPED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@SpringBootTest(classes = TestApp.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(RecordoExtension.class)
@Sql(scripts = "classpath:dummy-data.sql", executionPhase = BEFORE_TEST_CLASS)
class DummyControllerTest {

    private final DummyApi api = Recordo.create(DummyApi.class);

    @Test
    void should_find_all_by_status() {
        List<DummyEntity> entities = api.findAll(null, List.of(STOPPED, FAILED), null, null, null);
        assertThat(entities).hasSize(3)
                .extracting(DummyEntity::getStatus)
                .containsOnly(STOPPED, FAILED);
    }

    @Test
    void should_find_all_by_id() {
        List<DummyEntity> entities = api.findAll(102L, List.of(STOPPED, FAILED), null, null, null);
        assertThat(entities).hasSize(1)
                .extracting(DummyEntity::getId)
                .containsOnly(102L);
    }

    @Test
    void should_find_all_by_organization_id() {
        List<DummyEntity> entities = api.findAll(null, List.of(STOPPED, FAILED), null, 1L, null);
        assertThat(entities).hasSize(2)
                .extracting(DummyEntity::getOrganization)
                .extracting(Organization::getId)
                .containsOnly(1L);
    }

    @Test
    void should_find_all_by_name() {
        List<DummyEntity> entities = api.findAll(null, List.of(STOPPED, FAILED), "Test Project Alpha", null, null);
        assertThat(entities).hasSize(2)
                .extracting(DummyEntity::getName)
                .containsExactlyInAnyOrder("Test Project Alpha 1", "Test Project Alpha 2");
    }

    @Test
    void should_find_in_organization() {
        List<DummyEntity> entities = api.findInOrganization(1L, null, null, null, null);
        assertThat(entities).hasSize(2)
                .extracting(DummyEntity::getOrganization)
                .extracting(Organization::getId)
                .containsOnly(1L);
    }

    @Test
    void should_find_all_in_region() {
        List<DummyEntity> entities = api.findAll(null, List.of(STOPPED, FAILED), null, null, "US");
        assertThat(entities).hasSize(2)
                .extracting(DummyEntity::getStatus)
                .containsOnly(STOPPED, FAILED);
    }

    @Test
    void should_find_by_labels() {
        final List<DummyEntity> entities = api.findByLabels("urgent,beta", "urgent,beta");
        assertThat(entities).hasSize(2)
                .extracting(DummyEntity::getId)
                .containsOnly(101L, 103L);
    }

    @Test
    void should_find_by_properties() {
        final List<DummyEntity> entities = api.findByProperty("color", "red,blue");
        assertThat(entities).hasSize(2)
                .extracting(DummyEntity::getId)
                .containsOnly(101L, 103L);
    }
}
