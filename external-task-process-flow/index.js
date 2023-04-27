import { Client, logger, Variables } from 'camunda-external-task-client-js'
import mysql from 'mysql'
import nodeMailer from "nodemailer"


// configuration for the Client:
//  - 'baseUrl': url to the Process Engine
//  - 'logger': utility to automatically log important events

const baseUrl = `http://${process.env.CAMUNDA_ENGINE_HOST}:${process.env.CAMUNDA_ENGINE_PORT}/engine-rest`

const config = { baseUrl: baseUrl, use: logger };

// create a Client instance with custom configuration
const client = new Client(config);

// susbscribe to the topic: 'creditScoreChecker'
client.subscribe("StoreCustomerOrder", async function({ task, taskService }) {
  const processVariables = new Variables();
    console.log("Storing Customer order.....", task.variables.getAll())
    let insertId = 0;
   try {

    const vars = task.variables.getAll()

    const name = vars.name;
    const email = vars.email;
    const phoneNumber = vars.phoneNumber;
    const address = vars.address;
    const product = vars.product;
    const quantity = vars.quantity;

    

    var connection = mysql.createConnection({
        host     : process.env.MYSQL_HOST_NAME,
        user     : process.env.MYSQL_USER,
        password : process.env.MYSQL_PASSWORD,
        database : process.env.MYSQL_DATABASE
      });
       
      connection.connect();
       
      connection.query('INSERT INTO `customer_order` (`id`, `name`, `email`, `phone`, `address`, `product`, `quantity`, `orderStatus`) VALUES (NULL, "' + name +'", "' + email +'", "'+ phoneNumber +'", "' + address + '", "' + product + '", "'+ quantity + '", "");', 
      async function (error, results, fields) {
        if (error) {
          throw error;
        }
        console.log("insertId:",results.insertId)
        insertId = results.insertId
        console.log('Results: ', JSON.stringify(results));

        try {
          const processVariables = new Variables()
          processVariables.set('orderId', insertId)
          await taskService.complete(task, processVariables); 
        } catch(error) {
          logger.error(error)
        }

      });
  
      connection.end();

   } catch (error) {
    logger.error(error)
   }

    
});


// Update customer order status to REJECTED
client.subscribe("updateCustomerOrderStatusRejected", async function ({ task, taskService }) {
  console.log("Updating Customer order status to ORDER_REJECTED.....", task.variables.getAll())
  try {
    const userId = parseInt(task.variables.get('userId'))
    const orderId = parseInt(task.variables.get('orderId'))
    var connection = mysql.createConnection({
      host: process.env.MYSQL_HOST_NAME,
      user: process.env.MYSQL_USER,
      password: process.env.MYSQL_PASSWORD,
      database: process.env.MYSQL_DATABASE
    });

    connection.connect();

    // connection.query('UPDATE `customer_order` SET `orderStatus` = "ORDER_REJECTED" WHERE `customer_order`.`id` = ' + userId + ';',
    // function (error, results, fields) {
    //   if (error) throw error;
    //   console.log('Results: ', JSON.stringify(results));
    // });

    connection.query('UPDATE `customer_order` SET `orderStatus` = "ORDER_REJECTED" WHERE `customer_order`.`id` = ' + orderId + ';',
      function (error, results, fields) {
        if (error) throw error;
        console.log('Results: ', JSON.stringify(results));
      });

    connection.end();

  } catch (error) {
    logger.error(error)
  }


  await taskService.complete(task);
});


// Subscribe to the topic: 'SendRejectionEmail' and send a order rejection email to the customer
client.subscribe("SendRejectionEmail", async function ({ task, taskService }) {
  console.log("Sending Rejection Email.....", task.variables.getAll())

  // Generate test SMTP service account from ethereal.email
  // Only needed if you don't have a real mail account for testing
  // let testAccount = await nodeMailer.createTestAccount();

  // create reusable transporter object using the default SMTP transport
  let transporter = nodeMailer.createTransport({
    host: "smtp.ethereal.email",
    port: 587,
    // secure: false, // true for 465, false for other ports
    auth: {
      user: "llewellyn51@ethereal.email", // generated ethereal user
      pass: "GRG1VpEbj5vcDcHJ1a", // generated ethereal password
    },
  });


  // send mail with defined transport object
  let info = await transporter.sendMail({
    from: '"Bicycle Company 👻" <foo@example.com>', // sender address
    to: ["rahibbutt@gmail.com"], // list of receivers
    subject: "Your order has been rejected ✔", // Subject line
    text: "We're sorry, your order has been rejected because we are currently only accepting single bicycle orders.", // plain text body
    html: "<b>We're sorry, your order has been rejected because we are currently only accepting single bicycle orders.</b>", // html body
  });

  console.log("Message sent: %s", info.messageId);

  // Preview only available when sending through an Ethereal account
  console.log("Preview URL: %s", nodeMailer.getTestMessageUrl(info));

  await taskService.complete(task);
});


