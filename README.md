# Currency exchange coding challenge

Video showcasing basic program functions:

[![Currency exchange coding challenge](https://yt-embed.herokuapp.com/embed?v=A1mc3FU8AMg)](https://youtu.be/A1mc3FU8AMg "Currency exchange coding challenge")


Appended .csv file for importing sample exchange rates can be found [here](https://pastebin.com/raw/064fMTax).

Online service used for getting new exchange rate data - https://www.exchangerate-api.com/.

# Possible REST API endpoints

Postman collection can be found [here](https://pastebin.com/raw/3zcfG2Fx).

* `POST localhost:8080/exchange/rates` Used for uploading a .csv file with exchange rates that is attached in the request body.
* `GET localhost:8080/exchange/rates` Used to get all exchange rates currently in the system.
* `POST localhost:8080/exchange/clear` Used for clearing all the exchange rates.
* `GET localhost:8080/exchange/convert` Used for converting currencies based on user input. This information is passed in JSON format. Example :
`{
"currencyQuantity" : "16",
"currencyFrom" : "EUR",
"currencyTo" : "USD"
}`
* `POST localhost:8080/exchange/ratesOnline` Can be used for adding exchange rates that are received from an external online service.

Base currency and api key can be changed in application.properties
