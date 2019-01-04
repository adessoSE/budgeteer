package org.wickedsource.budgeteer.service.project;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.user.UserEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

public class ProjectMapperTest {

    @Test
    void testMapping(){

        //Set up
        ProjectEntity testEntity = new ProjectEntity();
        testEntity.setId(1L);
        testEntity.setName("testProject");
        testEntity.setProjectStart(new Date());
        testEntity.setProjectEnd(new Date());
        testEntity.setAuthorizedUsers(Collections.singletonList(new UserEntity()));
        testEntity.setContractFields(new HashSet<>());
        ProjectBaseDataMapper projectBaseDataMapper = new ProjectBaseDataMapper();

        //Test
        ProjectBaseData result = projectBaseDataMapper.map(testEntity);
        Assertions.assertEquals(testEntity.getId(), result.getId());
        Assertions.assertEquals(testEntity.getName(), result.getName());
    }
}
