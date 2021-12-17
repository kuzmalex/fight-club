package server.view.html;

import monitoring.ExecutionMetrics;
import server.RequestContext;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MetricMeasurePageFactory implements PageFactory{

    private final PageFactory pageFactory;
    private final RequestContext requestContext;

    public MetricMeasurePageFactory(PageFactory pageFactory, RequestContext requestContext) {
        this.pageFactory = pageFactory;
        this.requestContext = requestContext;
    }

    @Override
    public String from(Component format, Map<String, String> arguments) {

        long startTime = System.nanoTime();
        String page = pageFactory.from(format, arguments);
        long time = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);

        String metricsRecord = toText(requestContext.getExecutionMetrics(), time);

        return page.replace("</body>", metricsRecord + "</body>");
    }

    private String toText(ExecutionMetrics executionMetrics, long pageGenerationTime){
        return "<div class=\"metrics\"><b>page: " +pageGenerationTime+"ms, db: "
                +executionMetrics.getDbRequestNumber()+"req ("
                +executionMetrics.getDbRequestExecutionTime()+"ms) </b></div>";
    }
}
