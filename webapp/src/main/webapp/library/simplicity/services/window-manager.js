import MatWindow from "../components/modal/mat-window.js";

const windowsRegistry = new Map();

const request = indexedDB.open("anjunar", 2);

let db;

request.onupgradeneeded = function () {
    const db = request.result;
    db.createObjectStore("windowManager", {keyPath: "id"});
    db.createObjectStore("windowManager#maximized", {keyPath: "id"})
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

function saveWindow(url, matWindow, closed = false, minimized = false) {
    let value = {
        id: url,
        width: matWindow.offsetWidth,
        height: matWindow.offsetHeight,
        top: matWindow.offsetTop,
        left: matWindow.offsetLeft,
        zIndex: Number.parseInt(matWindow.style.zIndex),
        minimized : minimized,
        closed : closed
    }
    const tx = db.transaction("windowManager", "readwrite");
    const store = tx.objectStore("windowManager");
    store.put(value);
}

function saveWindowMaximized(url, matWindow) {
    let value = {
        id: url,
        width: matWindow.offsetWidth,
        height: matWindow.offsetHeight,
        top: matWindow.offsetTop,
        left: matWindow.offsetLeft,
        zIndex: Number.parseInt(matWindow.style.zIndex),
    }
    const tx = db.transaction("windowManager#maximized", "readwrite");
    const store = tx.objectStore("windowManager#maximized");
    store.put(value);
}

function loadWindow(url, callback) {
    const tx = db.transaction("windowManager", "readwrite");
    const store = tx.objectStore("windowManager");
    let idbRequest = store.get(url);
    idbRequest.onsuccess = callback;
}

function loadWindowMaximized(url, callback) {
    const tx = db.transaction("windowManager#maximized", "readwrite");
    const store = tx.objectStore("windowManager#maximized");
    let idbRequest = store.get(url);
    idbRequest.onsuccess = callback;
    idbRequest.onerror = callback;
}

function deleteWindowMaximized(url) {
    const tx = db.transaction("windowManager#maximized", "readwrite");
    const store = tx.objectStore("windowManager#maximized");
    let idbRequest = store.delete(url);
    idbRequest.onsuccess = () => {
        console.log(idbRequest);
    }
}

export const windowManager = new class WindowManager {

    register(view, configure, url, callback) {
        if (!windowsRegistry.has(url)) {
            loadWindow(url, (event) => {
                let result = event.target.result;

                let matWindow = new MatWindow();
                matWindow.content = view;
                matWindow.header = configure.header;
                matWindow.resizable = configure.resizable;
                matWindow.url = url;

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

                windowsRegistry.set(url, matWindow);

                matWindow.addEventListener("rendered", () => {
                    if (result) {
                        matWindow.style.top = result.top + "px";
                        matWindow.style.left = result.left + "px";
                    } else {
                        let top = matWindow.parentElement.offsetHeight / 2 - matWindow.offsetHeight / 2;
                        let left = matWindow.parentElement.offsetWidth / 2 - matWindow.offsetWidth / 2;

                        if (top < 0) {
                            top = 0;
                        }

                        if (left < 0) {
                            left = 0;
                        }

                        matWindow.style.top = top + "px";
                        matWindow.style.left = left + "px";
                    }

                    saveWindow(url, matWindow)

                    if (result?.minimized) {
                        matWindow.style.display = "none"
                    }
                })

                matWindow.addEventListener("windowEndResize", (event) => {
                    saveWindow(url, matWindow);
                })

                matWindow.addEventListener("windowEndDrag", (event) => {
                    saveWindow(url, matWindow)
                })

                callback(matWindow);

                this.clickWindow(matWindow);
            })
        }
    }

    openWindow(url, view, host, configure) {
        let matWindow = host.queryUpwards((element) => element.url);
        let urlWithHost = `${url}?host=${btoa(matWindow.url)}`;

        windowManager.register(view, configure, urlWithHost, (matWindow) => {
            let domRouter = document.querySelector("dom-router");
            domRouter.appendChild(matWindow);
        })
    }

    findByView(view) {
        for (const matWindow of windowsRegistry) {
            if (matWindow[1].content === view) {
                return matWindow[1];
            }
        }
        return null;
    }

    findByUrl(host) {
        return windowsRegistry.get(host);
    }

    clickWindow(matWindow) {
        let sort = zIndexSorted();

        let indexOf = sort.indexOf(matWindow);

        sort.splice(indexOf, 1);

        sort.push(matWindow);

        window.location.hash = matWindow.url;

        for (let i = 0; i < sort.length; i++) {
            const sortElement = sort[i];
            sortElement.style.zIndex = (i + 1) * 10;

            windowsRegistry.forEach((value, key) => {
                if (value === sortElement) {
                    saveWindow(key, sortElement);
                }
            })
        }
    }

    get windows() {
        return Array.from(windowsRegistry.values());
    }

    close(matWindow) {

        windowsRegistry.delete(matWindow.url);

        saveWindow(matWindow.url, matWindow, true)

        let zIndexes = zIndexSorted();
        if (zIndexes.length > 0) {
            let focus = zIndexes[zIndexes.length - 1];
            window.location.hash = focus.url;
        } else {
            window.location.hash = "#"
        }

        matWindow.remove();
    }

    minimize(matWindow) {

        saveWindow(matWindow.url, matWindow, false, true);

        matWindow.style.display = "none";

    }

    show(matWindow) {
        loadWindow(matWindow.url, (event) => {
            let result = event.target.result;

            matWindow.style.display = "block";
            matWindow.style.top = result.top + "px";
            matWindow.style.left = result.left + "px";
        })

        this.clickWindow(matWindow);

    }

    closeAll() {
        for (const window of this.windows) {
            this.close(window);
        }
    }

    maximize(matWindow) {
        loadWindowMaximized(matWindow.url, (event) => {
            let result = event.target.result;
            if (result) {
                matWindow.style.top = result.top + "px";
                matWindow.style.left = result.left + "px";
                matWindow.style.width = result.width + "px";
                matWindow.style.height = result.height + "px";
                deleteWindowMaximized(matWindow.url)
            } else {
                saveWindowMaximized(matWindow.url, matWindow);
                matWindow.style.top = "0";
                matWindow.style.left = "0"
                matWindow.style.width = matWindow.parentElement.offsetWidth + "px";
                matWindow.style.height = matWindow.parentElement.offsetHeight + "px";
                saveWindow(matWindow.url, matWindow);
            }
        })

    }

}