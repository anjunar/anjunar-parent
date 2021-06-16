let service;

export function loadRoot(reload = false, language) {
    if (service && !reload) {
        return service;
    }

    let request = new XMLHttpRequest();

    request.addEventListener("loadend", (response) => {
        service = JSON.parse(response.target.responseText);
    })

    let url = "service/root";
    if (language) {
        url = url + "?lang=" + language
    }

    request.open("GET", url, false)
    request.send();

    return service;
}