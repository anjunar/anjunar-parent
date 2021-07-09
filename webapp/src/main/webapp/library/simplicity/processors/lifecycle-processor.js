import {metaCache} from "../simplicity.js";

export function lifeCycle(force = false) {
    console.time("lifeCycle")
    for (const meta of metaCache) {
        for (const element of meta) {
            if (element.instance.isConnected) {
                element.update(force);
            } else {
                metaCache.delete(meta);
            }
        }
    }
    console.timeEnd("lifeCycle")
}