# Exchange Rate Service

This service continuously retrieves the EUR-to-USD exchange rate 
from the publicly available service at `http://api.fixer.io/`. 

## Configuration
The period of exchange rate retrievals is configurable.

## Endpoints
There is a single HTTP endpoint available. It supports both retrieving the latest
exchange rate and all rates within a provided time period.

GET calls:

    /rate?select=latest
    /rate?select=range&start_date=2017-09-23&end_date=2017-09-29
            
    
Response:

    [
        {
            "timestamp": "",
            "dollarsForEuro":1.0
        }
    ]
    
The timestamp in the response matches the timestamps received from the 
upstream service at `api.fixer.io`. It has day-granularity.

## Storage
There is no persistant storage set up. The retrieved exchange rates are merely 
kept in memory.

## Tests
To run unit test, please do:

    mvn test

#### TODOs:

* Properly serialize responses. Use dates.
* Make check interval configurable.
* Write some unit tests.


* Support a finer time range granularity than the current day-only granularity.
* Don't store new rate if it has the same value and timestamp (with regard to granularity).
* Improve hacky JSON handling.
* Add error descriptions to HTTP responses.
* Trim down Spring's chatty error responses.
* Use an async controller.
* Generalize for all exchange rates, not only EUR-to-USD.
* Point to a public exchange rate service with more frequent updates. 
The one at `api.fixer.io` updates only once a day. That's not very granular.
* Either use nice date/time classes everywhere or strings everywhere.
