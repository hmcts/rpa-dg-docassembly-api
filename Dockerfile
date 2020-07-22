ARG APP_INSIGHTS_AGENT_VERSION=2.5.1
FROM hmctspublic.azurecr.io/base/java:openjdk-11-distroless-1.4

COPY build/libs/rpa-dg-docassembly.jar lib/applicationinsights-agent-2.5.1.jar lib/AI-Agent.xml /opt/app/

CMD ["rpa-dg-docassembly.jar"]

EXPOSE 8080
