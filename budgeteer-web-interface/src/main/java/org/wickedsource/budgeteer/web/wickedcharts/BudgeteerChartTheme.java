package org.wickedsource.budgeteer.web.wickedcharts;

import com.googlecode.wickedcharts.highcharts.options.CreditOptions;
import com.googlecode.wickedcharts.highcharts.options.ExportingOptions;
import com.googlecode.wickedcharts.highcharts.options.Legend;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.theme.Theme;

public class BudgeteerChartTheme extends Theme {

    public BudgeteerChartTheme() {
        setColors(
                new HexColor("#00c0ef"),
                new HexColor("#f39c12"),
                new HexColor("#0073b7"),
                new HexColor("#00a65a"),
                new HexColor("#001f3f"),
                new HexColor("#39cccc"),
                new HexColor("#3d9970"),
                new HexColor("#3d9970"),
                new HexColor("#f56954"));

        setCredits(new CreditOptions().setEnabled(false));

        setLegend(new Legend().setEnabled(false));

        setExporting(new ExportingOptions().setEnabled(false));
    }
}
