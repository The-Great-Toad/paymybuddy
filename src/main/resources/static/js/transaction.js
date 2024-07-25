function changePageSize() {
    const sizeOptions = document.getElementsByClassName("size-option");

    for (let option of sizeOptions) {
        if (option.selected) {
            window.location = "/transactions?size="+option.value;
        }
    }
}