function submitUserInfo() {
    document.forms["user-info-form"].submit();
}

function submitUserPwd() {
    document.forms["user-pwd-form"].submit();
}

function submitDeposit() {
    document.forms["deposit-withdraw-form"].action = "/account/deposit";
    document.forms["deposit-withdraw-form"].submit();
}
function submitWithdraw() {
    document.forms["deposit-withdraw-form"].action = "/account/withdraw";
    document.forms["deposit-withdraw-form"].submit();
}

// Old Password eye icon toggle
const oldPasswordInput = document.querySelector("#old-password");
const oldPwdEyeIcon = document.querySelector(".pwd-field i.old-pwd");
oldPwdEyeIcon.addEventListener("click", () => {
    // Toggle the password input type between "password" and "text"
    oldPasswordInput.type = oldPasswordInput.type === "password" ? "text" : "password";
    // Update the eye icon class based on the password input type
    oldPwdEyeIcon.className = `fa-solid fa-eye${oldPasswordInput.type === "password" ? "" : "-slash"}`;
})
// Confirm Password eye icon toggle
const confirmPwdInput = document.querySelector("#confirm-pwd");
const confirmPwdEyeIcon = document.querySelector(".pwd-field i.confirm-pwd");
confirmPwdEyeIcon.addEventListener("click", () => {
    // Toggle the password input type between "password" and "text"
    confirmPwdInput.type = confirmPwdInput.type === "password" ? "text" : "password";
    // Update the eye icon class based on the password input type
    confirmPwdEyeIcon.className = `fa-solid fa-eye${confirmPwdInput.type === "password" ? "" : "-slash"}`;
})