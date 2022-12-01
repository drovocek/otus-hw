function save(url) {
    const form = document.querySelector('#addForm');

    if (form.checkValidity()) {
        const data = extractFormData(form);

        closeDialog();

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: data
        })
            .then(response => response.json())
            .then(id => {
                // const obj = JSON.parse(data);
                // obj['id'] = id;
                addTableRow(id);
            });
    }
}

function addTableRow(obj) {
    let newRow = $("<tr>");
    let cols = '';

    cols += `<td>${obj.id}</td>`;
    cols += `<td>${obj.name}</td>`;
    cols += `<td>${obj.street}</td>`;
    cols += `<td>${obj.number}</td>`;

    newRow.append(cols);
    $("#table").append(newRow);
}

function closeDialog() {
    $('#addDialog').modal('hide');
}

function extractFormData() {
    const form = $('#addForm');
    const array = form.serializeArray()
    const jsonData = {};
    jsonData['id'] = null;
    $.each(array, function () {
        jsonData[this.name] = this.value;
    });
    return JSON.stringify(jsonData);
}
