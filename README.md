### example CURLs:
curl -X GET http://localhost:8080/api/products

curl -X POST http://localhost:8080/api/quotes \
-H "Content-Type: application/json" \
-d '{"productId": 1, "customerDateOfBirth": "1990-01-01", "coverageAmount": 200000}'

curl -X POST http://localhost:8080/api/policies \
-H "Content-Type: application/json" \
-d '{"productId": 1, "customerName": "Alice", "customerDateOfBirth": "1990-01-01", "customerEmail": "alice@example.com", "coverageAmount": 200000}'


# InsuranceAPI
InsuranceAPI deals with insurance products, premium calculation, and policy creation
