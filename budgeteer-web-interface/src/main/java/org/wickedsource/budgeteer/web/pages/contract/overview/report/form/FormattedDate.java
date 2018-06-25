package org.wickedsource.budgeteer.web.pages.contract.overview.report.form;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormattedDate implements Serializable {
	private String label;
	private Date date;

	public FormattedDate(Date date, SimpleDateFormat format) {
		this.label = format.format(date);
		this.date = date;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}


}
