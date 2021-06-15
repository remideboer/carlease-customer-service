# Customer Service for Carlease assigment

## Functionality

RESTFull webservice facilitating CRUD operations on a Customer datastore Not using Spring REST repositories for this
service (even though that would speed things up tremendously)

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
- [ ] Phase 2: Swagger docs

```
api prefix and version should be handled by an api gateway
- [x] GET:    {domain}/customers       - returns al customers ? possible filtering
- [x] GET:    {domain}/customers/{id}  - returns specific customer
- [x] POST    {domain}/customers       - create new user
- [x] DELETE: {domain}/customers/{id}  - delete specific user
- [x] PUT:    {domain}/customers/{id}  - update specific user
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

```
development JWT
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNjIzNDk5NDk2fQ.0n3QQLClr65pSi59VebHVrts3pwzeo6zTeTK3Ru7wlU
```

### import

[docs](https://hub.docker.com/r/jboss/keycloak/)
