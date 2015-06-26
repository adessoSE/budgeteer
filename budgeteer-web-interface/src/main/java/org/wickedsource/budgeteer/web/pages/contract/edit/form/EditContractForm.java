package org.wickedsource.budgeteer.web.pages.contract.edit.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.persistence.contract.ContractEntity;
import org.wickedsource.budgeteer.service.contract.ContractBaseData;
import org.wickedsource.budgeteer.service.contract.ContractService;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.web.BudgeteerSession;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.fileUpload.CustomFileUpload;
import org.wickedsource.budgeteer.web.components.money.MoneyTextField;

import java.util.Arrays;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class EditContractForm extends Form<ContractBaseData> {

    @SpringBean
    private ContractService service;

    private String nameOfAttribute = "";

    private WebMarkupContainer table;

    private TextField<String> newAttributeField;
    private CustomFeedbackPanel feedbackPanel;

    public EditContractForm(String id){
        super(id);
        super.setDefaultModel(Model.of(service.getEmptyContractModel(BudgeteerSession.get().getProjectId())));
        addComponents();
    }


    public EditContractForm(String id, IModel<ContractBaseData> model) {
        super(id, model);
        Injector.get().inject(this);
        addComponents();
    }

    private void addComponents() {
        feedbackPanel = new CustomFeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        TextField<String> nameTextfield = new TextField<String>("contractName", model(from(getModelObject()).getContractName()));
        nameTextfield.setRequired(true);
        add(nameTextfield);

        TextField<String> internalNumberTextfield = new TextField<String>("internalNumber", model(from(getModelObject()).getInternalNumber()));
        internalNumberTextfield.setRequired(true);
        add(internalNumberTextfield);

        MoneyTextField budgetTextfield = new MoneyTextField("budget", model(from(getModelObject()).getBudget()));
        budgetTextfield.setRequired(true);
        add(budgetTextfield);

        add(new NumberTextField<Integer>("year", model(from(getModelObject()).getYear())));
        add(new DropDownChoice<ContractEntity.ContractType>("type",
                model(from(getModelObject()).getType()), Arrays.asList(ContractEntity.ContractType.values()),
                new EnumChoiceRenderer<ContractEntity.ContractType>(this)));

        final CustomFileUpload fileUpload = new CustomFileUpload("fileUpload", model(from(getModelObject()).getFileModel()));
        add(fileUpload);

        table = new WebMarkupContainer("attributeTable");
        table.setOutputMarkupId(true);
        table.setOutputMarkupPlaceholderTag(true);
        table.add(new ListView<DynamicAttributeField>("contractAttributes", model(from(getModelObject()).getContractAttributes())) {
            @Override
            protected void populateItem(ListItem<DynamicAttributeField> item) {
                item.add(new Label("attributeTitle", item.getModelObject().getName()));
                item.add(new TextField<String>("attributeValue", model(from(item.getModelObject()).getValue())));
            }
        });
        add(table);
        newAttributeField = new TextField<String>("nameOfNewAttribute", Model.of(nameOfAttribute));
        newAttributeField.setOutputMarkupId(true);
        add(newAttributeField);
        Button addAttribute = new AjaxButton("addAttribute") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if(newAttributeField.getModelObject() != null) {
                    ((ContractBaseData) form.getModelObject()).getContractAttributes().add(new DynamicAttributeField(newAttributeField.getModelObject(), ""));
                    target.add(table, newAttributeField, feedbackPanel);
                } else {
                    this.error(getString("feedback.error.nameEmpty"));
                    target.add(feedbackPanel);
                }
            }
        };
        add(addAttribute);
        add(new AjaxButton("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    ((ContractBaseData) form.getModelObject()).getFileModel().setFile(fileUpload.getFile());
                    ((ContractBaseData) form.getModelObject()).getFileModel().setFileName(fileUpload.getFileName());
                    ((ContractBaseData) form.getModelObject()).setContractId(service.save((ContractBaseData) form.getModelObject()));
                    this.success(getString("feedback.success"));
                } catch(Exception e){
                    e.printStackTrace();
                    this.error(getString("feedback.error"));
                }
                target.add(feedbackPanel);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });
    }
}
