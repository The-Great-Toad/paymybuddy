// Old Password eye icon toggle
const passwordInputEl = document.querySelector("#new-password");
const passwordEyeIcon = document.querySelector("#icon-new-pwd");
passwordEyeIcon.addEventListener("click", () => {
    // Toggle the password input type between "password" and "text"
    passwordInputEl.type = passwordInputEl.type === "password" ? "text" : "password";
    // Update the eye icon class based on the password input type
    passwordEyeIcon.className = `fa-solid fa-eye${passwordInputEl.type === "password" ? "" : "-slash"}`;
})