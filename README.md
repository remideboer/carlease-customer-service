# Customer Service for Carlease assigment

## Functionality

RESTFull webservice facilitating CRUD operations on a Customer datastore 
Not using Spring REST repositories for this service (even though that would speed things up tremendously)

- [x] list customers
- [x] create customer
- [ ] fetch customer by id
- [ ] update customers attribute
- [ ] delete specific customer
- [ ] fetch customers by name keyword
- [ ] authentication
- [ ] containerize

Phasing depends on how much time can be made free

### URL mapping
- [x] Phase 1: Basic URL mapping docs
- [ ] Phase 2: Swagger docs
```
api prefix and version should be handled by an api gateway
GET:    {domain}/customers       - returns al customers ? possible filtering
GET:    {domain}/customers/{id}  - returns specific customer
POST    {domain}/customers       - create new user
DELETE: {domain}/customers/{id}  - delete specific user
PUT:    {domain}/customers/{id}  - update specific user
```
## Authentication

- [ ] Phase 1: using JWT with using symmetric keys
- [ ] Phase 2: using JWT adding refresh token
- [ ] Phase 3: using JWT using asymmetric keys
- [ ] Phase 4: JWT facilitated by Keycloak

```shell
docker run -p 9090:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin quay.io/keycloak/keycloak:13.0.1
```

### import 
[docs](https://hub.docker.com/r/jboss/keycloak/)
