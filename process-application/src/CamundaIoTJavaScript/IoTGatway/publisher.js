var sensor = require("node-dht-sensor");
var mqtt = require("mqtt");

var mqttClient = mqtt.connect("mqtt://localhost:1883");
console.log('mqtt called');

mqttClient.on('connect',()=> {
 	console.log('mqtt pub called');
	
	setInterval(()=> {
		var sensorResult = sensor.read(11,17) 
		let timestamp =new Date();
		timestamp.setMinutes(timestamp.getMinutes()- timestamp.getTimezoneOffset())
		const message = {  
			id:'sensor_dht11',
			temperature:sensorResult.temperature,
			humidity:sensorResult.humidity ,
			timestamp:timestamp,
			location:'shopfloor' }
		
		console.log(JSON.stringify(message));
                mqttClient.publish('temperature_camundaIoT',JSON.stringify(message));

		 },3000);
	})
