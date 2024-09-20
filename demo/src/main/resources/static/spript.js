function button1Clicked() {
    // Get the field values
    const field0Value = document.getElementById("field0").value;
    const field1Value = document.getElementById("field1").value;
    const field21Value = document.getElementById("field21").value;
    const field22Value = document.getElementById("field22").value;
    const field23Value = document.getElementById("field23").value;
    const field31Value = document.getElementById("field31").value;
    const field32Value = document.getElementById("field32").value;
    const field33Value = document.getElementById("field33").value;
    const field41Value = document.getElementById("field41").value;
    const field42Value = document.getElementById("field42").value;
    const field43Value = document.getElementById("field43").value;
    const field51Value = document.getElementById("field51").value;
    const field52Value = document.getElementById("field52").value;
    const field53Value = document.getElementById("field53").value;
    const field61Value = document.getElementById("field61").value;
    const field62Value = document.getElementById("field62").value;
    const field63Value = document.getElementById("field63").value;


    // Create a JavaScript object with the field values
    const data = {
        1: field0Value,
        2: field1Value,
        3: field21Value,
        4: field22Value,
        5: field23Value,
        6: field31Value,
        7: field32Value,
        8: field33Value,
        9: field41Value,
        10: field42Value,
        11: field43Value,
        12: field51Value,
        13: field52Value,
        14: field53Value,
        15: field61Value,
        16: field62Value,
        17: field63Value
    };

    // Send the data to the backend
    fetch('/calculate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        // Check the value associated with key 1 /
        const isSuccess = data[1];

        if (isSuccess === true) {
            //console.log("Info: Correct info");
            console.log(Object.keys(data).length);
        } else if (isSuccess === false) {
            console.log("Info: Wrong info");
        } else {
            console.log("Info: Unknown condition");
        }
        let newText = data[2];
        console.log(newText);
        for(let i = 3; i < Object.keys(data).length; i++){
            newText += " ";
            newText += data[i];
            console.log(newText);
        }
        document.getElementById("answer").innerHTML = newText;
    })
    .catch(error => {
        console.error('Error:', error.message);
    });
}
