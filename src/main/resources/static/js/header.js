const navList = document.querySelectorAll(".nav-link")

function setActiveClass(target) {

    navList.forEach(link => {
        if (link === target && !link.classList.contains("active")) {
            console.log("adding active class to current link: ", link.attributes.getNamedItem("href").value)
            link.classList.add("active")
            link.setAttribute("aria-current", "page")
        } else if (link !== target && link.classList.contains("active")) {
            console.log("removing active class from link: ", link.attributes.getNamedItem("href").value)
            link.classList.remove("active")
            link.removeAttribute("aria-current")
        } else {
            console.log("nope: ", link === target)
            console.log("link: ", link)
            console.log("target: ", target)
        }
    })
}
// todo : currently not updating links as intended



