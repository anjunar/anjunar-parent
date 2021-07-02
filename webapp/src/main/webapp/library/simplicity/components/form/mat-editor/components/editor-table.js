import {customComponents} from "../../../../simplicity.js";

export const name = "editor-table";

export default class EditorTableElement extends HTMLTableElement {

    connectedCallback() {

        let element = this;

        resizableGrid(element);

        function resizableGrid(table) {
            /*
                        let row;
                        if (table.querySelector("thead")) {
                            row = table.querySelector("thead").querySelector("tr")
                        } else {
                            row = table.querySelector('tr')
                        }

                        let cols = row ? row.children : undefined;
                        if (!cols) return;

                        table.style.overflow = 'hidden';

                        for (let i = 0; i < cols.length; i++) {
                            let div = createDiv();
                            cols[i].appendChild(div);
                            cols[i].style.position = 'relative';
                            setListeners(div);
                        }

                        function setListeners(div) {
                            let pageX, curCol, nxtCol, curColWidth, nxtColWidth;

                            div.addEventListener('mousedown', function (e) {
                                curCol = e.target.parentElement;
                                nxtCol = curCol.nextElementSibling;
                                pageX = e.pageX;

                                let padding = paddingDiff(curCol);

                                curColWidth = curCol.offsetWidth - padding;
                                if (nxtCol)
                                    nxtColWidth = nxtCol.offsetWidth - padding;
                            });

                            div.addEventListener('mouseover', function (e) {
                                e.target.style.borderRight = '2px solid var(--main-selected-color)';
                            })

                            div.addEventListener('mouseout', function (e) {
                                e.target.style.borderRight = '';
                            })

                            document.addEventListener('mousemove', function (e) {
                                if (curCol) {
                                    let diffX = e.pageX - pageX;

                                    if (nxtCol)
                                        nxtCol.style.width = (nxtColWidth - (diffX)) + 'px';

                                    curCol.style.width = (curColWidth + diffX) + 'px';
                                }
                            });

                            document.addEventListener('mouseup', function (e) {
                                curCol = undefined;
                                nxtCol = undefined;
                                pageX = undefined;
                                nxtColWidth = undefined;
                                curColWidth = undefined
                            });
                        }

                        function createDiv() {
                            let tableHeight = table.offsetHeight;
                            let div = document.createElement('div');
                            div.style.top = 0;
                            div.style.right = 0;
                            div.style.textAlign = "left"
                            div.style.width = "100%"
                            // div.style.width = '5px';
                            div.style.position = 'absolute';
                            div.style.cursor = 'col-resize';
                            div.style.userSelect = 'none';
                            div.style.height = tableHeight + 'px';
                            return div;
                        }

                        function paddingDiff(col) {

                            if (getStyleVal(col, 'box-sizing') == 'border-box') {
                                return 0;
                            }

                            let padLeft = getStyleVal(col, 'padding-left');
                            let padRight = getStyleVal(col, 'padding-right');
                            return (parseInt(padLeft) + parseInt(padRight));

                        }

                        function getStyleVal(elm, css) {
                            return (window.getComputedStyle(elm, null).getPropertyValue(css))
                        }

                    */

        }

        document.addEventListener("keydown", (event) => {
            let selection = document.getSelection();
            let rangeAt = selection.getRangeAt(0);
            let endOffset = rangeAt.endOffset;
            let endContainer = rangeAt.endContainer;

            let tds = element.querySelectorAll("td");
            let parentElement = endContainer.queryUpwards((element) => {
                return element.localName === "td"
            });
            if (parentElement) {
                if (tds.item(tds.length - 1).isEqualNode(parentElement)) {
                    let length = endContainer.textContent.length;
                    if (length === endOffset) {
                        if (event.keyCode === 13 && event.ctrlKey) {
                            endContainer.queryUpwards((element) => {
                                return element.localName === "td"
                            }).innerHTML = endContainer.queryUpwards((element) => {
                                return element.localName === "td"
                            }).innerHTML + "<br/>"
                        } else {
                            if (event.keyCode === 13 && ! event.ctrlKey ) {
                                event.preventDefault();
                                let rowElement = document.createElement("tr");
                                element.appendChild(rowElement)

                                let columns = element
                                    .querySelector("tr")
                                    .querySelectorAll("td");
                                for (const column of columns) {
                                    let newTd = document.createElement("td");
                                    rowElement.appendChild(newTd);
                                }

                                resizableGrid(element);

                                return false;
                            }
                        }
                    }
                }
            }

            function deleteAllFreeCells() {
                let trs = element.querySelectorAll("tr");
                let tr = trs.item(trs.length - 1);
                let tds = tr.querySelectorAll("td");

                function allEmpty(tds) {
                    for (const argument of tds) {
                        if (argument.textContent.length > 0) {
                            return false;
                        }
                    }
                    return true;
                }

                if (allEmpty(tds)) {
                    for (const td of Array.from(tds)) {
                        td.remove();
                    }
                    resizableGrid(element);
                }
            }

            if (event.code === "Backspace") {
                deleteAllFreeCells();
            }


        })
    }
}

customElements.define(name, EditorTableElement, {extends: "table"})