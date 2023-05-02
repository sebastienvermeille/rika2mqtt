# Module rika-firenet 

Essential module of this project, as it provides an http service to communicate with rika-firenet service.

## Architecture of this module

### Responsibilities

Ensure that the connection with rika-firenet is established gracefully and kept alive.

### Opinionated choices

**Question:** Why use Retrofit ?

**Answer:** I like the concept of having annotations for such external services. 

Reason? Because they are subject to change in time and retrofit do that magic out of the box.

**Question:** Why use Retrofit + HttpClient ?

**Answer:** At the moment I could not have retrofit "forgetting context". 
This prevents the method isValidCredentials() to be called repeatably with expected response.

The workaround I used it to use another more simple http client instead for that operation.
If you have a good design solution, please submit a PR.


### How does it work ?

Class `RikaFirenetApi` define the external calls to rika-firenet.

Then it is all wrapped into `RikaFirenetService` for convenience.

