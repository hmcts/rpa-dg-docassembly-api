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
#Cloning repo and running though docker

git clone https://github.com/hmcts/dg-docassembly-api.git
cd rpa-dg-docassembly-api

az login
az acr login --name hmctspublic

docker-compose -f docker-compose-dependencies-simulator.yml pull
docker-compose -f docker-compose-dependencies-simulator.yml up

./gradlew assemble

To set up IDAM data run: ./idam-client-setup.sh 
To check the data you can log into IDAM-web-admin `http://localhost:8082` with:
Username: idamOwner@hmcts.net
Password: Ref0rmIsFun

DOCMOSIS_ACCESS_KEY=ZDYxMTkzZTQtMGY2Mi00NDM1LWIyN2ItNGRkNzdjOTczMjAwOjQ1NTE0ODQ ./gradlew bootRun
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
