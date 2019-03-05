package de.adesso.budgeteer.web.pages.administration;

import de.adesso.budgeteer.web.pages.base.basepage.BasePage;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import org.apache.wicket.Component;
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
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.DateRange;
import org.wickedsource.budgeteer.service.DateUtil;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.daterange.DateRangeInputField;
import org.wickedsource.budgeteer.web.components.multiselect.MultiselectBehavior;
import org.wickedsource.budgeteer.web.components.user.UserRole;
import org.wickedsource.budgeteer.web.components.user.UserRoleCheckBox;
import org.wickedsource.budgeteer.web.components.user.UserRoleDropdown;
import org.wickedsource.budgeteer.web.pages.administration.Project;
import org.wickedsource.budgeteer.web.pages.administration.UserChoiceRenderer;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;
import org.wickedsource.budgeteer.web.settings.BudgeteerSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

@Mount("/administartion-menu/edit-project/${id}")
public class EditProjectPage extends BasePage {

    @SpringBean
    private UserService userService;

    @SpringBean
    private ProjectService projectService;

    @SpringBean
    private BudgeteerSettings settings;

    private CustomFeedbackPanel feedbackPanel;


    public EditProjectPage(PageParameters pageParameters) {
        super(pageParameters);
      feedbackPanel = new CustomFeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);
       // add(new CustomFeedbackPanel("feedback"));
        add(createUserList("userList", new UsersInProjectModel(getParameterId())));
        add(createAddUserForm("addUserForm"));
        add(createEditProjectForm("projectChangeForm"));
        add(createBackButton());
    }

    private Form<Project> createEditProjectForm(String formId) {
        Form<Project> form = new Form<Project>(formId, model(from(projectService.findProjectById(getParameterId())))) {
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
        long projectID = getParameterId();
        return new ListView<User>(id, model) {

            @Override
            protected void populateItem(final ListItem<User> item) {
                item.add(new Label("username", model(from(item.getModel()).getName())));
                Link deleteButton = new Link("deleteButton") {
                    @Override
                    public void onClick() {
                        setResponsePage(new DeleteDialog() {
                            @Override
                            protected void onYes() {
                                userService.removeUserFromProject(projectID, item.getModelObject().getId());
                                setResponsePage(EditProjectPage.class, EditProjectPage.this.getPageParameters());
                            }

                            @Override
                            protected void onNo() {
                                setResponsePage(EditProjectPage.class, EditProjectPage.this.getPageParameters());
                            }

                            @Override
                            protected String confirmationText() {
                                return new StringResourceModel("delete.user.from.project.confirmation", EditProjectPage.this).getString();
                            }
                        });
                    }
                };
                UserRoleCheckBox adminCheckBox = new UserRoleCheckBox("adminCheckbox", item.getModelObject(), projectID);

                //UserRoleDropdown makeAdminList = new UserRoleDropdown("roleDropdown", item.getModelObject(), projectID);
                // a user may not delete herself/himself unless another admin is present
                if (item.getModelObject().getId() == thisUser.getId()){
                    List<User> usersInProjects = userService.getUsersInProject(projectID);
                    deleteButton.setVisible(false);
                    adminCheckBox.setVisible(false);
                    for(User user : usersInProjects){
                        if(user.getId() != thisUser.getId() && user.isProjectAdmin(projectID)){
                            deleteButton.setVisible(true);
                            adminCheckBox.setVisible(true);
                            break;
                        }
                    }
                }
                item.add(deleteButton);
                item.add(adminCheckBox);
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
                userService.addUserToProject(getParameterId(), getModelObject().getId());
            }
        };

        DropDownChoice<User> userChoice = new DropDownChoice<>("userChoice", form.getModel(),
                new UsersNotInProjectModel(getParameterId()), new UserChoiceRenderer());
        userChoice.setRequired(true);
        form.add(userChoice);
        return form;
    }

    @Override
    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model = new BreadcrumbsModel(BudgeteerAdministrationOverview.class);
        model.addBreadcrumb(EditProjectPage.class, getPageParameters());
        return model;
    }

    private Component createBackButton(){
        return new Link("backButton") {
            @Override
            public void onClick() {
                setResponsePage(BudgeteerAdministrationOverview.class);
            }
        };
    }

}
