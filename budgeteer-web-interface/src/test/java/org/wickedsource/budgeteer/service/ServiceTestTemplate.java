package org.wickedsource.budgeteer.service;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-service.xml", "classpath:spring-repository-mock.xml"})
public abstract class ServiceTestTemplate {

    @Autowired
    private ApplicationContext context;

    @Before
    public void resetMocks() {
        for (String name : context.getBeanDefinitionNames()) {
            Object bean = context.getBean(name);
            if (new MockUtil().isMock(bean)) {
                Mockito.reset(bean);
            }
        }
    }

}
