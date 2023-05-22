package org.apache.beam.it.gcp;

import com.google.common.collect.ImmutableMap;
import com.google.protobuf.util.Timestamps;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import org.apache.beam.it.common.PipelineLauncher;
import org.junit.Test;

public class PullMetricsLT extends IOLoadTestBase {

  private static final String READ_PCOLLECTION = "Counting element.out0";
  private static final String WRITE_PCOLLECTION = "Map records.out0";

  @Test
  public void testGetMetrics() {

    String jobId = System.getProperty("jobId");
    String project = System.getProperty("project");
    String region = System.getProperty("region", "us-central1");
    String jobType = System.getProperty("jobType", "JOB_TYPE_BATCH");
    String readPColl = System.getProperty("readPCollection", READ_PCOLLECTION);
    String writePColl = System.getProperty("writePCollection", WRITE_PCOLLECTION);
    String pipelineName = System.getProperty("pipelineName", "test");
    String runner = System.getProperty("runner", "Dataflow Legacy Runner");
    testName = System.getProperty("testName", "test");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ssXXX");
    TemporalAccessor dateTime = formatter.parse(jobId.substring(0, 19) + "-07:00");
    Instant created = Instant.from(dateTime);

    PipelineLauncher.LaunchInfo info =
        PipelineLauncher.LaunchInfo.builder()
            .setJobId(jobId)
            .setPipelineName(pipelineName)
            .setProjectId(project)
            .setRegion(region) // since one day ago
            .setCreateTime(
                Timestamps.toString(
                    Timestamps.fromMillis(Instant.EPOCH.until(created, ChronoUnit.MILLIS))))
            .setSdk("Apache Beam SDK for Java")
            .setVersion("2.50.0")
            .setJobType(jobType)
            .setRunner("DataflowRunner")
            .setParameters(
                ImmutableMap.of("runner", runner, "jobType", "JOB_TYPE_BATCH", "jobId", jobId))
            .setState(PipelineLauncher.JobState.DONE)
            .build();
    try {
      exportMetricsToBigQuery(
          info,
          getMetrics(
              info,
              MetricsConfiguration.builder()
                  .setInputPCollection(writePColl)
                  .setOutputPCollection(readPColl)
                  .build()));
    } catch (ParseException | InterruptedException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
