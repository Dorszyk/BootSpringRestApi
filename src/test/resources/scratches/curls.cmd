http://localhost:8190/restApi/swagger-ui/index.html

petstore3.swagger.io/api/v3/openapi.json

curl -i -X DELETE http://localhost:8190/restApi/employees/22

curl -i --location --request PATCH 'http://localhost:8190/restApi/employees/2/pet/10'

curl -i -X PATCH http://localhost:8190/restApi/employees/40/salary?newSalary=19221.00

curl -i -H "Accept: application/json" -H "httpStatus: 204" -X GET http://localhost:8190/restApi/employees/test-header

curl -i -X GET http://localhost:8190/restApi/employees/test-header

security
curl -i -H "Accept: application/json" -X GET http://localhost:8190/restApi/employees/2
curl -i -X POST http://localhost:8190/restApi/login --data-raw "username=user1&password=test"
curl -i -H "Accept: application/json" -H "Cookie: JSESSIONID=D3DE1726A5FE22310766B9E2A71D3274" -X GET http://localhost:8190/restApi/employees/7
curl -i -H "Accept: application/json" -H "Cookie: JSESSIONID=D3DE1726A5FE22310766B9E2A71D3274" -X GET http://localhost:8190/restApi/logout
