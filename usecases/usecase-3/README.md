##  Introduction

In this PoC we try accessing Schema Registry to download schema info object via URL from an external host


# Build and Test Locally

### prerequisites
* install docker, docker compose

## steps

Open command prompt in root directory of the repository, then:

1. create and run the infrastructure

    ```
    docker compose up -d
    ```
2. publish test schemas into topics:
    ```
   ./publish-schemas.sh
    ```
4. check schemas via GET method of Pulsar's Admin API, open http//:localhost:80 via browser ( NB enable 'allow-CORS' plugin for your browser)
    Use Swagger UI to invoke GET schemas method  (tenant and namespace are defaulted to [public] and [default] )

    for [people] topic 
    ```
    http://localhost:8080/admin/v2/schemas/public/default/people/schema/?authoritative=false
  
    ```
    
    for [assets] topic  
    ```

    http://localhost:8080/admin/v2/schemas/public/default/assets/?authoritative=false
    
    ```

