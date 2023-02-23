package de.adesso.budgeteer.persistence.project;

import de.adesso.budgeteer.common.date.DateRange;
import de.adesso.budgeteer.core.exception.NotFoundException;
import de.adesso.budgeteer.core.project.domain.Project;
import de.adesso.budgeteer.core.project.domain.ProjectWithDate;
import de.adesso.budgeteer.core.project.port.out.*;
import de.adesso.budgeteer.core.project.port.out.RemoveUserFromProjectPort;
import de.adesso.budgeteer.core.user.domain.User;
import de.adesso.budgeteer.persistence.budget.BudgetRepository;
import de.adesso.budgeteer.persistence.contract.ContractRepository;
import de.adesso.budgeteer.persistence.imports.ImportRepository;
import de.adesso.budgeteer.persistence.invoice.InvoiceRepository;
import de.adesso.budgeteer.persistence.person.DailyRateRepository;
import de.adesso.budgeteer.persistence.person.PersonRepository;
import de.adesso.budgeteer.persistence.record.PlanRecordRepository;
import de.adesso.budgeteer.persistence.record.WorkRecordRepository;
import de.adesso.budgeteer.persistence.user.UserEntity;
import de.adesso.budgeteer.persistence.user.UserRepository;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectAdapter
    implements GetProjectPort,
        GetDefaultProjectPort,
        GetUsersProjectsPort,
        UpdateDefaultProjectPort,
        ProjectExistsWithNamePort,
        CreateProjectPort,
        GetProjectWithDatePort,
        UpdateProjectPort,
        DeleteProjectPort,
        AddUserToProjectPort,
        RemoveUserFromProjectPort,
        ProjectExistsWithIdPort,
        GetProjectAttributesPort,
        ProjectHasContractsPort,
        UserHasAccessToProjectPort {

  private final ProjectMapper projectMapper;
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;
  private final DailyRateRepository dailyRateRepository;
  private final PlanRecordRepository planRecordRepository;
  private final WorkRecordRepository workRecordRepository;
  private final ImportRepository importRepository;
  private final BudgetRepository budgetRepository;
  private final InvoiceRepository invoiceRepository;
  private final ContractRepository contractRepository;
  private final PersonRepository personRepository;

  @Override
  public Project getProject(long projectId) {
    return projectMapper.mapToDomain(projectRepository.findById(projectId).orElseThrow());
  }

  @Override
  public Optional<Project> getDefaultProject(long userId) {
    return projectRepository.getDefaultProject(userId).map(projectMapper::mapToDomain);
  }

  @Override
  public List<Project> getUsersProjects(long userId) {
    return projectMapper.mapToDomain(projectRepository.getUsersProjects(userId));
  }

  @Override
  @Transactional
  public Project updateDefaultProject(long userId, long projectId) {
    projectRepository.updateDefaultProject(userId, projectId);
    return projectRepository
        .findById(projectId)
        .map(projectMapper::mapToDomain)
        .orElseThrow(IllegalArgumentException::new);
  }

  @Override
  public boolean projectExistsWithName(String name) {
    return projectRepository.existsByName(name);
  }

  @Override
  @Transactional
  public Project createProject(long userId, String name) {
    var projectEntity = new ProjectEntity();
    var userEntity = userRepository.findById(userId).orElseThrow();
    projectEntity.setName(name);
    projectEntity.setAuthorizedUsers(List.of(userEntity));
    return projectMapper.mapToDomain(projectRepository.save(projectEntity));
  }

  @Override
  public ProjectWithDate getProjectWithDate(long projectId) {
    return projectMapper.mapToProjectWithDate(projectRepository.findById(projectId).orElseThrow());
  }

  @Override
  @Transactional
  public Project updateProject(long projectId, String name, DateRange dateRange) {
    var projectEntity = projectRepository.findById(projectId).orElseThrow();
    projectEntity.setName(name);
    projectEntity.setProjectStart(Date.valueOf(dateRange.getStartDate()));
    projectEntity.setProjectEnd(Date.valueOf(dateRange.getEndDate()));
    return projectMapper.mapToDomain(projectRepository.save(projectEntity));
  }

  @Override
  @Transactional
  public void deleteProject(long projectId) {
    dailyRateRepository.deleteByProjectId(projectId);
    planRecordRepository.deleteByImportAndProjectId(projectId);
    workRecordRepository.deleteByImportAndProjectId(projectId);
    importRepository.deleteByProjectId(projectId);
    budgetRepository.deleteByProjectId(projectId);
    personRepository.deleteByProjectId(projectId);
    invoiceRepository.deleteInvoiceFieldByProjectId(projectId);
    invoiceRepository.deleteContractInvoiceFieldByProject(projectId);
    invoiceRepository.deleteByProjectId(projectId);
    contractRepository.deleteContractFieldByProjectId(projectId);
    contractRepository.deleteByProjectId(projectId);
    var projectEntity = projectRepository.findById(projectId).orElse(null);
    if (projectEntity != null) {
      List<UserEntity> userList = projectEntity.getAuthorizedUsers();
      if (userList != null) {
        for (UserEntity u : userList) {
          if (u.getDefaultProject() != null && u.getDefaultProject().getId() == projectId) {
            u.setDefaultProject(null);
            userRepository.save(u);
          }
        }
      }
    }
    projectRepository.deleteById(projectId);
  }

  @Override
  @Transactional
  public void addUserToProject(long userId, long projectId) throws NotFoundException {
    var projectEntity =
        projectRepository
            .findById(projectId)
            .orElseThrow(() -> new NotFoundException(Project.class));
    var userEntity =
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class));
    projectEntity.getAuthorizedUsers().add(userEntity);
    projectRepository.save(projectEntity);
  }

  @Override
  @Transactional
  public void removeUserFromProject(long userId, long projectId) throws NotFoundException {
    var projectEntity =
        projectRepository
            .findById(projectId)
            .orElseThrow(() -> new NotFoundException(Project.class));
    var userEntity =
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException(User.class));
    projectEntity.getAuthorizedUsers().remove(userEntity);
    projectRepository.save(projectEntity);
  }

  @Override
  public boolean projectExistsWithId(long projectId) {
    return projectRepository.existsById(projectId);
  }

  @Override
  public List<String> getProjectAttributes(long projectId) {
    return Optional.ofNullable(projectRepository.findByIdAndFetchContractFields(projectId))
        .map(ProjectEntity::getContractFields)
        .orElseGet(ArrayList::new)
        .stream()
        .map(ProjectContractField::getFieldName)
        .collect(Collectors.toList());
  }

  @Override
  public boolean projectHasContracts(long projectId) {
    return projectRepository.hasContracts(projectId);
  }

  @Override
  public boolean userHasAccessToProject(String username, long projectId) {
    return projectRepository.userInAuthorizedUsers(username, projectId);
  }
}
