## Withdrawal technical assessment

### Steps on how to run the app
For testing on a local environment please consider the following:

* The default link locally for testing using Swagger is http://localhost:8009/swagger-ui/index.html.
* I expose 2 endpoints via Swagger , one is for testing withdrawals and another is for adding new accounts for test purposes.
* On startup the application adds one account that can be used for testing.
* Something to note on a successful withdrawal , the SNS notifcation bit will fail (as we need to configure that bit on AWS accordingly with the correct credentials) but that failure should not stop the customer from making the withdrawal.
* Note for auditing we have an audit table for all attempts from a customer to withdraw  , this includes failed and successful withdrawals.
* Over an above this we also have a transaction table that stores activity related to successful transactions on the account. 
* H2 test DB can be accessed on http://localhost:8009/h2-console/* 
 
 
For details about the package structure, refactoring or any questions around the design , please contact send the any queries to thaboma@gmail.com
