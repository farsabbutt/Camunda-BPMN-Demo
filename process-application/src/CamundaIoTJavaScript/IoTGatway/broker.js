
var mosca = require ('mosca')
//var settings = {1883}

var broker = new mosca.Server(1883)

broker.on('ready',()=> {
      console.log('Broker is ready!')
    })
