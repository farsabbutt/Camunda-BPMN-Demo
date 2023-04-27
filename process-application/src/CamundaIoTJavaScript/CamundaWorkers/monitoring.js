var mqtt = require('mqtt');
const { Client, logger } = require("camunda-external-task-client-js");
const { Variables } = require("camunda-external-task-client-js");

const config = { baseUrl: "http://localhost:8080/engine-rest", use: logger };
const client = new Client(config);

client.subscribe("monitoring", async function({ task, taskService }) {

console.log("Camunda called"); 
const mqttClient = mqtt.connect('mqtt://192.168.137.71:1883',{clientId: 'mqttClient',clean: false});
console.log("mqtt connected");
mqttClient.on('connect',function(){
    mqttClient.subscribe('temperature_camundaIoT');
    
}); 
console.log("mqqtclient on called");

mqttClient.on('message', function(topic, message) {
        
    parsedMessage = JSON.parse(message);
    console.log("mqqtclient message recieved");//message recieved
    if(parsedMessage.temperature>50||parsedMessage.humidity>80){
        redAlert=1;
        console.log("Temp or Humidity too high"+ parsedMessage.humidity)    
            }
    else{
        redAlert=0;
        console.log("Temp or Humidity ok"+ parsedMessage.humidity); 
        }     
});


    
if (redAlert===1){
        // throw a iot error
    console.log("error alert # redAlert");
    const variables = new Variables().set('okTemp', 1);

    
   
    await taskService.complete(task,variables);
    }
else{

    
                
    const variables1 = new Variables().set('okTemp', 0);
    console.log("No error alert # NoAlert");
    // complete the task 
  
    await taskService.complete(task,variables1);       
    }  
   
});