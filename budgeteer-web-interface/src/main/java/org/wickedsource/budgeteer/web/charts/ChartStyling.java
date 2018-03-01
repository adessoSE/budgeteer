package org.wickedsource.budgeteer.web.charts;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import de.adesso.wickedcharts.chartjs.chartoptions.AxesScale;
import de.adesso.wickedcharts.chartjs.chartoptions.GridLines;
import de.adesso.wickedcharts.chartjs.chartoptions.Layout;
import de.adesso.wickedcharts.chartjs.chartoptions.Legend;
import de.adesso.wickedcharts.chartjs.chartoptions.Options;
import de.adesso.wickedcharts.chartjs.chartoptions.Padding;
import de.adesso.wickedcharts.chartjs.chartoptions.Scales;
import de.adesso.wickedcharts.chartjs.chartoptions.Ticks;
import de.adesso.wickedcharts.chartjs.chartoptions.colors.RgbColor;

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
		ClassLoader classLoader = ChartStyling.class.getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());

		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}

			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return result.toString();

	}
}
