import MatWindow from "../components/modal/mat-window.js";

const windowsRegistry = new Map();
const configureRegistry = new Map();

const request = indexedDB.open("anjunar", 2);

let db;

request.onupgradeneeded = function () {
    const db = request.result;
    db.createObjectStore("windowManager", {keyPath: "id"});
};

request.onsuccess = function () {
    db = request.result;
};

window.addEventListener("load", () => {
    const tx = db.transaction("windowManager", "readwrite");
    const store = tx.objectStore("windowManager");
    let idbRequest = store.getAll();

    idbRequest.onsuccess = (event) => {
        let result = idbRequest.result;
        for (const item of result) {
            if (!windowsRegistry.has(item.id) && !item.closed) {
                window.location.hash = item.id;
            }
        }
    }
})

function zIndexSorted() {
    let windows = Array.from(windowsRegistry.values());
    return windows.sort((lhs, rhs) => Number.parseInt(lhs.style.zIndex) - Number.parseInt(rhs.style.zIndex));
}

export const windowManager = new class WindowManager {

    register(view, configure, callback) {
        if (!windowsRegistry.has(configure.url)) {
            const tx = db.transaction("windowManager", "readwrite");
            const store = tx.objectStore("windowManager");
            let idbRequest = store.get(configure.url);
            idbRequest.onsuccess = (event) => {
                let result = idbRequest.result;

                let matWindow = new MatWindow();
                matWindow.content = view;
                matWindow.header = configure.header;
                matWindow.resizable = configure.resizable;

                if (result) {
                    if (configure.resizable) {
                        matWindow.style.width = result.width + "px";
                        matWindow.style.height = result.height + "px";
                    }
                    matWindow.style.left = result.left + "px";
                    matWindow.style.top = result.top + "px";
                    matWindow.style.zIndex = result.zIndex;
                } else {
                    if (configure.resizable) {
                        matWindow.style.width = configure.width + "px";
                        matWindow.style.height = configure.height + "px";
                    }
                }

                windowsRegistry.set(configure.url, matWindow);
                configureRegistry.set(matWindow, configure);

                matWindow.addEventListener("rendered", () => {
                    if (result) {
                        matWindow.style.top = result.top + "px";
                        matWindow.style.left = result.left + "px";
                    } else {
                        matWindow.style.top = matWindow.parentElement.offsetHeight / 2 - matWindow.offsetHeight / 2 + "px";
                        matWindow.style.left = matWindow.parentElement.offsetWidth / 2 - matWindow.offsetWidth / 2 + "px";
                    }

                    let value = {
                        id: configure.url,
                        width: matWindow.offsetWidth,
                        height: matWindow.offsetHeight,
                        top: matWindow.offsetTop,
                        left: matWindow.offsetLeft,
                        zIndex: Number.parseInt(matWindow.style.zIndex)
                    }
                    const tx = db.transaction("windowManager", "readwrite");
                    const store = tx.objectStore("windowManager");
                    store.put(value);

                    if (result?.minimized) {
                        matWindow.style.display = "none"
                    }
                })

                matWindow.addEventListener("windowEndResize", (event) => {
                    let value = {
                        id: configure.url,
                        width: matWindow.offsetWidth,
                        height: matWindow.offsetHeight,
                        top: matWindow.offsetTop,
                        left: matWindow.offsetLeft,
                        zIndex: Number.parseInt(matWindow.style.zIndex)
                    }
                    const tx = db.transaction("windowManager", "readwrite");
                    const store = tx.objectStore("windowManager");
                    store.put(value);
                })

                matWindow.addEventListener("windowEndDrag", (event) => {
                    let value = {
                        id: configure.url,
                        width: matWindow.offsetWidth,
                        height: matWindow.offsetHeight,
                        top: matWindow.offsetTop,
                        left: matWindow.offsetLeft,
                        zIndex: Number.parseInt(matWindow.style.zIndex)
                    }
                    const tx = db.transaction("windowManager", "readwrite");
                    const store = tx.objectStore("windowManager");
                    store.put(value);
                })

                callback(matWindow);
            }
        }
    }

    clickWindow(matWindow) {
        let sort = zIndexSorted();

        let indexOf = sort.indexOf(matWindow);

        sort.splice(indexOf, 1);

        sort.push(matWindow);

        let configure = configureRegistry.get(matWindow);

        window.location.hash = configure.url;

        for (let i = 0; i < sort.length; i++) {
            const sortElement = sort[i];
            sortElement.style.zIndex = (i + 1) * 10;

            windowsRegistry.forEach((value, key) => {
                if (value === sortElement) {
                    let value = {
                        id: key,
                        width: sortElement.offsetWidth,
                        height: sortElement.offsetHeight,
                        top: sortElement.offsetTop,
                        left: sortElement.offsetLeft,
                        zIndex: Number.parseInt(sortElement.style.zIndex)
                    }
                    const tx = db.transaction("windowManager", "readwrite");
                    const store = tx.objectStore("windowManager");
                    store.put(value);
                }
            })
        }
    }

    get windows() {
        return Array.from(windowsRegistry.values());
    }

    getConfigure(matWindow) {
        return configureRegistry.get(matWindow);
    }

    close(matWindow) {
        let configure = configureRegistry.get(matWindow);

        windowsRegistry.delete(configure.url);
        configureRegistry.delete(matWindow);

        let value = {
            id: configure.url,
            width: matWindow.offsetWidth,
            height: matWindow.offsetHeight,
            top: matWindow.offsetTop,
            left: matWindow.offsetLeft,
            zIndex: Number.parseInt(matWindow.style.zIndex),
            closed: true
        }
        const tx = db.transaction("windowManager", "readwrite");
        const store = tx.objectStore("windowManager");
        store.put(value);

        let zIndexes = zIndexSorted();
        if (zIndexes.length > 0) {
            let focus = zIndexes[zIndexes.length - 1];
            let zIndexConfigure = configureRegistry.get(focus);
            window.location.hash = zIndexConfigure.url;
        }

        matWindow.remove();
    }

    minimize(matWindow) {

        let configure = configureRegistry.get(matWindow);

        let value = {
            id: configure.url,
            width: matWindow.offsetWidth,
            height: matWindow.offsetHeight,
            top: matWindow.offsetTop,
            left: matWindow.offsetLeft,
            zIndex: Number.parseInt(matWindow.style.zIndex),
            closed: false,
            minimized: true
        }
        const tx = db.transaction("windowManager", "readwrite");
        const store = tx.objectStore("windowManager");
        store.put(value);

        matWindow.style.display = "none";
    }

    show(matWindow) {
        let configure = configureRegistry.get(matWindow);
        const tx = db.transaction("windowManager", "readwrite");
        const store = tx.objectStore("windowManager");
        let idbRequest = store.get(configure.url);
        idbRequest.onsuccess = (event) => {
            let result = idbRequest.result;

            matWindow.style.display = "block";
            matWindow.style.top = result.top + "px";
            matWindow.style.left = result.left + "px";
        }
    }

    closeAll() {
        for (const window of this.windows) {
            this.close(window);
        }
    }
}