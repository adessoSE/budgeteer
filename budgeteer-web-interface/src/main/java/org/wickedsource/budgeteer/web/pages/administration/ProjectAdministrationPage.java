package org.wickedsource.budgeteer.web.pages.administration;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.BudgeteerSettings;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.daterange.DateRangeInputField;
import org.wickedsource.budgeteer.web.components.multiselect.MultiselectBehavior;
import org.wickedsource.budgeteer.web.components.user.UserRole;
import org.wickedsource.budgeteer.web.pages.base.basepage.BasePage;
import org.wickedsource.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.user.login.LoginPage;
import org.wickedsource.budgeteer.web.pages.user.selectproject.SelectProjectPage;
import org.wickedsource.budgeteer.web.pages.user.selectproject.SelectProjectWithKeycloakPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

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

    public ProjectAdministrationPage() {
        add(new CustomFeedbackPanel("feedback"));
        add(createUserList("userList", new UsersInProjectModel(BudgeteerSession.get().getProjectId())));
        add(createDeleteProjectButton("deleteProjectButton"));
        add(createAddUserForm("addUserForm"));
        add(createEditProjectForm("projectChangeForm"));
    }

    private Form<Project> createEditProjectForm(String formId) {
        Form<Project> form = new Form<Project>(formId, model(from(projectService.findProjectById(BudgeteerSession.get().getProjectId())))) {
            @Override
            protected void onSubmit() {
                super.onSubmit();
                if(getModelObject().getName() == null){
                    error(getString("error.no.name"));
                }else {
                    Project ent = getModelObject();
                    projectService.save(ent);
                    success(getString("project.saved"));
                }
            }
        };
        form.add(new TextField<>("projectTitle", model(from(form.getModelObject()).getName())));
        DateRange defaultDateRange = new DateRange(DateUtil.getBeginOfYear(), DateUtil.getEndOfYear());
        form.add(new DateRangeInputField("projectStart", model(from(form.getModelObject()).getDateRange()), defaultDateRange, DateRangeInputField.DROP_LOCATION.DOWN));
        return form;
    }

    private ListView<User> createUserList(String id, IModel<List<User>> model) {
        User thisUser = BudgeteerSession.get().getLoggedInUser();
        long projectID = BudgeteerSession.get().getProjectId();
        return new ListView<User>(id, model) {

            @Override
            protected void populateItem(final ListItem<User> item) {
                item.add(new Label("username", model(from(item.getModel()).getName())));
                Link deleteButton = new Link("deleteButton") {
                    @Override
                    public void onClick() {
                        setResponsePage(new DeleteDialog(() -> {
                            if(thisUser.getId() == item.getModelObject().getId()){
                                userService.removeUserFromProject(projectID, item.getModelObject().getId());
                                BudgeteerSession.get().logout(); // Log the user out if he deletes himself
                            }else{
                                userService.removeUserFromProject(projectID, item.getModelObject().getId());
                            }
                            setResponsePage(ProjectAdministrationPage.class, getPageParameters());
                            return null;
                        }, () -> {
                            setResponsePage(ProjectAdministrationPage.class, getPageParameters());
                            return null;
                        }));
                    }
                };

                List<String> choices = new ArrayList<>();
                for(UserRole e : UserRole.values()){
                    choices.add(e.toString());
                }
                ListMultipleChoice<String> makeAdminList = new ListMultipleChoice<>("roleDropdown", new Model<>(
                        new ArrayList<>(item.getModelObject().getRoles().get(projectID))), choices);
                HashMap<String, String> options = new HashMap<>();
                options.clear();
                options.put("buttonWidth","'120px'");
                makeAdminList.add(new MultiselectBehavior(options));
                makeAdminList.add(new AjaxFormComponentUpdatingBehavior("change") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        if(!makeAdminList.getModelObject().isEmpty()) {

                            //Check if the user is losing admin privileges and ask if they are sure
                            if(item.getModelObject().getId() == thisUser.getId() &&
                                    !makeAdminList.getModelObject().contains("admin") && item.getModelObject().getRoles().get(projectID).contains("admin")){
                                setResponsePage(new DeleteDialog(() -> {
                                    userService.removeAllRolesFromUser(item.getModelObject().getId(), projectID);
                                    for (String e : makeAdminList.getModelObject()) {
                                        userService.addRoleToUser(item.getModelObject().getId(), projectID, UserRole.getEnum(e));
                                    }
                                    if(item.getModelObject().getId() == thisUser.getId()){
                                        BudgeteerSession.get().setLoggedInUser(item.getModelObject());
                                    }
                                    setResponsePage(DashboardPage.class);
                                    return null;
                                }, () -> {
                                    setResponsePage(ProjectAdministrationPage.class);
                                    return null;
                                }));
                                return;
                            }

                            userService.removeAllRolesFromUser(item.getModelObject().getId(), projectID);
                            for (String e : makeAdminList.getModelObject()) {
                                userService.addRoleToUser(item.getModelObject().getId(), projectID, UserRole.getEnum(e));
                            }
                        }
                        if(item.getModelObject().getId() == thisUser.getId()){
                            BudgeteerSession.get().setLoggedInUser(item.getModelObject());
                        }
                        target.add(ProjectAdministrationPage.this);
                    }
                });


                // a user may not delete herself/himself unless another admin is present
                if (item.getModelObject().getId() == thisUser.getId()){
                    List<User> usersInProjects = userService.getUsersInProject(projectID);
                    deleteButton.setVisible(false);
                    makeAdminList.setVisible(false);
                    for(User e : usersInProjects){
                        if(e.getId() != thisUser.getId() && e.getRoles().get(projectID).contains("admin")){
                            deleteButton.setVisible(true);
                            makeAdminList.setVisible(true);
                            break;
                        }
                    }
                }
                item.add(deleteButton);
                item.add(makeAdminList);
                item.setOutputMarkupId(true);
            }

            @Override
            protected ListItem<User> newItem(int index, IModel<User> itemModel) {
                return super.newItem(index, new ClassAwareWrappingModel<>(itemModel, User.class));
            }
        };
    }

    private Form<User> createAddUserForm(String id) {
        Form<User> form = new Form<User>(id, new Model<>(new User())) {
            @Override
            protected void onSubmit() {
                userService.addUserToProject(BudgeteerSession.get().getProjectId(), getModelObject().getId());
            }
        };

        DropDownChoice<User> userChoice = new DropDownChoice<>("userChoice", form.getModel(), new UsersNotInProjectModel(BudgeteerSession.get().getProjectId()), new UserChoiceRenderer());
        userChoice.setRequired(true);
        form.add(userChoice);
        return form;
    }

    private Link createDeleteProjectButton(String id) {
        return new Link(id) {
            @Override
            public void onClick() {
                setResponsePage(new DeleteDialog(() -> {
                    projectService.deleteProject(BudgeteerSession.get().getProjectId());
                    BudgeteerSession.get().setProjectSelected(false);

                    if (settings.isKeycloakActivated()) {
                        setResponsePage(new SelectProjectWithKeycloakPage());
                    } else {
                        setResponsePage(new SelectProjectPage(LoginPage.class, new PageParameters()));
                    }
                    return null;
                }, () -> {
                    setResponsePage(ProjectAdministrationPage.class, getPageParameters());
                    return null;
                }));
            }
        };
    }

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(DashboardPage.class, ProjectAdministrationPage.class);
    }

}
