console.log("importing test spreadsheet...");
fetch("./simple-test.xlsm")
    .then(response => {
        return response.blob();
    })
    .then(data => {
        const dT = new DataTransfer();
        dT.items.add(new File([data], './simple-test.xlsm'));
        inp.files = dT.files;
        return data;
    });
