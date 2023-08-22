package org.wickedsource.budgeteer.web.pages.administration;

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
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
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

  @SpringBean private UserService userService;

  @SpringBean private ProjectService projectService;

  public ProjectAdministrationPage() {
    add(new CustomFeedbackPanel("feedback"));
    add(createUserList(() -> userService.getUsersInProject(BudgeteerSession.get().getProjectId())));
    add(createDeleteProjectButton());
    add(createAddUserForm());
    add(createEditProjectForm());
  }

  private Form<Project> createEditProjectForm() {
    var form =
        new Form<>(
            "projectChangeForm",
            Model.of(projectService.findProjectById(BudgeteerSession.get().getProjectId()))) {
          @Override
          protected void onSubmit() {
            super.onSubmit();
            if (getModelObject().getName() == null) {
              error(getString("error.no.name"));
              return;
            }
            var project = getModelObject();
            projectService.save(project);
            success(getString("project.saved"));
          }
        };
    form.add(
        new TextField<>(
            "projectTitle", LambdaModel.of(form.getModel(), Project::getName, Project::setName)));
    var defaultDateRange = new DateRange(DateUtil.getBeginOfYear(), DateUtil.getEndOfYear());
    form.add(
        new DateRangeInputField(
            "projectStart",
            LambdaModel.of(form.getModel(), Project::getDateRange, Project::setDateRange),
            defaultDateRange,
            DateRangeInputField.DROP_LOCATION.DOWN));
    return form;
  }

  private ListView<User> createUserList(IModel<List<User>> model) {
    var currentUser = BudgeteerSession.get().getLoggedInUser();
    return new ListView<>("userList", model) {
      @Override
      protected void populateItem(final ListItem<User> item) {
        item.add(new Label("username", item.getModel().map(User::getName)));
        var deleteButton =
            new Link<>("deleteButton") {
              @Override
              public void onClick() {
                setResponsePage(
                    new DeleteDialog() {
                      @Override
                      protected void onYes() {
                        userService.removeUserFromProject(
                            BudgeteerSession.get().getProjectId(), item.getModelObject().getId());
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
        if (item.getModelObject().equals(currentUser)) {
          deleteButton.setVisible(false);
        }
        item.add(deleteButton);
      }
    };
  }

  private Form<User> createAddUserForm() {
    var form =
        new Form<>("addUserForm", new Model<>(new User())) {
          @Override
          protected void onSubmit() {
            userService.addUserToProject(
                BudgeteerSession.get().getProjectId(), getModelObject().getId());
          }
        };

    var userChoice =
        new DropDownChoice<>(
                "userChoice",
                form.getModel(),
                () -> userService.getUsersNotInProject(BudgeteerSession.get().getProjectId()),
                new UserChoiceRenderer())
            .setRequired(true);
    form.add(userChoice);
    return form;
  }

  private Link<Void> createDeleteProjectButton() {
    return new Link<>("deleteProjectButton") {
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
