let service;

export function loadRoot(reload = false) {
    if (service && !reload) {
        return service;
    }

    let request = new XMLHttpRequest();

    request.addEventListener("loadend", (response) => {
        service = JSON.parse(response.target.responseText);
    })

    let url = "service";
    request.open("GET", url, false)
    request.send();

    return service;
}

export function language(language) {

    let request = new XMLHttpRequest();

    let url = "service/language?lang=" + language;

    request.open("GET", url)
    request.send();

    return service;
}