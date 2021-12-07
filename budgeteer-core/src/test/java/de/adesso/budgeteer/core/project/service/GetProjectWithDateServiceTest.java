package de.adesso.budgeteer.core.project.service;

import de.adesso.budgeteer.common.date.DateRange;
import de.adesso.budgeteer.core.project.domain.ProjectWithDate;
import de.adesso.budgeteer.core.project.port.out.GetProjectWithDatePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProjectWithDateServiceTest {

    @InjectMocks private GetProjectWithDateService getProjectWithDateService;
    @Mock private GetProjectWithDatePort getProjectWithDatePort;

    @Test
    void shouldReturnProjectWithDate() {
        var expectedProject = new ProjectWithDate(1L, "name", new DateRange(LocalDate.now(), LocalDate.now().plusDays(5)));
        when(getProjectWithDatePort.getProjectWithDate(expectedProject.getId())).thenReturn(expectedProject);

        var returnedProject = getProjectWithDateService.getProjectWithDate(expectedProject.getId());

        assertThat(returnedProject).isEqualTo(expectedProject);
    }
}
