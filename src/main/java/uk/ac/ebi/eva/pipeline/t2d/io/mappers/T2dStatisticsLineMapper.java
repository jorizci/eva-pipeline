package uk.ac.ebi.eva.pipeline.t2d.io.mappers;

import org.springframework.batch.item.file.LineMapper;
import uk.ac.ebi.eva.pipeline.t2d.model.T2dStatistics;

public class T2dStatisticsLineMapper implements LineMapper<T2dStatistics> {

    private String[] headerParameters;

    @Override
    public T2dStatistics mapLine(String line, int lineNumber) throws Exception {
        String[] values = line.split("\t");
        T2dStatistics t2dStatistics = new T2dStatistics();
        int minLength = Math.min(headerParameters.length, values.length - 1);
        for (int i = 0; i <= minLength; i++) {
            t2dStatistics.putStatistic(headerParameters[i], values[i]);
        }
        return t2dStatistics;
    }

    public void setHeaderParameters(String[] headerParameters) {
        this.headerParameters = headerParameters;
    }
}
