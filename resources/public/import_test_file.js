var file_to_load="./simple-test-sparse.xlsm";

console.log("Importing test spreadsheet '" + file_to_load + "'...");
fetch(file_to_load)
    .then(response => {
        return response.blob();
    })
    .then(data => {
        const dT = new DataTransfer();
        dT.items.add(new File([data], file_to_load));
        inp.files = dT.files;
        return data;
    });
