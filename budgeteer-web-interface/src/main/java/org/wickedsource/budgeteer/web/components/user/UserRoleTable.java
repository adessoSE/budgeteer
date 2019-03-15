package org.wickedsource.budgeteer.web.components.user;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.user.User;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.ClassAwareWrappingModel;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.pages.administration.ProjectAdministrationPage;
import org.wickedsource.budgeteer.web.pages.administration.UsersInProjectModel;
import org.wickedsource.budgeteer.web.pages.base.delete.DeleteDialog;

import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class UserRoleTable extends Panel {
    @SpringBean
    private UserService userService;

    private CustomFeedbackPanel feedbackPanel;

    public UserRoleTable(String id, long projectID, CustomFeedbackPanel feedbackPanel, Class originPage,
                         Class afterUserRemovingPage, Class afterAdminDeletionPage, PageParameters pageParameters) {
        super(id);
        this.feedbackPanel = feedbackPanel;

        IModel<List<User>> model = new UsersInProjectModel(projectID);
        this.setOutputMarkupId(true);
        UserRoleTable table = this;

        User thisUser = BudgeteerSession.get().getLoggedInUser();

        add(new ListView<User>("userList", model) {
            @Override
            protected void populateItem(final ListItem<User> item) {
                item.add(new Label("username", model(from(item.getModel()).getName())));
                Link deleteButton = new Link("deleteButton") {
                    @Override
                    public void onClick() {
                        setResponsePage(new DeleteDialog() {
                            @Override
                            protected void onYes() {
                                long itemId = item.getModelObject().getId();
                                userService.removeUserFromProject(projectID, itemId);
                                if (itemId == thisUser.getId()) {
                                    setResponsePage(afterUserRemovingPage, pageParameters);
                                } else {
                                    setResponsePage(originPage, pageParameters);
                                }
                            }

                            @Override
                            protected void onNo() {
                                setResponsePage(originPage, pageParameters);
                            }

                            @Override
                            protected String confirmationText() {
                                return table.getString("delete.person.confirmation");
                            }
                        });
                    }
                };

                UserRoleCheckBox adminCheckBox = new UserRoleCheckBox("adminCheckbox", item.getModelObject(),
                        projectID, feedbackPanel, table, originPage, afterAdminDeletionPage, pageParameters);

                int projectAdminsCount = countProjectAdmins(projectID);

                // only if there is more than one admin for this project, the current admins can become non-admins or be removed from the project
                if (item.getModelObject().isProjectAdmin(projectID) && projectAdminsCount < 2) {
                    deleteButton.setVisible(false);
                    adminCheckBox.setVisible(false);
                }

                item.add(deleteButton);
                item.add(adminCheckBox);
                item.setOutputMarkupId(true);
            }

            @Override
            protected ListItem<User> newItem(int index, IModel<User> itemModel) {
                return super.newItem(index, new ClassAwareWrappingModel<>(itemModel, User.class));
            }
        });
    }

    private int countProjectAdmins(long projectID) {
        // Count how many admins for the project exist
        int projectAdminCount = 0;
        List<User> usersInProjects = userService.getUsersInProject(projectID);
        for (User e : usersInProjects) {
            if (e.isProjectAdmin(projectID)) {
                projectAdminCount++;
            }
        }

        return projectAdminCount;
    }
}
