import {builder, customComponents} from "../../../../simplicity.js";

export default class ToolbarTools extends HTMLElement {

    render() {
        builder(this, [
            {
                element : "div",
                style : {
                    display : "flex"
                },
                children : [
                    {
                        element : "button",
                        type : "button",
                        className : "iconBig",
                        title : "Copy",
                        text : "file_copy",
                        onClick : () => {
                            document.execCommand("copy")
                        }
                    },
                    {
                        element : "button",
                        type : "button",
                        className : "iconBig",
                        title : "Copy",
                        text : "content_cut",
                        onClick : () => {
                            document.execCommand("cut")
                        }
                    },
                    {
                        element : "button",
                        type : "button",
                        className : "iconBig",
                        title : "Copy",
                        text : "undo",
                        onClick : () => {
                            document.execCommand("undo")
                        }
                    }
                ]
            },
            {
                element : "div",
                style : {
                    display : "flex"
                },
                children : [
                    {
                        element : "button",
                        type : "button",
                        className : "iconBig",
                        title : "Delete",
                        text : "delete",
                        onClick : () => {
                            document.execCommand("delete")
                        }
                    },
                    {
                        element : "button",
                        type : "button",
                        className : "iconBig",
                        title : "Select All",
                        text : "select_all",
                        onClick : () => {
                            document.execCommand("selectAll")
                        }
                    },
                    {
                        element : "button",
                        type : "button",
                        className : "iconBig",
                        title : "Redo",
                        text : "redo",
                        onClick : () => {
                            document.execCommand("redo")
                        }
                    }
                ]
            }
        ])
    }

}

customComponents.define("toolbar-tools", ToolbarTools)