package de.adesso.budgeteer.web.pages.administration;

import de.adesso.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import de.adesso.budgeteer.web.pages.base.basepage.BasePage;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import de.adesso.budgeteer.web.pages.base.delete.DeleteDialog;
import de.adesso.budgeteer.web.pages.user.login.LoginPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.project.ProjectBaseData;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.multiselect.MultiselectBehavior;
import org.wickedsource.budgeteer.web.settings.BudgeteerSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("/administartion-console")
public class BudgeteerAdministrationOverview extends BasePage {

    @SpringBean
    private UserService userService;

    @SpringBean
    private ProjectService projectService;

    @SpringBean
    private BudgeteerSettings settings;

    public BudgeteerAdministrationOverview() {
        add(new CustomFeedbackPanel("feedback"));
        add(createUserList("userList", new UsersInProjectModel(BudgeteerSession.get().getProjectId())));
        add(createProjectList("projectList", new ListModel<>(projectService.getAllProjects())));
    }

    private ListView<User> createUserList(String id, IModel<List<User>> model) {
        User thisUser = BudgeteerSession.get().getLoggedInUser();
        return new ListView<User>(id, model) {

            @Override
            protected void populateItem(final ListItem<User> item) {
                item.add(new Label("username", model(from(item.getModel()).getName())));
                Link deleteUserButton = new Link("deleteUserButton") {
                    @Override
                    public void onClick() {
                        setResponsePage(new org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog() {
                            @Override
                            protected void onYes() {
                                if(thisUser.getId() == item.getModelObject().getId()){
                                    userService.removeUser(item.getModelObject().getId());
                                    BudgeteerSession.get().logout(); // Log the user out if he deletes himself
                                }else{
                                    userService.removeUser(item.getModelObject().getId());
                                }
                                setResponsePage(BudgeteerAdministrationOverview.class, getPageParameters());
                            }

                            @Override
                            protected void onNo() {
                                setResponsePage(BudgeteerAdministrationOverview.class, getPageParameters());
                            }

                            @Override
                            protected String confirmationText() {
                                return "Are you sure you want to remove this user from the project?";
                            }
                        });
                    }
                };

                List<String> choices = new ArrayList<>();
                choices.add("admin");
                choices.add("user");
                DropDownChoice<String> makeAdminList = new DropDownChoice<>(
                        "roleDropdown", new Model<>(item.getModelObject().getGlobalRole()), choices);
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
                                    !makeAdminList.getModelObject().contains("admin")
                                    && item.getModelObject().getGlobalRole().equals("admin")){
                                setResponsePage(
                                        new org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog() {
                                            @Override
                                            protected void onYes() {
                                                userService.setGlobalRoleForUser(item.getModelObject().getId(),makeAdminList.getModelObject());
                                                if(item.getModelObject().getId() == thisUser.getId()){
                                                    BudgeteerSession.get().setLoggedInUser(item.getModelObject());
                                                }
                                                setResponsePage(LoginPage.class);
                                            }

                                            @Override
                                            protected void onNo() {
                                                setResponsePage(BudgeteerAdministrationOverview.class);
                                            }

                                            @Override
                                            protected String confirmationText() {
                                                return "You will lose admin privileges and access to the administration menu if you proceed, are you sure?";
                                            }
                                        });
                            }else {
                                userService.setGlobalRoleForUser(item.getModelObject().getId(), makeAdminList.getModelObject());
                            }
                        }
                        if(item.getModelObject().getId() == thisUser.getId()){
                            BudgeteerSession.get().setLoggedInUser(item.getModelObject());
                        }
                        target.add(BudgeteerAdministrationOverview.this);
                    }
                });


                // a user may not delete herself/himself unless another admin is present
                if (item.getModelObject().getId() == thisUser.getId()){
                    List<User> allUsers = userService.getAllUsers();
                    deleteUserButton.setVisible(false);
                    makeAdminList.setVisible(false);
                    for(User e : allUsers){
                        if(e.getId() != thisUser.getId() && e.getGlobalRole().equals("admin")){
                            deleteUserButton.setVisible(true);
                            makeAdminList.setVisible(true);
                            break;
                        }
                    }
                }
                item.add(deleteUserButton);
                item.add(makeAdminList);
            }

            @Override
            protected ListItem<User> newItem(int index, IModel<User> itemModel) {
                return super.newItem(index, new ClassAwareWrappingModel<>(itemModel, User.class));
            }
        };
    }

    private ListView<ProjectBaseData> createProjectList(String id, IModel<List<ProjectBaseData>> model) {
        return new ListView<ProjectBaseData>(id, model) {
            @Override
            protected void populateItem(final ListItem<ProjectBaseData> item) {
                item.add(new Label("projectname", model(from(item.getModel()).getName())));
                Link deleteProjectButton = new Link("deleteProjectButton") {
                    @Override
                    public void onClick() {

                        setResponsePage(new DeleteDialog(() -> {
                            projectService.deleteProject(item.getModelObject().getId());
                            setResponsePage(BudgeteerAdministrationOverview.class, getPageParameters());
                            return null;
                        }, () -> {
                            setResponsePage(BudgeteerAdministrationOverview.class, getPageParameters());
                            return null;
                        }));
                    }
                };
                item.add(deleteProjectButton);
            }

            @Override
            protected ListItem<ProjectBaseData> newItem(int index, IModel<ProjectBaseData> itemModel) {
                return super.newItem(index, new ClassAwareWrappingModel<>(itemModel, ProjectBaseData.class));
            }
        };
    }

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        return new BreadcrumbsModel(BudgeteerAdministrationOverview.class);
    }

}
