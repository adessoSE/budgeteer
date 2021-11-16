package de.adesso.budgeteer.core.budget.service;

import de.adesso.budgeteer.core.budget.port.out.UpdateBudgetNotePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateBudgetNoteServiceTest {

    @Mock private UpdateBudgetNotePort updateBudgetNotePort;
    @InjectMocks private UpdateBudgetNoteService updateBudgetNoteService;

    @Test
    void shouldUpdateNote() {
        var id = 1L;
        var newNote = "abc";
        doNothing().when(updateBudgetNotePort).updateBudgetNote(id, newNote);

        updateBudgetNoteService.updateBudgetNote(id, newNote);

        verify(updateBudgetNotePort).updateBudgetNote(id, newNote);
    }
}