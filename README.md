# rpa-dg-doc-assembly-api
 A Restful API that facilitates the functioning of the doc-assembly web component, by proxying calls to external services, and aggregating backend calls

# Setup

This service relies heavily on Docmosis API and access to templates available for Docmosis instance.

Turn on your vpn and use the following system variables to provide correct URLs and access keys:

- DOCMOSIS_TEMPLATES_ENDPOINT
- DOCMOSIS_TEMPLATES_ENDPOINT_AUTH
- DOCMOSIS_ACCESS_KEY
- DOCMOSIS_ENDPOINT

```
az login
az acr login --name hmctspublic && az acr login --name hmctsprivate
./bin/start-local-environment.sh
./gradlew assemble
DOCMOSIS_ACCESS_KEY=<DOCMOSIS_ACCESS_KEY> ./gradlew bootRun
```


### Running contract or pact tests:

You can run contract or pact tests as follows:
```
./gradlew clean
```

```
./gradlew contract
```

You can then publish your pact tests locally by first running the pact docker-compose:

```
docker-compose -f docker-pactbroker-compose.yml up
```

and then using it to publish your tests:

```
./gradlew pactPublish
```
