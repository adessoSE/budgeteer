package org.wickedsource.budgeteer.service.statistics;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TargetAndActual {

	private List<MoneySeries> actualSeries = new ArrayList<MoneySeries>();
	private MoneySeries targetSeries;
}
