# Exchange Rate Service

This service continuously retrieves the EUR-to-USD exchange rate 
from the publicly available service at `http://api.fixer.io/`. 

## Configuration
The period of exchange rate retrievals is configurable via the properties file located at:

    src/main/resources/application.properties

## Endpoints
There is a single HTTP endpoint available. It supports both retrieving the latest
exchange rate and all rates within a provided time period.

GET calls:

    /rate?select=latest
    /rate?select=range&start_date=2017-09-23&end_date=2017-09-29
            
Response:

    [
        {
            "timestamp": "2017-10-15",
            "dollarsForEuro":1.0
        }
    ]
    
The timestamp in the response matches the timestamps received from the 
upstream service at `api.fixer.io`. It has day-granularity.

## Storage
There is no persistent storage set up. The retrieved exchange rates are merely 
kept in memory.

## Tests
To run unit tests, do:

    mvn test

#### TODOs:

* Once an actual DB will be queried, add a cache to not always do I/O.
* Support a finer time range granularity than the current day-only granularity.
* Improve hacky JSON handling.
* Add error descriptions to HTTP responses.
* Trim down Spring's chatty error responses.
* Cut down on logic in controller.
* Use an async controller.
* Generalize for all exchange rates, not only EUR-to-USD.
* Point to a public exchange rate service with more frequent updates. 
The one at `api.fixer.io` updates only once a day. That's not very granular.
* Write more unit tests.
