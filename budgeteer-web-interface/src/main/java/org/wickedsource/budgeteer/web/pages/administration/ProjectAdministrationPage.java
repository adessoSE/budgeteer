package org.wickedsource.budgeteer.web.pages.administration;

import de.adesso.budgeteer.core.project.port.in.AddUserToProjectUseCase;
import de.adesso.budgeteer.core.project.port.in.RemoveUserFromProjectUseCase;
import de.adesso.budgeteer.core.user.port.in.GetUsersInProjectUseCase;
import de.adesso.budgeteer.core.user.port.in.GetUsersNotInProjectUseCase;
import java.util.List;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.user.UserModel;
import org.wickedsource.budgeteer.service.user.UserModelMapper;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.daterange.DateRangeInputField;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.user.login.LoginPage;
import org.wickedsource.budgeteer.web.pages.user.selectproject.SelectProjectPage;

@Mount("/administration")
public class ProjectAdministrationPage extends BasePage {
  @SpringBean private GetUsersInProjectUseCase getUsersInProjectUseCase;
  @SpringBean private GetUsersNotInProjectUseCase getUsersNotInProjectUseCase;
  @SpringBean private ProjectService projectService;
  @SpringBean private RemoveUserFromProjectUseCase removeUserFromProjectUseCase;
  @SpringBean private AddUserToProjectUseCase addUserToProjectUseCase;
  @SpringBean private UserModelMapper userModelMapper;

  public ProjectAdministrationPage() {
    add(new CustomFeedbackPanel("feedback"));
    add(
        createUserList(
            "userList",
            () ->
                userModelMapper.map(
                    getUsersInProjectUseCase.getUsersInProject(
                        BudgeteerSession.get().getProjectId()))));
    add(createDeleteProjectButton("deleteProjectButton"));
    add(createAddUserForm("addUserForm"));
    add(createEditProjectForm("projectChangeForm"));
  }

  private Form<Project> createEditProjectForm(String formId) {
    var form =
        new Form<>(
            formId,
            Model.of(projectService.findProjectById(BudgeteerSession.get().getProjectId()))) {
          @Override
          protected void onSubmit() {
            super.onSubmit();
            if (getModelObject().getName() == null) {
              error(getString("error.no.name"));
            } else {
              Project ent = getModelObject();
              projectService.save(ent);
              success(getString("project.saved"));
            }
          }
        };
    form.add(
        new TextField<>(
            "projectTitle", LambdaModel.of(form.getModel(), Project::getName, Project::setName)));
    DateRange defaultDateRange = new DateRange(DateUtil.getBeginOfYear(), DateUtil.getEndOfYear());
    form.add(
        new DateRangeInputField(
            "projectStart",
            LambdaModel.of(form.getModel(), Project::getDateRange, Project::setDateRange),
            defaultDateRange,
            DateRangeInputField.DROP_LOCATION.DOWN));
    return form;
  }

  private ListView<UserModel> createUserList(String id, IModel<List<UserModel>> model) {
    UserModel thisUser = BudgeteerSession.get().getLoggedInUser();
    return new ListView<>(id, model) {
      @Override
      protected void populateItem(final ListItem<UserModel> item) {
        item.add(new Label("username", item.getModel().map(UserModel::getName)));
        var deleteButton =
            new Link<>("deleteButton") {
              @Override
              public void onClick() {

                setResponsePage(
                    new DeleteDialog() {
                      @Override
                      protected void onYes() {
                        removeUserFromProjectUseCase.removeUserFromProject(
                            item.getModelObject().getId(), BudgeteerSession.get().getProjectId());
                        setResponsePage(ProjectAdministrationPage.class, getPageParameters());
                      }

                      @Override
                      protected void onNo() {
                        setResponsePage(ProjectAdministrationPage.class, getPageParameters());
                      }

                      @Override
                      protected String confirmationText() {
                        return ProjectAdministrationPage.this.getString(
                            "delete.person.confirmation");
                      }
                    });
              }
            };
        // a user may not delete herself/himself
        if (item.getModelObject().equals(thisUser)) {
          deleteButton.setVisible(false);
        }
        item.add(deleteButton);
      }

      @Override
      protected ListItem<UserModel> newItem(int index, IModel<UserModel> itemModel) {
        return super.newItem(index, new ClassAwareWrappingModel<>(itemModel, UserModel.class));
      }
    };
  }

  private Form<UserModel> createAddUserForm(String id) {
    var form =
        new Form<>(id, new Model<>(new UserModel())) {
          @Override
          protected void onSubmit() {
            addUserToProjectUseCase.addUserToProject(
                getModelObject().getId(), BudgeteerSession.get().getProjectId());
          }
        };

    var userChoice =
        new DropDownChoice<>(
            "userChoice",
            form.getModel(),
            () ->
                userModelMapper.map(
                    getUsersNotInProjectUseCase.getUsersNotInProject(
                        BudgeteerSession.get().getProjectId())),
            new UserChoiceRenderer());
    userChoice.setRequired(true);
    form.add(userChoice);
    return form;
  }

  private Link<Void> createDeleteProjectButton(String id) {
    return new Link<>(id) {
      @Override
      public void onClick() {
        setResponsePage(
            new DeleteDialog() {
              @Override
              protected void onYes() {
                projectService.deleteProject(BudgeteerSession.get().getProjectId());
                BudgeteerSession.get().setProjectSelected(false);

                setResponsePage(new SelectProjectPage(LoginPage.class, new PageParameters()));
              }

              @Override
              protected void onNo() {
                setResponsePage(ProjectAdministrationPage.class, getPageParameters());
              }

              @Override
              protected String confirmationText() {
                return ProjectAdministrationPage.this.getString("delete.project.confirmation");
              }
            });
      }
    };
  }

  @Override
  protected BreadcrumbsModel getBreadcrumbsModel() {
    return new BreadcrumbsModel(DashboardPage.class, ProjectAdministrationPage.class);
  }
}
