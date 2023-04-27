// MQTT publisher
var mqtt = require('mqtt')
var mqttClient= mqtt.connect('mqtt://localhost:1883')

const randomSensorValue = (min, max) => Math.random() * (max - min) + min;

mqttClient.on('connect', () => {
    console.log('Publisher connected to MQTT broker');
    setInterval(() => {
        const value = randomSensorValue(1, 49);// random Temprature value
        let d = new Date();
        d.setMinutes(d.getMinutes() - d.getTimezoneOffset())
    
        const message = {
            id: 'my-sensor',
            type: 'temperature',
            location: 'machine1',
            timestamp: d,
            value
        }
        console.log(JSON.stringify(message));
        mqttClient.publish('temperature_camundaIoT', JSON.stringify(message));
    }, 1000);
})