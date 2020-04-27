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
az acr login --name hmcts --subscription 1c4f0704-a29e-403d-b719-b90c34ef14c9
./gradlew assemble
docker-compose -f docker-compose-dependencies.yml pull
docker-compose -f docker-compose-dependencies.yml up --build
```
To set up IDAM data install `https://stedolan.github.io/jq/`. 
For linux: `sudo apt-get install jq`. 
For mac: `brew install jq`.

Then run: `./idam-client-setup.sh`. 


To check the data you can log into IDAM-web-admin `http://localhost:8082` with:
Username `idamOwner@hmcts.net`
Password `Ref0rmIsFun`

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