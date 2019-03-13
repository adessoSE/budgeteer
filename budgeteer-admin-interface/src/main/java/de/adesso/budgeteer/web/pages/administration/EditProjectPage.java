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
import org.wickedsource.budgeteer.web.components.user.UserRoleTable;
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
        add(new UserRoleTable("projectUsers", getParameterId(), feedbackPanel, EditProjectPage.class, EditProjectPage.class, EditProjectPage.class, pageParameters));
        add(createAddUserForm("addUserForm"));
        add(createEditProjectForm("projectChangeForm"));
        add(createBackButton());
    }

    private Form<Project> createEditProjectForm(String formId) {
        Form<Project> form = new Form<Project>(formId, model(from(projectService.findProjectById(getParameterId())))) {
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
        form.add(new TextField<>("projectTitle", model(from(form.getModelObject()).getName())));
        DateRange defaultDateRange = new DateRange(DateUtil.getBeginOfYear(), DateUtil.getEndOfYear());
        form.add(new DateRangeInputField("projectStart", model(from(form.getModelObject()).getDateRange()), defaultDateRange, DateRangeInputField.DROP_LOCATION.DOWN));
        return form;
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

    private Component createBackButton() {
        return new Link("backButton") {
            @Override
            public void onClick() {
                setResponsePage(BudgeteerAdministrationOverview.class);
            }
        };
    }
}
