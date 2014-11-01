package org.wickedsource.budgeteer.service.project;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    /**
     * Creates a new empty project with the given name.
     *
     * @param projectName name of the project.
     * @return the base data of the newly create project.
     */
    public ProjectBaseData createProject(String projectName) {
        ProjectBaseData project = new ProjectBaseData();
        project.setId(1l);
        project.setName(projectName);
        return project;
    }

    /**
     * Returns all projects the given user has access to.
     *
     * @param userId ID of the user
     * @return list of all projects the user has access to.
     */
    public List<ProjectBaseData> getProjectsForUser(long userId) {
        List<ProjectBaseData> list = new ArrayList<ProjectBaseData>();
        for (int i = 1; i <= 5; i++) {
            ProjectBaseData project = new ProjectBaseData();
            project.setId(i);
            project.setName("Project " + i);
            list.add(project);
        }
        return list;
    }

    /**
     * Deletes the given project and all its data from the database.
     *
     * @param projectId ID of the project to delete.
     */
    public void deleteProject(long projectId) {

    }

}
