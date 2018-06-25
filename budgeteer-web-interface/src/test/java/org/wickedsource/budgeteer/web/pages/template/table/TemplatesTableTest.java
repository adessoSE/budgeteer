package org.wickedsource.budgeteer.web.pages.template.table;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.ReportType;
import org.wickedsource.budgeteer.service.template.Template;
import org.wickedsource.budgeteer.service.template.TemplateService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.templates.TemplateFilter;
import org.wickedsource.budgeteer.web.pages.templates.table.TemplateListModel;
import org.wickedsource.budgeteer.web.pages.templates.table.TemplatesTable;

public class TemplatesTableTest extends AbstractWebTestTemplate {

	@Autowired
	private TemplateService templateService;

	@Test
	void testRender() {
		WicketTester tester = getTester();
		TemplateListModel model = new TemplateListModel( new TemplateFilter(1L));
		TemplatesTable table = new TemplatesTable("table", model);
		tester.startComponentInPage(table);
	}

	private List<Template> createTemplates(){
		return Arrays.asList(new Template(1, "temp1", "temp1Desc", ReportType.BUDGET_REPORT,null,false, 1),
				new Template(2, "temp2", "temp2Desc", ReportType.BUDGET_REPORT,null,false, 1));
	}

	@Override
	public void setupTest(){
		when(templateService.getTemplatesInProject(anyLong())).thenReturn(createTemplates());
	}
}