// susbscribe to the topic: 'SendConfirmationEmail'
client.subscribe("SendConfirmationEmail", async function ({ task, taskService }) {
  console.log("Sending Confirmation Email.....", task.variables.getAll())

  // Generate test SMTP service account from ethereal.email
  // Only needed if you don't have a real mail account for testing
  // let testAccount = await nodeMailer.createTestAccount();

  // create reusable transporter object using the default SMTP transport
  let transporter = nodeMailer.createTransport({
    host: "smtp.ethereal.email",
    port: 587,
    // secure: false, // true for 465, false for other ports
    auth: {
      user: "llewellyn51@ethereal.email", // generated ethereal user
      pass: "GRG1VpEbj5vcDcHJ1a", // generated ethereal password
    },
  });


  // send mail with defined transport object
  let info = await transporter.sendMail({
    from: '"Bicycle Company 👻" <foo@example.com>', // sender address
    to: ["rahibbutt@gmail.com"], // list of receivers
    subject: "Your order was confirmed ✔", // Subject line
    text: "Good news! Your order has been confirmed.", // plain text body
    html: "<b>Good news! Your order has been confirmed.</b>", // html body
  });

  console.log("Message sent: %s", info.messageId);

  // Preview only available when sending through an Ethereal account
  console.log("Preview URL: %s", nodeMailer.getTestMessageUrl(info));

  await taskService.complete(task);
});


// Update customer order status to APPROVED
client.subscribe("updateCustomerOrderStatusApproved", async function ({ task, taskService }) {
  console.log("Updating Customer order status to ORDER_APPROVED.....", task.variables.getAll())
  try {
    const userId = parseInt(task.variables.get('userId'))
    const orderId = parseInt(task.variables.get('orderId'))
    var connection = mysql.createConnection({
      host: process.env.MYSQL_HOST_NAME,
      user: process.env.MYSQL_USER,
      password: process.env.MYSQL_PASSWORD,
      database: process.env.MYSQL_DATABASE
    });

    connection.connect();

    // connection.query('UPDATE `customer_order` SET `orderStatus` = "ORDER_REJECTED" WHERE `customer_order`.`id` = ' + userId + ';',
    // function (error, results, fields) {
    //   if (error) throw error;
    //   console.log('Results: ', JSON.stringify(results));
    // });

    connection.query('UPDATE `customer_order` SET `orderStatus` = "ORDER_APPROVED" WHERE `customer_order`.`id` = ' + orderId + ';',
      function (error, results, fields) {
        if (error) throw error;
        console.log('Results: ', JSON.stringify(results));
      });

    connection.end();

  } catch (error) {
    logger.error(error)
  }


  await taskService.complete(task);
});


//Check finished product availability
client.subscribe("checkFinishedProductAvailability", async function ({ task, taskService }) {
  console.log("Checking if product available", task.variables.getAll())
  const vars = task.variables.getAll()
  const name = vars.name;
  const email = vars.email;
  const phoneNumber = vars.phoneNumber;
  const address = vars.address;
  const product = vars.product;
  const quantity = vars.quantity;


  var connection = mysql.createConnection({
    host: process.env.MYSQL_HOST_NAME,
    user: process.env.MYSQL_USER,
    password: process.env.MYSQL_PASSWORD,
    database: process.env.MYSQL_DATABASE_FINISHED_PRODUCT
  });
  console.log("Connection to finished_products DB established...")

  connection.connect(function (err) {
    if (err) throw err;
    connection.query('SELECT * FROM `product_stock`', function (err, result, fields) {
      if (err) throw err;
      console.log('finished products available : ', JSON.stringify(result));
    });
  });

  // Uncomment the random function below if you wish that the process should randomnly determine if the finished product is available or not
  // const isProductAvailable = Math.random() >= 0.5;
  const isProductAvailable = false;

  const processVariables = new Variables()
  processVariables.setTyped(
    "isAvailable", 
    {
    type: "Boolean",
    value: isProductAvailable,
    valueInfo: {}
  })


  await taskService.complete(task, processVariables);
});



