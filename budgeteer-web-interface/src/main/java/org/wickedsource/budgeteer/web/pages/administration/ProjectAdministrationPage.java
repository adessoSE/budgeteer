package org.wickedsource.budgeteer.web.pages.administration;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
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
import org.wickedsource.budgeteer.web.components.user.UserRoleTable;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.user.login.LoginPage;
import org.wickedsource.budgeteer.web.pages.user.selectproject.SelectProjectPage;
import org.wickedsource.budgeteer.web.pages.user.selectproject.SelectProjectWithKeycloakPage;
import org.wickedsource.budgeteer.web.settings.BudgeteerSettings;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("/administration")
public class ProjectAdministrationPage extends BasePage {

    @SpringBean
    private UserService userService;

    @SpringBean
    private ProjectService projectService;

    @SpringBean
    private BudgeteerSettings settings;

    private CustomFeedbackPanel feedbackPanel = new CustomFeedbackPanel("feedback");

    public ProjectAdministrationPage() {
        add(feedbackPanel.setOutputMarkupId(true));
        add(new UserRoleTable("projectUsers", BudgeteerSession.get().getProjectId(), feedbackPanel,
                ProjectAdministrationPage.class, SelectProjectPage.class, DashboardPage.class, getPageParameters()));
        add(createDeleteProjectButton("deleteProjectButton"));
        add(createAddUserForm("addUserForm"));
        add(createEditProjectForm("projectChangeForm"));
    }

    private Form<Project> createEditProjectForm(String formId) {
        Form<Project> form = new Form<>(formId, model(from(projectService.findProjectById(BudgeteerSession.get().getProjectId()))));

        //Ajax behaviour needed to get data on form submission as we use an ajaxLink for it
        TextField<String> textField = (TextField<String>) new TextField<>("projectTitle",
                model(from(form.getModelObject()).getName())).add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {}});

        DateRange defaultDateRange = new DateRange(DateUtil.getBeginOfYear(), DateUtil.getEndOfYear());

        //Ajax behaviour needed to get data on form submission as we use an ajaxLink for it
        DateRangeInputField dateField = (DateRangeInputField) new DateRangeInputField("projectStart",
                model(from(form.getModelObject()).getDateRange()), defaultDateRange,
                DateRangeInputField.DROP_LOCATION.DOWN).add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {}});

        AjaxLink submitLink = new AjaxLink("submitLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                if(textField.getModelObject() == null || textField.getModelObject().isEmpty()){
                    feedbackPanel.error(ProjectAdministrationPage.this.getString("error.no.name"));
                }else{
                    Project ent = form.getModelObject();
                    ent.setName(textField.getModelObject());
                    ent.setDateRange(dateField.getModelObject());
                    projectService.save(ent);
                    feedbackPanel.success(ProjectAdministrationPage.this.getString("project.saved"));
                }
                target.add(feedbackPanel);
            }
        };

        form.add(submitLink);
        form.add(textField);
        form.add(dateField);
        return form;
    }

    private Form<User> createAddUserForm(String id) {
        Form<User> form = new Form<User>(id, new Model<>(new User())) {
            @Override
            protected void onSubmit() {
                userService.addUserToProject(BudgeteerSession.get().getProjectId(), getModelObject().getId());
            }
        };

        DropDownChoice<User> userChoice = new DropDownChoice<>("userChoice", form.getModel(),
                new UsersNotInProjectModel(BudgeteerSession.get().getProjectId()), new UserChoiceRenderer());
        userChoice.setRequired(true);
        form.add(userChoice);
        return form;
    }

    private Link createDeleteProjectButton(String id) {
        return new Link(id) {
            @Override
            public void onClick() {
                setResponsePage(new DeleteDialog() {
                    @Override
                    protected void onYes() {
                        projectService.deleteProject(BudgeteerSession.get().getProjectId());
                        BudgeteerSession.get().setProjectSelected(false);

                        if (settings.isKeycloakActivated()) {
                            setResponsePage(new SelectProjectWithKeycloakPage());
                        } else {
                            setResponsePage(new SelectProjectPage(LoginPage.class, new PageParameters()));
                        }
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
