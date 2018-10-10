package org.wickedsource.budgeteer.web.pages.user.edituser;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.user.EditUserData;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.user.edit.EditUserPage;

import static org.mockito.Mockito.when;

public class EditUserPageTest extends AbstractWebTestTemplate {

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUpMocks() {
        when(userService.loadUserToEdit(1L)).thenReturn(new EditUserData(1L, "test", "test@budgeteer.local", "password", null, null));
    }

    @Test
    void test() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        tester.assertRenderedPage(EditUserPage.class);
    }

    @Override
    protected void setupTest() {
    }
}
