/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.megachucky.kafka.streams.machinelearning;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;


/**
 * @author Kai Waehner
 */
public class KsqlUdfDeepLearningMqttIotExample {

	private static final String imageInputTopic = "ImageInputTopic";
	private static final String imageOutputTopic = "ImageOutputTopic";

	private static final String server = "localhost";
	private static final Integer port = 9000;

	// Image path will be received from Kafka message to topic 'imageInputTopic'
	private static String imagePath = null;

	public static void main(String[] args) throws Exception {

		// Configure Kafka Streams Application
		final String bootstrapServers = args.length > 0 ? args[0] : "localhost:9092";

		final Properties streamsConfiguration = new Properties();

		// Give the Streams application a unique name. The name must be unique
		// in the Kafka cluster against which the application is run.
		streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-tensorflow-serving-gRPC-example");

		// Where to find Kafka broker(s).
		streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

		// Specify default (de)serializers for record keys and for record
		// values.
		streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

		// In the subsequent lines we define the processing topology of the Streams
		// application.
		final StreamsBuilder builder = new StreamsBuilder();

		// Construct a `KStream` from the input topic "ImageInputTopic", where
		// message values represent lines of text
		final KStream<String, String> imageInputLines = builder.stream(imageInputTopic);
		
		
		KStream<String, Object> transformedMessage = imageInputLines.mapValues(value -> {

			System.out.println("Image path: " + value);

			imagePath = value;

//			TensorflowObjectRecogniser recogniser = new TensorflowObjectRecogniser(server, port);

			System.out.println("Image = " + imagePath);
			InputStream jpegStream;
			try {
				jpegStream = new FileInputStream(imagePath);
				
				// Prediction of the TensorFlow Image Recognition model:
//				List<Map.Entry<String, Double>> list = recogniser.recognise(jpegStream);
//				String prediction = list.toString();
//				System.out.println("Prediction: " + prediction);
//				recogniser.close();
				jpegStream.close();
   
//                                 return prediction;
				return null;
			} catch (Exception e) {
				e.printStackTrace();
      
                                 return Collections.emptyList().toString();
			}

		});
		


		// Send prediction information to Output Topic
		transformedMessage.to(imageOutputTopic);

		// Start Kafka Streams Application to process new incoming images from the Input
		// Topic
		final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);

		streams.cleanUp();

		streams.start();

		System.out.println("Image Recognition Microservice is running...");

		System.out.println("Input images arrive at Kafka topic " + imageInputTopic + "; Output predictions going to Kafka topic "
				+ imageOutputTopic);

		// Add shutdown hook to respond to SIGTERM and gracefully close Kafka
		// Streams
		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

	}
}
