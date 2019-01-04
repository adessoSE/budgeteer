package de.adesso.budgeteer.web;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.record.AggregatedRecord;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.user.UserRole;
import org.wickedsource.budgeteer.web.pages.administration.Project;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:spring-web.xml", "classpath:spring-service-mock.xml"})
public abstract class AbstractWebTestTemplate {

    @Autowired
    BudgeteerAdminConsoleApplication application;

    @Autowired protected ProjectService projectServiceMock;
    @Autowired protected UserService userServiceMock;

    private static WicketTester tester;

    @BeforeEach
    public void setUp() {
        // Provide default recordServiceMock mocks for tests.
        // If required, explicit behavior can be implemented for each test class in setupTest()
        when(projectServiceMock.findProjectById(anyLong())).thenReturn(new Project(0,new Date(), new Date(),"test"));
        when(userServiceMock.getUsersInProject(1L)).thenReturn(getUsersInProject());
        if (tester == null) {
            tester = new WicketTester(application);
            loginAndSetProject();
        }
        setupTest();
    }

    /**
     * Subclasses can use this method to provide the configuration needed by
     * each test.
     */
    protected abstract void setupTest();

    private void loginAndSetProject() {
        User user = new User();
        user.setId(1L);
        user.setName("username");
        BudgeteerSession.get().login(user);
        BudgeteerSession.get().setProjectSelected(true);
    }

    private List<AggregatedRecord> getWeeklyAggregationForPerson(long personId) {
        Random random = new Random();
        List<AggregatedRecord> list = new ArrayList<AggregatedRecord>();
        for (int i = 0; i < 20; i++) {
            AggregatedRecord record = new AggregatedRecord();
            record.setAggregationPeriodTitle("Week #" + i);
            record.setAggregationPeriodStart(new Date());
            record.setAggregationPeriodStart(new Date());
            record.setBudgetBurned_net(MoneyUtil.createMoneyFromCents(random.nextInt(8000)));
            record.setBudgetPlanned_net(MoneyUtil.createMoneyFromCents(random.nextInt(4000)));
            record.setHours(random.nextDouble());
            list.add(record);
        }
        return list;
    }

    protected List<User> getUsersInProject() {
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setId(i);
            user.setName("User " + i);
            user.setMail("mail@mail.de");
            user.setGlobalRole(UserRole.USER);
            Map<Long, List<UserRole>> userprojectRoles = new HashMap<>();
            userprojectRoles.put(1L, new ArrayList<>(Collections.singletonList(UserRole.USER)));
            user.setRoles(userprojectRoles);
            users.add(user);
        }
        return users;
    }

    protected WicketTester getTester() {
        return tester;
    }

}

