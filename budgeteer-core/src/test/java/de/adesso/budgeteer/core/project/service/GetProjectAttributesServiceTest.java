package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.core.project.port.out.GetProjectAttributesPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProjectAttributesServiceTest {

    @InjectMocks private GetProjectAttributesService getProjectAttributesService;
    @Mock private GetProjectAttributesPort getProjectAttributesPort;

    @Test
    void shouldReturnProjectAttributes() {
        var projectId = 1L;
        var expectedAttributes = List.of("a", "b");
        when(getProjectAttributesPort.getProjectAttributes(projectId)).thenReturn(expectedAttributes);

        var returnedAttributes = getProjectAttributesService.getProjectAttributes(projectId);

        assertThat(returnedAttributes).isEqualTo(expectedAttributes);
    }

    @Test
    void shouldReturnEmptyListIfProjectHasNoAttributes() {
        var projectId = 1L;
        when(getProjectAttributesPort.getProjectAttributes(projectId)).thenReturn(Collections.emptyList());

        var returnedAttributes = getProjectAttributesService.getProjectAttributes(projectId);

        assertThat(returnedAttributes).isEmpty();
    }
}
