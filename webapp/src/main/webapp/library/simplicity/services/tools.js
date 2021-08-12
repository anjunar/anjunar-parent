let parser = new DOMParser();

export function setCookie(cname, cvalue, exdays) {
    const d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    const expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

export function getCookie(cname) {
    const name = cname + "=";
    const decodedCookie = decodeURIComponent(document.cookie);
    const ca = decodedCookie.split(';');
    for(let i = 0; i <ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

export function hateoas(links, name) {
    if (links) {
        return links.find(link => link.rel === name)
    }
    return null;
}

export function dateFormat(value) {
    let date = new Date(value);
    let options = {weekday: 'long', year: 'numeric', month: 'long', day: 'numeric', hour: 'numeric', minute: 'numeric', second: 'numeric'};
    return new Intl.DateTimeFormat(user.language, options).format(date);
}

export function distinct(items, extractor) {
    let distinctArray = [];
    for (let item of items) {
        let extracted = extractor(item);
        let find = distinctArray.find((element) => extractor(element) === extracted);
        if (!find) {
            distinctArray.push(item);
        }
    }
    return distinctArray;
}

export function create(html) {
    return parser.parseFromString(html, "text/html").querySelector("body").firstChild
}

export function debounce(func, wait, immediate) {
    let timeout;
    return function () {
        const context = this, args = arguments;
        const later = function () {
            timeout = null;
            if (!immediate) func.apply(context, args);
        };
        const callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func.apply(context, args);
    };
}

