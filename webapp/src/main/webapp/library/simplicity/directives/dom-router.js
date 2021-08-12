import {get} from "../services/router.js"
import {customComponents, HTMLWindow} from "../simplicity.js";
import MatWindow from "../components/modal/mat-window.js";
import {windowManager} from "../services/window-manager.js";

export default class DomRouter extends HTMLElement {

    #level = 0

    get level() {
        return this.#level;
    }

    set level(value) {
        this.#level = value;
    }

    onWindowHashchange(url) {
        console.time("load")

        let segments = url.split("#");

        if (segments.length === 1) {
            return;
        }

        let hash = segments[this.#level + 1];
        let indexOf = hash.indexOf("?");
        let hashes, path;
        if (indexOf === -1) {
            hashes = [hash]
            path = hash;
        } else {
            hashes = [hash.substring(0, indexOf), hash.substring(indexOf + 1)];
            path = hashes[0];
        }

        let result = {};
        if (hashes[1]) {
            let rawQueryParams = hashes[1].split("&");
            for (const rawQueryParam of rawQueryParams) {
                let queryParamRegex = /(\w+)=([\w\d\-/?=]*)/g;
                let queryParameterRegexResult = queryParamRegex.exec(rawQueryParam);
                result[queryParameterRegexResult[1]] = queryParameterRegexResult[2]
            }
        }

        let newPath = "../../.." + path + ".js";
        import(newPath)
            .then((module) => {

                let view;
                view = new module.default();
                this.openWindow(view, hash, result);


            })
    }

    openWindow(view, hash, result = {}) {
        view.queryParams = result;

        let configure = get(view.localName);

        if (configure.guard) {

            view.reload = () => {
                let target = configure.guard(view);

                let guardResult = Reflect.ownKeys(target);

                let promises = [];
                for (const property of guardResult) {
                    let guardResultElement = target[property];
                    promises.push(guardResultElement);
                }

                return Promise.all(promises).then((results) => {
                    guardResult.forEach((property, index) => {
                        view[property] = results[index];
                    });
                })
            }

            view.reload().then(() => {
                for (const child of Array.from(this.children)) {
                    if (!(child instanceof MatWindow)) {
                        child.remove();
                    }
                }
                if (view instanceof HTMLWindow) {
                    windowManager.register(view, configure, hash, (matWindow) => {
                        this.appendChild(matWindow);
                    });

                } else {
                    this.appendChild(view);
                }

                console.timeEnd("load")
            })

        } else {
            for (const child of Array.from(this.children)) {
                if (!(child instanceof MatWindow)) {
                    child.remove();
                }
            }
            if (view instanceof HTMLWindow) {

                windowManager.register(view, configure, hash, (matWindow) => {
                    this.appendChild(matWindow);
                });

            } else {
                this.appendChild(view);
            }

            console.timeEnd("load")
        }
    }

    render() {
        window.addEventListener("hashchange", (event) => {
            this.onWindowHashchange(event.newURL || window.location.hash);
        })

        window.dispatchEvent(new Event("hashchange"));
    }


}

customComponents.define("dom-router", DomRouter);