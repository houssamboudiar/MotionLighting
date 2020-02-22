var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/dataStream', function (greeting) {
            showGreeting(JSON.parse(greeting.body).motion ,
                         JSON.parse(greeting.body).light ,
                         JSON.parse(greeting.body).led);
        });
    });
    $(`#greetings`).html(`<h1 class='dark connection' >Connection State : <span class="state orange">connecting ... </span> </h1>`);
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
   
    $("#greetings").html(`<h1 class='dark connection' >Connection State : <span class="state red">Not Connected </span> </h1>`);
    $("#motion").html(`<h3 class="sensor" id="motion">Motion Sensor: <span class="state red">Not Connected</span></h3>`);
    $("#light").html(`<h3 class="sensor" id="light">Light Sensor: <span class="state red">Not Connected</span></h3>`);
    $("#led").html(`<h3 class="sensor" id="led">Led: <span class="state red">Not Connected</span></h3>`);

}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'led': $("#led").val()}));
}

function showGreeting( motion , light , led ) {
  
    if (motion === 0) {
        $(`#motion`).html(`<h3 class="sensor" id="motion">Motion Sensor: <span class="state orange">Motion Undetected</span> <span class='value'>${motion}</value></h3>`);
    } else {
        $(`#motion`).html(`<h3 class="sensor" id="motion">Motion Sensor: <span class="state green">Motion detected</span> <span class='value'>${motion}</value></h3>`);
    }
    if (light > 500) {
        $(`#light`).html(`<h3 class="sensor" id="light">Light Sensor: <span class="state orange">Dark Envirement</span> <span class='value'>${light}</value></h3>`);
    } else {
        $(`#light`).html(`<h3 class="sensor" id="light">Light Sensor: <span class="state green">Light Envirement</span> <span class='value'>${light}</value></h3>`);
    }
    if (led === 0) {
        $(`#led`).html(`<h3 class="sensor" id="led">Led: <span class="state orange">off </span><span class='value'>${led}</value></h3>`);
    } else {
        $(`#led`).html(`<h3 class="sensor" id="led">Led: <span class="state green">on </span><span class='value'>${led}</value></h3>`);
    }
    $(`#greetings`).html(`<h1 class='dark connection' >Connection State : <span class="state green">Connected </span></h1>`);
}


function connection() { 
    connect();
    setInterval(() => { sendName(); }, 500);
}

$(function () {
    $("#connect-btn").click( function(){
        if( $(this).is(':checked') ) connection()
        else disconnect()
    });
})