// Update customer order status to 'PRODUCTION'
client.subscribe("updateCustomerOrderStatusProduction", async function ({ task, taskService }) {
  console.log("Updating Customer order status to PRODUCTION.....", task.variables.getAll())
  try {
    const userId = parseInt(task.variables.get('userId'))
    const orderId = parseInt(task.variables.get('orderId'))
    var connection = mysql.createConnection({
      host: process.env.MYSQL_HOST_NAME,
      user: process.env.MYSQL_USER,
      password: process.env.MYSQL_PASSWORD,
      database: process.env.MYSQL_DATABASE
    });

    connection.connect();

    // connection.query('UPDATE `customer_order` SET `orderStatus` = "ORDER_REJECTED" WHERE `customer_order`.`id` = ' + userId + ';',
    // function (error, results, fields) {
    //   if (error) throw error;
    //   console.log('Results: ', JSON.stringify(results));
    // });

    connection.query('UPDATE `customer_order` SET `orderStatus` = "ORDER_UNDER_PRODUCTION" WHERE `customer_order`.`id` = ' + orderId + ';',
      async function (error, results, fields) {
        if (error) throw error;
        console.log('Results: ', JSON.stringify(results));
        await taskService.complete(task);
      });

    connection.end();

  } catch (error) {
    logger.error(error)
  }


});

// Update customer order status to 'SHIPPED'
client.subscribe("UpdateCustomerOrderStatusShipped", async function ({ task, taskService }) {
  console.log("Updating Customer order status to SHIPPED.....", task.variables.getAll())
  try {
    const userId = parseInt(task.variables.get('userId'))
    const orderId = parseInt(task.variables.get('orderId'))
    var connection = mysql.createConnection({
      host: process.env.MYSQL_HOST_NAME,
      user: process.env.MYSQL_USER,
      password: process.env.MYSQL_PASSWORD,
      database: process.env.MYSQL_DATABASE
    });

    connection.connect();

    connection.query('UPDATE `customer_order` SET `orderStatus` = "ORDER_SHIPPED" WHERE `customer_order`.`id` = ' + orderId + ';',
      async function (error, results, fields) {
        if (error) throw error;
        console.log('Results: ', JSON.stringify(results));
        await taskService.complete(task);
      });

    connection.end();

  } catch (error) {
    logger.error(error)
  }


});


// Release customer order
client.subscribe("releaseCustomerOrder", async function ({ task, taskService }) {
  console.log("Releasing customer order to production pool...", task.variables.getAll())

  await taskService.complete(task);


});


// Store production order into 'production_DB'
client.subscribe("StoreProductionOrder", async function ({ task, taskService }) {
  const processVariables = new Variables();
    console.log("Storing production order in production_DB...", task.variables.getAll())
    let insertId = 0;

   try {
    const vars = task.variables.getAll()
    const name = vars.selectProduct;
    const quantity = vars.selectQuantity;

    var connection = mysql.createConnection({
        host     : process.env.MYSQL_HOST_NAME,
        user     : process.env.MYSQL_USER,
        password : process.env.MYSQL_PASSWORD,
        database : process.env.MYSQL_DATABASE_PRODUCTION
      });
       
      connection.connect();
       
      connection.query('INSERT INTO `production_order` (`id`, `name`, `quantity`) VALUES (NULL, "' + name +'", "'+ quantity + '");', 
      async function (error, results, fields) {
        if (error) {
          throw error;
        }
        console.log("insertId:",results.insertId)
        insertId = results.insertId
        console.log('Results: ', JSON.stringify(results));

        try {
          const processVariables = new Variables()
          processVariables.set('orderId', insertId)
          await taskService.complete(task, processVariables); 
        } catch(error) {
          logger.error(error)
        }

      });
  
      connection.end();

   } catch (error) {
    logger.error(error)
   }
});


