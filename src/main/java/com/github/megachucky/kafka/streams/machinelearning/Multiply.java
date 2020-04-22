package com.github.megachucky.kafka.streams.machinelearning;

import io.confluent.ksql.function.udf.Udf;
import io.confluent.ksql.function.udf.UdfDescription;
import io.confluent.ksql.function.udf.UdfParameter;

@UdfDescription(name = "multiply", description = "multiplies 2 numbers")
public class Multiply {

  @Udf(description = "multiply two non-nullable INTs.")
  public long multiply(@UdfParameter("v1") final int v1, @UdfParameter("v2")  final int v2) {
    return v1 * v2;
  }

  @Udf(description = "multiply two non-nullable BIGINTs.")
  public long multiply(@UdfParameter("v1") final long v1, @UdfParameter("v2")  final long v2) {
    return v1 * v2;
  }

  @Udf(description = "multiply two nullable BIGINTs. If either param is null, null is returned.")
  public Long multiply(@UdfParameter("v1") final Long v1, @UdfParameter("v2")  final Long v2) {
    return v1 == null || v2 == null ? null : v1 * v2;
  }

  @Udf(description = "multiply two non-nullable DOUBLEs.")
  public double multiply(@UdfParameter("v1") final double v1, @UdfParameter("v2")  double v2) {
    return v1 * v2;
  }
}

