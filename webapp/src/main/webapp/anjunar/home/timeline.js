import {builder, customViews, HTMLWindow} from "../../library/simplicity/simplicity.js";
import MatInfiniteScroll from "../../library/simplicity/components/table/mat-infinite-scroll.js";
import {jsonClient} from "../../library/simplicity/services/client.js";
import TextPost from "./timeline/post/text-post.js";
import {i18nFactory} from "../../library/simplicity/services/i18nResolver.js";
import ImagePost from "./timeline/post/image-post.js";
import LinkPost from "./timeline/post/link-post.js";
import SystemPost from "./timeline/post/system-post.js";

export default class Timeline extends HTMLWindow {

    render() {

        this.style.display = "block";

        builder(this, {
            element: "div",
            children: [
                {
                    element: "div",
                    style : {
                        display : "flex",
                        justifyContent : "space-around"
                    },
                    children : [
                        {
                            element : "button",
                            type : "bottom",
                            className : "material-icons",
                            text : "notes",
                            onClick : (event) => {
                                event.stopPropagation();
                                window.location.hash = "#/anjunar/home/timeline/post/text-post-dialog";
                                return false;
                            }
                        },
                        {
                            element : "button",
                            type : "bottom",
                            className : "material-icons",
                            text : "link",
                            onClick : (event) => {
                                event.stopPropagation();
                                window.location.hash = "#/anjunar/home/timeline/post/link-post-dialog";
                                return false;
                            }
                        },
                        {
                            element : "button",
                            type : "bottom",
                            className : "material-icons",
                            text : "photo_camera",
                            onClick : (event) => {
                                event.stopPropagation();
                                window.location.hash = "#/anjunar/home/timeline/post/image-post-dialog";
                                return false;
                            }
                        }
                    ]
                },
                {
                    element: MatInfiniteScroll,
                    onItems: (event) => {
                        let domWindow = this.window.querySelector("dom-window");
                        domWindow.checkScrollBars();
                    },
                    items: {
                        direct: (query, callback) => {

                            let body = {
                                user : user.id,
                                index : query.index,
                                limit : query.limit,
                                sort : ["created:desc"]
                            }

                            jsonClient.post(`service/home/timeline`, {body : body})
                                .then((response) => {
                                    callback(response.rows, response.size, response.links);
                                });

                        }
                    },
                    meta: {
                        part: {
                            element: (item) => {
                                switch (item["@type"]) {
                                    case "Image" : return {
                                        element: ImagePost,
                                        post : item
                                    }
                                    case "Link" : return {
                                        element: LinkPost,
                                        post : item
                                    }
                                    case "System" : return {
                                        element: SystemPost,
                                        post : item
                                    }
                                    default : return {
                                        element: TextPost,
                                        post : item
                                    }
                                }
                            }
                        }
                    }
                }
            ]
        })
    }

}

customViews.define({
    name: "home-timeline",
    class: Timeline,
    header: "Timeline",
    resizable: true,
    minWidth : 500,
    width: 600,
    height: 600
})

const i18n = i18nFactory({
    "Post here...": {
        "en-DE": "Post here...",
        "de-DE": "Hier posten..."
    }
});
