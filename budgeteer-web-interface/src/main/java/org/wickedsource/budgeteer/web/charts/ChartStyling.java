package org.wickedsource.budgeteer.web.charts;

import de.adesso.wickedcharts.chartjs.chartoptions.*;
import de.adesso.wickedcharts.chartjs.chartoptions.colors.RgbColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ChartStyling {
	public static List<RgbColor> getColors() {
		return Arrays.asList(
				new RgbColor(0, 192, 239),
                new RgbColor(243, 156, 18),
                new RgbColor(0, 115, 183),
                new RgbColor(0, 166, 90),
                new RgbColor(0, 31, 63),
                new RgbColor(57, 204, 204),
                new RgbColor(61, 153, 112),
                new RgbColor(245, 105, 84)
                );
	}

	public static String getFontFamily() {
		return "Lucida Sans Unicode";
	}
	
	public static Number getFontSize() {
		return 11;
	}

	public static Layout getLayout() {
		return new Layout()
				.setPadding(new Padding()
						.setBottom(15)
						.setLeft(10)
						.setTop(10)
						.setRight(10));
	}

	public static Scales getScales() {
		return new Scales()
				.setYAxes(new AxesScale()
						.setGridLines(new GridLines()
								.setDrawBorder(false))
						.setDisplay(true)
						.setTicks(new Ticks()
								.setFontFamily(ChartStyling.getFontFamily())
								.setFontSize(ChartStyling.getFontSize())))
				.setXAxes(new AxesScale()
						.setTicks(new Ticks()
								.setFontFamily(ChartStyling.getFontFamily())
								.setFontSize(ChartStyling.getFontSize()))
						.setGridLines(new GridLines()
								.setDisplay(false)
								.setColor(new RgbColor(230, 230, 230))));
	}

	public static Options getOptions() {
		return new Options()
				.setLayout(ChartStyling.getLayout())
				.setMaintainAspectRatio(false)
				.setResponsive(true)
				.setLegend(new Legend()
						.setDisplay(false))
				.setScales(ChartStyling.getScales());
	}

	public static String readFile(String fileName) {
		StringBuilder result = new StringBuilder("");

		// Get file from resources folder
		// do not use files, this will not work in production!
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        try {
            for (String line; (line = reader.readLine()) != null; ) {
				result.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}
