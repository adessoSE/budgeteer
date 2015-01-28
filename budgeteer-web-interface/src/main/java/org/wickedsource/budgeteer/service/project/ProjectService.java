package org.wickedsource.budgeteer.service.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.persistence.budget.BudgetRepository;
import org.wickedsource.budgeteer.persistence.imports.ImportRepository;
import org.wickedsource.budgeteer.persistence.person.PersonRepository;
import org.wickedsource.budgeteer.persistence.project.ProjectEntity;
import org.wickedsource.budgeteer.persistence.project.ProjectRepository;
import org.wickedsource.budgeteer.persistence.record.PlanRecordRepository;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;
import org.wickedsource.budgeteer.persistence.user.UserEntity;
import org.wickedsource.budgeteer.persistence.user.UserRepository;

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
    private BudgetRepository budgetRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ImportRepository importRepository;

    @Autowired
    private PlanRecordRepository planRecordRepository;

    @Autowired
    private WorkRecordRepository workRecordRepository;

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
     * Returns the default project for a given user
     * @param userId ID of the user
     * @return the default project respectively null if no project is set as default
     */
    public ProjectBaseData getDefaultProjectForUser(long userId){
        UserEntity user = userRepository.findOne(userId);
        ProjectEntity defaultProject = user.getDefaultProject();
        if(defaultProject == null){
            return null;
        }
        return mapper.map(defaultProject);
    }

    /**
     * Deletes the given project and all its data from the database.
     *
     * @param projectId ID of the project to delete.
     */
    public void deleteProject(long projectId) {
        planRecordRepository.deleteByProjectId(projectId);
        workRecordRepository.deleteByProjectId(projectId);
        importRepository.deleteByProjectId(projectId);
        budgetRepository.deleteByProjectId(projectId);
        personRepository.deleteByProjectId(projectId);
        projectRepository.delete(projectId);
    }

    /**
     * Sets the given project as the default project for the given user
     * @param userId ID of the user for that the default project should be set
     * @param projectId ID of the project that should become the default-project
     */
    public void setDefaultProject(long userId, long projectId){
        ProjectEntity project = projectRepository.findOne(projectId);
        UserEntity user = userRepository.findOne(userId);
        user.setDefaultProject(project);
        userRepository.save(user);
    }
}
