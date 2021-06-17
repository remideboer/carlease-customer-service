# Customer Service for Carlease assigment

## Functionality

RESTFull webservice facilitating CRUD operations on a Customer datastore Not using Spring REST repositories for this
service (even though that would speed things up tremendously)
Endpoints require a valid JWT issued by an API-gateway sharing the same secret

- [x] list customers
- [x] create customer
- [x] fetch customer by id
- [x] update customers attribute
- [x] delete specific customer
- [ ] fetch customers by name keyword
- [x] authentication
- [ ] containerize

Phasing depends on how much time can be made free

### URL mapping

- [x] Phase 1: Basic URL mapping docs
- [ ] Phase 2: Swagger docs / REST Docs

```
api prefix and version should be handled by an api gateway
- [x] GET:    {domain}/customers       - returns al customers ? possible filtering
- [x] GET:    {domain}/customers/{id}  - returns specific customer
- [x] POST    {domain}/customers       - create new customer
- [x] DELETE: {domain}/customers/{id}  - delete specific customer
- [x] PUT:    {domain}/customers/{id}  - update specific customer
```

Sample POST/PUT Body

```json
{
  "firstName": "fname",
  "lastName": "lname",
  "lastNamePrefix": null,
  "phoneNumber": "0101234567",
  "email": "post@test.nl",
  "street": "teststraat",
  "postalCode": "1234AB",
  "houseNumber": 123,
  "addition": null,
  "city": "teststad"
}
```

## Authentication

- [x] Phase 1: using JWT with using symmetric keys
- [ ] Phase 2: using JWT adding refresh token
- [ ] Phase 3: using JWT using asymmetric keys
- [ ] Phase 4: JWT facilitated by Keycloak

```shell
docker run -p 9090:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin quay.io/keycloak/keycloak:13.0.1
```

### Phase 1: JWT with using symmetric keys

This service shall not support login and generating JWT's that is delegated to another service. This service only needs
to be able to validate the token even though a first validation is performed at the system entry point. All endpoints
need authentication, so a global filter is used in front of the controllers to check for a valid JWT. User details are
not required by this service and can be made available in claims. Spring Security will therefore not be used by this
service. The signing secret is shared between services. From a security viewpoint not the most secure if a secret is
compromised.

- [x] Adding filter (OncePerRequest)
- [x] Unit Test validation of JWT
- [x] Integration Test application of filter
- [x] Modify already present tests with a mock filter

#### Implementation

JWT is evaluated in
a [```OncePerRequest```](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/filter/OncePerRequestFilter.html)
filter on signature integrity and on expiration using the [Nimbus JOSE](https://connect2id.com/products/nimbus-jose-jwt)
library.

## Run application

This service runs on port 9092

In development use dev profile:

```shell
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

When not using an authentication service to create a valid token got to [jwt.io](https://jwt.io/) set the expiration ```"exp"``` claim
to later then current UTC and use a secret in the dev profile:

JWT payload example
```json
{
  "sub": "1234567890",
  "name": "Jaap Test",
  "exp": 1624003711
}
```

```
// current development secret
secret=imasecretimasecretimasecretimasecret
```

### import

[docs](https://hub.docker.com/r/jboss/keycloak/)
