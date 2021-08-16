package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.port.out.ProjectHasContractsPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectHasContractsServiceTest {

    @InjectMocks private ProjectHasContractsService projectHasContractsService;
    @Mock private ProjectHasContractsPort projectHasContractsPort;

    @Test
    void shouldReturnTrueIfProjectHasContracts() {
        var projectId = 1L;
        when(projectHasContractsPort.projectHasContracts(projectId)).thenReturn(true);

        var hasProjects = projectHasContractsService.projectHasContracts(projectId);

        assertThat(hasProjects).isTrue();
    }

    @Test
    void shouldReturnFalseIfProjectHasNoContracts() {
        var projectId = 1L;
        when(projectHasContractsPort.projectHasContracts(projectId)).thenReturn(false);

        var hasProjects = projectHasContractsService.projectHasContracts(projectId);

        assertThat(hasProjects).isFalse();
    }
}
