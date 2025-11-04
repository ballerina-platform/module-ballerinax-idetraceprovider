/*
 * Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com) All Rights Reserved.
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ballerina.observe.trace.idetraceprovider;

import io.ballerina.runtime.api.values.BString;
import io.ballerina.runtime.observability.tracer.spi.TracerProvider;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SdkTracerProviderBuilder;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;

import java.io.PrintStream;

/**
 * IdeTraceProvider - A lightweight HTTP-based tracer provider for IDE development and debugging.
 */
public class IdeTracerProvider implements TracerProvider {
    private static final String TRACER_NAME = "idetraceprovider";
    private static final PrintStream console = System.out;
    private static final AttributeKey<String> SERVICE_NAME = AttributeKey.stringKey("service.name");

    static SdkTracerProviderBuilder tracerProviderBuilder;

    @Override
    public String getName() {
        return TRACER_NAME;
    }

    @Override
    public void init() {
        // Initialization handled in initializeConfigurations
    }

    public static void initializeConfigurations(BString endpoint) {

        // Use OpenTelemetry's standard OTLP/HTTP exporter
        OtlpHttpSpanExporter exporter = OtlpHttpSpanExporter.builder()
                .setEndpoint(endpoint.getValue())
                .build();

        tracerProviderBuilder = SdkTracerProvider.builder()
                .setSampler(Sampler.alwaysOn())  // Capture all traces for development
                .addSpanProcessor(SimpleSpanProcessor.create(exporter));  // Flush immediately for real-time debugging

        console.println("ballerina: started publishing traces to IdeTraceProvider (OTLP/HTTP) on "
                + endpoint + " (real-time mode)");
    }

    @Override
    public Tracer getTracer(String serviceName) {
        return tracerProviderBuilder.setResource(
                Resource.create(Attributes.of(SERVICE_NAME, serviceName)))
                .build().get(TRACER_NAME);
    }

    @Override
    public ContextPropagators getPropagators() {
        return ContextPropagators.create(W3CTraceContextPropagator.getInstance());
    }
}
