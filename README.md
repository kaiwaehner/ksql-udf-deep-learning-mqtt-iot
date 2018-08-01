# Deep Learning UDF for KSQL for Streaming Anomaly Detection of MQTT IoT Sensor Data

I built a KSQL UDF for sensor analytics. It leverages the new [API features of KSQL to build UDF / UDAF functions easily with Java](https://docs.confluent.io/current/ksql/docs/udf.html) to do continuous stream processing on incoming events.

## Use Case: Connected Cars - Real Time Streaming Analytics using Deep Learning
Continuously process millions of events from connected devices (sensors of cars in this example):
![](pictures/Connected_Cars_IoT_Deep_Learning.png)

## Architecture: Sensor Data via Confluent MQTT Proxy to Kafka Cluster for KSQL Processing and Real Time Analytics
This project focuses on the ingestion of data into Kafka via MQTT and processing of data via KSQL:
![](pictures/MQTT_Proxy_Confluent_Cloud.png)
If you want to see the other part (integration with sink applications like Elasticsearch / Grafana), please take a look at the project "[KSQL for streaming IoT data](https://github.com/kaiwaehner/ksql-fork-with-deep-learning-function)", which shows how to realize the integration with ElasticSearch via Kafka Connect.

## Source Code
Here is the full source code for the [Anomaly Detection KSQL UDF](https://github.com/kaiwaehner/ksql-udf-deep-learning-mqtt-iot/blob/master/src/main/java/com/github/megachucky/kafka/streams/machinelearning/Anomaly.java).

It is pretty easy to develop UDFs. Just implement the function in one Java method within a UDF class:

                @Udf(description = "apply analytic model to sensor input")
                public String anomaly(String sensorinput){ "YOUR LOGIC" }



## How to run it?

### Requirements
- Java 8
- [Confluent Platform](https://www.confluent.io/download/) (Confluent Enterprise if you want to use the Confluent MQTT Proxy, Confluent Open Source if you just want to run the KSQL UDF and send data via kafkacat instead of MQTT)
- MQTT Client (I use [Mosquitto](https://mosquitto.org/download/) in the demo)
- [kafkacat](https://github.com/edenhill/kafkacat) (optional - if you do not want to use MQTT Producers, and of course you can also use kafka-console-producer instead, but kafkacat is much more comfortable)

### Step-by-step demo
[Install Confluent Platform](https://www.confluent.io/download/) and then follow these steps to [deploy the UDF, create MQTT events and process them via KSQL leveraging the analytic model](https://github.com/kaiwaehner/ksql-udf-deep-learning-mqtt-iot/blob/master/live-demo.adoc).





