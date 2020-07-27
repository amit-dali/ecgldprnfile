var HttpClient = function () {
    this.get = function (aUrl, aCallback) {
        let anHttpRequest = new XMLHttpRequest();
        anHttpRequest.onreadystatechange = function () {
            if (anHttpRequest.readyState == 4 && anHttpRequest.status == 200)
                aCallback(anHttpRequest.responseText);
        }

        anHttpRequest.open("GET", aUrl, true);
        anHttpRequest.send(null);
    }
}


function getUsers() {


    const client = new HttpClient();
    let getStoresURL = "http://localhost:8080/users";

    client.get(getStoresURL, function (response) {
        let responseJSON = JSON.parse(response);
        let tableRef = document.getElementById('userTable').getElementsByTagName('tbody')[0];
        tableRef.innerHTML = '';
        for (let i = 0; i < responseJSON.length; i++) {
            let newRow = tableRef.insertRow();
            let currentUser = responseJSON[i];

            addCell(newRow, 0, currentUser.name);
            addCell(newRow, 1, currentUser.address);
            addCell(newRow, 2, currentUser.postCode);
            addCell(newRow, 3, currentUser.phone);
            addCell(newRow, 4, currentUser.creditLimit);
            addCell(newRow, 5, currentUser.dateOfBirth);
            addCell(newRow, 6, currentUser.backendSystem);
        }
    });
}

function addCell(row, index, value) {
    var cell = row.insertCell(index);
    if (value) {
        cell.appendChild(document.createTextNode(value));
    } else {
        cell.appendChild(document.createTextNode(""));
    }
}

