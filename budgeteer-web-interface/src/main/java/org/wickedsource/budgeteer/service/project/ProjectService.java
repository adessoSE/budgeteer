package org.wickedsource.budgeteer.service.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.persistence.user.UserEntity;
import org.wickedsource.budgeteer.persistence.user.UserRepository;
import org.wickedsource.budgeteer.service.user.UserService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectBaseDataMapper mapper;

    /**
     * Creates a new empty project with the given name.
     *
     * @param projectName name of the project.
     * @return the base data of the newly create project.
     */
    public ProjectBaseData createProject(String projectName, long initialUserId) {
        UserEntity user = userRepository.findOne(initialUserId);
        ProjectEntity project = new ProjectEntity();
        project.setName(projectName);
        project.getAuthorizedUsers().add(user);
        ProjectEntity savedProject = projectRepository.save(project);
        user.getAuthorizedProjects().add(savedProject);
        return mapper.map(savedProject);
    }

    /**
     * Returns all projects the given user has access to.
     *
     * @param userId ID of the user
     * @return list of all projects the user has access to.
     */
    public List<ProjectBaseData> getProjectsForUser(long userId) {
        UserEntity user = userRepository.findOne(userId);
        return mapper.map(user.getAuthorizedProjects());
    }

    /**
     * Deletes the given project and all its data from the database.
     *
     * @param projectId ID of the project to delete.
     */
    public void deleteProject(long projectId) {
        projectRepository.delete(projectId);
    }

}
