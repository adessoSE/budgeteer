package de.adesso.budgeteer.web.pages.invoice.edit.form;

import de.adesso.budgeteer.service.DateUtil;
import de.adesso.budgeteer.service.contract.DynamicAttributeField;
import de.adesso.budgeteer.service.invoice.InvoiceBaseData;
import de.adesso.budgeteer.service.invoice.InvoiceService;
import de.adesso.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import de.adesso.budgeteer.web.components.daterange.DateInputField;
import de.adesso.budgeteer.web.components.fileUpload.CustomFileUpload;
import de.adesso.budgeteer.web.components.money.MoneyTextField;
import de.adesso.budgeteer.web.components.monthRenderer.MonthRenderer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Arrays;
import java.util.List;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class EditInvoiceForm  extends Form<InvoiceBaseData> {

    @SpringBean
    private InvoiceService service;

    private String nameOfAttribute = "";

    private WebMarkupContainer table;

    private TextField<String> newAttributeField;
    private CustomFeedbackPanel feedbackPanel;

    public EditInvoiceForm(String id, final long contractId){
        super(id);
        super.setDefaultModel(Model.of(service.getEmptyInvoiceModel(contractId)));
        addComponents();
    }


    public EditInvoiceForm(String id, IModel<InvoiceBaseData> model) {
        super(id, model);
        Injector.get().inject(this);
        addComponents();
    }

    private void addComponents() {
        feedbackPanel = new CustomFeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        TextField<String> invoiceNameTextfield = new TextField<>("invoiceName", model(from(getModelObject()).getInvoiceName()));
        invoiceNameTextfield.setRequired(true);
        add(invoiceNameTextfield);

        TextField<String> nameTextfield = new TextField<>("contractName", model(from(getModelObject()).getContractName()));
        nameTextfield.setEnabled(false);
        add(nameTextfield);

        TextField<String> internalNumberTextfield = new TextField<>("internalNumber", model(from(getModelObject()).getInternalNumber()));
        internalNumberTextfield.setRequired(true);
        add(internalNumberTextfield);

        MoneyTextField budgetTextfield = new MoneyTextField("sum", model(from(getModelObject()).getSum()));
        budgetTextfield.setRequired(true);
        add(budgetTextfield);

        add(new DropDownChoice<>("year", model(from(getModelObject()).getYear()), DateUtil.getCurrentYears(5)));
        List<Integer> monthList = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11);
        add(new DropDownChoice<>("month", model(from(getModelObject()).getMonth()), monthList, new MonthRenderer()));

        add(new DateInputField("paidDate", model(from(getModelObject()).getPaidDate())));
        add(new DateInputField("dueDate", model(from(getModelObject()).getDueDate())));

        final CustomFileUpload fileUpload = new CustomFileUpload("fileUpload", model(from(getModelObject()).getFileUploadModel()));
        add(fileUpload);

        table = new WebMarkupContainer("attributeTable");
        table.setOutputMarkupId(true);
        table.setOutputMarkupPlaceholderTag(true);
        table.add(new ListView<DynamicAttributeField>("invoiceAttributes", model(from(getModelObject()).getDynamicInvoiceFields())) {
            @Override
            protected void populateItem(ListItem<DynamicAttributeField> item) {
                item.add(new Label("attributeTitle", item.getModelObject().getName()));
                item.add(new TextField<>("attributeValue", model(from(item.getModelObject()).getValue())));
            }
        });
        add(table);
        newAttributeField = new TextField<>("nameOfNewAttribute", Model.of(nameOfAttribute));
        newAttributeField.setOutputMarkupId(true);
        add(newAttributeField);
        AjaxButton addAttributeButton = new AjaxButton("addAttribute") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (newAttributeField.getModelObject() != null) {
                    ((InvoiceBaseData) form.getModelObject()).getDynamicInvoiceFields().add(new DynamicAttributeField(newAttributeField.getModelObject(), ""));
                    target.add(table, newAttributeField, feedbackPanel);
                } else {
                    this.error(getString("feedback.error.nameEmpty"));
                    target.add(feedbackPanel);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        };
        add(addAttributeButton);
        add(new AjaxButton("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    ((InvoiceBaseData) form.getModelObject()).getFileUploadModel().setFile(fileUpload.getFile());
                    ((InvoiceBaseData) form.getModelObject()).getFileUploadModel().setFileName(fileUpload.getFileName());
                    ((InvoiceBaseData) form.getModelObject()).setInvoiceId(service.save((InvoiceBaseData) form.getModelObject()));
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