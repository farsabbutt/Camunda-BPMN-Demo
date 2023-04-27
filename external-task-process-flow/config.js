module.exports = {
  
    /**Declaration of databases for my development environment**/
      "development": {
          "databases": {
              "customer_DB": {
                  "database": process.env.RDS_DATABASE1, //you should always save these values in environment variables
                  "username": process.env.RDS_USERNAME1,  //only for testing purposes you can also define the values here
                  "password":  process.env.RDS_PASSWORD1,
                  "host": process.env.RDS_HOSTNAME1,
                  "port": process.env.RDS_PORT1,
                  "dialect": "postgres"  //here you need to define the dialect of your databse, in my case it is Postgres
              },
              "product_DB": {
                  "database": process.env.RDS_DATABASE2, 
                  "username": process.env.RDS_USERNAME2,  
                  "password":  process.env.RDS_PASSWORD2,
                  "host": process.env.RDS_HOSTNAME2,
                  "port": process.env.RDS_PORT2,
                  "dialect": "mssql"  //second database can have a different dialect
              },
          },
      }
  }