import {parse, multipartToJSON} from "./multipart.js"
import {lifeCycle} from "../processors/lifecycle-processor.js";

const exceptionHandlers = [];

export function registerExceptionHandler(value) {
    exceptionHandlers.push(value);
}

export const htmlClient = new class HTMLClient {

    action(method, url, options) {

        let request = new XMLHttpRequest;
        let parser = new DOMParser();

        request.open(method, url);
        request.setRequestHeader("content-type", "html/text");

        let executor = (resolve, reject) => {
            request.addEventListener("loadend", (event) => {
                let status = event.target.status;

                if (status >= 200 && status < 300) {
                    let response = event.target.responseText;
                    resolve(parser.parseFromString(response, "text/html"))
                } else {
                    reject(event.target);
                    for (const exceptionHandler of exceptionHandlers) {
                        exceptionHandler(event.target);
                    }
                }

            });
        };

        let promise = new Promise(executor);

        if (options instanceof FormData) {
            request.addEventListener("progress", (event) => {
                console.log(event)
            })
            request.send(options);
        } else {
            if (options && options.body) {
                request.send(JSON.stringify(options.body));
            } else {
                request.send();
            }
        }

        return promise;
    }

    get(url, options) {
        return this.action("GET", url, options)
    }

    put(url, options) {
        return this.action("PUT", url, options)
    }

    delete(url, options) {
        return this.action("DELETE", url, options)
    }

    post(url, options) {
        return this.action("POST", url, options)
    }

    options(url, options) {
        return this.action("OPTIONS", url, options)
    }


}

export const jsonClient = new class JSONClient {

    action(method, url, options) {

        let request = new XMLHttpRequest;

        request.open(method, url);
        request.setRequestHeader("content-type", "application/json");

        if (options?.headers) {
            for (const key of Object.keys(options.headers)) {
                request.setRequestHeader(key, options.headers[key]);
            }
        }

        let executor = (resolve, reject) => {
            request.addEventListener("loadend", (event) => {
                let status = event.target.status;

                if (status >= 200 && status < 300) {
                    if (event.target.responseText.length > 0) {
                        let response = JSON.parse(event.target.responseText);
                        resolve(response)
                    } else {
                        resolve("")
                    }
                } else {
                    reject(event.target);
                    for (const exceptionHandler of exceptionHandlers) {
                        exceptionHandler(event.target);
                    }
                }

            });
        };

        let promise = new Promise(executor);

        if (options && options.body) {
            request.send(JSON.stringify(options.body));
        } else {
            request.send();
        }

        return promise;
    }

    get(url, options) {
        return this.action("GET", url, options)
    }

    put(url, options) {
        return this.action("PUT", url, options)
    }

    delete(url, options) {
        return this.action("DELETE", url, options)
    }

    post(url, options) {
        return this.action("POST", url, options)
    }

    options(url, options) {
        return this.action("OPTIONS", url, options)
    }

}

export const jsonClientSynchronous = new class JSONClient {

    action(method, url, options) {

        let request = new XMLHttpRequest;

        request.open(method, url, false);
        request.setRequestHeader("content-type", "application/json");

        let executor = (resolve, reject) => {
            request.addEventListener("loadend", (event) => {
                let status = event.target.status;

                if (status >= 200 && status < 300) {
                    if (event.target.responseText.length > 0) {
                        let response = JSON.parse(event.target.responseText);
                        resolve(response)
                    } else {
                        resolve("")
                    }
                } else {
                    reject(event.target);
                    for (const exceptionHandler of exceptionHandlers) {
                        exceptionHandler(event.target);
                    }
                }

            });
        };

        let promise = new Promise(executor);

        if (options && options.body) {
            request.send(JSON.stringify(options.body));
        } else {
            request.send();
        }

        return promise;
    }

    get(url, options) {
        return this.action("GET", url, options)
    }

    put(url, options) {
        return this.action("PUT", url, options)
    }

    delete(url, options) {
        return this.action("DELETE", url, options)
    }

    post(url, options) {
        return this.action("POST", url, options)
    }

    options(url, options) {
        return this.action("OPTIONS", url, options)
    }


}


export const formClient = new class JSONClient {

    action(method, url, options) {

        let request = new XMLHttpRequest;

        request.open(method, url);

        let executor = (resolve, reject) => {
            request.addEventListener("loadend", (event) => {
                let status = event.target.status;

                if (status >= 200 && status < 300) {
                    if (event.target.responseText.length > 0) {
                        let response = multipartToJSON(parse(event.target.responseText));
                        resolve(response)
                        window.setTimeout(() => {
                            lifeCycle();
                        }, 10)
                    } else {
                        resolve("")
                        window.setTimeout(() => {
                            lifeCycle();
                        }, 10)
                    }
                } else {
                    reject(event.target);
                    window.setTimeout(() => {
                        lifeCycle();
                    }, 10)
                    for (const exceptionHandler of exceptionHandlers) {
                        exceptionHandler(event.target);
                    }
                }

            });
        };

        let promise = new Promise(executor);

        if (options) {
            request.send(options);
        } else {
            request.send();
        }

        return promise;
    }

    get(url, options) {
        return this.action("GET", url, options)
    }

    put(url, options) {
        return this.action("PUT", url, options)
    }

    delete(url, options) {
        return this.action("DELETE", url, options)
    }

    post(url, options) {
        return this.action("POST", url, options)
    }

    options(url, options) {
        return this.action("OPTIONS", url, options)
    }


}
