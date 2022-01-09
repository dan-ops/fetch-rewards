# Fetch Rewards

Fetch Rewards assessment

## Prerequisites

### Java version 11 or greater must be installed

#### Windows JDK installation
Download zip from [https://jdk.java.net/17/](https://jdk.java.net/17/) and install.

Make sure the JAVA_PATH has been set. [Setting JAVA_PATH for windows](https://mkyong.com/java/how-to-set-java_home-on-windows-10/)

#### Mac JDK installation
You can follow these instructions for Mac OS. [Mac OS JDK installation](https://mkyong.com/java/how-to-install-java-on-mac-osx/)

## Application API Endpoints

The transactions API endpoint will be used to add transactions for a given payer. The transaction consists of the payer, points total and the timestamp.
```
POST /v1/points/transactions
```
---
The spend API endpoint will be used to spend points. The oldest points should be spent first and no payer should end up with a negative balance.
```
POST /v1/points/spend
```

---
The balance API endpoint will return the points balance for all payers.
```
GET /v1/points/balance
```

## Running The Application

Clone the repository and navigate to the root directory and run

```bash
./gradlew bootRun
```

Alternatively, you can also import the project into an IDE such as IntelliJ and run the application there

## Usage

Once the application is running we can test the API endpoints using something like Postman or from the terminal using curl

```bash
curl --location --request POST 'localhost:8080/v1/points/transactions' \
--header 'Content-Type: application/json' \
--data-raw '{
    "payer": "DANNON",
    "timestamp": "2022-01-01T12:00:00Z",
    "points": 100
}'
```

```bash
curl --location --request POST 'localhost:8080/v1/points/spend' \
--header 'Content-Type: application/json' \
--data-raw '{
    "points": 100
}'
```

```bash
curl --location --request GET 'localhost:8080/v1/points/balance'
```
