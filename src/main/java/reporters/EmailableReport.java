package reporters;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.util.List;

public class EmailableReport implements IReporter {
    private PrintWriter out;

    private enum Align {LEFT, CENTER, RIGHT}

    private enum Color {RED, YELLOW, GREEN, ORANGE, NONE}

    private int totalPassedTests = 0;
    private int totalSkippedTests = 0;
    private int totalFailedTests = 0;
    private int totalSuiteTests = 0;
    private String totalSuccessRate = "";
    private long totalDuration = 0L;

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        if (suites == null || outputDirectory == null) {
            return;
        }
        doMath(suites);
        try {
            createWriter(outputDirectory);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        startHtml();
        writeOverview(suites);
        endHtml();
        out.flush();
        out.close();
    }

    private void doMath(List<ISuite> suites) {
        suites.forEach(iSuite -> iSuite.getResults().values().forEach(iSuiteResult -> {
            ITestContext iTestContext = iSuiteResult.getTestContext();
            totalPassedTests += iTestContext.getPassedTests().size();
            totalSkippedTests += iTestContext.getSkippedTests().size();
            totalFailedTests += iTestContext.getFailedTests().size();
            totalDuration += (iTestContext.getEndDate().getTime() - iTestContext.getStartDate().getTime()) / 1000;
        }));
        totalSuiteTests = totalPassedTests + totalSkippedTests + totalFailedTests;
        totalSuccessRate = Math.round(totalPassedTests * 100f / totalSuiteTests) + "%";
    }

    private void createWriter(String directory) throws IOException {
        String reportFileName = "emailable-report.html";
        File file = new File(directory + "/" + reportFileName);
        if (file.exists())
            file.delete();
        out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
    }

    private void startHtml() {
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        out.println("<head>");
        out.println("<meta charset='utf-8'>");
        out.println("<title>COW Login Automation Test Report</title>");
        out.println("<style type=\"text/css\">");
        out.println("h1 {text-shadow: 0 1px 0 #ccc, 0 2px 0 #c9c9c9, 0 3px 0 #bbb,0 4px 0 #b9b9b9, 0 5px 0 #aaa, 0 6px 1px rgba(0,0,0,.1),0 0 5px rgba(0,0,0,.1),0 1px 3px rgba(0,0,0,.3),0 3px 5px rgba(0,0,0,.2), 0 5px 10px rgba(0,0,0,.25),0 10px 10px rgba(0,0,0,.2),0 20px 20px rgba(0,0,0,.15); color: #8A2BE2;}");
        out.println("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show}");
        out.println("td,th {border:1px solid #009;padding:.25em .5em}");
        out.println(".green {background-color: #9bff8e}");
        out.println(".yellow {background-color: #fff48e}");
        out.println(".red {background-color: #ff8e8e}");
        out.println(".orange {background-color: #ffdd8e}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1> Google Login Automation Test Report </h1>");

    }

    private void endHtml() {
        out.println("</body></html>");
    }

    private String formatDuration(Long durationSec) {
        //Display as h + m, e.g. 5h 30m
        if (durationSec > 3600)
            return (durationSec / 3600) + "h " + ((durationSec / 3600) / 60) + "m";
        //Display as m + s, e.g. 4m 21s
        if (durationSec > 60)
            return (durationSec / 60) + "m " + (durationSec % 60) + "s";
        return durationSec + "s";
    }

    private void writeOverview(List<ISuite> suites) {
        out.println("<table cellspacing=\"0\" cellpadding=\"0\">");
        out.println("<tr>");
        column("Test Name", Align.LEFT, Color.ORANGE);
        column("Passed", Align.CENTER, Color.GREEN);
        column("Skipped", Align.CENTER, Color.YELLOW);
        column("Failed", Align.CENTER, Color.RED);
        column("Total", Align.CENTER, Color.ORANGE);
        column("Pass %", Align.CENTER, Color.ORANGE);
        column("Duration", Align.CENTER, Color.ORANGE);
        out.println("</tr>");
        suites.forEach(iSuite -> iSuite.getResults().values().forEach(iSuiteResult -> {
            ITestContext iTestContext = iSuiteResult.getTestContext();
            int passedTests = iTestContext.getPassedTests().size();
            int skippedTests = iTestContext.getSkippedTests().size();
            int failedTests = iTestContext.getFailedTests().size();
            int totalTests = passedTests + skippedTests + failedTests;
            if (totalTests != 0) {
                String successRate = Math.round(passedTests * 100f / totalTests) + "%";
                long durationSec = (iTestContext.getEndDate().getTime() - iTestContext.getStartDate().getTime()) / 1000;
                out.println("<tr>");
                cell(iTestContext.getName(), Align.LEFT, Color.ORANGE, "td");
                cell(passedTests, Color.GREEN);
                cell(skippedTests, Color.YELLOW);
                cell(failedTests, Color.RED);
                cell(totalTests, Color.ORANGE);
                cell(successRate, Align.CENTER, Color.ORANGE, "td");
                cell(formatDuration(durationSec), Align.CENTER, Color.ORANGE, "td");
                out.println("</tr>");
            }
        }));
        out.println("<tr>");
        cell("Total", Align.LEFT, Color.ORANGE, "td");
        cell(totalPassedTests, Color.GREEN);
        cell(totalSkippedTests, Color.YELLOW);
        cell(totalFailedTests, Color.RED);
        cell(totalSuiteTests, Color.ORANGE);
        cell(totalSuccessRate, Align.CENTER, Color.ORANGE, "td");
        cell(formatDuration(totalDuration), Align.CENTER, Color.ORANGE, "td");
        out.println("</tr>");
        out.println("</table>");
    }

    private void cell(String text, Align align, Color color, String tag) {
        String textAlign = "center";
        switch (align) {
            case LEFT:
                textAlign = "left";
                break;
            case RIGHT:
                textAlign = "right";
                break;
            case CENTER:
                textAlign = "center";
        }
        String colour = "";
        switch (color) {
            case RED:
                colour = "class=\"red\"";
                break;
            case YELLOW:
                colour = "class=\"yellow\"";
                break;
            case GREEN:
                colour = "class=\"green\"";
                break;
            case ORANGE:
                colour = "class=\"orange\"";
                break;
            case NONE:
                colour = "";
        }
        out.println("<" + tag + " style=\"text-align:" + textAlign + ";\"" + colour + ">" + text + "</" + tag + ">");
    }

    private void cell(Integer text, Color color) {
        cell(text.toString(), Align.CENTER, color, "td");
    }

    private void column(String label, Align align, Color color) {
        cell(label, align, color, "th");
    }
}
