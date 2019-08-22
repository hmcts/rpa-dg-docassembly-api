FROM hmctspublic.azurecr.io/base/java:openjdk-8-distroless-1.1

COPY build/libs/rpa-dg-docassembly.jar /opt/app/

CMD ["rpa-dg-docassembly.jar"]

EXPOSE 8080
