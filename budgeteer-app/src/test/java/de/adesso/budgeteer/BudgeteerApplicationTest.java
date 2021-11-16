package de.adesso.budgeteer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class BudgeteerApplicationTest {

    @Test
    void contextLoads() {
        /* Verify that the context properly loads */
        Assertions.assertTrue(true);
    }

}
