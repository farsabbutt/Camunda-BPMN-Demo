var mongoose = require('mongoose');
var mqtt = require('mqtt')
const { Client, logger } = require("camunda-external-task-client-js");

var Schema = mongoose.Schema;



///////////////////////////////////
/////// MONGODB  //////////////////
//////////////////////////////////
//Connection to the a mongodb database path localhost:port/nameOfCollection

mongoose.connect('mongodb://localhost:27017/camunda_project', {useNewUrlParser: true, useUnifiedTopology: true});

const db = mongoose.connection;
db.on('error', console.error.bind(console, 'connection error:'));
db.once('open', function() {
    console.log('connection to Mongodb Successful !') ;
    });
//Definition of a schema
var Temperature = new Schema({
    ProductionOrderId: Number,
    sensor_id: String,

    temp: Number,
    humidity:Number,
    location: String,
    timestamps:String

});
console.log('schema created');
//Creation of the model
var TemperatureModel = mongoose.model('temperatures', Temperature);
console.log('model created')
//camunda bpmn
// create a Client instance with custom configuration
const config = { baseUrl: "http://localhost:8080/engine-rest", use: logger };
const client = new Client(config);

// susbscribe to the BPMN topic:
client.subscribe("SubscribeToIoT", async function({ task, taskService }) {

        var mproductionOrderId =task.variables.get("productionOrderId");

        ///////////////////////////////////
        /////// MQTT  //////////////////
        //////////////////////////////////      
        const mqttClient = mqtt.connect('mqtt://192.168.137.71:1883',{clientId: 'mqttClient',clean: false});
        console.log("mqtt connected");
        mqttClient.on('connect',function(){
            mqttClient.subscribe('temperature_camundaIoT');
             });     
        console.log("topic subscribed");
        
        mqttClient.on('message', function(topic, message) {
           const parsedMessage = JSON.parse(message);
           var myTemperatureModel = new TemperatureModel({ProductionOrderId:mproductionOrderId,sensor_id:parsedMessage.id,temp:parsedMessage.temperature,humidity:parsedMessage.humidity,location:parsedMessage.location,timestamps:parsedMessage.timestamp});
           myTemperatureModel.save();
               
            //console.log("document updated ");
        });
        //console.log("document updated ");
        await taskService.complete(task);   
      
});
