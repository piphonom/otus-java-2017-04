var ws;

init = function () {
    ws = new WebSocket("ws://localhost:8080/dbservice");
    ws.onopen = function (event) {

    }

    ws.onmessage = function (event) {
        document.getElementById('dbId').innerHTML = JSON.parse(event.data).id;
        document.getElementById('dbUserName').innerHTML = JSON.parse(event.data).name;
        document.getElementById('dbAge').innerHTML = JSON.parse(event.data).age;
        document.getElementById('dbPhone').innerHTML = JSON.parse(event.data).phones;
        document.getElementById('dbAddress').innerHTML = JSON.parse(event.data).address;
    }

    ws.onclose = function (event) {

    }
};

function sendMessage() {
    var userNameField = document.getElementById("username");
    var message = userNameField.value;
    ws.send(message);
    messageField.value = '';
}