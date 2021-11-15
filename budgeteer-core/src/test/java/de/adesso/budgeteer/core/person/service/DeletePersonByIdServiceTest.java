package de.adesso.budgeteer.core.person.service;

import de.adesso.budgeteer.core.person.port.out.DeletePersonByIdPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeletePersonByIdServiceTest {
    @Mock
    private DeletePersonByIdPort deletePersonByIdPort;
    @InjectMocks
    private DeletePersonByIdService deletePersonByIdService;

    @Test
    void shouldDeletePerson() {
        var personId = 1L;
        doNothing().when(deletePersonByIdPort).deletePersonById(personId);

        deletePersonByIdService.deletePersonById(1L);

        verify(deletePersonByIdPort).deletePersonById(personId);
    }
}