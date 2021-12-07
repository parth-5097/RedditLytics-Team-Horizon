let webSocket;
let messageInput;
let keyResultData;
let setOfKey = [];

let myHeaders = new Headers();
myHeaders.append("Content-Type", "application/json");

let requestOptions = {
  method: 'POST',
  headers: myHeaders,
  redirect: 'follow'
};

setInterval(function () {
     for (let value of setOfKey) {
            doSend({ message: value.split('"').join('') });
     }
},15000);

function addDataToTable(d, table){
    if(d.length > 0){
        for (let i = 0; i < 10; i++) {
                            let row = table.insertRow(0);
                            let cell1 = row.insertCell(0);
                            let cell2 = row.insertCell(1);
                            let cell3 = row.insertCell(2);
                            let cell4 = row.insertCell(3);
                            let it = d[0].data[i];
                            cell1.innerHTML = new Date(it.created_utc).toLocaleDateString();
                            cell2.innerHTML = `<a href="/userprofile/${it.author}">${it.author}</a>`;
                            cell2.addEventListener("click", function() {

                                window.location.href = "/userprofile/" + it.author;
                            });
                            cell3.innerHTML = `<a href="/subreddit/${it.subreddit}">${it.subreddit}</a>`;
                            cell3.addEventListener("click", function() {
                                window.location.href = "/subreddit/" + it.subreddit;
                            });
                            cell4.innerHTML = it.title;
                      }
                      let row2 = table.insertRow(0);
                      row2.insertCell(0);
                      row2.insertCell(1);
                      row2.insertCell(2);
                      let cell5 = row2.insertCell(3);
                      cell5.style = "font-size:20px";
                      setOfKey.push(d[0].key.toString());
                      setOfKey = [...new Set(setOfKey)];
                      cell5.innerHTML = new String("Search Term : " + d[0].key + "   (Sentiment Analyser is running....)");
                d.shift();
                addDataToTable(d, table);
            }
        }

function onMessage(evt) {
    keyResultData = JSON.parse(evt.data);
    let table = document.getElementById("records_table");
    $("#records_table").empty();

    requestOptions.body = JSON.stringify({
        "id": sessionStorage.getItem("key"),
        "value": keyResultData
    });
    fetch("http://localhost:9000/setCache", requestOptions)
      .then(response => response.text())
      .then(result => console.log(result))
      .catch(error => console.log('error', error));

    addDataToTable(keyResultData, table);
}

if(!sessionStorage.getItem("key")){
    var requestOptionForGet = {
          method: 'GET',
          redirect: 'follow'
        };

        fetch("http://localhost:9000/getCacheId", requestOptionForGet)
          .then(response => response.text())
          .then(result => {
                if(checkIfValidUUID(result)){
                    sessionStorage.setItem("key",result);
                }
          })
          .catch(error => console.log("error"));
} else {
    var requestOptionForGet = {
              method: 'GET',
              redirect: 'follow'
            };

    fetch('http://localhost:9000/getCache/"' + sessionStorage.getItem("key") + '"', requestOptionForGet)
          .then(response => response.text())
          .then(result => {
            keyResultData = JSON.parse(result);
            let table = document.getElementById("records_table");
            $("#records_table").empty();
            addDataToTable(keyResultData, table);
          })
          .catch(error => console.log('error', error));
}

function checkIfValidUUID(str) {
  const regexExp = /^[0-9a-fA-F]{8}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{12}$/gi;

  return regexExp.test(str);
}

function appendMessageToView(title, message) {
    $("#messageContent").append("<p>" + title + ": " + message + "</p>");
}

function doSend(protocolMessage) {
    if(webSocket.readyState == WebSocket.OPEN) {
        webSocket.send(JSON.stringify(protocolMessage.message));
    } else {
        console.log("Could not send data. Websocket is not open.");
    }
}

window.addEventListener("load", init, false);

$("#sendButton").click(function () {
    newMessage();
});

$("#btn").click(function() {
    window.location.href = "/search/" + document.getElementById("messageInput").value;
});

$(window).on("keydown", function (e) {
    if (e.which == 13) {
        newMessage();
        return false;
    }
});

function newMessage() {
    messageInput = $("#messageInput").val();
    if ($.trim(messageInput) == "") {
        return false;
    }

    appendMessageToView("Me", messageInput);

    var message = {
        message: messageInput
    };

    doSend(message);
}