package org.wickedsource.budgeteer.web.pages.user.selectproject;

import java.io.Serializable;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.project.ProjectBaseData;
import org.wickedsource.budgeteer.service.project.ProjectNameAlreadyInUseException;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.user.login.LoginPage;

@Mount("/selectProject")
public class SelectProjectPage extends DialogPageWithBacklink {

  @SpringBean private ProjectService projectService;

  private final CustomFeedbackPanel feedbackPanel;

  public SelectProjectPage() {
    this(LoginPage.class, new PageParameters());
  }

  public SelectProjectPage(
      Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
    super(backlinkPage, backlinkParameters);

    add(createBacklink("backlink1"));
    add(createLogoutlink());
    add(createNewProjectForm());
    add(createChooseProjectForm());
    feedbackPanel = new CustomFeedbackPanel("feedback");
    feedbackPanel.setOutputMarkupId(true);
    add(feedbackPanel);
  }

  /**
   * Construct a new SelectProjectPage and show a error message.
   *
   * @param propertyKey A key to a String property of the error message you want to display.
   * @see #error(Serializable)
   */
  public SelectProjectPage(String propertyKey) {
    this();
    if (propertyKey != null && !propertyKey.isEmpty()) {
      this.error(this.getString(propertyKey));
    }
  }

  private Form<String> createNewProjectForm() {
    var form =
        new Form<>("newProjectForm", Model.of("")) {
          @Override
          protected void onSubmit() {
            try {
              var project =
                  projectService.createProject(
                      getModelObject(), BudgeteerSession.get().getLoggedInUser().getId());
              BudgeteerSession.get().setProjectId(project.getId());
              setResponsePage(DashboardPage.class);
            } catch (ProjectNameAlreadyInUseException exception) {
              this.error(getString("newProjectForm.projectName.AlreadyInUse"));
            }
          }
        };
    form.add(new RequiredTextField<>("projectName", form.getModel()));
    return form;
  }

  private Form<ProjectBaseData> createChooseProjectForm() {
    var form =
        new Form<>(
            "chooseProjectForm",
            () ->
                projectService.getDefaultProjectForUser(
                    BudgeteerSession.get().getLoggedInUser().getId()));
    form.setOutputMarkupId(true);
    var choice =
        new DropDownChoice<>(
                "projectChoice",
                form.getModel(),
                () ->
                    projectService.getProjectsForUser(
                        BudgeteerSession.get().getLoggedInUser().getId()),
                new ProjectChoiceRenderer())
            .setRequired(true);
    choice.add(
        new OnChangeAjaxBehavior() {
          @Override
          protected void onUpdate(AjaxRequestTarget target) {
            target.add(feedbackPanel);
          }
        });
    form.add(choice);

    var markProjectAsDefault =
        new AjaxSubmitLink("markProject") {
          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            try {
              projectService.setDefaultProject(
                  BudgeteerSession.get().getLoggedInUser().getId(), form.getModelObject().getId());
              info(getString("chooseProjectForm.defaultProject.successful"));
            } catch (Exception e) {
              error(getString("chooseProjectForm.defaultProject.failed"));
            }
            target.add(form, feedbackPanel);
          }
        };

    var goButton =
        new AjaxSubmitLink("goButton") {
          @Override
          protected void onSubmit(AjaxRequestTarget target) {
            BudgeteerSession.get().setProjectId(form.getModelObject().getId());
            setResponsePage(DashboardPage.class);
          }
        };
    form.add(markProjectAsDefault);
    form.add(goButton);
    return form;
  }

  private Link<Void> createLogoutlink() {
    return new Link<>("logoutLink") {
      @Override
      public void onClick() {
        BudgeteerSession.get().logout();
        setResponsePage(LoginPage.class);
      }
    };
  }
}
