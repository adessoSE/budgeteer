package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.port.out.GetBudgetNotePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GetBudgetNoteServiceTest {

    @Mock private GetBudgetNotePort getBudgetNotePort;
    @InjectMocks private GetBudgetNoteService getBudgetNoteService;

    @Test
    void shouldReturnNote() {
        var id = 1L;
        var expected = "note";
        given(getBudgetNotePort.getBudgetNote(id)).willReturn(expected);

        var returned = getBudgetNoteService.getBudgetNote(id);

        assertThat(returned).isEqualTo(expected);
    }
}