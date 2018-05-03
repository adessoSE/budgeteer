package org.wickedsource.budgeteer.service.user;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PasswordHasherTest {

    @Test
    void test(){
        PasswordHasher hasher = new PasswordHasher();
        String hash = hasher.hash("Test");
        Assertions.assertEquals("c6ee9e33cf5c6715a1d148fd73f7318884b41adcb916021e2bc0e800a5c5dd97f5142178f6ae88c8fdd98e1afb0ce4c8d2c54b5f37b30b7da1997bb33b0b8a31", hash);

        String hash2 = hasher.hash("Test123");
        Assertions.assertNotEquals(hash2, hash);
    }

}
