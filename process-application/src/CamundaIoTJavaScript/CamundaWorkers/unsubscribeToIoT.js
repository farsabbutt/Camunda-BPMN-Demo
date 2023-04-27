var mqtt = require('mqtt')

const { Client, logger } = require("camunda-external-task-client-js");

// configuration for the Client:
//  - 'baseUrl': url to the Process Engine
//  - 'logger': utility to automatically log important events
const config = { baseUrl: "http://localhost:8080/engine-rest", use: logger };

// create a Client instance with custom configuration
const client = new Client(config);

// susbscribe to the topic
client.subscribe("unsubscribeToIoT", async function({ task, taskService }) {
  
  const mqttClient = mqtt.connect('mqtt://192.168.137.71:1883',{clientId: 'mqttClient',clean: true});

  mqttClient.unsubscribe('temperature_camundaIoT',console.log);
  
  mqttClient.end();
  // Put your business logic
  // complete the task
  await taskService.complete(task);
});