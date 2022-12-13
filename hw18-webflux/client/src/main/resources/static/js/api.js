const streamErr = e => {
    console.warn("error");
    console.warn(e);
}

function save(url) {
    const form = document.querySelector('#addForm');
    form.classList.add('was-validated');

    if (form.checkValidity()) {
        const data = extractFormData(form);
        console.log(data);
        closeDialog();

        fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: data
        })
            .then((response) => {
                console.log("RESP")
                return can.ndjsonStream(response.body);
            })
            .then(dataStream => {
                const reader = dataStream.getReader();
                const read = result => {
                    if (result.done) {
                        return;
                    }

                    addTableRow(result.value);
                    reader.read().then(read, streamErr);
                }
                reader.read().then(read, streamErr);
            });
    }
}

function addTableRow(obj) {
    let newRow = $("<tr>");
    let cols = '';

    cols += `<td>${obj.id}</td>`;
    cols += `<td>${obj.name}</td>`;
    cols += `<td>${obj.street}</td>`;
    cols += `<td>${obj.phone}</td>`;

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

document.addEventListener('DOMContentLoaded', event => {
    console.log("LOAD")
    const addButton = document.querySelector('#addButton');
    addButton.classList.remove("disabled");
});
