package org.wickedsource.budgeteer.web.pages.dashboard.dailyratechart;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.wickedsource.budgeteer.MoneyUtil;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;

import de.adesso.wickedcharts.chartjs.jackson.JsonRenderer;

@SuppressWarnings("unused")
public class AverageDailyRateChartconfigurationTest extends AbstractWebTestTemplate  {
   
	// Example Javascript rendering
	
    @Test
    public void test() {
        WicketTester tester = getTester();
        AverageDailyRateChartModel model = new AverageDailyRateChartModel(1l, 5);
        model.getObject().add(MoneyUtil.createMoney(1));
        model.getObject().add(MoneyUtil.createMoney(5));
        model.getObject().add(MoneyUtil.createMoney(2));
        model.getObject().add(MoneyUtil.createMoney(4));
        model.getObject().add(MoneyUtil.createMoney(3));
    	AverageDailyRateChartConfiguration config = new AverageDailyRateChartConfiguration(model);
    	JsonRenderer json = new JsonRenderer();
    	System.out.println(json.toJson(config));
    }

}
