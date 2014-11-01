package org.wickedsource.budgeteer.web.pages.user.selectproject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.project.ProjectBaseData;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.Mount;
import org.wickedsource.budgeteer.web.pages.base.dialogpage.DialogPageWithBacklink;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;

@Mount("/selectProject")
public class SelectProjectPage extends DialogPageWithBacklink {

    @SpringBean
    private ProjectService projectService;

    @SpringBean
    private UserService userService;

    public SelectProjectPage(Class<? extends WebPage> backlinkPage, PageParameters backlinkParameters) {
        super(backlinkPage, backlinkParameters);
        add(createBacklink("backlink1"));
        add(createBacklink("backlink2"));
        add(createNewProjectForm("newProjectForm"));
        add(createChooseProjectForm("chooseProjectForm"));
    }

    private Form<String> createNewProjectForm(String id) {
        Form<String> form = new Form<String>(id, new Model<String>("")) {
            @Override
            protected void onSubmit() {
                ProjectBaseData project = projectService.createProject(getModelObject());
                userService.addUserToProject(project.getId(), BudgeteerSession.get().getLoggedInUser().getId());
                BudgeteerSession.get().setProjectId(project.getId());
                setResponsePage(DashboardPage.class);
            }
        };
        form.add(new FeedbackPanel("feedback"));
        form.add(new RequiredTextField<String>("projectName", form.getModel()));
        return form;
    }

    private Form<ProjectBaseData> createChooseProjectForm(String id) {
        Form<ProjectBaseData> form = new Form<ProjectBaseData>("chooseProjectForm", new Model<ProjectBaseData>(new ProjectBaseData())) {
            @Override
            protected void onSubmit() {
                BudgeteerSession.get().setProjectId(getModelObject().getId());
                setResponsePage(DashboardPage.class);
            }
        };
        DropDownChoice<ProjectBaseData> choice = new DropDownChoice<ProjectBaseData>("projectChoice", form.getModel(), new ProjectsForUserModel(BudgeteerSession.get().getLoggedInUser().getId()), new ProjectChoiceRenderer());
        form.add(choice);
        return form;
    }

}
