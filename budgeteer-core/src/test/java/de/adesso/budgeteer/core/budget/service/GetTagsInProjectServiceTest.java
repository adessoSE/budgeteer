package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.port.out.GetTagsInProjectPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GetTagsInProjectServiceTest {
    @Mock private GetTagsInProjectPort getTagsInProjectPort;
    @InjectMocks private GetTagsInProjectService getTagsInProjectService;

    @Test
    void shouldReturnAllTagsInProject() {
        var projectId = 1L;
        var expected = List.of("tag1", "tag2");
        given(getTagsInProjectPort.getTagsInProject(projectId)).willReturn(expected);

        var result = getTagsInProjectService.getTagsInProject(projectId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldReturnEmptyListIfNoTagsExist() {
        var projectId = 1L;
        given(getTagsInProjectPort.getTagsInProject(projectId)).willReturn(new ArrayList<>());

        var result = getTagsInProjectService.getTagsInProject(projectId);

        assertThat(result).isEmpty();
    }
}
