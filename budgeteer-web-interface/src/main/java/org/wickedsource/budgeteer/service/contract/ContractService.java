package org.wickedsource.budgeteer.service.contract;

import de.adesso.budgeteer.persistence.budget.BudgetEntity;
import de.adesso.budgeteer.persistence.budget.BudgetRepository;
import de.adesso.budgeteer.persistence.contract.ContractEntity;
import de.adesso.budgeteer.persistence.contract.ContractFieldEntity;
import de.adesso.budgeteer.persistence.contract.ContractRepository;
import de.adesso.budgeteer.persistence.invoice.InvoiceRepository;
import de.adesso.budgeteer.persistence.project.ProjectContractField;
import de.adesso.budgeteer.persistence.project.ProjectEntity;
import de.adesso.budgeteer.persistence.project.ProjectRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.web.pages.contract.overview.table.ContractOverviewTableModel;

@Service
@Transactional
public class ContractService {

  @Autowired private ContractRepository contractRepository;

  @Autowired private ProjectRepository projectRepository;

  @Autowired private BudgetRepository budgetRepository;

  @Autowired private InvoiceRepository invoiceRepository;

  @Autowired private ContractDataMapper mapper;

  @PreAuthorize("canReadProject(#projectId)")
  public ContractOverviewTableModel getContractOverviewByProject(long projectId) {
    ContractOverviewTableModel result = new ContractOverviewTableModel();
    result.setContracts(mapper.map(contractRepository.findByProjectId(projectId)));
    return result;
  }

  @PreAuthorize("canReadContract(#contractId)")
  public ContractBaseData getContractById(long contractId) {
    return mapper.map(contractRepository.findById(contractId).orElse(null));
  }

  @PreAuthorize("canReadProject(#projectId)")
  public List<ContractBaseData> getContractsByProject(long projectId) {
    List<ContractEntity> contracts = new LinkedList<ContractEntity>();
    contracts.addAll(contractRepository.findByProjectId(projectId));
    return mapper.map(contracts);
  }

  @PreAuthorize("canReadProject(#projectId)")
  public ContractBaseData getEmptyContractModel(long projectId) {
    ProjectEntity project =
        projectRepository.findById(projectId).orElseThrow(RuntimeException::new);
    ContractBaseData model = new ContractBaseData(projectId);
    var fields = project.getContractFields();
    for (ProjectContractField field : fields) {
      model.getContractAttributes().add(new DynamicAttributeField(field.getFieldName(), ""));
    }
    return model;
  }

  public long save(ContractBaseData contractBaseData) {
    ProjectEntity project =
        projectRepository
            .findById(contractBaseData.getProjectId())
            .orElseThrow(RuntimeException::new);
    ContractEntity contractEntity = new ContractEntity();
    contractEntity.setId(0);
    contractEntity.setProject(project);

    if (contractBaseData.getContractId() != 0) {
      contractEntity =
          contractRepository
              .findById(contractBaseData.getContractId())
              .orElseThrow(RuntimeException::new);
    }
    // Update basic information
    contractEntity.setName(contractBaseData.getContractName());
    contractEntity.setBudget(contractBaseData.getBudget());
    contractEntity.setInternalNumber(contractBaseData.getInternalNumber());
    contractEntity.setStartDate(DateUtil.toLocalDate(contractBaseData.getStartDate()));
    contractEntity.setType(contractBaseData.getType());
    contractEntity.setLink(contractBaseData.getFileModel().getLink());
    contractEntity.setFileName(contractBaseData.getFileModel().getFileName());
    contractEntity.setFile(contractBaseData.getFileModel().getFile());
    if (contractBaseData.getTaxRate().compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Taxrate must be positive.");
    } else {
      contractEntity.setTaxRate(contractBaseData.getTaxRate());
    }

    // Use LinkedHashMap as backing map implementation to ensure insertion order
    Map<String, ContractFieldEntity> contractFields =
        contractEntity.getContractFields().stream()
            .collect(
                Collectors.toMap(
                    field -> field.getField().getFieldName(),
                    Function.identity(),
                    (a, b) -> a,
                    LinkedHashMap::new));

    for (DynamicAttributeField dynamicAttribute : contractBaseData.getContractAttributes()) {
      ContractFieldEntity fieldEntity = contractFields.get(dynamicAttribute.getName().trim());
      if (fieldEntity != null) {
        fieldEntity.setValue(StringUtils.trimToEmpty(dynamicAttribute.getValue()));
      } else {
        ContractFieldEntity newFieldEntity =
            createNewContractField(contractBaseData, dynamicAttribute, project);
        contractEntity.getContractFields().add(newFieldEntity);
        contractFields.put(newFieldEntity.getField().getFieldName(), newFieldEntity);
      }
    }
    contractRepository.save(contractEntity);

    return contractEntity.getId();
  }

  @PreAuthorize("canReadContract(#contractId)")
  public void deleteContract(long contractId) {
    List<BudgetEntity> budgets = budgetRepository.findByContractId(contractId);
    for (BudgetEntity budgetEntity : budgets) {
      budgetEntity.setContract(null);
    }
    budgetRepository.saveAll(budgets);

    invoiceRepository.deleteInvoiceFieldsByContractId(contractId);
    invoiceRepository.deleteInvoicesByContractId(contractId);

    contractRepository.deleteById(contractId);
  }

  @PreAuthorize("canReadContract(#contractId)")
  public List<Date> getMonthList(long contractId) {
    List<Date> months = new ArrayList<Date>();
    ContractEntity contract = contractRepository.findByIdAndFetchInvoiceFields(contractId);
    Calendar cal = Calendar.getInstance();
    cal.setTime(DateUtil.toDate(contract.getStartDate()));
    Calendar currentDate = Calendar.getInstance();
    currentDate.setTime(new Date());
    while (cal.before(currentDate)) {
      months.add(cal.getTime());
      cal.add(Calendar.MONTH, 1);
    }
    return months;
  }

  @PreAuthorize("canReadProject(#projectId)")
  public List<Date> getMonthListForProjectId(long projectId) {
    List<ContractEntity> contracts = contractRepository.findByProjectId(projectId);
    var startDate = LocalDate.now();
    for (ContractEntity contract : contracts) {
      if (contract.getStartDate().isBefore(startDate)) {
        startDate = contract.getStartDate();
      }
    }

    var currentDate = LocalDate.now();
    var months = new ArrayList<Date>();
    for (var date = startDate.withDayOfMonth(1);
        date.isBefore(currentDate);
        date = date.plusMonths(1)) {
      months.add(DateUtil.toDate(date));
    }
    return months;
  }

  @PreAuthorize("canReadProject(#projectId)")
  public boolean projectHasContracts(long projectId) {
    List<ContractEntity> contracts = contractRepository.findByProjectId(projectId);
    return (null != contracts && !contracts.isEmpty());
  }

  private ContractFieldEntity createNewContractField(
      ContractBaseData contractBaseData,
      DynamicAttributeField dynamicAttribute,
      ProjectEntity project) {
    ProjectContractField projectContractField =
        Optional.ofNullable(
                projectRepository.findContractFieldByName(
                    contractBaseData.getProjectId(), dynamicAttribute.getName().trim()))
            .orElse(new ProjectContractField(dynamicAttribute.getName().trim(), project));
    return new ContractFieldEntity(
        projectContractField, StringUtils.trimToEmpty(dynamicAttribute.getValue()));
  }
}