// Randomn function to check components availability in components database
client.subscribe("CheckComponentsAvailability", async function ({ task, taskService }) {
  console.log("Checking if product available", task.variables.getAll())
  const vars = task.variables.getAll()
  const name = vars.selectProduct;
  const quantity = vars.selectQuantity;


  var connection = mysql.createConnection({
    host: process.env.MYSQL_HOST_NAME,
    user: process.env.MYSQL_USER,
    password: process.env.MYSQL_PASSWORD,
    database: process.env.MYSQL_DATABASE_FINISHED_PRODUCT
  });
  console.log("Connection to components_DB established...")

  connection.connect(function (err) {
    if (err) throw err;
    connection.query('SELECT * FROM `product_stock`', function (err, result, fields) {
      if (err) throw err;
      console.log('components available : ', JSON.stringify(result));
    });
  });

  // Uncomment the random function below if you wish that the process should randomnly determine if all the components are available or not
  // const isProductAvailable = Math.random() >= 0.5;
  const isComponentAvailable = false;

  const processVariables = new Variables()
  processVariables.setTyped(
    "allComponentsAvailable", 
    {
    type: "Boolean",
    value: isComponentAvailable,
    valueInfo: {}
  })


  await taskService.complete(task, processVariables);
});



// Start warehouse operation to move components inside the Warehouse
client.subscribe("startWarehouseOperation", async function ({ task, taskService }) {
    console.log("Warehouse operation has started!")
    const vars = task.variables.getAll()
    await taskService.complete(task, vars);
    console.log("Warehouse operation completed successfully!", task.variables.getAll())
});

// Start Warehouse operation to store the finished product
client.subscribe("startWarehouseOperationStoreProduct", async function ({ task, taskService }) {
  console.log("Operation to store the finished product initiated inside the Warehouse...")
  const vars = task.variables.getAll()
  await taskService.complete(task, vars);
  console.log("Operation of storing the finished product inside Warehouse completed!", task.variables.getAll())
});


//Send production order
client.subscribe("sendProductionOrder", async function ({ task, taskService }) {
  console.log("Sending production order initiated...", task.variables.getAll())
  const vars = task.variables.getAll()

  // Uncomment the random function below if you wish that the process should randomnly determine to be marked as 'completed' or 'stopped'
  // const isProductAvailable = Math.random() >= 0.5;
  const productionStatus = true;

  const processVariables = new Variables()
  processVariables.setTyped(
    "productionStatusPass", 
    {
    type: "Boolean",
    value: productionStatus,
    valueInfo: {}
  })

  await taskService.complete(task, processVariables, vars);
  console.log("Production order status sent successfully!")
});

// Production order completed - Message intermediate throw event from production worker pool to order management pool
client.subscribe("productionOrderCompleted", async function ({ task, taskService }) {
  await taskService.complete(task);
  console.log("Production order completed and sent to the order management successfully!", task.variables.getAll())
});


// Update customer order status to 'PRODUCTION'
client.subscribe("updateCustomerOrderStatusShipped", async function ({ task, taskService }) {
  console.log("Updating Customer order status to SHIPPED...", task.variables.getAll())
  try {
    const userId = parseInt(task.variables.get('userId'))
    const orderId = parseInt(task.variables.get('orderId'))
    var connection = mysql.createConnection({
      host: process.env.MYSQL_HOST_NAME,
      user: process.env.MYSQL_USER,
      password: process.env.MYSQL_PASSWORD,
      database: process.env.MYSQL_DATABASE
    });

    connection.connect();

    connection.query('UPDATE `customer_order` SET `orderStatus` = "ORDER_SHIPPED" WHERE `customer_order`.`id` = ' + orderId + ';',
      async function (error, results, fields) {
        if (error) throw error;
        console.log('Results: ', JSON.stringify(results));
        await taskService.complete(task);
      });

    connection.end();

  } catch (error) {
    logger.error(error)
  }


});


// Start warehouse operation to prepare the order for the shipment
client.subscribe("startWarehouseOperationPrepareShipment", async function ({ task, taskService }) {
  console.log("Preparing for shipment inside the warehouse...")
  const vars = task.variables.getAll()
  await taskService.complete(task, vars);
  console.log("Order sent for delivery!", task.variables.getAll())
});


// ${execution.getProcessEngineServices().getRuntimeService().createMessageCorrelation("ActionService").processInstanceId(execution.getProcessInstanceId()).setVariable("test", "hello").setVariables(execution.getProcessEngineServices().getRuntimeService().getVariables(execution.getProcessInstanceId())).correlateWithResult()}

