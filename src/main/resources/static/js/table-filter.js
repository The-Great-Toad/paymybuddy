const textInput = document.querySelector("#keyword");
let tableRows = document.querySelectorAll(".tr-content");

textInput.addEventListener("keyup", () => {
    const value = textInput.value
    tableRows.forEach(row => filterRow(row, value))
    if ([null, ""].includes(value)) clearFilter();
});

function filterRow(row, value) {
    const children = row.children;
    for (let child of children) {
        if (child.innerHTML.toLowerCase().includes(value.toLowerCase())) {
            row.style.display = "";
            return;
        }
    }
    row.style.display = "none";
}

function clearFilter() {
    // console.log("clearing filter");
    textInput.value = "";
    tableRows.forEach(row => row.style.display = "")
}