# rpa-dg-doc-assembly-api
 A Restful API that facilitates the functioning of the doc-assembly web component, by proxying calls to external services, and aggregating backend calls

# Setup

This service relies heavily on Docmosis API and access to templates available for Docmosis instance.

Use the following system variables to provide correct URLs and access keys:

- DOCMOSIS_TEMPLATES_ENDPOINT
- DOCMOSIS_TEMPLATES_ENDPOINT_AUTH
- DOCMOSIS_ACCESS_KEY
- DOCMOSIS_ENDPOINT

```
az login
az acr login --name hmcts --subscription 1c4f0704-a29e-403d-b719-b90c34ef14c9
./gradlew assemble
docker-compose -f docker-compose-demo.yml pull
docker-compose -f docker-compose-demo.yml build
docker-compose -f docker-compose-demo.yml up
```


