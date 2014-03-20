
/**
 * Module dependencies.
 */

var express = require('express');
var routes = require('./routes');
var user = require('./routes/user');
var http = require('http');
var path = require('path');

var app = express();

// all environments
app.set('port', process.env.PORT || 56789);
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.json());
app.use(express.urlencoded());
app.use(express.methodOverride());
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));

// development only
if ('development' == app.get('env')) {
  app.use(express.errorHandler());
}

app.get('/', routes.index);
app.get('/users', user.list);

var counter = 0;
http.createServer(app).listen(app.get('port'), function(){
  console.log('Express server listening on port ' + app.get('port'));
  setInterval(connectRemoteServer, 10);
});
var net = require('net');
var HOST = '54.203.251.40';
var PORT = 8000;
var connectRemoteServer=function(){
        if(counter > 50000) {
            console.log('Reached max size 27000');
            return;
        }
	var client = new net.Socket();
	client.connect(PORT, HOST, function() {
	    console.log('CONNECTED TO: ' + HOST + ':' + PORT); 
	    //client.write('I am Chuck Norris!');
            counter ++;
	});

	client.on('data', function(data) {
	    console.log('DATA: ' + data);
	//    client.destroy();
	    
	});
	client.on('close', function() {
	    console.log('Connection closed');
	});
};

