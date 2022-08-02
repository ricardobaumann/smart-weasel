# Banking Transactions

Handles banking account and transactions

## Architecture and design

the service was built with Java and SpringBoot, persisting information on a dockerized PostgreSL instance. The entire
stack is configurable through the `docker-compose` file on the root folder.

On startup the database structure is created, and a sample customer is set up.

## Requirements

- docker
- docker-compose

## Setup

- On the root folder, execute `docker-compose up`
- Check the [Local URL](http://localhost:8080/swagger-ui.html). You should see the API documentation

## Usage

- The services starts with a customer

````
  "organization_name": "ACME Corp",
  "organization_bic": "OIVUSCLQXXX",
  "organization_iban": "FR10474608000002006107XXXXX"
````

with a balance of 100000 euros. Others can be added on the `Init.java` class.

- On the [Bulk Tnsaction endpoint](http://localhost:8080/swagger-ui/index.html#/bulk-transaction-controller/post), feel
  free to input the request samples and POST it.

