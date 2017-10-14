# Exchange Service Task

Develop a service that constantly checks the currency exchange rate from Euro to US-Dollar (1 Euro = x Dollar).

* The check period has to be configurable and the results are stored in a database.
* The service has an HTTP-Resource with the following endpoints (The protocol design is up to you) :
    1. Get latest rate
    2. Get historical rates from startDate to endDate
* Please ensure the functionality of the business logic using unit-tests
* The exchange rate can be taken from a public service or be mocked.
* The database access does not need to be fully implemented, an interface is sufficient.
* The project should be managed with maven and the tests have to be executable using 'mvn test'.
* The service itself does not need to be executable.
