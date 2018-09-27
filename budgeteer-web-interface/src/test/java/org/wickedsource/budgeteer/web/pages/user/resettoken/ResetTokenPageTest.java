package org.wickedsource.budgeteer.web.pages.user.resettoken;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.persistence.user.UserEntity;
import org.wickedsource.budgeteer.service.user.EditUserData;
import org.wickedsource.budgeteer.service.user.UserIdNotFoundException;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class ResetTokenPageTest extends AbstractWebTestTemplate {

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUpMocks() {
        try {
            when(userService.getUserById(1L)).thenReturn(new UserEntity(1L, "test", "password", "test@budgeteer.local", false, null, null));
        } catch (UserIdNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test() {
        WicketTester tester = getTester();
        PageParameters pageParameters = new PageParameters();
        pageParameters.add("userId", 1L);
        pageParameters.add("valid", 0);
        ResetTokenPage resetTokenPage = new ResetTokenPage(pageParameters);
        tester.startPage(resetTokenPage);
        tester.assertRenderedPage(ResetTokenPage.class);
    }

    @Override
    protected void setupTest() {

    }
}
