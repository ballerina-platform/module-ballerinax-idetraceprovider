## Package Overview

The IdeTraceProvider Observability Extension is a lightweight tracing extension for the <a target="_blank" href="https://ballerina.io/">Ballerina</a> language.

It provides a simple HTTP-based implementation for tracing and publishing traces to an OTLP/HTTP compatible trace collector, optimized for IDE and development environments.

## Enabling IdeTraceProvider Extension

To package the IdeTraceProvider extension into the Jar, follow these steps:

1. Add the following import to your program:
```ballerina
import ballerinax/idetraceprovider as _;
```

2. Add the following to the `Ballerina.toml` when building your program:
```toml
[package]
org = "my_org"
name = "my_package"
version = "1.0.0"

[build-options]
observabilityIncluded=true
```

3. To enable the extension and publish traces, add the following to the `Config.toml` when running your program:
```toml
[ballerina.observe]
tracingEnabled=true
tracingProvider="idetraceprovider"

[ballerinax.idetraceprovider]
endpoint="http://localhost:59500/v1/traces"  # Optional. Default value is "http://localhost:59500/v1/traces"
```
